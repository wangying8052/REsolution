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
import java.util.Map;

public class Suggestions {
	Map<String, UndoClass> undoClasses = new HashMap<String, UndoClass>  ();
	Map<String, UndoEntity> UndoEntities = new HashMap<String, UndoEntity> ();
	ArrayList<UndoEntity>  UndoEntitySort = new ArrayList<UndoEntity>();//左边的
	ArrayList<UndoClass>  undoClasseSort = new ArrayList<UndoClass>();//右边的
	double[] Q_Orig = new double[undoClasseSort.size()+UndoEntitySort.size()+1];//原始曲线
	double[] Q_Undo = new double[undoClasseSort.size()+UndoEntitySort.size()+1];//有撤销之后的曲线
	
	
	private int undoClassLength = 0;
	private int undoEntityLength = 0;
	
	public Map<String, UndoClass> getUndoClasses() {
		return undoClasses;
	}
	public void setUndoClasses(Map<String, UndoClass> undoClasses) {
		this.undoClasses = undoClasses;
	}
	public Map<String, UndoEntity> getUndoEntities() {
		return UndoEntities;
	}
	public void setUndoEntities(Map<String, UndoEntity> undoEntities) {
		UndoEntities = undoEntities;
	}
	public ArrayList<UndoEntity> getUndoEntitySort() {
		return UndoEntitySort;
	}
	public void setUndoEntitySort(ArrayList<UndoEntity> undoEntitySort) {
		UndoEntitySort = undoEntitySort;
	}
	public ArrayList<UndoClass> getUndoClasseSort() {
		return undoClasseSort;
	}
	public void setUndoClasseSort(ArrayList<UndoClass> undoClasseSort) {
		this.undoClasseSort = undoClasseSort;
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
	public int getUndoClassLength() {
		return undoClassLength;
	}
	public void setUndoClassLength(int undoClassLength) {
		this.undoClassLength = undoClassLength;
	}
	public int getUndoEntityLength() {
		return undoEntityLength;
	}
	public void setUndoEntityLength(int undoEntityLength) {
		this.undoEntityLength = undoEntityLength;
	}
	
	
}
