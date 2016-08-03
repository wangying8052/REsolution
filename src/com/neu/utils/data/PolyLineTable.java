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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.Refactor.NonInheritance.Suggestions;

public class PolyLineTable extends Table{
	
	private int stepRowPX;
	private int stepColumnPX;
	private double stepRowValue;
	private int stepColumnValue;
	private double maxValue;
	private double minValue;
	
	private int dotWidth = 8;
	private int dotHeight = 8;
	
	String title;
	
	private List<Double> qValueList;
	private List<Double> qUndoValueList;
	
	private Suggestions sg;
	
	public PolyLineTable() {
	}
	
	public PolyLineTable(Suggestions sg, String title) {
		this.sg = sg;
		this.title = title;
		initTable();
	}

	private void initTable() {
		calculateColumn(this.sg);
		calculateRow(this.getQValue(this.sg));
	}

	public PolyLineTable(int rowCount, int columnCount) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
	}
	
	public PolyLineTable(int rowCount, int columnCount, String title) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.title = title;
	}

	public List<Double> getqValueList() {
		this.qValueList = getQValue(this.sg);
		
		return qValueList;
	}

	public void setqValueList(List<Double> qValueList) {
		this.qValueList = qValueList;
	}

	public List<Double> getqUndoValueList() {
		this.qUndoValueList = getQUndoValue(this.sg);
		return qUndoValueList;
	}

	public void setqUndoValueList(List<Double> qUndoValueList) {
		this.qUndoValueList = qUndoValueList;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Suggestions getSg() {
		return sg;
	}

	public void setSg(Suggestions sg) {
		this.sg = sg;
	}

	public int getStepRowPX() {
		return stepRowPX;
	}

	public void setStepRowPX(int stepRow) {
		this.stepRowPX = stepRow;
	}

	public int getStepColumnPX() {
		return stepColumnPX;
	}

	public void setStepColumnPX(int stepColumn) {
		this.stepColumnPX = stepColumn;
	}
	
	public double getStepRowValue() {
		return (double)Math.round(stepRowValue*10000)/10000;
	}

	public void setStepRowValue(double stepRowValue) {
		this.stepRowValue = stepRowValue;
	}

	public int getStepColumnValue() {
		return stepColumnValue;
	}

	public void setStepColumnValue(int stepColumnValue) {
		this.stepColumnValue = stepColumnValue;
	}
	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}
	
	public int getDotWidth() {
		return dotWidth;
	}

	public void setDotWidth(int dotWidth) {
		this.dotWidth = dotWidth;
	}

	public int getDotHeight() {
		return dotHeight;
	}

	public void setDotHeight(int dotHeight) {
		this.dotHeight = dotHeight;
	}

	public List<Dot> getDotList(int LEFT_BORDER,int TOP_BORDER,List<Double> qValueList) {
		
		return getNots(LEFT_BORDER, TOP_BORDER, qValueList);
	}

	public List<Dot> getUndoAfterDotList(int LEFT_BORDER,int TOP_BORDER,List<Double> qUndoValueList) {
		
		return getNots(LEFT_BORDER, TOP_BORDER, qUndoValueList);
	}
	
	public List<Line> getLineList(List<Dot> dotList) {
		
		return getLines(dotList);
	}	
	public List<Line> getLineUndoAfaterList(List<Dot> dotUndoList) {
		
		return getLines(dotUndoList);
	}
	
	public int calculateRowStep(int lineLenth) {
		this.stepRowPX = (int)(lineLenth/this.getRowCount()+0.5);
		return this.stepRowPX;
	}

	public int calculateColumnStep(int lineLenth) {
		this.stepColumnPX = (int)(lineLenth/this.getColumnCount()+0.5);		
		return this.stepColumnPX;
	}
	
	private List<Dot> getNots(int LEFT_BORDER, int TOP_BORDER,
			List<Double> qValueList) {
		List<Dot> dotList = new ArrayList<Dot>();
		for (int i = 0; i < qValueList.size(); i++) {
			Double qValue = qValueList.get(i);
			
			Dot dot = calculateDot(i,qValue,LEFT_BORDER,TOP_BORDER);
			dotList.add(dot);
		}
		return dotList;
	}
	private List<Line> getLines(List<Dot> dotList) {
		List<Line> lineList = new ArrayList<Line>();
		for (int i = 0; i < dotList.size()-1; i++) {
			Dot sDot = dotList.get(i);
			Dot tDot = dotList.get(i+1);
			Line line = new Line(sDot, tDot);
			lineList.add(line);
		}
		return lineList;
	}
	
	
	private Dot calculateDot(int xValue, double qValue, int LEFT_BORDER, int TOP_BORDER) {
		int x = (int)(new Float(this.getStepColumnPX() * xValue)/this.stepColumnValue + LEFT_BORDER);
		int y = (int)(this.getStepRowPX() * ( this.maxValue- qValue)/this.stepRowValue+ TOP_BORDER);		
		Dot dot = new Dot(x - this.dotWidth/2, y - this.dotHeight/2, this.dotWidth, this.dotHeight);
		return dot;
	}
	
	private void calculateColumn(Suggestions sg){
		
		int column = sg.getUndoEntitySort().size()+sg.getUndoClasseSort().size();
		
		if (column<10) {
			this.stepColumnValue = 1;
			this.columnCount = 10;
		} else {
			float realStep = (new Float(column))/10;
			this.stepColumnValue =(int)(realStep+0.5);
				
			if (realStep>this.stepColumnValue) {//小于四  舍去
				Float stepfloat = new Float(this.stepColumnValue);
					
				float difference =(float)Math.round((realStep-stepfloat)*100)/100;//保留两位小数
				
				int increament = (int)Math.ceil((difference*10)/this.stepColumnValue);
				
				this.columnCount = increament+10;
			} else {
				this.columnCount = 10;
			}
		}
		
	}
	
	private void calculateRow(List<Double> qValueList) {
		this.maxValue = Collections.max(qValueList);
		this.minValue = Collections.min(qValueList);
	
		this.stepRowValue = ((double)Math.round((this.maxValue - this.minValue)*10000)/10000)/10;
				
		this.rowCount = 10;
	}

	private List<Double> getQValue(Suggestions sg) {
		double[] qValueArray = sg.getQ_Orig();
		List<Double> qValueList = new ArrayList<Double>();
		
		for (int i = 0; i < qValueArray.length; i++) {
			double d = qValueArray[i];			
			qValueList.add(d);
		}
		
		return qValueList;
	}
	private List<Double> getQUndoValue(Suggestions sg) {
		double[] qUndoValueArray = sg.getQ_Undo();
		List<Double> qUndoValueList = new ArrayList<Double>();
		for (int i = 0; i < qUndoValueArray.length; i++) {
			double d = qUndoValueArray[i];
			qUndoValueList.add(d);
		}
		return qUndoValueList;
	}
}

