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
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ClassContent extends JFrame{
	
	private ArrayList< ArrayList<String> > cns;
	private String name;
	
	public ClassContent() {
		
		init();
	
	}
	public ClassContent(ArrayList< ArrayList<String> > cns) {
		this.cns = cns;
		init();
	}

	public ClassContent(ArrayList<ArrayList<String>> cns, String name) {
		this.cns = cns;
		this.name = name;
		init();
	}
	private void init() {
		
		this.add(buildContentPanel());
		
		this.setTitle("content");
		this.setIconImage(new ImageIcon(getClass().getResource("icons/logoicon.gif")).getImage());
		this.setSize(400, 350);
		this.setLocation(500, 300);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.setAlwaysOnTop(true);
	}

	private Component buildContentPanel() {
		JPanel resultPanel = new JPanel(new BorderLayout());
		
		JTextArea contentArea = new JTextArea();
		contentArea.setMargin(new Insets(5, 5, 5, 5));
		contentArea.setEditable(true);
		
		contentArea.setText(this.getContent(this.cns));
		
		JScrollPane jScrollPane = new JScrollPane(contentArea);
		jScrollPane.getHorizontalScrollBar().setUI(new CBScrollBarUI(Color.gray));
		jScrollPane.getVerticalScrollBar().setUI(new CBScrollBarUI(Color.gray));
		
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		resultPanel.add(jScrollPane,BorderLayout.CENTER);
		
		return resultPanel;
	}
	
	private String getContent(ArrayList<ArrayList<String>> cns) {

		StringBuilder strContent = new StringBuilder();
		strContent.append(name);
		strContent.append("\r\n");
		strContent.append("-----------------------------------------------------");
		strContent.append("\r\n");
		if (cns == null) {
			strContent.append("this is nothing");
		} else {
			for (ArrayList<String> arrayList : cns) {
				for (String string : arrayList) {
					strContent.append(string);
					strContent.append("\r\n");
				}
			}
		}
		return strContent.toString();
	}
}
