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

package com.neu.utils.data;

import java.util.ArrayList;
import java.util.List;

public class ClassNode extends Dot{
	public String name;
	List<String> parentList;
	List<String> OutdependencyList;
	
	private ArrayList< ArrayList<String> > cns;
	
	DotType type;

	public ClassNode(int x, int y, int width, int hight) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = hight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getParentList() {
		return parentList;
	}

	public void setParentList(List<String> parentList) {
		this.parentList = parentList;
	}

	public List<String> getOutdependencyList() {
		return OutdependencyList;
	}

	public void setOutdependencyList(List<String> outdependencyList) {
		OutdependencyList = outdependencyList;
	}

	public DotType getType() {
		return type;
	}

	public void setType(DotType type) {
		this.type = type;
	}

	public ArrayList<ArrayList<String>> getCns() {
		return cns;
	}

	public void setCns(ArrayList<ArrayList<String>> cns) {
		this.cns = cns;
	}

	
	
}