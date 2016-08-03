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

package com.neu.utils.listener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import com.Refactor.NonInheritance.CMN;
import com.jeantessier.dependencyfinder.gui.REsolution;
import com.neu.utils.component.CBScrollBarUI;
import com.neu.utils.component.HermalMatrixPanel;
import com.neu.utils.component.TabbedPanel;
import com.neu.utils.data.TabName;

public class ControlButton extends MouseAdapter implements ActionListener{

	private REsolution model = null;
	List<List<String>> mergeSetList = null;
	
	public ControlButton(REsolution model){
		this.model = model;
	}
	
	public ControlButton(REsolution model,List<List<String>> mergeSetList){
		this.model = model;
		//准备数据
		this.mergeSetList = mergeSetList;
				
	}
		
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("Undo")) {
			model.batchCheckBoxs(false);
		}
		if (e.getActionCommand().equals("CheckAll")) {
			model.batchCheckBoxs(true);
		}
		if (e.getActionCommand().indexOf("detail")>=0) {
			String detailButtonCommand = e.getActionCommand();
			
			int index = Integer.parseInt(detailButtonCommand.split("_")[1]);
			buildhermalmatrixPanel(index);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (e.getClickCount()==2) {//对双击事件进行相应，并且取得被双击的checkbox的索引
			
			JCheckBox box = (JCheckBox)e.getComponent();
			int index = Integer.parseInt(box.getText().split("_")[1]);
			buildDisplayUnicomFilmPanel(index);
		}
		
	}
	
	private void buildhermalmatrixPanel(int buttionIndex) {
		
		JPanel resultPanel = new JPanel(new BorderLayout());
		
		JPanel titlePanel = new JPanel();
		titlePanel.add(new JLabel("Four types of weighted coupling matrixes of Class Component "+buttionIndex));
		
		JPanel centerPanel = new JPanel(new GridLayout(2,2));
		
		ArrayList<double[][]> dataList = null;
		try {
			dataList = CMN.DrawCCMatrix(buttionIndex-1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		centerPanel.add(buildHermalMatrixPanel(dataList.get(0),"SAW Matrix"));
		centerPanel.add(buildHermalMatrixPanel(dataList.get(1),"MIW Matrix"));
		centerPanel.add(buildHermalMatrixPanel(dataList.get(2),"FCW Matrix"));
		centerPanel.add(buildHermalMatrixPanel(dataList.get(3),"SSW Matrix"));
		
		
		resultPanel.add(titlePanel,BorderLayout.NORTH);
		resultPanel.add(centerPanel,BorderLayout.CENTER);
		
		JTabbedPane pane =this.model.getResultRightPanelJTabbledPane();
		pane.add(TabName.HM,resultPanel);
		int index = TabbedPanel.getCount();
		pane.setTabComponentAt(index, new TabbedPanel(pane,model));
		pane.setSelectedIndex(index);
		
	}

	
	private Component buildHermalMatrixPanel(double[][] ds, String title) {
		
		JPanel resultPanel = new JPanel(new BorderLayout());
		

		JPanel w = new JPanel();
		w.add(new JLabel(" "));
		JPanel e = new JPanel();
		e.add(new JLabel(" "));
		JPanel s = new JPanel();
		s.add(new JLabel(title));
		JPanel c = new HermalMatrixPanel(ds);
	
		
		resultPanel.add(w,BorderLayout.WEST);
		resultPanel.add(e,BorderLayout.EAST);
		resultPanel.add(s,BorderLayout.SOUTH);
		resultPanel.add(c,BorderLayout.CENTER);
		
		return resultPanel;
	}

	public void buildDisplayUnicomFilmPanel(int compoentIndex){
		
		JPanel resultPanel = new JPanel(new BorderLayout());
		
		JPanel n = new JPanel();
		n.add(new JLabel(" "));
		JPanel s = new JPanel();
		s.add(new JLabel(" "));
		JPanel w = new JPanel();
		w.add(new JLabel(" "));
		w.add(new JLabel(" "));
		w.add(new JLabel(" "));
		w.add(new JLabel(" "));
		w.add(new JLabel(" "));
		w.add(new JLabel(" "));

		JPanel e = new JPanel();
		e.add(new JLabel(" "));
		e.add(new JLabel(" "));
		e.add(new JLabel(" "));
		e.add(new JLabel(" "));
		e.add(new JLabel(" "));
		e.add(new JLabel(" "));
	
		JPanel unicomFilmPanel = buildCenterPanel(compoentIndex);
		
		resultPanel.add(n,BorderLayout.NORTH);
		resultPanel.add(s,BorderLayout.SOUTH);
		resultPanel.add(w,BorderLayout.WEST);
		resultPanel.add(e,BorderLayout.EAST);
		resultPanel.add(unicomFilmPanel,BorderLayout.CENTER);
		
		
		JTabbedPane pane =this.model.getResultRightPanelJTabbledPane();
		pane.add(TabName.CC,resultPanel);
		int index = TabbedPanel.getCount();
		pane.setTabComponentAt(index, new TabbedPanel(pane,model));
		pane.setSelectedIndex(index);
		
		
		
	}

	private JPanel buildCenterPanel(int compoentIndex) {
		
		JPanel unicomFilmPanel = new JPanel(new BorderLayout());
		
		JTextArea dependenciesResultArea = new JTextArea();
		JScrollPane result = new JScrollPane(dependenciesResultArea);
		result.getHorizontalScrollBar().setUI(new CBScrollBarUI(Color.gray));
		result.getVerticalScrollBar().setUI(new CBScrollBarUI(Color.gray));
		result.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		
		dependenciesResultArea.setEditable(true);
		String strContent = getDisplayContent(compoentIndex);
		dependenciesResultArea.setText(strContent);
		
		JPanel titlePanel = new JPanel();
		titlePanel.add(new JLabel("The merged entity set of Class Component "+compoentIndex));
		
		unicomFilmPanel.add(titlePanel,BorderLayout.NORTH);
		unicomFilmPanel.add(result,BorderLayout.CENTER);
		
		return unicomFilmPanel;
	}

	
	private String getDisplayContent(int compoentIndex) {
		
		StringBuilder strContent = new StringBuilder();
		if (mergeSetList != null && mergeSetList.size()>0) {
			List<String> methodlist = mergeSetList.get(compoentIndex-1);
			for (int i=0; i<methodlist.size(); i++) {
				String string = methodlist.get(i);
				strContent.append(i+1);
				strContent.append(". ");
				strContent.append(string);
				strContent.append("\r\n");
			}
		}
		return strContent.toString();
	}
}

