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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.neu.utils.data.Table;


public class HermalMatrixPanel extends JPanel{
	
	private int TOP_BORDER ;
	private int LEFT_BORDER ;
	private int RIGHT_BORDER ;
	private int BOTTOM_BORDER ;
	
	private int stepRow;
	
	private List<Color> colors;
	
	private Table table;
	double[][] data;
	
	public HermalMatrixPanel(double[][] hm) {
		data = wrapData(hm);
//		this.setBackground(Color.white);
	}
	
	private void initBorder() {
		TOP_BORDER = 15;
		LEFT_BORDER = (int)(this.getWidth()/3.0);
		RIGHT_BORDER = (int)(this.getWidth()/8.0);
		BOTTOM_BORDER = 15;
	}
	
	private double[][] wrapData(double[][] hm) {
		
		for (int i = 0; i < hm.length; i++) {
			double[] ds = hm[i];
			for (int j = 0; j < ds.length; j++) {
				double d = ds[j];
				d = (double) Math.round(d*100)/100;
			}
		}
		
		return hm;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		initBorder();
		
		colors = setColors();
		int length = this.data.length;
		table  = new Table(length,length);
		stepRow = calculateRowStep(this.getHeight()-BOTTOM_BORDER-TOP_BORDER,length);
		
		drawTable(g,table,data);
		drawColorBar(g,RIGHT_BORDER,table);
		this.repaint();
	}
	private int calculateRowStep(int lineLenth, float length) {
		int stepRow = (int)(lineLenth/length+0.5);
		
//		float shang = (lineLenth-stepRow*length)/length;
//		
//		if (shang>0) {
//			stepRow+=shang;
//		}
		return stepRow;
	}

	private void drawTable(Graphics g, Table table, double[][] hm) {
		
		drawHermalMatrix(g,table,hm);
		drawXAxis(g,table);
		drawYAxis(g,table);
	}
	
	private void drawColorBar(Graphics g, int width,Table table) {
		
		int LEFT_BORDER = (int)(width/5.0);
		int RIGHT_BORDER = (int)(width/10.0);
		
		int RGB_WIDTH = (int)((width - LEFT_BORDER - RIGHT_BORDER)*3.0/8);
		
		int step =(int)(stepRow*table.getRowCount()/100.0f+0.5);
		int startX = LEFT_BORDER+this.LEFT_BORDER+table.getColumnCount()*stepRow;
		int startY = TOP_BORDER;
		
		int i;
		
		g.setColor(Color.black);
		g.drawString("1.0", startX+RGB_WIDTH+5, startY);
		
		for (i = 99; i >=0 ; i--) {
			g.setColor(this.colors.get(i));
			g.fillRect(startX, startY+(99-i)*step, RGB_WIDTH, step);
			if (i%20 == 0) {
				g.setColor(Color.black);
				g.drawString(""+(i/100.0f), startX+RGB_WIDTH+5, startY+(99-i)*step);
			}
		}
		
	}
	
	
	private void drawHermalMatrix(Graphics g, Table table, double[][] hm) {
		
		Graphics2D g2= (Graphics2D)g;

		int x = LEFT_BORDER;
		int y = TOP_BORDER;
		for (int i = 0; i < hm.length; i++) {
			double[] ds = hm[i];
			for (int j = 0; j < ds.length; j++) {
				double d = ds[j];
				if (hm.length<=100) {
					g2.setColor(Color.black);
					g2.drawRect(x, y, stepRow, stepRow);
				}
				Color color = setColorByValue(d);
				g2.setColor(color);
				g2.fillRect(x+1, y+1, stepRow, stepRow);
				x += stepRow;
			}
			x=LEFT_BORDER;
			y += stepRow;
		}

	}

	private Color setColorByValue(double d) {
			
		double scope = (double)Math.round(d*100)/100;
		
		if (Math.round(scope*100)*100>=0 && Math.round(scope*100)<100) {			
			Color color = colors.get((int) Math.round(scope*100));
			return color;
		}
		
		return new Color(0,0,0.5f);
	}
	
	
	public List<Color> setColors() {
		
		List<Color> colors = new ArrayList<Color>();
		
		float colorBarLength=103.0f;//设置颜色条的长度
		
		float tempLength=colorBarLength/4;
		
		for(int i=0;i<tempLength/2;i++)// jet 
		{ 
			Color color = new Color(0,0,(tempLength/2+i)/tempLength); 
			colors.add(color);
		} 
		
		for(float i=tempLength/2+1;i<tempLength/2+tempLength;i++)// jet 
		{ 
			Color color = new Color(0,(i-tempLength/2)/tempLength,1); 
			colors.add(color); 
		} 
		for(float i=tempLength/2+tempLength+1;i<tempLength/2+2*tempLength;i++)// jet 
		{ 
			Color color = new Color((i-tempLength-tempLength/2)/tempLength,1,(tempLength*2+tempLength/2-i)/tempLength); 
			colors.add(color);
		} 
		for(float i=tempLength/2+2*tempLength+1;i<tempLength/2+3*tempLength;i++)// jet 
		{ 
			Color color = new Color(1,(tempLength*3+tempLength/2-i)/tempLength,0); 
			colors.add(color); 
		} 
		for(float i=tempLength/2+3*tempLength+1;i<colorBarLength;i++)// jet 
		{ 
			Color color = new Color((colorBarLength-i+tempLength/2)/(tempLength),0,0); 
			colors.add(color); 
		} 
		return colors;
	}

	
	private void drawXAxis(Graphics g, Table table) {
		int step = calcuteXAxis(table.getColumnCount());
		int x = LEFT_BORDER;
		int y = TOP_BORDER-5;
		for (int i = 0; i <= table.getColumnCount(); i++) {
			g.setFont(new Font("Times New Roman", 1, 15));
			if (i%step == 0) {
				g.drawString(i+"", x, y);
			}
			x += stepRow;
		}
	}

	private void drawYAxis(Graphics g, Table table) {
		int step = calcuteYAxis(table.getRowCount());
		int x = LEFT_BORDER-28;
		int y =  TOP_BORDER;
		for (int i = 0; i <= table.getRowCount(); i++) {
			g.setFont(new Font("Times New Roman", 1, 15));
			if (i%step == 0) {
				g.drawString(i+"", x, y);
			}
			y += stepRow;
		}
	}
	
	private int calcuteXAxis(int columnCount) {
		
		int step = (int)(columnCount/10.0+0.5);
		return step;
	}
	private int calcuteYAxis(int rowCount) {
		
		int step = (int)(rowCount/10.0+0.5);
		return step;
	}

}