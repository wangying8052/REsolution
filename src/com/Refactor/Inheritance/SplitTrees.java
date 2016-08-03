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

package com.Refactor.Inheritance;

import java.util.ArrayList;

public class SplitTrees {
	
	public int TreeIndex;//levels中第几个树需要被分解
	int TreeNum;//被分解成几棵树
	ArrayList<ArrayList<ArrayList<extend>>> levelSplit = new ArrayList<ArrayList<ArrayList<extend>>>();//这个棵树分解的结构
	public double detaQ = 0;
	public int move = 0;//cost
	public int getTreeNum() {
		return TreeNum;
	}
	public ArrayList<ArrayList<ArrayList<extend>>> getLevelSplit() {
		return levelSplit;
	}
	public double getDetaQ() {
		return detaQ;
	}
}
