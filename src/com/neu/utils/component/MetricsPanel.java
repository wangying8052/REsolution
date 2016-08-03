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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.Refactor.Metrics.CompareBeforeExtend;
import com.neu.utils.data.ClassNode;
import com.neu.utils.data.Dot;

public class MetricsPanel extends JPanel{
	
	private CompareBeforeExtend cm;
	private Dot[] dots = new Dot[16];
	private double[] metricValues;
	private int zeroLine;
	private Map<Integer, String> tipsMap;
	
	public MetricsPanel() {
		this.setBackground(Color.white);
		init();
	}

	public MetricsPanel(CompareBeforeExtend cm){
		this.cm = cm;
		this.setBackground(Color.white);
		
		init();
	}
	
	private void init() {
		
		tipsMap = wrapTipsMap();
		
		final JComponent c= this;
		c.addMouseMotionListener(new MouseMotionListener() {
			
			public void mouseMoved(MouseEvent e) {
				Point point = e.getPoint();
                int x = (int)point.getX();
        		int y = (int)point.getY();
        		
        		for (int i = 0; i < dots.length; i++) {
					Dot d = dots[i];
					if (x>=d.getX() && x<=(d.getX()+d.getWidth()) && 
        					y>=d.getY() && y<=(d.getY()+d.getHeight())) {
        				JPopupMenu popup = new JPopupMenu();  
                        popup.setLayout(new BorderLayout());  
                        JPanel infoPanel = createtInfoPanel(tipsMap.get(i));  
                        popup.add(infoPanel, BorderLayout.CENTER);  

                        popup.show(c, x+5, y+5);
        			}
				}
			}
			
			public void mouseDragged(MouseEvent e) {
			}
		});
		
	}

	private JPanel createtInfoPanel(String content) {
		JPanel infoPanel = new JPanel();   
        infoPanel.setLayout(new BorderLayout());  

        infoPanel.add(new JLabel(content),BorderLayout.CENTER);  
        return infoPanel;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		drawMQC(g);
	}

	private void drawMQC(Graphics g) {
		
		g.drawRect((int)(this.getWidth()*0.2), 
				(int)(this.getHeight()*0.1), 
				(int)(this.getWidth()*0.7), 
				(int)(this.getHeight()*0.6));
		
		drawX(g);
		
		drawY(g);
		
		drawMetrics(g);
		
		//绘制y轴说明文字
		String s = "Metrics Quotient Change";
		drawRotateStr(g,s,this.getHeight()*0.45f, this.getWidth()*0.15f);
		
		g.drawString("*Quality Change = (After - Before) / Before", (int)(this.getWidth()*0.5), (int)(this.getHeight()*0.8));
		
	}

	/**
	 * 画出矩阵数据
	 * @param g
	 */
	private void drawMetrics(Graphics g) {		
		int xStep = (int)(this.getWidth()*0.7/17);
		int yStep = (int)(this.getHeight()*0.6/7);
		this.metricValues = this.wrapMetricValues();
		
		Color[] colorList = setColorList();
		
		for (int i = 1; i <= metricValues.length; i++) {
			double d = metricValues[i-1];
			g.setColor(colorList[i-1]);
			if (d>=0) {
				g.fillRect((int)(this.getWidth()*0.2)+i*xStep-10, (int)(this.zeroLine-d*yStep)+1, 20, (int)(d*yStep));
			} else {
				g.fillRect((int)(this.getWidth()*0.2)+i*xStep-10, this.zeroLine+1, 20, (int)(Math.abs(d)*yStep));
			}
			
		}
		g.setColor(Color.black);
	}

	
	/**
	 * 画出Y轴
	 * @param g
	 */
	private void drawY(Graphics g) {
		int yStep = (int)(this.getHeight()*0.6/7);
		for (int i = 1; i <= 6; i++) {
			g.drawLine((int)(this.getWidth()*0.2), (int)(this.getHeight()*0.7)-i*yStep, (int)(this.getWidth()*0.2)+10, (int)(this.getHeight()*0.7)-i*yStep);
			g.drawLine((int)(this.getWidth()*0.9), (int)(this.getHeight()*0.7)-i*yStep, (int)(this.getWidth()*0.9)-10, (int)(this.getHeight()*0.7)-i*yStep);
		}
		
		this.metricValues = this.wrapMetricValues();
		double max = calMaxMetricValues(metricValues);
		double min = calMinMetricValues(metricValues);
		
		double yValue = (double)Math.round(((max - min)/7)*10000)/10000;
		
		int y=0;
		for (double i = min; i <=max ; i+=yValue) {
			
			double st = (double)Math.round(i*10000)/10000;
			
			g.drawString(st+"", (int)(this.getWidth()*0.2)-45, (int)(this.getHeight()*0.7)-y*yStep+5);
			y++;
		}
		
		//画基线
		if (min<0) {
			zeroLine = (int)(this.getHeight()*0.7-((0-min)/yValue)*yStep);
			g.setColor(Color.red);
			g.drawString("0", (int)(this.getWidth()*0.2)-15, zeroLine);
			g.setColor(Color.black);
			g.drawLine((int)(this.getWidth()*0.2), zeroLine, (int)(this.getWidth()*0.9), zeroLine);
		}
		
	}

	/**
	 * 画出X轴
	 * @param g
	 */
	private void drawX(Graphics g) {
		
		int xStep = (int)(this.getWidth()*0.7/17);
		String[] xAxis = {"DSC","ANA","DAM","MPC","CAM","MOA","NOP","CIS","NOM","RFC","LCOM","NOC","MT","RE","FL","UN"};
		for (int i = 1; i <= 16; i++) {
			g.drawLine((int)(this.getWidth()*0.2)+i*xStep, (int)(this.getHeight()*0.7), (int)(this.getWidth()*0.2)+i*xStep, (int)(this.getHeight()*0.7)-10);
			g.drawLine((int)(this.getWidth()*0.2)+i*xStep, (int)(this.getHeight()*0.1), (int)(this.getWidth()*0.2)+i*xStep, (int)(this.getHeight()*0.1)+10);
			g.drawString(xAxis[i-1], (int)(this.getWidth()*0.2)+i*xStep-10, (int)(this.getHeight()*0.7)+15);
			dots[i-1] = new Dot((int)(this.getWidth()*0.2)+i*xStep-12, (int)(this.getHeight()*0.7)+13,20,10);
		}
	}
	
	/**
	 * 旋转坐标画出y轴说明标签
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

	/**
	 * 封装颜色
	 * @return
	 */
	private Color[] setColorList() {
		Color[] colors = {
				new Color(136, 0, 21),
				new Color(237, 28, 36),
				new Color(255, 127, 39),
				new Color(223, 213, 0),
				new Color(24, 131, 56),
				new Color(0, 162, 232),
				new Color(63, 72, 204),
				new Color(163, 73, 164),
				new Color(185, 122, 87),
				new Color(255, 174, 201),
				new Color(255, 201, 14),
				new Color(239, 228, 176),
				new Color(181, 230, 29),
				new Color(153, 217, 234),
				new Color(112, 146, 190),
				new Color(200, 191, 231)
				};
		
		return colors;
	}

	
	/**
	 * 封装提示信息数据
	 * @return
	 */
	private Map<Integer, String> wrapTipsMap() {
		tipsMap = new HashMap<Integer, String>();
		
		tipsMap.put(0, "DSC, Design Size in Classes");
		tipsMap.put(1, "ANA, Average Number of Ancestors");
		tipsMap.put(2, "DAM, Data Access Metric");
		tipsMap.put(3, "MPC");
		tipsMap.put(4, "CAM,Cohesion Among Methods in Class");
		tipsMap.put(5, "MOA, Measure of Aggregation");
		tipsMap.put(6, "NOP, Number of Polymorphic Method");
		tipsMap.put(7, "CIS, Class Interface Size");
		tipsMap.put(8, "NOM, Number Of Method");
		tipsMap.put(9, "RFC, Response sets For Class");
		tipsMap.put(10, "LCOM, Lack of Cohesion Of Methods");
		tipsMap.put(11, "NOC, Number of Children of Class");
		tipsMap.put(12, "MT,Maintainability");
		tipsMap.put(13, "RE,Reusability");
		tipsMap.put(14, "FL,Flexibility");
		tipsMap.put(15, "UN,Understandability");
		
		
		return tipsMap;
	}
	
	
	private double calMaxMetricValues(double[] metricValues) {
		
		double max = 0;
		for (int i = 0; i < metricValues.length; i++) {
			double d = metricValues[i];
			if (max<d) {
				max = d;
			}
		}
		return Math.ceil(max);
//		return max;
	}
	
	private double calMinMetricValues(double[] metricValues) {
		
		double min = 0;
		for (int i = 0; i < metricValues.length; i++) {
			double d = metricValues[i];
			if (min>d) {
				min = d;
			}
		}
//		return min;
		return Math.floor(min);
	}
	
	/**
	 * 封装矩形长度数据
	 * @return
	 */
	private double[] wrapMetricValues(){
		double[] metricValues = new double[16];
		
		metricValues[0] = this.cm.compareOODMetric.getDSC();
		metricValues[1] = this.cm.compareOODMetric.getANA();
		metricValues[2] = this.cm.compareOODMetric.getDAM();
		metricValues[3] = this.cm.compareOODMetric.getMPC();
		metricValues[4] = this.cm.compareOODMetric.getCAM();
		metricValues[5] = this.cm.compareOODMetric.getMOA();
		metricValues[6] = this.cm.compareOODMetric.getNOP();
		metricValues[7] = this.cm.compareOODMetric.getCIS();
		metricValues[8] = this.cm.compareOODMetric.getNOM();
		metricValues[9] = this.cm.compareMetric.getRFC();
		metricValues[10] = this.cm.compareMetric.getLCOM();
		metricValues[11] = this.cm.compareMetric.getNOC();
		metricValues[12] = this.cm.compareMetric.getMt();
		metricValues[13] = this.cm.compareOODMetric.getReusability();
		metricValues[14] = this.cm.compareOODMetric.getFlexibility();
		metricValues[15] = this.cm.compareOODMetric.getUnderstandability();
		return metricValues;
	}
}

