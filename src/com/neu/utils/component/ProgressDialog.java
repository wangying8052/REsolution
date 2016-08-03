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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jeantessier.dependencyfinder.gui.REsolution;
import com.sun.awt.AWTUtilities;

public class ProgressDialog implements ChangeListener{

	JFrame container;
	JDialog jj;
	CircleProgressBar progressbar;
    JLabel label;
    Timer timer;
    String title;
    int x = 500;
    int y = 200;
	
	public ProgressDialog(JFrame container,CircleProgressBar progressBar,String title,int x,int y) {
		this.x = x;
		this.y = y;
		this.progressbar = progressBar;
		this.progressbar.addChangeListener(this);
		this.title = title;
		this.container = container;
		initUI(container, this.progressbar);
	}

	public void initUI(JFrame container, JProgressBar progressBar) {
			
		jj = new JDialog(container, "", true);
		Container c = jj.getContentPane();
		c.setLayout(new BorderLayout());
		
		label = new JLabel("ready",JLabel.CENTER);
		label.setBackground(new Color(238, 238, 238));
		c.add(label,BorderLayout.NORTH);
		
		Dimension size = progressBar.getPreferredSize();
		size.width = 80;
		progressBar.setPreferredSize(size);
		progressBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		progressBar.setForeground(new Color(10,111,40));
		
		c.add(progressBar, BorderLayout.CENTER);
		
		jj.setUndecorated(true);
//		jj.setOpacity(0.3f);
		jj.pack();
		jj.setResizable(false);
		jj.setBounds((int)(x*1.0/2)-50, (int)(y*1.0/3), 200, 200);
		jj.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		jj.setVisible(true);
		
	}

	public void dispose() {
		jj.dispose();
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == progressbar && jj != null) {
			int max = progressbar.getMaximum();
			int value = progressbar.getValue();		
			double percent = (double)Math.round(progressbar.getPercentComplete()*100);
			if (value+1 <max && progressbar.isStringPainted() == true) {
				if (label!=null) {
					label.setText(title+": "+percent+"%");
				}
			}else {
				jj.dispose();
			}
		}
		
	}

	public void buildUI() {
		initUI(container, this.progressbar);
	}

}