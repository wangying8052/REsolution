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

import java.io.IOException;

import org.xml.sax.SAXException;

import com.mathworks.toolbox.javabuilder.MWException;
import com.neu.utils.listener.ProgressBarLisenter;

public class MainInheritance {

	public static void InheritanceOperations(ProgressBarLisenter barLisenter)throws SAXException, IOException, MWException{
		RefactorInheritance.InheritanceTreesOperations(barLisenter);
	}
}
