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

package com.neu.utils.service;

import java.util.ArrayList;

import javax.swing.JPanel;

import com.Refactor.NonInheritance.Suggestions;
import com.Refactor.NonInheritance.UndoClass;
import com.Refactor.NonInheritance.UndoEntity;
import com.neu.utils.component.DisplayPanel;
import com.neu.utils.data.Data;

public class MethodPanel implements Observer{
	
	private Subject data;
	private DisplayPanel panel;

	public MethodPanel(Subject data) {
		this.data = data;
		data.registerObserver(this);
		
	}
	
	public JPanel buildPanel(){
		
		String contentStr = wrapMethodList();
		
		this.panel = new  DisplayPanel(contentStr);
		
		return panel.buildPanel();
	}

	public void update(Suggestions sg) {
		
		String contentStr = wrapMethodList();
		
		int undoLength = sg.getUndoEntityLength();
		int totallines = sg.getUndoEntitySort().size();
		
		this.panel.repaint(contentStr,totallines,undoLength);
	}

	
	public String wrapMethodList(){
		
		StringBuilder contentStr = new StringBuilder();
		
		Suggestions suggestions =((Data) data).getSg();
		
		ArrayList<UndoEntity>  undoEntitySort = suggestions.getUndoEntitySort();
		
		if (undoEntitySort != null && undoEntitySort.size() > 0) {
			
			for (int i = 0; i < undoEntitySort.size(); i++) {
				UndoEntity undoEntity = undoEntitySort.get(i);
				contentStr.append(i+1);
				contentStr.append(" Move Method: ");
				contentStr.append(undoEntity.getEntityname());
				contentStr.append("\r\n");
			}
			
		} else {
			contentStr.append("There is no need for classes to be refactored.");
		}
				
		return contentStr.toString();
	}
	
}