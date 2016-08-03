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

package com.neu.utils.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.neu.utils.data.Dot;

public class CoefficientJPanel extends JPanel{
	
	private double[][] mPCS;
	private String s;
	private double pingjunzhi = -1;
	private double zhongzhi = -1;
	private double fangcha = -1;
	
	public static void main(String[] args) {
		
		Test test =new Test();
	}
	
	
	public CoefficientJPanel() {
		init();
	}

	public CoefficientJPanel(double[][] mPCS, String s,double pingjunzhi, double zhongzhi, double fangcha) {
				
		this.mPCS = mPCS;
		this.s = s;
		this.pingjunzhi = pingjunzhi;
		this.zhongzhi = zhongzhi;
		this.fangcha = fangcha;
		init();
	}


	public CoefficientJPanel(double[][] mPCS, String s) {
		
		this.mPCS = mPCS;
		this.s = s;
		init();
	}


	private void init() {
		this.setBackground(Color.white);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		g.drawRect((int)(this.getWidth()*0.1), 
				(int)(this.getHeight()*0.1), 
				(int)(this.getWidth()*0.7), 
				(int)(this.getHeight()*0.8));
		
		drawX(g);
		drawY(g);
		drawMetrics((Graphics2D)g);
		
		if (false == (this.pingjunzhi ==-1 && this.zhongzhi ==-1 && this.fangcha ==-1)) {
			drawLegend((Graphics2D)g);
			drawLable((Graphics2D)g,this.pingjunzhi,this.zhongzhi,this.fangcha);
		}
		
		//绘制y轴说明文字
		drawRotateStr(g,this.s,this.getHeight()*0.45f, this.getWidth()*0.05f);
		
		g.drawString("50 merging operation", (int)(this.getWidth()*0.38), (int)(this.getHeight()*0.98));
		
	}
	
	private void drawLegend(Graphics2D g) {
		
		g.drawRect((int)(this.getWidth()*0.82), 
				(int)(this.getHeight()*0.104), 
				(int)(this.getWidth()*0.169), 
				(int)(this.getHeight()*0.22));
		g.setColor(Color.blue);
		g.drawLine((int)(this.getWidth()*0.83), 
				(int)(this.getHeight()*0.16), 
				(int)(this.getWidth()*0.88), 
				(int)(this.getHeight()*0.16));
		g.fillRect((int)(this.getWidth()*0.847), (int)(this.getHeight()*0.155), 8, 8);
		g.setColor(Color.black);
		g.drawString("Original Classes", (int)(this.getWidth()*0.892), (int)(this.getHeight()*0.163));
		
		g.setColor(Color.red);
		g.drawLine((int)(this.getWidth()*0.83), 
				(int)(this.getHeight()*0.21), 
				(int)(this.getWidth()*0.88), 
				(int)(this.getHeight()*0.21));
		g.fillOval((int)(this.getWidth()*0.847), (int)(this.getHeight()*0.205), 8, 8);
		g.setColor(Color.black);
		g.drawString("Merged Classes", (int)(this.getWidth()*0.892), (int)(this.getHeight()*0.213));
		
		g.setColor(Color.green);
		g.drawLine((int)(this.getWidth()*0.83), 
				(int)(this.getHeight()*0.26), 
				(int)(this.getWidth()*0.88), 
				(int)(this.getHeight()*0.26));
		
		GeneralPath triangle = new GeneralPath();
		triangle.moveTo((int)(this.getWidth()*0.847), (int)(this.getHeight()*0.255)+2);
		triangle.lineTo((int)(this.getWidth()*0.847)+10, (int)(this.getHeight()*0.255)+2);
		triangle.lineTo((int)(this.getWidth()*0.847)+5, (int)(this.getHeight()*0.255+10));
		triangle.closePath();
		// 实心箭头
		g.fill(triangle);
		g.setColor(Color.black);
		g.drawString("Extract Classes", (int)(this.getWidth()*0.892), (int)(this.getHeight()*0.263));
	}
	private void drawLable(Graphics2D g,double pingjunzhi, double zhongzhi, double fangcha) {
		
		g.drawRect((int)(this.getWidth()*0.82), 
				(int)(this.getHeight()*0.405), 
				(int)(this.getWidth()*0.169), 
				(int)(this.getHeight()*0.22));
		
		g.drawString("Average MoJoFM : "+(double)Math.round(pingjunzhi*10000)/10000,
				(int)(this.getWidth()*0.835), (int)(this.getHeight()*0.465) );
		g.drawString("Median MoJoFM : "+(double)Math.round(zhongzhi*10000)/10000,
				(int)(this.getWidth()*0.835), (int)(this.getHeight()*0.515) );
		g.drawString("Variance : "+(double)Math.round(fangcha*10000)/10000,
				(int)(this.getWidth()*0.835), (int)(this.getHeight()*0.565) );
		
	}


	/**
	 * 画出X轴
	 * @param g
	 */
	private void drawX(Graphics g) {
		
		int xStep = (int)(this.getWidth()*0.7/10);
		for (int i = 0; i < 10; i++) {
			g.drawLine((int)(this.getWidth()*0.1)+i*xStep, (int)(this.getHeight()*0.9), (int)(this.getWidth()*0.1)+i*xStep, (int)(this.getHeight()*0.9)-10);
			g.drawLine((int)(this.getWidth()*0.1)+i*xStep, (int)(this.getHeight()*0.1), (int)(this.getWidth()*0.1)+i*xStep, (int)(this.getHeight()*0.1)+10);
			g.drawString(i*5+"", (int)(this.getWidth()*0.1)+i*xStep, (int)(this.getHeight()*0.9)+15);
		}
		g.drawString("50", (int)(this.getWidth()*0.8), (int)(this.getHeight()*0.9)+15);
	}
	
	/**
	 * 画出Y轴
	 * @param g
	 */
	private void drawY(Graphics g) {
		int stepvalue = (int)(wrapMetricValues()/8.0f);
		int yStep = (int)(this.getHeight()*0.8/8);
		for (int i = 0; i < 8; i++) {
			g.drawLine((int)(this.getWidth()*0.1), (int)(this.getHeight()*0.9)-i*yStep, (int)(this.getWidth()*0.1)+10, (int)(this.getHeight()*0.9)-i*yStep);
			g.drawLine((int)(this.getWidth()*0.8), (int)(this.getHeight()*0.9)-i*yStep, (int)(this.getWidth()*0.8)-10, (int)(this.getHeight()*0.9)-i*yStep);
			
			g.drawString(i*stepvalue+"", (int)(this.getWidth()*0.1)-35, (int)(this.getHeight()*0.9)-i*yStep);
		}
		g.drawString(stepvalue*8+"", (int)(this.getWidth()*0.1)-35, (int)(this.getHeight()*0.1));
	}
	
	
	/**
	 * 画出矩阵数据
	 * @param g
	 */
	private void drawMetrics(Graphics2D g) {
		int xLength = this.mPCS[0].length;
		int xStep = (int)(this.getWidth()*0.7/xLength);
		
		double[][] metricValues = this.mPCS;
		List<Integer[]> dotList = new ArrayList<Integer[]>();
		
		int[] xPoints = new int[xLength];
		for (int i = 1; i <= xLength; i++) {
			xPoints[i-1] = (int)(this.getWidth()*0.1)+i*xStep;
		}
		
		int[] yPoints = new int[xLength];
		Color[] colorList = setColorList();
		for (int i = 0; i < metricValues.length ; i++) {
			Integer[] yPoints1 = new Integer[xLength];
			double[] d = metricValues[i];
			for (int j = 0; j < d.length; j++) {
				double y = d[j];
				yPoints[j] = (int)(this.getHeight()*(0.9-y*0.8/this.wrapMetricValues()));
				yPoints1[j] = (int)(this.getHeight()*(0.9-y*0.8/this.wrapMetricValues()));
			}
			dotList.add(yPoints1);
			g.setStroke(new BasicStroke(2.0f)); 
			g.setColor(colorList[i]);
			g.drawPolyline(xPoints, yPoints, xLength);
			g.setColor(Color.black);
		}
		
		drawDots(g, dotList, xPoints, colorList);

	}


	private void drawDots(Graphics2D g, List<Integer[]> dotList, int[] xPoints,
			Color[] colorList) {
		for (int j = 0; j < dotList.size(); j++) {
			Integer[] dots = dotList.get(j);
			g.setColor(colorList[j]);
			if (j==0) {
				for (int i = 0; i < dots.length; i++) {
					Integer y = dots[i];
					g.fillOval(xPoints[i]-4, y-4, 8, 8);
				}
			}
			if (j==1) {
				for (int i = 0; i < dots.length; i++) {
					Integer y = dots[i];
					g.fillRect(xPoints[i]-4, y-4, 8, 8);
				}
			}
			if (j==2) {
				for (int i = 0; i < dots.length; i++) {
					Integer y = dots[i];
					GeneralPath triangle = new GeneralPath();
					triangle.moveTo(xPoints[i]-5, y-5);
					triangle.lineTo(xPoints[i]+5, y-5);
					triangle.lineTo(xPoints[i], y);
					triangle.closePath();
					// 实心箭头
					g.fill(triangle);
//					g.fillOval(xPoints[i]-4, y-4, 8, 8);
				}
			}
			
		}
		g.setColor(Color.black);
	}
	
	private Color[] setColorList() {
		
		Color[] colors = {Color.blue,Color.red,Color.green};
		
		return colors;
	}


	private int wrapMetricValues() {
		
		double max = 0;
		
		for (int i = 0; i < mPCS.length; i++) {
			double[] ds = mPCS[i];
			for (int j = 0; j < ds.length; j++) {
				double d = ds[j];
				if (max<d) {
					max = d;
				}
			}
		}
		
		return (int)(Math.ceil(max/100)*100);
	}


	/**
	 * 旋转坐标画出y周说明标签
	 * @param g
	 * @param s
	 * @param x
	 * @param y
	 */
	private void drawRotateStr(Graphics g,String s,float x,float y) {
		Graphics2D g2 = (Graphics2D)g;
		//旋转270度
		g2.translate(0, this.getHeight());
		g2.rotate(1.5*Math.PI);
		
		//绘制y轴说明文字
		g2.setFont(new Font("Times New Roman", 1, 14));
		g2.drawString(s, x,y);
		
		//恢复正常坐标
		g2.translate(this.getHeight(), 0);
		g2.rotate(0.5*Math.PI);
	}

}
class Test extends JFrame{
	
	public Test() {
		init();
	}
	private void init() {
		
		this.add(new CoefficientJPanel());
		
		this.setTitle("test");
		
		this.setSize(900, 850);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setVisible(true);
		
	}

}
