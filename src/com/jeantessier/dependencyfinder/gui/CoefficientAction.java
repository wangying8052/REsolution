/*
****************************************************
* REsolution is an automatic software refactoring tool      
****************************************************
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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.xml.sax.SAXException;

import com.Refactor.AdjustCoefficients.Adjust;
import com.Refactor.AdjustCoefficients.MainAdjust;
import com.Refactor.NonInheritance.preprocessing;
import com.mathworks.toolbox.javabuilder.MWException;
import com.neu.utils.component.CircleProgressBar;
import com.neu.utils.component.CoefficientJPanel;
import com.neu.utils.component.ProgressDialog;
import com.neu.utils.component.TabbedPanel;
import com.neu.utils.data.TabName;
import com.neu.utils.listener.ProgressBarLisenter;

public class CoefficientAction extends AbstractAction implements Runnable {
	
	public static boolean flag = false;
	public REsolution model;
	public CoefficientAction(REsolution model) {
		this.model = model;

		putValue(Action.LONG_DESCRIPTION, "Adjust coefficients of SAW, MIW, SEW and SSW");
		putValue(Action.NAME, "Cofficients");
		putValue(Action.SMALL_ICON,
				new ImageIcon(getClass().getResource("icons/CoefficientsGreen.png")));
	}

	public void actionPerformed(ActionEvent e) {
		new Thread(this).start();
	}

	public void run() {
		
		Date start = new Date();
    	model.getStatusLine().showInfo("Adjusting ...");
    	
    	try {
    		
			if (preprocessing.DSC > 100) {
				ProgressBarLisenter barLisenter = new ProgressBarLisenter();
		    	barLisenter.addBars(model.getProgressBar());
				final CircleProgressBar progressBar = new CircleProgressBar();
		    	SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						new ProgressDialog(model, progressBar,"Adjusting",model.getWidth(),model.getHeight());
					}
				});
		    	barLisenter.addBars(progressBar);
		    	barLisenter.beginSession();
				MainAdjust.AdjustCoefficients(barLisenter);
				barLisenter.endSession();
			}else {
				
				JOptionPane.showConfirmDialog(null,
						"There are too few classes for adjusting coefficients!",
						"system tips", JOptionPane.YES_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		JPanel panel = buildResultPanel();

		JTabbedPane pane = this.model.getResultRightPanelJTabbledPane();
		pane.add(TabName.CO, panel);
		int index = TabbedPanel.getCount();
		pane.setTabComponentAt(index, new TabbedPanel(pane,model));
		pane.setSelectedIndex(index);
				
		Date stop = new Date();

	    model.getStatusLine().showInfo("Done (" + ((stop.getTime() - start.getTime()) / (double) 1000) + " secs).");
	    model.setTitle("REsolution - Coefficients");
	}

	private JPanel buildResultPanel() {
		JPanel result = new JPanel(new BorderLayout());
		
		JPanel n = new JPanel();
		n.add(new JLabel("Metric values of the system before and after refactoring"));
		JPanel s = new JPanel();
		s.add(new JLabel());
		JPanel w = new JPanel();
		w.add(new JLabel());
		JPanel e = new JPanel();
		e.add(new JLabel());
		JPanel c = new JPanel(new GridLayout(2,1));
		
		JPanel cUp = new CoefficientJPanel(Adjust.MPCS,"MPC",Adjust.pingjunzhi,Adjust.zhongzhi,Adjust.fangcha);
		JPanel cDown = new CoefficientJPanel(Adjust.LCOMS,"LCOM");
		
		c.add(cUp);
		c.add(cDown);
		
		result.add(n,BorderLayout.NORTH);
		result.add(s,BorderLayout.SOUTH);
		result.add(w,BorderLayout.WEST);
		result.add(e,BorderLayout.EAST);
		result.add(c,BorderLayout.CENTER);
		
		return result;
	}

	
}
