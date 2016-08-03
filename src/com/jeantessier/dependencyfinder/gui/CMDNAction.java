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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.dom4j.DocumentException;

import com.Refactor.NonInheritance.CMN;
import com.neu.utils.component.CBScrollBarUI;
import com.neu.utils.component.GridBagPanel;
import com.neu.utils.component.HermalMatrixPanel;
import com.neu.utils.component.RadialGraphView;
import com.neu.utils.component.TabbedPanel;
import com.neu.utils.data.Node;
import com.neu.utils.data.TabName;
import com.neu.utils.io.FileUtils;
import com.neu.utils.listener.ControlButton;
import com.neu.utils.service.ButtonStyle;
import com.neu.utils.service.CMDNUtil;
import com.sun.media.sound.ModelAbstractChannelMixer;

public class CMDNAction extends AbstractAction implements Runnable{	
	
	//联通片
	
	public static boolean flag = false;
	
	private REsolution model = null;

    public CMDNAction(REsolution model) {
    	
        this.model = model;

        putValue(Action.LONG_DESCRIPTION, "Construct CMDN and do refactoring preprocessing");
        putValue(Action.NAME, "CMDN");
        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("icons/CMDNGreen.png")));
    }

    public void actionPerformed(ActionEvent e) {
    	
    	if (e.getSource().toString().contains("JToolBar")) {
    		new Thread(this).start();
		}
    }

   
	public void run(){		
		buildNetworks();
	}

	private void buildNetworks() {
		if (flag == false) {
			
			buildOptionPanel();
			
			JPanel resultPanel = buildResultPanelTabbedPane();
			JTabbedPane pane = model.getResultRightPanelJTabbledPane();
			pane.add(TabName.RN, resultPanel);
			
			int index = TabbedPanel.getCount();
			pane.setTabComponentAt(index,new TabbedPanel(pane,model));
			pane.setSelectedIndex(index);
			this.setEnabled(false);
			flag = true;
		}
		model.activeNext(this);
		model.activeLast(2);
	}

	private JPanel buildOptionPanel() {
		JPanel jPanel = model.getControlCheckjPanel();
		//准备数据
		String fileName = "method_list.xml";
		List<List<String>> unicomFilmList = null;
		try {
			unicomFilmList = CMDNUtil.getUnicomFilm(fileName);
		} catch (DocumentException e) {
			JOptionPane.showConfirmDialog(null, 
					"Excuse me,please restruction software, firstly!", 
					"system tips", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
		}

//		 创建checkboxLIst
		List<JCheckBox> checkBoxList = model.getCheckBoxList();
		List<JButton> buttonList = model.getButtonList();
		
		if (unicomFilmList != null && unicomFilmList.size() > 0) {
			ControlButton button = new ControlButton(this.model, unicomFilmList);

			for (int i = 1; i <= unicomFilmList.size(); i++) {
				final JCheckBox jCheckBox = new JCheckBox();
				jCheckBox.setText("Compoent_" + i);
				jCheckBox.addMouseListener(button);
				jCheckBox.setSelected(true);				
				
				checkBoxList.add(jCheckBox);
				
				ImageIcon icon=new ImageIcon(getClass().getResource("icons/detail.png"));
				JButton detailButton = new JButton(icon);
				detailButton.setToolTipText("Visualize weighted coupling matrixes");
				ButtonStyle buttonStyle = new ButtonStyle();
				buttonStyle.setButtonStyle(detailButton);
				
				detailButton.addActionListener(button);
				detailButton.setActionCommand("detail_"+i);
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
	        
//	        checkPanel.setPreferredSize(new Dimension(190, checkBoxList.size() * 20));
//	        checkPanel.revalidate();
	        
			jPanel.add(scrollPane);
		}
		return jPanel;
	}

	/**
	 * 构建结果面板右边面板的选项卡面板
	 * @author revo
	 */
	private JPanel buildResultPanelTabbedPane() {
		
		JPanel panel = new JPanel(new GridLayout(1,2));
		
		//准备数据
		String inhentance_DATA = Node.NODE_TYPE_INHERITANCE+"_net.xml";
		String non_inhentance_DATA = Node.NODE_TYPE_NON_INHERITANCE+"_net.xml";
		
		//构建面板内容
		JPanel inhentance;
		JPanel non_inhentance;
		try {
			inhentance = RadialGraphView.getRadialGraphView(inhentance_DATA,
					"name");
			
			non_inhentance = RadialGraphView.getRadialGraphView(
					non_inhentance_DATA, "name");
			
			panel.add(buildLeftPanel(inhentance,"Inheritance Network"));
			panel.add(buildLeftPanel(non_inhentance,"Non-Inheritance Network"));

			return panel;
		} catch (Exception e) {
			
			FileUtils.fileWrite("cnmd.log", e.getMessage());
			
			JOptionPane.showConfirmDialog(null, 
					"Excuse me,Can not found networks data!", 
					"system tips", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
		}
		
		return panel;
	}

	public static JComponent buildLeftPanel(JComponent data,String title){
		
		JPanel result = new JPanel(new BorderLayout());
		
		//中央面板
		JPanel  centerjPanel =new JPanel(new BorderLayout());
		JPanel titlePanel = new JPanel();
		titlePanel.add(new JLabel(title));
		centerjPanel.add(titlePanel,BorderLayout.NORTH);
		centerjPanel.add(data, BorderLayout.CENTER);
		
		//四边面板
		JPanel n = new JPanel();
		JPanel w = new JPanel();
		JPanel e = new JPanel();
		JLabel jLabel_1 = new JLabel(" ");
		JLabel jLabel_3 = new JLabel(" ");
		JLabel jLabel_4 = new JLabel(" ");
		n.add(jLabel_1);
		w.add(jLabel_3);
		e.add(jLabel_4);
		
		
		result.add(n,BorderLayout.NORTH);
//		result.add(buildSouthPanel(),BorderLayout.SOUTH);
		result.add(w,BorderLayout.WEST);
		result.add(e,BorderLayout.EAST);
		result.add(centerjPanel,BorderLayout.CENTER);
		
		return result;
	}
	
	private static Component buildSouthPanel() {
		JPanel result = new JPanel(new BorderLayout());
		
		JLabel filterLabel = new JLabel("Filter: ");
		
		JPanel c = new JPanel(new GridLayout(1,2));
		JPanel densityPanel = new JPanel(new BorderLayout());
		JPanel degreePanel = new JPanel(new BorderLayout());
		JLabel densityLabel = new JLabel("Density");
		JLabel degreeLabel = new JLabel("Degree");
		
		JScrollBar densityBar = new JScrollBar(JScrollBar.HORIZONTAL, 99, 1, 0, 100);
		JScrollBar degreeBar = new JScrollBar(JScrollBar.HORIZONTAL, 99, 1, 0, 100);
		
		
		densityPanel.add(densityLabel,BorderLayout.WEST);
		densityPanel.add(densityBar,BorderLayout.CENTER);
		densityPanel.add(new JPanel(),BorderLayout.EAST);
		
		degreePanel.add(degreeLabel,BorderLayout.WEST);
		degreePanel.add(degreeBar,BorderLayout.CENTER);
		degreePanel.add(new JPanel(),BorderLayout.EAST);
		
		c.add(densityPanel);
		c.add(degreePanel);
		
		result.add(filterLabel,BorderLayout.WEST);
		result.add(c,BorderLayout.CENTER);
		
		return result;
	}
	
}

