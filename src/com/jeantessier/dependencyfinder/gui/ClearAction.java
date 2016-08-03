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

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import com.Refactor.NonInheritance.NonInheritanceRefactoring;
import com.jeantessier.dependency.VisitorBase;
import com.neu.utils.component.TabbedPanel;

public class ClearAction extends AbstractAction implements Runnable {
	
	public REsolution model;
	public ClearAction(REsolution model) {
		this.model = model;

		putValue(Action.LONG_DESCRIPTION, "Clear variables");
		putValue(Action.NAME, "Clear");
		putValue(Action.SMALL_ICON,
				new ImageIcon(getClass().getResource("icons/ClearGreen.png")));
	}

	public void actionPerformed(ActionEvent e) {
		new Thread(this).start();
	}

	public void run() {
		
		if (JOptionPane.showConfirmDialog
				(null,"This action will clear all the variables of the current system. Are you sure you want to proceed?", "system tips", 
						JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION) {
			//清空结果面板
			JTabbedPane pane = model.getResultRightPanelJTabbledPane();
			if (pane != null) {
				pane.removeAll();
				DependencyExtractAction.flag = false;
				SrcAction.allSrcPaths.clear();
				SrcAction.classesMap.clear();
				VisitorBase.judge = false;
				CMDNAction.flag = false;
				RefactorAction.flag = false;
				TreeAction.flag = false;
				InheritRefactorAction.flag = false;
				MetricsAction.flag = false;
//				CoefficientAction.flag = false;
			}
			TabbedPanel.clear();
			NonInheritanceRefactoring.ClearAction();
			
//			model.clearNodeFactory();
			model.setNewDependencyGraph();
			//清空选择栏
			if (model.getCheckBoxList() != null) {
				model.getCheckBoxList().clear();
			}
			if (null != model.getButtonList()) {
				model.getButtonList().clear();
			}
			if (null != model.getTreeCheckBoxList()) {
				model.getTreeCheckBoxList().clear();
			}
			if (null != model.getTreeButtonList()) {
				model.getTreeButtonList().clear();
			}
			if (model.getTreeCheckjPanel().getComponentCount()>=2) {
				model.getTreeCheckjPanel().remove(1);
			}
			if (model.getControlCheckjPanel().getComponentCount()>=2) {
				model.getControlCheckjPanel().remove(1);
			}
			model.repaint();
			//重置功能按钮状态
			model.resetState();
			
		}
		
	}

	

	
}
