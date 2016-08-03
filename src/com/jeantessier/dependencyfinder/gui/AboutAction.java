/*
****************************************************
* REsolution is an automatic software refactoring tool      
****************************************************
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

import java.awt.event.*;
import javax.swing.*;

import com.jeantessier.dependencyfinder.*;

public class AboutAction extends AbstractAction {
    private JFrame model;

    public AboutAction(JFrame model) {
        this.model = model;

        putValue(Action.LONG_DESCRIPTION, "Show version information");
        putValue(Action.NAME, "About");
        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("icons/logoicon.gif")));
    }

    public void actionPerformed(ActionEvent e) {
//        Version version = new Version();

        Object[] message = new Object[2];
        message[0] = "<html><b>" + "REsolution" + "</b></html>";
        message[1] = "<html>"
        		+"<br>/*"
        		+ "*****************************************************************************<br>"
        		+"* REsolution is an automatic software refactoring tool<br>"
        		+"******************************************************************************<br>"
        		+"*  Copyright (c) 2016, Wang Ying, Yin Hongjian<br>"
        		+"*  E-mail: wangying8052@163.com<br>"
        		+"*  All rights reserved.<br>"
        		+"*  <br>"
        		+"*  This file is part of REsolution.<br>"
        		+"*  <br>"
        		+"*  REsolution is free software: you can redistribute it and/or modify<br>"
        		+"*  it under the terms of the GNU General Public License as published by<br>"
        		+"*  the Free Software Foundation, either version 3 of the License, or<br>"
        		+"*  (at your option) any later version.<br>"
        		+"*  <br>"
        		+"*  REsolution is distributed in the hope that it will be useful,<br>"
        		+"*  but WITHOUT ANY WARRANTY; without even the implied warranty of<br>"
        		+"*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br>"
        		+"*  GNU General Public License for more details.<br>"
        		+"*  <br>"
        		+"*  You should have received a copy of the GNU General Public License<br>"
        		+"*  along with REsolution.  If not, see <a href='http://www.gnu.org/licenses/'>http://www.gnu.org/licenses/</a>.<br>"
        		+"*/ <br>"
        		+"</html>";
      

        String title = "About REsolution";

        Icon icon = new ImageIcon(getClass().getResource("icons/logoicon.gif"));

//        JOptionPane.showMessageDialog(model, message, title, JOptionPane.INFORMATION_MESSAGE, icon);
        JOptionPane.showMessageDialog(model, message, title, JOptionPane.INFORMATION_MESSAGE, icon);
    }
}
