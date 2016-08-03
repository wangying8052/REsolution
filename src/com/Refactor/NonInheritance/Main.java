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

package com.Refactor.NonInheritance;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import com.Refactor.AdjustCoefficients.Adjust;
import com.Refactor.Inheritance.returnvalue;
import com.Refactor.classparser.*;
import com.jeantessier.dependencyfinder.gui.SrcAction;
import com.mathworks.toolbox.javabuilder.MWException;


public class Main {
	
	static ArrayList<String> buchai = new ArrayList<String>();// 如果一个类内部只包含3个函数，不做拆分
	static ArrayList< ArrayList<String> >jiubuchai = new  ArrayList< ArrayList<String> >();// 如果一个类内部只包含3个函数，不做拆分
	static ArrayList<ArrayList<String>> methodbukefenlocal = new ArrayList<ArrayList<String>>();
	static double OriQ = 0;
	static double RefQ = 0;
	static double[][] A;
	public static void mainNoninheritance() throws SAXException, IOException, MWException {
		
		 NonInheritanceRefactoring.RefactorNonIheritance(NonInheritanceRefactoring.a, NonInheritanceRefactoring.b, NonInheritanceRefactoring.c, NonInheritanceRefactoring.d);//非继承体系重构			 
	}
}
