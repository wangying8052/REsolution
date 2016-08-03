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


public class extend {
 public int matrixInd = 10000;//在classnamelist中的序号
 public String TreenodeName;
 public ArrayList<Integer> SubClassInd = new  ArrayList<Integer>();//子类的索引
 public ArrayList<Integer> SuperClassInd = new  ArrayList<Integer>();  //父类的索引
 public ArrayList<String> SubClassName = new  ArrayList<String>();//子类的类名
 public ArrayList<String> SuperClassName = new  ArrayList<String>();  //父类的类名
 
 public ArrayList<String> OutdependencyClassName = new  ArrayList<String>();//被这个类所依赖所有类的类名
 
 public ArrayList<String> IndependencyClassName = new  ArrayList<String>();//所有依赖这个类的类名
 
 public int DrawlevelNo = 10000;//在可视化时，该类应处于的层级
 
 public double DeltaQ = 0;
 public int move = 0;
 
 public boolean split = false; //是否被分解了
 
 public boolean interfaceornot = false;   //是不是接口
 public ArrayList< ArrayList<String> > cns = new ArrayList< ArrayList<String> >();//包含的属性列表
 
 public boolean NOfuleibeifenjie = false;  //父类节点是否被分解
 public int mainidx = 0;  //主继承树标号
 
 public NodeColor color;
}
