/*
****************************************************
* REsolution is an automatic software refactoring tool      
****************************************************
 *  Copyright (c) 2016, Wang Ying, Yin Hongjian, YU Hai, ZHU Zhiliang.
 *  E-mail: yuhai@126.com
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
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.xml.sax.SAXException;

import com.Refactor.AdjustCoefficients.MainAdjust;
import com.Refactor.Inheritance.MainInheritance;
import com.Refactor.NonInheritance.Main;
import com.Refactor.NonInheritance.NonInheritanceRefactoring;
import com.Refactor.NonInheritance.Suggestions;
import com.Refactor.NonInheritance.preprocessing;
import com.mathworks.toolbox.javabuilder.MWException;
import com.neu.utils.component.CoefficientJPanel;
import com.neu.utils.component.ProgressDialog;
import com.neu.utils.component.TabbedPanel;
import com.neu.utils.data.Data;
import com.neu.utils.listener.ProgressBarLisenter;

public class GoAction extends AbstractAction implements Runnable {
	
	public REsolution model;
	public GoAction(REsolution model) {
		this.model = model;

		putValue(Action.LONG_DESCRIPTION, "One-key restructure software");
		putValue(Action.NAME, "Go!");
		putValue(Action.SMALL_ICON,
				new ImageIcon(getClass().getResource("icons/GoGreen.png")));
	}

	public void actionPerformed(ActionEvent e) {
		new Thread(this).start();
	}

	public void run() {
		
		Date start = new Date();
		boolean goResult = true;
//		NonInheritanceRefactoring.ClearAction1();
		if (RefactorAction.flag == false) {
			RefactorAction refactorAction = new RefactorAction(model);
			goResult = refactorAction.refactor();
			model.activeAt(4);
		}
		if (goResult == true) {
			if (TreeAction.flag == false) {
				TreeAction treeAction = new TreeAction(model);
				treeAction.tree();
				model.activeAt(5);
			}
			if (InheritRefactorAction.flag == false) {
				InheritRefactorAction inheritRefactorAction = new InheritRefactorAction(model);
				inheritRefactorAction.inheritRefctor();
				model.activeAt(6);
			}
			if (MetricsAction.flag == false) {
				MetricsAction metricsAction = new MetricsAction(model);
				metricsAction.metrics();
			}
		}
		
		Date stop = new Date();

		model.getStatusLine().showInfo("Done (" + ((stop.getTime() - start.getTime()) / (double) 1000)+ " secs).");
		model.setTitle("REsolution - Go");
	}

	

	
}
