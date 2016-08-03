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
import java.util.Comparator;

public class Comparatormethodline implements Comparator {

	public int compare(Object arg0, Object arg1) {
		Methodfeature user0 = (Methodfeature) arg0;
		Methodfeature user1 = (Methodfeature) arg1;
		int flag = 0;
		// 首先比较年龄，如果年龄相同，则比较名字
		if (user0.methodlinesbe > user1.methodlinesbe) {
			flag = 1;
		}
		if (user0.methodlinesbe < user1.methodlinesbe) {
			flag = -1;
		}
//		if (user0.methodlinesbe == user1.methodlinesbe)
//			System.out.println("bug!" + user0.methodlinesbe + user0.methodlinesbe);
		return flag;

	}
}
