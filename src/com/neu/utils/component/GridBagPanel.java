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
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class GridBagPanel extends  JPanel
{
    private static final long serialVersionUID = -5214441555967215113L;
    private  GridBagLayout gridbag;
    private  GridBagConstraints c;
    private JPanel panel = new JPanel();
    
    public GridBagPanel() {
    	this.setLayout(new BorderLayout());
		this.add(panel,BorderLayout.NORTH);
    	init();
	}
    
    protected void makebutton(Component comp, GridBagLayout gridbag,
            GridBagConstraints c)
    {
        gridbag.setConstraints(comp, c);
        panel.add(comp);
    }

    public void init()
    {
    	
    	 gridbag = new GridBagLayout();
         c = new GridBagConstraints();
         panel.setFont(new Font("SansSerif", Font.PLAIN, 14));
         panel.setLayout(gridbag);
    }

    public void addComp(JCheckBox checkBox,JButton button) {
    	 c.gridwidth = 1;
    	 c.anchor = GridBagConstraints.WEST;
         makebutton(checkBox, gridbag, c);
         c.gridwidth = GridBagConstraints.REMAINDER; //reset to the default
         makebutton(button, gridbag, c);

	}
}
