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

package com.neu.utils.component;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.Refactor.NonInheritance.Suggestions;
import com.neu.utils.data.Data;
import com.neu.utils.data.Dot;
import com.neu.utils.data.Line;
import com.neu.utils.data.PolyLineTable;
import com.neu.utils.service.Observer;
import com.neu.utils.service.Subject;

public class TablePanel extends JPanel implements Observer{
	
	private int LEFT_BORDER = (int)(this.getWidth()/5);
	private int RIGHT_BORDER = 40;
	private int TOP_BORDER = 40;
	private int BOTTOM_BORDER = 50;
	private PolyLineTable table;
	private Subject data;
	private Graphics2D g2d;
	private boolean flag =false;
	
	public TablePanel(Subject data) {
		this.data = data;
		this.data.registerObserver(this);
		
		this.setBackground(Color.white);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		g2d = (Graphics2D) g;
		
		table  = new PolyLineTable(getSuggestions(),"Weighted Moduiarity");
		
		drawTable(g2d,table);
		drawDataOrig(g2d,table);
		if (flag == true) {
			drawDataUndo(g2d, table);
		}
		
		
	}

	private void drawDataUndo(Graphics g, PolyLineTable table) {
		
		List<Dot> dotList = table.getUndoAfterDotList(this.LEFT_BORDER,this.TOP_BORDER,table.getqUndoValueList());
		List<Line> lineList = table.getLineUndoAfaterList(dotList);

		drawData((Graphics2D)g,table,dotList,lineList,new Color(255, 149, 184));
		
	}

	private void drawDataOrig(Graphics2D g, PolyLineTable table) {
		
		List<Dot> dotList = table.getDotList(this.LEFT_BORDER,this.TOP_BORDER,table.getqValueList());
		List<Line> lineList = table.getLineList(dotList);
		
		drawData(g,table,dotList,lineList,new Color(10,111,40));
		
	}
	
	private void drawData(Graphics2D g, PolyLineTable table,List<Dot> dotList,List<Line> lineList,Color color){
		
		g.setColor(color);
		for (Dot dot : dotList) {
			g.fillRect(dot.getX(), dot.getY(), dot.getWidth(), dot.getHeight());
		}
		for (Line line : lineList) {
			g.setStroke(new BasicStroke(2));
			g.drawLine(line.getsDot().getX()+table.getDotWidth()/2, line.getsDot().getY()+table.getDotWidth()/2, 
					line.gettDot().getX()+table.getDotHeight()/2, line.gettDot().getY()+table.getDotHeight()/2);
		}
	}
	
	
	private void drawTable(Graphics g, PolyLineTable table) {
		initBorder();
		drawTableBorder(g,table);
		drawXAxis(g,table);
		drawYAxis(g,table);
		drawTitle(g,table);
	}

	private void drawTitle(Graphics g, PolyLineTable table) {
		g.setFont(new Font("Times New Roman", 1, 14));
		g.drawString(table.getTitle(), this.getWidth()/2-50, (TOP_BORDER*2)/3); 
	}

	private void drawXAxis(Graphics g, PolyLineTable table) {
		
		int x = LEFT_BORDER;
		int y = this.getHeight()-BOTTOM_BORDER+15;
		
		int stepColumn = table.calculateColumnStep(this.getWidth()-RIGHT_BORDER-LEFT_BORDER);
		for (int i = 0; i <= table.getColumnCount(); i++) {
			g.setFont(new Font("Times New Roman", 1, 15));
			g.drawString(i*table.getStepColumnValue()+"", x, y);
			x += stepColumn;
		}
		 
	}

	private void drawYAxis(Graphics g, PolyLineTable table) {
		
		int x = LEFT_BORDER-45;
		int y = this.getHeight()-BOTTOM_BORDER+5;
		double yValue = (double)Math.round(table.getMinValue()*10000)/10000;
				
		int stepRow = table.calculateRowStep(this.getHeight()-BOTTOM_BORDER-TOP_BORDER);
		for (int i = 0; i <= table.getRowCount(); i++) {
			g.setFont(new Font("Times New Roman", 1, 15));
			g.drawString(yValue+"", x, y);			
			yValue = (double)Math.round((yValue +table.getStepRowValue())*10000)/10000;
			y -= stepRow;
		}
		
	}
	
	private void drawTableBorder(Graphics g, PolyLineTable table) {
				
		g.drawLine(LEFT_BORDER, TOP_BORDER, this.getWidth()-RIGHT_BORDER, TOP_BORDER);
		int stepRow = table.calculateRowStep(this.getHeight()-BOTTOM_BORDER-TOP_BORDER);
		for (int i = 1; i < table.getRowCount(); i++) {
			drawRow(g,table,LEFT_BORDER, TOP_BORDER+(stepRow*i), this.getWidth()-RIGHT_BORDER, TOP_BORDER+(stepRow*i));
		}
		g.drawLine(LEFT_BORDER, this.getHeight()-BOTTOM_BORDER, this.getWidth()-RIGHT_BORDER, this.getHeight()-BOTTOM_BORDER);
		
		
		g.drawLine(LEFT_BORDER, TOP_BORDER, LEFT_BORDER, this.getHeight()-BOTTOM_BORDER);
		int stepColumn = table.calculateColumnStep(this.getWidth()-RIGHT_BORDER-LEFT_BORDER);
		for (int i = 1; i < table.getColumnCount(); i++) {
			drawColumn(g,table,LEFT_BORDER+(stepColumn*i), TOP_BORDER, LEFT_BORDER+(stepColumn*i), this.getHeight()-BOTTOM_BORDER);
		}
		g.drawLine(this.getWidth()-RIGHT_BORDER, TOP_BORDER, this.getWidth()-RIGHT_BORDER, this.getHeight()-BOTTOM_BORDER);
	}

	

	private void drawRow(Graphics g, PolyLineTable table ,int x0, int y0,int x1,int y1) {
		
		int step = (x1-x0)/(table.getColumnCount()*10);
		do{
			g.drawLine(x0, y0, x0+step, y1);
			x0+=step*2;	
		}while(x0<x1);
		
	}
	private void drawColumn(Graphics g, PolyLineTable table, int x0, int y0,int x1,int y1) {
		
		int step = (y1-y0)/(table.getRowCount()*10);
		do{
			g.drawLine(x0, y0, x0, y0+step);
			y0+=step*2;	
		}while(y0<y1);
		
	}

	private void initBorder() {
		LEFT_BORDER = (int)(this.getWidth()/15);
		RIGHT_BORDER = (int)(this.getWidth()/20);
		TOP_BORDER = (int)(this.getHeight()/17);
		BOTTOM_BORDER = (int)(this.getHeight()/17);
	}

	public void update(Suggestions sg) {
		this.setSuggestions(sg);
		flag = true;
		this.paint(this.getGraphics());
	}

	private Suggestions getSuggestions() {
		return ((Data)this.data).getSg();
	}
	private void setSuggestions(Suggestions sg) {
		((Data)this.data).setSg(sg);
	}
}
