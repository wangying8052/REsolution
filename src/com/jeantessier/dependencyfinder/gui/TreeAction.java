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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import com.Refactor.Inheritance.NodeColor;
import com.Refactor.Inheritance.RefactorInheritance;
import com.Refactor.Inheritance.SplitTrees;
import com.Refactor.Inheritance.extend;
import com.Refactor.NonInheritance.preprocessing;
import com.neu.utils.component.CBScrollBarUI;
import com.neu.utils.component.GridBagPanel;
import com.neu.utils.component.TabbedPanel;
import com.neu.utils.component.TreeDetailPanel;
import com.neu.utils.component.TreeJPanel;
import com.neu.utils.data.TabName;
import com.neu.utils.service.ButtonStyle;

public class TreeAction extends AbstractAction implements Runnable{
	
	//联通片
	public static boolean flag = false;
	
	private REsolution model;

	public TreeAction(REsolution model) {
    	
        this.model = model;

        putValue(Action.LONG_DESCRIPTION, "Analyze the inheritance trees of system");
        putValue(Action.NAME, "Tree");
        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("icons/TreeGreen.png")));
    }

    public void actionPerformed(ActionEvent e) {
        new Thread(this).start();
    }

	public void run(){
		
		tree();
	}

	public void tree() {
		
		if (flag == false) {
			buildOptionPanel();
			
			JTabbedPane pane = model.getOptionPane();
			
			pane.setSelectedIndex(1);
			model.activeNext(this);
			model.inactiveAt(4);
			flag = true;
		}
		
		
	}

	private JPanel buildOptionPanel() {
		JPanel jPanel = model.getTreeCheckjPanel();
		//准备数据
		ArrayList<ArrayList<ArrayList<extend>>> treeList = null;
		try {
			treeList = getTreeData();
		} catch (IOException e) {
			JOptionPane.showConfirmDialog(null, 
					"Excuse me,please restruction software, firstly!", 
					"system tips", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
		}

//		 创建checkboxLIst
		List<JCheckBox> checkBoxList = model.getTreeCheckBoxList();
		List<JButton> buttonList = model.getTreeButtonList();
		
		if (treeList != null && treeList.size() > 0) {
			ControlTreeListener button = new ControlTreeListener(this.model, treeList);

			for (int i = 1; i <= treeList.size(); i++) {
				JCheckBox jCheckBox = new JCheckBox();
				jCheckBox.setText("Tree_" + i);
				jCheckBox.addMouseListener(button);
				jCheckBox.setSelected(true);
				checkBoxList.add(jCheckBox);
				
				Icon icon=new ImageIcon(getClass().getResource("icons/detail.png"));
				final JButton detailButton = new JButton(icon);
				detailButton.setEnabled(false);
				ButtonStyle buttonStyle = new ButtonStyle();
				buttonStyle.setButtonStyle(detailButton);
				
				detailButton.addActionListener(button);
				detailButton.setActionCommand("detail_"+i);
				
				detailButton.addMouseListener(new MouseListener() {
					
					@Override
					public void mouseReleased(MouseEvent e) {}
					
					@Override
					public void mousePressed(MouseEvent e) {}
					
					@Override
					public void mouseExited(MouseEvent e) {}
					
					@Override
					public void mouseEntered(MouseEvent e) {
						if (detailButton.isEnabled()) {
							detailButton.setToolTipText("Show Refactoring Result");
						} else {
							detailButton.setToolTipText("It has no need to be restructured");
						}
					}
					
					@Override
					public void mouseClicked(MouseEvent e) {}
				});
				buttonList.add(detailButton);
			}
			
			// 添加到面板中

			GridBagPanel checkPanel = new GridBagPanel();
			for (int i = 0; i < checkBoxList.size(); i++) {
				JCheckBox jCheckBox = checkBoxList.get(i);
				JButton detailButton = buttonList.get(i);
				checkPanel.addComp(jCheckBox, detailButton);
			}
			
			JScrollPane scrollPane = new JScrollPane(
	                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
	                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setViewportView(checkPanel);
	        scrollPane.getHorizontalScrollBar().setUI(new CBScrollBarUI(Color.gray));
	        scrollPane.getVerticalScrollBar().setUI(new CBScrollBarUI(Color.gray));
	        
//	        checkPanel.setPreferredSize(new Dimension(190, checkBoxList.size() * 30));
//	        checkPanel.revalidate();
//			
			jPanel.add(scrollPane,BorderLayout.CENTER);
		}
		return jPanel;
	}
	
	private ArrayList<ArrayList<ArrayList<extend>>> getTreeData() throws IOException{
		
		RefactorInheritance.levels =  RefactorInheritance.getInheritanceTreelevels(preprocessing.extendsMatrix.clone());
		RefactorInheritance.levels =  RefactorInheritance.getinterfaces(RefactorInheritance.levels);
		RefactorInheritance.levels =  RefactorInheritance.getTreesBeforeRefactoring(RefactorInheritance.levels);//重构前的继承树结构
//		RefactorInheritance.PrintTrees(RefactorInheritance.levels);
		return RefactorInheritance.levels;
	}
	
}

class ControlTreeListener extends MouseAdapter implements ActionListener{

	private REsolution model = null;
	ArrayList<ArrayList<ArrayList<extend>>> treeBoxList = null;
	
	public ControlTreeListener(REsolution model){
		this.model = model;
	}
	
	public ControlTreeListener(REsolution model,ArrayList<ArrayList<ArrayList<extend>>> treeBoxList){
		this.model = model;
		//准备数据
		this.treeBoxList = treeBoxList;
				
	}
		
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("Undo")) {
			
			model.batchTreeCheckBoxs(false);
		}
		if (e.getActionCommand().equals("CheckAll")) {
			
			model.batchTreeCheckBoxs(true);
		}
		if (e.getActionCommand().indexOf("detail")>=0) {
			String detailButtonCommand = e.getActionCommand();
			
			int index = Integer.parseInt(detailButtonCommand.split("_")[1]);
			buildSplitTreePanel(index);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (e.getClickCount()==2) {//对双击事件进行相应，并且取得被双击的checkbox的索引
			
			JCheckBox box = (JCheckBox)e.getComponent();
			int index = Integer.parseInt(box.getText().split("_")[1]);
			buildInheritTreePanel(index);
		}
		
	}
	
	
	/**
	 * 构建分解继承树面板
	 * @param buttionIndex
	 */
	private void buildSplitTreePanel(int buttionIndex) {
		
		JPanel resultPanel = new JPanel(new BorderLayout());
		
		JPanel titlePanel = new JPanel();
		titlePanel.add(new JLabel("Inheritance Tree "+buttionIndex));
		
		JPanel centerPanel = new TreeDetailPanel(getSplitTree(buttionIndex));
				
		resultPanel.add(titlePanel,BorderLayout.NORTH);
		resultPanel.add(centerPanel,BorderLayout.CENTER);
		
		JTabbedPane pane =this.model.getResultRightPanelJTabbledPane();
		pane.add(TabName.ST,resultPanel);
		int index = TabbedPanel.getCount();
		pane.setTabComponentAt(index, new TabbedPanel(pane,model));
		pane.setSelectedIndex(index);
	}

	
	/**
	 * 构建原始继承树面板的中心面板
	 * @param compoentIndex
	 */
	public void buildInheritTreePanel(int compoentIndex){
		
		JPanel resultPanel = new JPanel(new BorderLayout());
		
		JPanel n = new JPanel();
		n.add(new JLabel(" "));
		JPanel s = new JPanel();
		s.add(new JLabel(" "));
		JPanel w = new JPanel();
		w.add(new JLabel(" "));

		JPanel e = new JPanel();
		e.add(new JLabel(" "));
	
		JPanel treePanel = buildCenterPanel(compoentIndex);
		
		resultPanel.add(n,BorderLayout.NORTH);
		resultPanel.add(s,BorderLayout.SOUTH);
		resultPanel.add(w,BorderLayout.WEST);
		resultPanel.add(e,BorderLayout.EAST);
		resultPanel.add(treePanel,BorderLayout.CENTER);
		
		
		JTabbedPane pane =this.model.getResultRightPanelJTabbledPane();
		pane.add(TabName.IT,resultPanel);
		int index = TabbedPanel.getCount();
		pane.setTabComponentAt(index, new TabbedPanel(pane,model));
		pane.setSelectedIndex(index);
		
	}

	/**
	 * 构建原始继承树面板的中心面板
	 * @param compoentIndex
	 * @return
	 */
	private JPanel buildCenterPanel(int compoentIndex) {
		
		JPanel resultPanel = new JPanel(new BorderLayout());
		
		JPanel titlePanel = new JPanel();
		titlePanel.add(new JLabel("Inheritance Tree "+compoentIndex));
		
		JPanel treePanel = new TreeJPanel(getTree(compoentIndex));
		
		treePanel.addMouseListener((MouseListener) treePanel);
		
		resultPanel.add(titlePanel,BorderLayout.NORTH);
		resultPanel.add(treePanel,BorderLayout.CENTER);
		
		return resultPanel;
	}

	/**
	 * 得到所选择的继承树的索引
	 * @param compoentIndex
	 * @return
	 */
	private List<ArrayList<extend>> getTree(int compoentIndex) {
		List<ArrayList<extend>> tree = treeBoxList.get(compoentIndex-1);
		return tree;
	}
	
	private ArrayList<ArrayList<ArrayList<extend>>> getSplitTree(int compoentIndex) {
		ArrayList<SplitTrees> SplitTree = RefactorInheritance.rv.SplitTree;
		
		compoentIndex -=1;
		
		for (SplitTrees tree : SplitTree) {
			if (tree.TreeIndex == compoentIndex) {
				return mergeAloneNode(tree.getLevelSplit());
			}
		}
		return null;
	}

	/**
	 * 归并孤立节点到第一棵树中
	 * @param levelSplit
	 * @return
	 */
	private ArrayList<ArrayList<ArrayList<extend>>> mergeAloneNode(
			ArrayList<ArrayList<ArrayList<extend>>> levelSplit) {
		
		List<ArrayList<extend>> fatherTree = levelSplit.get(0);
		
		if (levelSplit.size()>1) {
			for (int i = 1; i < levelSplit.size(); i++) {
				ArrayList<ArrayList<extend>> tree = levelSplit.get(i);
				addToRootTree(fatherTree, tree);
				if(tree.size() == 0){
					levelSplit.remove(i);
					i--;
				}
			}
		}
		
		return levelSplit;
	}

	/**
	 * 将分解的子树添加到原来的树
	 * @param fatherTree 父树
	 * @param tree 需要被检查的子树
	 */
	private void addToRootTree(List<ArrayList<extend>> fatherTree,
			ArrayList<ArrayList<extend>> tree) {
		
		for (int i = 0; i < tree.size(); i++) {
			ArrayList<extend> oneLevel = tree.get(i);
			for (int j = 0; j < oneLevel.size(); j++) {
				extend ext = oneLevel.get(j);
				if (ext.SuperClassName.size() == 0 && ext.SubClassName.size() == 0) {
					
					for(String str : ext.IndependencyClassName){
						int[] levelCount = getLevelCount(str,fatherTree);
						if (levelCount[0] > -1 && levelCount[1]>-1) {
							addNode(fatherTree,levelCount,ext);
							oneLevel.remove(j);
							j--;
						}
					}
				}
			}
			
			if(oneLevel.size() == 0){
				tree.remove(i);
				i--;
			}
			
		}
		
	}

	/**
	 * 得到父节点所在的位置
	 * 
	 * @param str
	 * @param newTree
	 * @return indexs[0] 层数 ，indexs[1] 列数
	 */
	private int[] getLevelCount(String str, List<ArrayList<extend>> fatherTree) {

		int[] indexs = new int[2];

		for (int i = 0; i < fatherTree.size(); i++) {
			ArrayList<extend> oneLevel = fatherTree.get(i);
			for (int j = 0; j < oneLevel.size(); j++) {
				extend ext = oneLevel.get(j);
				if (str.equals(ext.TreenodeName)) {
					indexs[0] = i;
					indexs[1] = j;
					return indexs;
				}
			}
		}

		return indexs;
	}
	
	/**
	 * 插入孤立节点
	 * @param rootTree
	 * @param levelCount
	 * @param ext
	 */
	private void addNode(List<ArrayList<extend>> rootTree, int levelCount[], extend ext) {
		
		List<extend> array = new ArrayList<extend>();
		Iterator<extend> iterator = rootTree.get(levelCount[0]).iterator();
		for (int i = 0; i <= rootTree.get(levelCount[0]).size(); i++) {
			if (i == (levelCount[1] + 1)) {
				array.add(ext);
			} else {
				array.add(iterator.next());
			}
		}

		rootTree.get(levelCount[0]).clear();
		for (int i = 0; i < array.size(); i++) {
			rootTree.get(levelCount[0]).add(array.get(i));
		}
	}
	
	
}
