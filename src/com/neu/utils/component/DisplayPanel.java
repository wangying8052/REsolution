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

package com.neu.utils.component;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class DisplayPanel extends JPanel{
	
	private String contentStr;
	private TextPanel scrollPane;
	
	public DisplayPanel(){
		
	}
	
	public DisplayPanel(String str){
		this.contentStr = str;
	}
	
	public JPanel buildPanel() {
		// 中间显示面板
		JPanel centerPanel = new JPanel(new BorderLayout());

		JPanel guidePanel = buildGuidePanel();
		
		scrollPane = new TextPanel();
		scrollPane.setText(contentStr);

		centerPanel.add(guidePanel, BorderLayout.WEST);
		centerPanel.add(scrollPane, BorderLayout.CENTER);
				
		return centerPanel;
	}
	
	/**
	 * 构建排列顺序导航箭头面板
	 * @return
	 */
	private JPanel buildGuidePanel() {

		JPanel guidePanel = new JPanel(new BorderLayout());
		JLabel qLabel = new JLabel("△Q");
		JLabel lowerLabel = new JLabel("lower");

		JPanel jPanel_north = new JPanel();
		jPanel_north.add(qLabel);
//		jPanel_north.setBackground(Color.white);
		JPanel jPanel_south = new JPanel();
		jPanel_south.add(lowerLabel);
//		jPanel_south.setBackground(Color.white);

		guidePanel.add(jPanel_north, BorderLayout.NORTH);
		guidePanel.add(new ArrowLinePanel(), BorderLayout.CENTER);
		guidePanel.add(jPanel_south, BorderLayout.SOUTH);

		return guidePanel;
	}

	public String getContentStr() {
		return contentStr;
	}

	public void setContentStr(String contentStr) {
		this.contentStr = contentStr;
	}

	public void repaint(String contentStr, int totalLines, int undoLines) {
		
		scrollPane.setText(contentStr,totalLines, undoLines);
		this.repaint();
	}
	
}
