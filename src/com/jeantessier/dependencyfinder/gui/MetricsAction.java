/*
 * ***************************************************
 * REsolution is an automatic software refactoring tool      
 * ***************************************************
 *  Copyright (c) 2016, Wang Ying, Yin Hongjian
 *  E-mail: wangying8052@163.com
 *  All rights reserved.
 *
 * This file is part of REsolution.
 *
 * REsolution is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * REsolution is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with REsolution.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.jeantessier.dependencyfinder.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.File;
import java.util.Date;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicBorders;

import com.Refactor.Inheritance.RefactorInheritance;
import com.Refactor.Metrics.CompareBeforeExtend;
import com.Refactor.Metrics.MainMetrics;
import com.Refactor.NonInheritance.NonInheritanceRefactoring;
import com.neu.utils.component.CircleProgressBar;
import com.neu.utils.component.MetricsPanel;
import com.neu.utils.component.ProgressDialog;
import com.neu.utils.component.RadialGraphView;
import com.neu.utils.component.TabbedPanel;
import com.neu.utils.data.TabName;
import com.neu.utils.io.FileUtils;
import com.neu.utils.listener.ProgressBarLisenter;


public class MetricsAction extends AbstractAction implements Runnable {
	
	public static boolean flag = false;
	private static String str = "";
	
	public REsolution model;
	public MetricsAction(REsolution model) {
		this.model = model;

		putValue(Action.LONG_DESCRIPTION, "Evaluate refactoring results by metrics");
		putValue(Action.NAME, "Metric");
		putValue(Action.SMALL_ICON,
				new ImageIcon(getClass().getResource("icons/MetricsGreen.png")));
	}

	public void actionPerformed(ActionEvent e) {
		new Thread(this).start();
	}

	public void run() {
		
		metrics();
	}

	public void metrics() {
		if (flag == false) {
			
			Date start = new Date();
			ProgressBarLisenter barLisenter = new ProgressBarLisenter();
	    	final CircleProgressBar progressBar = new CircleProgressBar();
	    	SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new ProgressDialog(model, progressBar,"Measuring",model.getWidth(),model.getHeight());
				}
			});
	    	barLisenter.addBars(progressBar);
	    	barLisenter.addBars(model.getProgressBar());
	    	barLisenter.setMaxValue(6);
	    	barLisenter.beginSession();
	    	
	    	try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}

	    	
			////////////////////////////////////////
	    	
	    	model.getStatusLine().showInfo("Metrics ...");
			CompareBeforeExtend cm = MainMetrics.MainBeforeOrAfterRefactorMetrics
					(RefactorInheritance.rv.levelsRefactor,NonInheritanceRefactoring.result0, barLisenter);

			JPanel panel = buildResultPanel(cm);

			JTabbedPane pane = this.model.getResultRightPanelJTabbledPane();
			pane.add(TabName.ME, panel);
			int index = TabbedPanel.getCount();
			pane.setTabComponentAt(index, new TabbedPanel(pane,model));
			pane.setSelectedIndex(index);
			
			///////////////////////////////////////////
			barLisenter.endSession();
			Date stop = new Date();

		    model.getStatusLine().showInfo("Done (" + ((stop.getTime() - start.getTime()) / (double) 1000) + " secs).");
		    model.setTitle("REsolution - Metrics");
		    model.activeNext(this);
		    model.inactiveAt(6);
			flag = true;
			if (RefactorAction.flag == true && 
			    		TreeAction.flag == true &&
			    		InheritRefactorAction.flag == true && 
			    		MetricsAction.flag == true) {
				model.inactiveAt(8);
			}
		}
	}

	/**
	 * 构建重构建议面板
	 * @param title 
	 * @param cm 
	 * 
	 * @return
	 */
	private JPanel buildResultPanel(CompareBeforeExtend cm) {

		JPanel resultPanel = new JPanel(new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.add(new JLabel("Metric quality change"));
//		JPanel southPanel = new JPanel();
//		southPanel.add(new JLabel("*Quality Change = (After - Before) / Before"));


		JPanel centerPanel = new MetricsPanel(cm);
		JPanel ePanel = buildMetricsTable(cm);

		resultPanel.add(northPanel, BorderLayout.NORTH);
//		resultPanel.add(southPanel, BorderLayout.SOUTH);
		resultPanel.add(centerPanel, BorderLayout.CENTER);
		resultPanel.add(ePanel, BorderLayout.EAST);

		return resultPanel;
	}
	
	
	private JPanel buildMetricsTable(CompareBeforeExtend cm) {
		JPanel result = new JPanel(new BorderLayout());
		
		String[][] rowData = warpData(cm);
 		String[] column = {"Metrics","Before","After","Ratio"};
		final JTable table = new JTable(rowData,column);
		table.setRowHeight(24);
				
		for (int i = 0; i < rowData.length; i++) {
			String[] col = rowData[i];
			for (int j = 0; j < col.length; j++) {
				String row = col[j];
				str+=row+",";
			}
			str+="\r\t";
		}
		
		table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (SwingUtilities.isRightMouseButton(e) == true) {
					getPopup(table).show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		
		JPanel n = new JPanel(new GridLayout(5,1));
		n.add(new JLabel(" "));
		n.add(new JLabel(" "));
		n.add(new JLabel(" "));
		n.add(new JLabel(" "));
		n.add(new JLabel("Metrics"));
		n.setBackground(Color.white);
		
		result.add(n ,BorderLayout.NORTH);
		result.add(table,BorderLayout.CENTER);
		result.add(new JLabel(" "),BorderLayout.EAST);
		
		return result;
	}

	private static JPopupMenu getPopup(final JComponent comp) {

		JPopupMenu popup = new JPopupMenu("Popup");
		
		JMenuItem item = new JMenuItem("Save");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Save File");
				chooser.setFileFilter(new FileFilter() {
					@Override
					public String getDescription() {
						return "*.csv";
					}
					@Override
					public boolean accept(File f) {
						boolean result = false;
				        if (f.isDirectory() || f.getName().endsWith(".csv")) {
				            result = true;
				        }
				        return result;
					}
				});
				
				int returnValue = chooser.showSaveDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		            if (chooser.getSelectedFiles().length > 1) {
		            	JOptionPane.showConfirmDialog(null, "Excuse me,you have to select only one file", "system tips", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
					}else {
						File file = chooser.getSelectedFile();
						
						FileUtils.fileWrite(file.getAbsolutePath()+".csv", str);
					}
		         }
			}
		});
		
		popup.add(item);
		popup.setInvoker(comp);
		return popup;
	}
	
	
	private String[][] warpData(CompareBeforeExtend cm) {
		
		String[][] rowData = {
				{"Metrics","Before","After","Ratio"},
				{"DSC",(double)Math.round(cm.beforeOODMetric.getDSC()*10000)/10000+"",(double)Math.round(cm.extendOODMetric.getDSC()*10000)/10000+"",(double)Math.round(cm.compareOODMetric.getDSC()*10000)/10000+""},
				{"ANA",(double)Math.round(cm.beforeOODMetric.getANA()*10000)/10000+"",(double)Math.round(cm.extendOODMetric.getANA()*10000)/10000+"",(double)Math.round(cm.compareOODMetric.getANA()*10000)/10000+""},
				{"DAM",(double)Math.round(cm.beforeOODMetric.getDAM()*10000)/10000+"",(double)Math.round(cm.extendOODMetric.getDAM()*10000)/10000+"",(double)Math.round(cm.compareOODMetric.getDAM()*10000)/10000+""},
				{"MPC",(double)Math.round(cm.beforeOODMetric.getMPC()*10000)/10000+"",(double)Math.round(cm.extendOODMetric.getMPC()*10000)/10000+"",(double)Math.round(cm.compareOODMetric.getMPC()*10000)/10000+""},
				{"CAM",(double)Math.round(cm.beforeOODMetric.getCAM()*10000)/10000+"",(double)Math.round(cm.extendOODMetric.getCAM()*10000)/10000+"",(double)Math.round(cm.compareOODMetric.getCAM()*10000)/10000+""},
				{"MOA",(double)Math.round(cm.beforeOODMetric.getMOA()*10000)/10000+"",(double)Math.round(cm.extendOODMetric.getMOA()*10000)/10000+"",(double)Math.round(cm.compareOODMetric.getMOA()*10000)/10000+""},
				{"NOP",(double)Math.round(cm.beforeOODMetric.getNOP()*10000)/10000+"",(double)Math.round(cm.extendOODMetric.getNOP()*10000)/10000+"",(double)Math.round(cm.compareOODMetric.getNOP()*10000)/10000+""},
				{"CIS",(double)Math.round(cm.beforeOODMetric.getCIS()*10000)/10000+"",(double)Math.round(cm.extendOODMetric.getCIS()*10000)/10000+"",(double)Math.round(cm.compareOODMetric.getCIS()*10000)/10000+""},
				{"NOM",(double)Math.round(cm.beforeOODMetric.getNOM()*10000)/10000+"",(double)Math.round(cm.extendOODMetric.getNOM()*10000)/10000+"",(double)Math.round(cm.compareOODMetric.getNOM()*10000)/10000+""},
				{"RFC",(double)Math.round(cm.beforeMetric.getRFC()*10000)/10000+"",(double)Math.round(cm.extendMetric.getRFC()*10000)/10000+"",(double)Math.round(cm.compareMetric.getRFC()*10000)/10000+""},
				{"LCOM",(double)Math.round(cm.beforeMetric.getLCOM()*10000)/10000+"",(double)Math.round(cm.extendMetric.getLCOM()*10000)/10000+"",(double)Math.round(cm.compareMetric.getLCOM()*10000)/10000+""},
				{"NOC",(double)Math.round(cm.beforeMetric.getNOC()*10000)/10000+"",(double)Math.round(cm.extendMetric.getNOC()*10000)/10000+"",(double)Math.round(cm.compareMetric.getNOC()*10000)/10000+""},
				{"MT",(double)Math.round(cm.beforeMetric.getMt()*10000)/10000+"",(double)Math.round(cm.extendMetric.getMt()*10000)/10000+"",(double)Math.round(cm.compareMetric.getMt()*10000)/10000+""},
				{"RE",(double)Math.round(cm.beforeOODMetric.getReusability()*10000)/10000+"",(double)Math.round(cm.extendOODMetric.getReusability()*10000)/10000+"",(double)Math.round(cm.compareOODMetric.getReusability()*10000)/10000+""},
				{"FL",(double)Math.round(cm.beforeOODMetric.getFlexibility()*10000)/10000+"",(double)Math.round(cm.extendOODMetric.getFlexibility()*10000)/10000+"",(double)Math.round(cm.compareOODMetric.getFlexibility()*10000)/10000+""},
				{"UN",(double)Math.round(cm.beforeOODMetric.getUnderstandability()*10000)/10000+"",(double)Math.round(cm.extendOODMetric.getUnderstandability()*10000)/10000+"",(double)Math.round(cm.compareOODMetric.getUnderstandability()*10000)/10000+""}
				};
		
		return rowData;
	}

}
