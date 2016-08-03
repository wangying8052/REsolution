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

package com.neu.utils.data;

public class Node {
	
	/**
	 * node type of Inhentance Network
	 */
	public static final String NODE_TYPE_INHERITANCE="inheritance";
	
	/**
	 * node type of Inhentance Network
	 */
	public static final String NODE_TYPE_NON_INHERITANCE="non_inheritance";
	
	private int id;
	private String text;
	private String type;
	
	public Node() {
	}

	
	public Node(int id) {
		this.id = id;
	}

	public Node(int id, String text) {
		this.id = id;
		this.text = text;
	}
	
	

	public Node(int id, String text, String type) {
		this.id = id;
		this.text = text;
		this.type = type;
	}


	public int getId() {
		return id;
	}

//	public void setId(int id) {
//		this.id = id;
//	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
