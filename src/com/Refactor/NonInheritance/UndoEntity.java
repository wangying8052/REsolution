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

package com.Refactor.NonInheritance;

public class UndoEntity {
	  String entityname;//实体名字
	  String source;//源类
	  String target;//目标类名
	  public int newNo = -1;
	  int targetidx = 0;
	  double detaQ = 0;//模块度增量
	  
	  
	public String getEntityname() {
		return entityname;
	}
	public void setEntityname(String entityname) {
		this.entityname = entityname;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public int getTargetidx() {
		return targetidx;
	}
	public void setTargetidx(int targetidx) {
		this.targetidx = targetidx;
	}
	public double getDetaQ() {
		return detaQ;
	}
	public void setDetaQ(double detaQ) {
		this.detaQ = detaQ;
	}
	  
	  
	  
}
