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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.*;

import org.xml.sax.SAXException;

import com.Refactor.Inheritance.InheritSuggestions;
import com.Refactor.Inheritance.RefactorInheritance;
import com.Refactor.Inheritance.SplitTrees;
import com.Refactor.Inheritance.extend;
import com.Refactor.NonInheritance.NonInheritanceRefactoring;
import com.Refactor.NonInheritance.Suggestions;
import com.Refactor.NonInheritance.preprocessing;
import com.Refactor.NonInheritance.tool;
import com.mathworks.toolbox.javabuilder.MWException;
import com.neu.utils.component.CBScrollBarUI;
import com.neu.utils.component.CircleProgressBar;
import com.neu.utils.component.ProgressDialog;
import com.neu.utils.component.SimpleScatterPanel;
import com.neu.utils.component.TabbedPanel;
import com.neu.utils.component.TablePanel;
import com.neu.utils.data.Data;
import com.neu.utils.data.TabName;
import com.neu.utils.listener.ProgressBarLisenter;
import com.neu.utils.service.InheritSuggestion;

public class InheritRefactorAction extends AbstractAction implements Runnable {

	public static boolean flag = false;
	
	public REsolution model;

	public InheritRefactorAction(REsolution model) {
		this.model = model;

		putValue(Action.LONG_DESCRIPTION,
				"Perform Inheritance refactoring operations");
		putValue(Action.NAME, "Refactor");
		putValue(Action.SMALL_ICON,
				new ImageIcon(getClass()
						.getResource("icons/Refactor2Green.png")));
	}

	public void actionPerformed(ActionEvent e) {
		new Thread(this).start();
	}

	public void run() {
		
		inheritRefctor();
	}

	public void inheritRefctor() {

		if (flag == false) {
			
			ArrayList<Integer> selectedCheckBoxIndexs = getSelectedCheckBoxIndex(model.getTreeCheckBoxList());
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
					new ProgressDialog(model, progressBar,
							"Inheritance Refactoring", model.getWidth(), model
									.getHeight());
				}
			});
			barLisenter.addBars(progressBar);
			barLisenter.addBars(model.getProgressBar());

			barLisenter.beginSession();
			// ////////////////////////////////////

			model.getStatusLine().showInfo("Inheritance Restructuring...");
			InheritSuggestions sg = inheritRefactor(selectedCheckBoxIndexs,
					barLisenter);

			String title = "Inheritance Refactoring Suggestions";

			Data data = new Data(sg);

			JPanel panel = buildResultPanel(data, title);

			JTabbedPane pane = this.model.getResultRightPanelJTabbledPane();
			pane.add(TabName.R2A, panel);
			int index = TabbedPanel.getCount();
			pane.setTabComponentAt(index, new TabbedPanel(pane, model));
			pane.setSelectedIndex(index);

			setDetailButtonEnable();

			// /////////////////////////////////////////

			barLisenter.endSession();
			Date stop = new Date();
			model.getStatusLine().showInfo(
					"Done (" + ((stop.getTime() - start.getTime()) / (double) 1000)
							+ " secs).");
			model.setTitle("REsolution - Refactor2");
			model.activeNext(this);
			model.inactiveAt(5);
			
			flag = true;
		}
		
		

	}

	private void setDetailButtonEnable() {
		ArrayList<SplitTrees> SplitTree = RefactorInheritance.rv.SplitTree;

		for (SplitTrees tree : SplitTree) {
			model.getTreeButtonList().get(tree.TreeIndex).setEnabled(true);
		}
	}

	private InheritSuggestions inheritRefactor(ArrayList<Integer> selectedCheckBoxIndexs,
			ProgressBarLisenter barLisenter) {

		String log1 = "D:\\Tree.txt";
		ArrayList<ArrayList<ArrayList<extend>>> levelsRefactor = (ArrayList<ArrayList<ArrayList<extend>>>) RefactorInheritance.levels
				.clone();
		RefactorInheritance.IndexChechBox = selectedCheckBoxIndexs;

		for (int i = 0; i < RefactorInheritance.IndexChechBox.size(); i++) {
			System.out.println("IndexChechBox===="
					+ RefactorInheritance.IndexChechBox.get(i));
		}

		RefactorInheritance.persent = 0;
		ArrayList<SplitTrees> SplitTree = null;

		barLisenter.setMaxValue(levelsRefactor.size());

		try {
			levelsRefactor = RefactorInheritance.RefactorEachLevel(log1,
					levelsRefactor, preprocessing.extendsMatrix.clone(),
					barLisenter);
			SplitTree = RefactorInheritance.TidyResults(levelsRefactor);// 需要被分解的树
																		// 信息
		} catch (Exception e) {
			e.printStackTrace();
		}
		SplitTree = RefactorInheritance.CalculatInheritanceTreesQ(SplitTree,
				levelsRefactor);
		RefactorInheritance.rv.levelsRefactor = levelsRefactor;
		RefactorInheritance.rv.SplitTree = SplitTree;
		tool.GetTreelog(0);
		// RefactorInheritance.CalculatInheritanceTreesDetaQ(SplitTree,levelsRefactor,
		// 0);
		// RefactorInheritance.rv.levelsRefactor = levelsRefactor;
		// RefactorInheritance.rv.SplitTree = SplitTree;

		/**
		 * 封装数据
		 */
		InheritSuggestions suggestions = new InheritSuggestions(SplitTree,
				RefactorInheritance.Q_Orig, RefactorInheritance.Q_Undo);

		RefactorInheritance.sgs = suggestions;

		return suggestions;
	}

	/**
	 * 构建重构建议面板
	 * 
	 * @return
	 */
	private JPanel buildResultPanel(Data data, String title) {

		JPanel resultPanel = new JPanel(new BorderLayout());

		JPanel titlePanel = new JPanel();
		JLabel titleLabel = new JLabel(title);
		titlePanel.add(titleLabel);

		JPanel centerPanel_TOP = buildCenterPanelUpJPanel(data, title);
		JPanel centerPanel_BOTTOM = buildCenterPanelDownJPanel(data, title);

		JPanel centerPanel = new JPanel(new GridLayout(2, 1));
		centerPanel.add(centerPanel_TOP);
		centerPanel.add(centerPanel_BOTTOM);

		resultPanel.add(titlePanel, BorderLayout.NORTH);
		resultPanel.add(centerPanel, BorderLayout.CENTER);

		return resultPanel;
	}

	private JPanel buildCenterPanelDownJPanel(Data data, String title) {

		JPanel resultPanel = new JPanel(new GridLayout(1, 2));

		JPanel centerPanel_L_Down = buildCenterPanelLeftDownJPanel(data);
		JPanel centerPanel_R_Down = buildCenterPanelRightDownJPanel(data);
		resultPanel.add(centerPanel_L_Down);
		resultPanel.add(centerPanel_R_Down);

		return resultPanel;
	}

	private JPanel buildCenterPanelUpJPanel(Data data, String title) {
		JPanel resultPanel = buildRefactorAdvice(data, title);
		resultPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.green), "Suggestions"));
		return resultPanel;
	}

	/**
	 * 构建右下
	 * 
	 * @param data
	 * 
	 * @return
	 */
	private JPanel buildCenterPanelRightDownJPanel(Data data) {
		JPanel resultPanel = new JPanel(new GridLayout(2, 1));

		JPanel jPanel = buildInheritTreeControl(data);
		resultPanel.add(jPanel);
		resultPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.green), "Undo"));

		return resultPanel;
	}

	private JPanel buildInheritTreeControl(final Data data) {
		JPanel result = new JPanel(new GridLayout(1, 3));

		JPanel jPanel_1 = new JPanel(new GridLayout(2, 1));
		JPanel jPanel_1_1 = new JPanel();

		final int maxEntity = data.getSg().getQ_Undo().length;

		final int prevalue = maxEntity - 1;
		JScrollBar moveEntityBar = new JScrollBar(JScrollBar.HORIZONTAL,
				prevalue, 1, 0, maxEntity);
		moveEntityBar.setUI(new CBScrollBarUI());
		moveEntityBar.addAdjustmentListener(new AdjustmentListener() {

			public void adjustmentValueChanged(AdjustmentEvent e) {
				RefactorInheritance.CalculatInheritanceTreesDetaQ(prevalue
						- e.getValue());
				data.getSg().setQ_Undo(RefactorInheritance.Q_Undo);
				((InheritSuggestions) data.getSg()).setUndoLength(prevalue
						- e.getValue());
				data.setMeasurements(data.getSg());
			}
		});

		JPanel jPanel_1_2 = new JPanel(new GridLayout(4, 1));
		JLabel inheritTreeLabel = new JLabel(
				"Undo the Refactoring Operations");
		JPanel jPanel = new JPanel();
		jPanel.add(inheritTreeLabel);
		jPanel_1_2.add(jPanel);
		jPanel_1_2.add(new JLabel());
		jPanel_1_2.add(moveEntityBar);

		jPanel_1.add(jPanel_1_1);
		jPanel_1.add(jPanel_1_2);

		result.add(new JLabel());
		result.add(jPanel_1);
		result.add(new JLabel());

		return result;
	}

	/**
	 * 构建折线图面板
	 * 
	 * @return 折线图面板
	 */
	private JPanel buildCenterPanelLeftDownJPanel(Data data) {
		JPanel resultPanel = new JPanel(new BorderLayout());

		JPanel n = new JPanel();
		n.add(new JLabel(" "));
		JPanel s = new JPanel();
		s.add(new JLabel("Refactory Suggestions"));
		JPanel w = new JPanel(new GridLayout(1, 3));
		w.add(new JLabel(" "));
		w.add(new JLabel("Q"));
		w.add(new JLabel(" "));
		JPanel e = new JPanel();
		e.add(new JLabel(" "));

		// JPanel c = new TablePanel(data);
		JPanel c = new SimpleScatterPanel(data);

		resultPanel.add(n, BorderLayout.NORTH);
		resultPanel.add(s, BorderLayout.SOUTH);
		resultPanel.add(w, BorderLayout.WEST);
		resultPanel.add(e, BorderLayout.EAST);
		resultPanel.add(c, BorderLayout.CENTER);

		resultPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.green),
				"Weighted modularity change curves"));

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

		// 中间显示面板
		JPanel centerPanel = buildPanel(data, title);

		JPanel s = new JPanel();
		s.add(new JLabel(" "));
		JPanel w = new JPanel();
		w.add(new JLabel(" "));
		JPanel e = new JPanel();
		e.add(new JLabel(" "));

		resultPanel.add(s, BorderLayout.SOUTH);
		resultPanel.add(w, BorderLayout.WEST);
		resultPanel.add(e, BorderLayout.EAST);
		resultPanel.add(centerPanel, BorderLayout.CENTER);

		return resultPanel;
	}

	/**
	 * 构建重构建议文本框面板
	 * 
	 * @param data
	 * @param title
	 * @return 重构建议文本框面板
	 */
	private JPanel buildPanel(Data data, String title) {
		InheritSuggestion advicePanel = new InheritSuggestion(data);
		return advicePanel.buildPanel();
	}

	/**
	 * 得到用户选择要重构的联通片的索引
	 * 
	 * @param checkBoxList
	 * @return indexList
	 */
	private ArrayList<Integer> getSelectedCheckBoxIndex(
			List<JCheckBox> checkBoxList) {
		ArrayList<Integer> indexList = new ArrayList<Integer>();

		for (int index = 0; index < checkBoxList.size(); index++) {

			JCheckBox checkBox = checkBoxList.get(index);

			if (checkBox.isSelected() == true) {
				indexList.add(Integer
						.parseInt(checkBox.getText().split("_")[1]) - 1);
			}
		}
		return indexList;
	}

}
