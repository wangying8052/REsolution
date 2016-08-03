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

package com.Refactor.NonInheritance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Refactor.classparser.*;
import com.jeantessier.dependencyfinder.gui.SrcAction;




public class change {

	
	public static ArrayList<String> FilterClasses(){
		ArrayList<String> Fclassname = new ArrayList<String>();
		for(int i = 0; i < SrcAction.classname.size(); i++){
			if(SrcAction.classesMap.containsKey(SrcAction.classname.get(i))){
				Fclassname.add(SrcAction.classname.get(i));
			}
		}
		return Fclassname;
	}
	
}
