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

package com.Refactor.Inheritance;

import java.util.ArrayList;

import com.Refactor.NonInheritance.Suggestions;

public class InheritSuggestions extends Suggestions {

	public static ArrayList<SplitTrees> SplitTree = new ArrayList<SplitTrees>();
	private double[] Q_Orig = new double[SplitTree.size()+1];//原始曲线
	private double[] Q_Undo = new double[SplitTree.size()+1];//有撤销之后的曲线
	
	private int undoLength = 0;
	
	public InheritSuggestions() {
		super();
	}
	
	public InheritSuggestions(ArrayList<SplitTrees> splitTree, double[] q_Orig,
			double[] q_Undo) {
		super();
		SplitTree = splitTree;
		Q_Orig = q_Orig;
		Q_Undo = q_Undo;
	}

	public ArrayList<SplitTrees> getSplitTree() {
		return SplitTree;
	}

	public void setSplitTree(ArrayList<SplitTrees> splitTree) {
		SplitTree = splitTree;
	}

	public double[] getQ_Orig() {
		return Q_Orig;
	}

	public void setQ_Orig(double[] q_Orig) {
		Q_Orig = q_Orig;
	}

	public double[] getQ_Undo() {
		return Q_Undo;
	}

	public void setQ_Undo(double[] q_Undo) {
		Q_Undo = q_Undo;
	}

	public int getUndoLength() {
		return undoLength;
	}

	public void setUndoLength(int undoLength) {
		this.undoLength = undoLength;
	}
	
	
}
