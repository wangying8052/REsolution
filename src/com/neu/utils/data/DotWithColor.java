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

import java.awt.Color;

public class DotWithColor extends Dot {
	
	protected int xValue;
	protected double qValue;
	
	protected Color color;
	protected int undoMethod;
	protected int undoClass;
	protected int undoLength;
	protected boolean isClicked = false;

	public DotWithColor(int i, int j, int dotWidth, int dotHeight) {
		super(i, j, dotWidth, dotHeight);
	}

	
	public int getxValue() {
		return xValue;
	}

	public void setxValue(int xValue) {
		this.xValue = xValue;
	}

	public double getqValue() {
		return qValue;
	}

	public void setqValue(double qValue) {
		this.qValue = qValue;
	}



	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getUndoMethod() {
		return undoMethod;
	}

	public void setUndoMethod(int undoMethod) {
		this.undoMethod = undoMethod;
	}

	public int getUndoClass() {
		return undoClass;
	}

	public void setUndoClass(int undoClass) {
		this.undoClass = undoClass;
	}

	
	public int getUndoLength() {
		return undoLength;
	}

	public void setUndoLength(int undoLength) {
		this.undoLength = undoLength;
	}

	public boolean isClicked() {
		return isClicked;
	}

	public void setClicked(boolean isClicked) {
		this.isClicked = isClicked;
	}
	
	
	
}
