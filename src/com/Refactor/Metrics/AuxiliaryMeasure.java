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

package com.Refactor.Metrics;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AuxiliaryMeasure {
	public ArrayList<ArrayList<String>> BeforeRefactor;
	public ArrayList<ArrayList<String>> ExtendRefactor;
	int[][] BeforeRefactorInheritance;
	int[][] ExtendRefactorInheritance;
	
	
	public ArrayList<ArrayList<String>> getBeforeRefactor() {
		return BeforeRefactor;
	}

	public void setBeforeRefactor(ArrayList<ArrayList<String>> beforeRefactor) {
		BeforeRefactor = beforeRefactor;
	}

	public ArrayList<ArrayList<String>> getExtendRefactor() {
		return ExtendRefactor;
	}

	public void setExtendRefactor(ArrayList<ArrayList<String>> extendRefactor) {
		ExtendRefactor = extendRefactor;
	}

	public int[][] getBeforeRefactorInheritance() {
		return BeforeRefactorInheritance;
	}

	public void setBeforeRefactorInheritance(int[][] beforeRefactorInheritance) {
		BeforeRefactorInheritance = beforeRefactorInheritance;
	}

	public int[][] getExtendRefactorInheritance() {
		return ExtendRefactorInheritance;
	}

	public void setExtendRefactorInheritance(int[][] extendRefactorInheritance) {
		ExtendRefactorInheritance = extendRefactorInheritance;
	}
	
}
