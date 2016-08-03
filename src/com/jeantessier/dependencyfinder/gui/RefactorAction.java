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

import hep.aida.bin.StaticBin1D;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import org.xml.sax.SAXException;

import com.Refactor.NonInheritance.GenerateRefactoringSuggestions;
import com.Refactor.NonInheritance.NonInheritanceRefactoring;
import com.Refactor.NonInheritance.Suggestions;
import com.Refactor.NonInheritance.UndoClass;
import com.Refactor.NonInheritance.UndoEntity;
import com.Refactor.NonInheritance.preprocessing;
import com.Refactor.NonInheritance.semantic;
import com.Refactor.NonInheritance.tool;
import com.mathworks.toolbox.javabuilder.MWException;
import com.neu.utils.component.ArrowLinePanel;
import com.neu.utils.component.CBScrollBarUI;
import com.neu.utils.component.CircleProgressBar;
import com.neu.utils.component.ProgressDialog;
import com.neu.utils.component.ScatterPanel;
import com.neu.utils.component.TabbedPanel;
import com.neu.utils.component.TablePanel;
import com.neu.utils.component.TextPanel;
import com.neu.utils.data.Data;
import com.neu.utils.data.TabName;
import com.neu.utils.listener.ProgressBarLisenter;
import com.neu.utils.service.ClassPanel;
import com.neu.utils.service.MethodPanel;
import com.neu.utils.service.Observer;
import com.opensymphony.webwork.Main;


public class RefactorAction extends AbstractAction implements Runnable {
	
	public static boolean flag = false;
	
	public REsolution model;
	public RefactorAction(REsolution model) {
		this.model = model;

		putValue(Action.LONG_DESCRIPTION, "Perform Non-inheritance refactoring operations");
		putValue(Action.NAME, "Refactor");
		putValue(Action.SMALL_ICON,
				new ImageIcon(getClass().getResource("icons/Refactor1Green.png")));
	}

	public void actionPerformed(ActionEvent e) {
		
		if (e != null) {
			boolean reault = model.validateParam();
			
			if (false == reault) {
    			return;
			}
		}
		
		new Thread(this).start();
	}

	public void run() {
		refactor();
	}

	public void refactor() {
    	if (flag == false) {
    		
    		ArrayList<Integer> selectedCheckBoxIndexs = getSelectedCheckBoxIndex(model.getCheckBoxList());
    		
    		if (false == (selectedCheckBoxIndexs.size()>0)) {
    			JOptionPane.showConfirmDialog(null,
    					"Please select at least one class connected component to be refactored.",
    					"system tips", JOptionPane.YES_OPTION,
    					JOptionPane.INFORMATION_MESSAGE);
    			return;
    		}
    		
    		
    		Date start = new Date();
        	ProgressBarLisenter barLisenter = new ProgressBarLisenter();
        	final CircleProgressBar progressBar = new CircleProgressBar();
        	SwingUtilities.invokeLater(new Runnable() {
    			@Override
    			public void run() {
    				new ProgressDialog(model, progressBar,"Non-inheritance Refactoring",model.getWidth(),model.getHeight());
    			}
    		});
        	barLisenter.addBars(progressBar);
        	barLisenter.addBars(model.getProgressBar());
        	progressBar.setMaximum(preprocessing.MergeSetList.size());
        	model.getProgressBar().setMaximum(preprocessing.MergeSetList.size());
        	barLisenter.beginSession();
        	////////////////////////
        	
    		NonInheritanceRefactoring.Clear();//王阿果
    		
    		model.getStatusLine().showInfo("Non-Inheritance Restructuring...");
    		
    		Suggestions sg =  refactorSoftware(selectedCheckBoxIndexs,barLisenter);
    		
    		String titleLeft = "Move Method/Field Refactoring Suggestions";
    		String titleRight = "Extract Class Refactoring Suggestions";

    		Data data = new Data(sg);
    		
    		JPanel panel = buildResultPanel(data, titleLeft,titleRight);

    		JTabbedPane pane = ((REsolution)this.model).getResultRightPanelJTabbledPane();
    		pane.add(TabName.RA, panel);
    		int index = TabbedPanel.getCount();
    		pane.setTabComponentAt(index, new TabbedPanel(pane,model));
    		pane.setSelectedIndex(index);
    		
    		////////////////////////////////////////
    		barLisenter.endSession();
    		Date stop = new Date();

    	    model.getStatusLine().showInfo("Done (" + ((stop.getTime() - start.getTime()) / (double) 1000) + " secs).");
    	    model.setTitle("REsolution - Refactor1");
    	    model.activeNext(this);
    	    model.inactiveAt(3);
    	    flag = true;
		}
		
	}

	/**
	 * 构建重构建议面板
	 * 
	 * @return
	 */
	private JPanel buildResultPanel(Data data, String titleLeft, String titleRight) {

		JPanel resultPanel = new JPanel(new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.add(new JLabel("Non-inheritance Refactoring Suggestions"));

		JPanel centerPanel_L_Up = buildCenterPanelLeftUpJPanel(data,
				titleLeft);
		JPanel centerPanel_R_Up = buildCenterPanelRightUpJPanel(data,
				titleRight);
		
		
		JPanel centerPanel_L_Down = buildCenterPanelLeftDownJPanel(data);
		JPanel centerPanel_R_Down = buildCenterPanelRightDownJPanel(data);

		JPanel centerPanel = new JPanel(new GridLayout(2, 2));
		centerPanel.add(centerPanel_L_Up);
		centerPanel.add(centerPanel_R_Up);
		centerPanel.add(centerPanel_L_Down);
		centerPanel.add(centerPanel_R_Down);

		resultPanel.add(northPanel, BorderLayout.NORTH);
		resultPanel.add(centerPanel, BorderLayout.CENTER);

		return resultPanel;
	}

	/**
	 * 构建右下
	 * @param data 
	 * 
	 * @return
	 */
	private JPanel buildCenterPanelRightDownJPanel(Data data) {
		JPanel resultPanel = new JPanel(new GridLayout(2,1));
		
		JPanel jPanel_1 = buildMoveEntityControl(data);
		JPanel jPanel_2 = buildExtractClassControl(data);
		
		resultPanel.add(jPanel_1);
		resultPanel.add(jPanel_2);
		
		resultPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.green),"Undo"));

		return resultPanel;
	}

	private JPanel buildExtractClassControl(final Data data) {
		
		JPanel result = new JPanel(new GridLayout(1,3));
		
		JPanel jPanel_2 = new JPanel(new BorderLayout());
		JLabel extractClassLabel = new JLabel("Undo Extract Class Refactoring");
		JPanel jPanel_2_1 = new JPanel();
		jPanel_2_1.add(extractClassLabel);
		
		final int maxClass = data.getSg().getUndoClasseSort().size();
		
		JScrollBar extractClassBar = new JScrollBar(JScrollBar.HORIZONTAL, maxClass, 1, 0, maxClass+1);
		extractClassBar.setUI(new CBScrollBarUI());
		extractClassBar.setOrientation(JScrollBar.HORIZONTAL);
		extractClassBar.addAdjustmentListener(new AdjustmentListener() {
			
			public void adjustmentValueChanged(AdjustmentEvent e) {
				update(e.getValue(),0,data,1);
			}
		});
		
		JPanel jPanel_2_2 = new JPanel(new GridLayout(8,1));
		jPanel_2_2.add(new JLabel());
		jPanel_2_2.add(extractClassBar);
		
		jPanel_2.add(jPanel_2_1,BorderLayout.NORTH);
		jPanel_2.add(jPanel_2_2,BorderLayout.CENTER);
		
		result.add(new JLabel());
		result.add(jPanel_2);
		result.add(new JLabel());
		
		return result;
	}

	private JPanel buildMoveEntityControl(final Data data) {
		
		JPanel result = new JPanel(new GridLayout(1,3));
		
		JPanel jPanel_1 = new JPanel(new BorderLayout());
		JLabel moveEntityLabel = new JLabel("Undo Move Method/Field Refactoring");
		JPanel jPanel_1_1 = new JPanel();
		jPanel_1_1.add(moveEntityLabel);
		
		final int maxEntity = data.getSg().getUndoEntitySort().size();
		
		JScrollBar moveEntityBar = new JScrollBar(JScrollBar.HORIZONTAL, maxEntity, 1, 0, maxEntity+1);
		moveEntityBar.setUI(new CBScrollBarUI());
		
		moveEntityBar.addAdjustmentListener(new AdjustmentListener() {
			
			public void adjustmentValueChanged(AdjustmentEvent e) {				
				update(0,e.getValue(),data,2);
			}
		});
		
		JPanel jPanel_1_2 = new JPanel(new GridLayout(8,1));
		jPanel_1_2.add(new JLabel());
		jPanel_1_2.add(moveEntityBar);
		
		jPanel_1.add(jPanel_1_1,BorderLayout.NORTH);
		jPanel_1.add(jPanel_1_2,BorderLayout.CENTER);
		
		result.add(new JLabel());
		result.add(jPanel_1);
		result.add(new JLabel());
		
		return result;
	}

	static int undoClasss = 0;
	static int undoEntity = 0;
	private static void update(int classValue,int entityValue,Data data,int sourceId){
		
		int maxEntity = data.getSg().getUndoEntitySort().size();
		int maxClass = data.getSg().getUndoClasseSort().size();
		
		if (sourceId == 1) {
			undoClasss = maxClass-classValue;
			
		} else if(sourceId == 2){
			undoEntity = maxEntity - entityValue;
		}
		NonInheritanceRefactoring.GetQSorttoDrawFigure(undoClasss, undoEntity, preprocessing.MergeSetList.size());
		
		data.getSg().setUndoClassLength(undoClasss);
		data.getSg().setUndoEntityLength(undoEntity);
		
		data.setMeasurements(NonInheritanceRefactoring.sgs);
		
	}
	
	/**
	 * 构建左下
	 * 
	 * @return
	 */
	private JPanel buildCenterPanelLeftDownJPanel(Data data) {
		JPanel resultPanel = new JPanel(new BorderLayout());
		
		JPanel n = new JPanel();
		n.add(new JLabel(" "));
		JPanel s = new JPanel();
		s.add(new JLabel("Refactory Suggestions"));
		JPanel w = new JPanel(new GridLayout(1,3));
		w.add(new JLabel(" "));
		w.add(new JLabel("Q"));
		w.add(new JLabel(" "));
		JPanel e = new JPanel();
		e.add(new JLabel(" "));
		
//		JPanel c = new TablePanel(data);
		JPanel c = new ScatterPanel(data);
		
		resultPanel.add(n,BorderLayout.NORTH);
		resultPanel.add(s,BorderLayout.SOUTH);
		resultPanel.add(w,BorderLayout.WEST);
		resultPanel.add(e,BorderLayout.EAST);
		resultPanel.add(c,BorderLayout.CENTER);
		
		resultPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.green),"Weighted modularity change curves"));
		

		return resultPanel;
	}

	
	/**
	 * 构建右上
	 * 
	 * @return
	 */
	private JPanel buildCenterPanelRightUpJPanel(Data data, String title) {
		JPanel resultPanel = buildRefactorAdvice(data, title);
		resultPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.green), "Extract Class"));
		return resultPanel;
	}

	/**
	 * 构建左上
	 * 
	 * @return
	 */
	private JPanel buildCenterPanelLeftUpJPanel(Data data, String title) {
		JPanel resultPanel = buildRefactorAdvice(data, title);
		resultPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.green),"Move Method/Field"));
		return resultPanel;
	}

	/**
	 * 构建重构建议降序排列的面板
	 * 
	 * @param list
	 * @param title
	 * @return
	 */
	private JPanel buildRefactorAdvice(Data data, String title) {

		JPanel resultPanel = new JPanel(new BorderLayout());

		JPanel titlePanel = new JPanel();
		JLabel titleLabel = new JLabel(title);
		titlePanel.add(titleLabel);

		// 中间显示面板
		JPanel centerPanel = buildPanel(data,title);

		JPanel s = new JPanel();
		s.add(new JLabel(" "));
		JPanel w = new JPanel();
		w.add(new JLabel(" "));
		JPanel e = new JPanel();
		e.add(new JLabel(" "));

		resultPanel.add(titlePanel, BorderLayout.NORTH);
		resultPanel.add(s, BorderLayout.SOUTH);
		resultPanel.add(w, BorderLayout.WEST);
		resultPanel.add(e, BorderLayout.EAST);
		resultPanel.add(centerPanel, BorderLayout.CENTER);

		return resultPanel;
	}

	
	private JPanel buildPanel(Data data, String title){
		
		if (title.indexOf("Method")>=0) {
			MethodPanel advicePanel = new MethodPanel(data);
			return advicePanel.buildPanel();	
		} else if(title.indexOf("Class")>=0){
			ClassPanel advicePanel = new ClassPanel(data);
			return advicePanel.buildPanel();
		}
		
		return new JPanel();
	}

	
	/**
	 * 重构软件
	 * @param barLisenter 
	 */
	private Suggestions refactorSoftware(ArrayList<Integer> selectedCheckBoxIndexs, ProgressBarLisenter barLisenter) {

		NonInheritanceRefactoring.result0.clear();
		double i = 1;
		NonInheritanceRefactoring.persent = 0;

		
		NonInheritanceRefactoring.IndexChechBox = selectedCheckBoxIndexs;// 与学弟对完接口删掉

		for (int p = 0; p < preprocessing.MergeSetList.size(); p++) {
			if (NonInheritanceRefactoring.IndexChechBox.contains(p)) {
				ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
				try {
					temp = NonInheritanceRefactoring.RefactorOneConnect(
							preprocessing.MergeSetList.get(p),
							NonInheritanceRefactoring.a,
							NonInheritanceRefactoring.b,
							NonInheritanceRefactoring.c,
							NonInheritanceRefactoring.d);
				} catch (IOException e) {
					JOptionPane.showConfirmDialog(null,
							"Excuse me,fail to load source file!",
							"system tips", JOptionPane.YES_OPTION,
							JOptionPane.INFORMATION_MESSAGE);

				} catch (SAXException e) {
					JOptionPane.showConfirmDialog(null,
							"Excuse me,fail to parse file!", "system tips",
							JOptionPane.YES_OPTION,
							JOptionPane.INFORMATION_MESSAGE);

				} catch (MWException e) {
					JOptionPane.showConfirmDialog(null,
							"Excuse me,fail to call mathworks lib!",
							"system tips", JOptionPane.YES_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
				}
				NonInheritanceRefactoring.result0.addAll(temp);
				NonInheritanceRefactoring.persent = i
						/ (double) preprocessing.MergeSetList.size();
			}
			
			barLisenter.endFile();
			
		}
		NonInheritanceRefactoring.FinalSortSuggestions();// 分析最终重构建议

		
		//sg.UndoEntitySort得到左边的面板数据
		//sg.undoClasseSort得到右边的面板数据
		
		try {// 打印非继承体系重构建议
			GenerateRefactoringSuggestions
					.PrintRefactoringSuggestions(NonInheritanceRefactoring.sgs);
		} catch (IOException e) {
			JOptionPane.showConfirmDialog(null,
					"Excuse me,fail to parse file!", "system tips",
					JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
		}

		System.out.println("size ===="+preprocessing.MergeSetList.size());
		
		tool.GetQlog(0, 0, preprocessing.MergeSetList.size());// 撤销操作
		
		
		return NonInheritanceRefactoring.sgs;
	}

	/**
	 * 得到用户选择要重构的联通片的索引
	 * 
	 * @param checkBoxList
	 * @return
	 */
	private ArrayList<Integer> getSelectedCheckBoxIndex(
			List<JCheckBox> checkBoxList) {
		ArrayList<Integer> indexList = new ArrayList<Integer>();

		for (int index = 0; index < checkBoxList.size(); index++) {

			JCheckBox checkBox = checkBoxList.get(index);

			if (checkBox.isSelected() == true) {
				indexList.add(Integer.parseInt(checkBox.getText().split("_")[1])-1);
			}
		}
		return indexList;
	}

}
