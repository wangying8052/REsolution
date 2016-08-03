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

package com.neu.utils.service;

import java.util.ArrayList;

import javax.swing.JPanel;

import com.Refactor.NonInheritance.Suggestions;
import com.Refactor.NonInheritance.UndoClass;
import com.neu.utils.component.DisplayPanel;
import com.neu.utils.data.Data;

public class ClassPanel implements Observer{
	
	private Subject data;
	private DisplayPanel panel;

	public ClassPanel(Subject data) {
		this.data = data;
		data.registerObserver(this);
		
	}
	
	public JPanel buildPanel(){
		
		String contentStr = wrapClassList();
		
		this.panel = new  DisplayPanel(contentStr);
		
		return panel.buildPanel();
	}

	public void update(Suggestions sg) {

		String contentStr = wrapClassList();
		
		int undoLines = sg.getUndoClassLength();
		int totallines = sg.getUndoClasseSort().size();
		
		this.panel.repaint(contentStr,totallines,undoLines);
	}
	
	public String wrapClassList(){
		
		StringBuilder contentStr = new StringBuilder();
		
		Suggestions suggestions =((Data) data).getSg();
		
		int entitySize = suggestions.getUndoEntitySort().size();
		
		ArrayList<UndoClass>  undoClasseSort= suggestions.getUndoClasseSort();
		
		if (undoClasseSort != null && undoClasseSort.size() > 0) {
			for (int i = 0; i < undoClasseSort.size(); i++) {
				UndoClass undoClass = undoClasseSort.get(i);
				
				contentStr.append(i+entitySize+1);
				contentStr.append(" Extract Class: ");
				contentStr.append(undoClass.getClassname());
				contentStr.append("\r\n");
			}
			
		} else {
			contentStr.append("There is no need for classes to be refactored.");
		}
				
		return contentStr.toString();
	}
	
}