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

public class Line{
	
	private Dot sDot;
	private Dot tDot;
	
	public Line() {
	}
	public Line(Dot sDot, Dot tDot) {
		this.sDot = sDot;
		this.tDot = tDot;
	}
	
	public Dot getsDot() {
		return sDot;
	}
	public void setsDot(Dot sDot) {
		this.sDot = sDot;
	}
	public Dot gettDot() {
		return tDot;
	}
	public void settDot(Dot tDot) {
		this.tDot = tDot;
	}
	@Override
	public String toString() {
		return "Line [sDot.x=" + sDot.x +",sDot.y=" + sDot.y + ", tDot.x=" + tDot.x+ ", tDot.y=" + tDot.y + "]";
	}
	
	
}
