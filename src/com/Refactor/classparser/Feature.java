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

package com.Refactor.classparser;
import java.util.ArrayList;
import java.util.List;

public class Feature {
	
	public String name;
	public ArrayList<String> inboundClassList = new ArrayList<String>();
	ArrayList<String> inboundAttributeList = new ArrayList<String>();
	public ArrayList<String> inboundMethodList = new ArrayList<String>();
	public ArrayList<String> inboundFeatureList = new ArrayList<String>();
	public ArrayList<String> outboundClassList = new ArrayList<String>();
	public ArrayList<String> outboundAttributeList = new ArrayList<String>();
	public ArrayList<String> outboundMethodList = new ArrayList<String>();
	public ArrayList<String> outboundFeatureList = new ArrayList<String>();
	public ArrayList<String> extract = new ArrayList<String>();
	public boolean dead = false;
	boolean outleaf = false;

	public int methodlinesbe = 0;
	public int methodlinesaf = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getInboundClassList() {
		return inboundClassList;
	}

	public void setInboundClassList(ArrayList<String> inboundClassList) {
		this.inboundClassList = inboundClassList;
	}

	public List<String> getInboundAttributeList() {
		return inboundAttributeList;
	}

	public void setInboundAttributeList(ArrayList<String> inboundAttributeList) {
		this.inboundAttributeList = inboundAttributeList;
	}

	public List<String> getInboundMethodList() {
		return inboundMethodList;
	}

	public void setInboundMethodList(ArrayList<String> inboundMethodList) {
		this.inboundMethodList = inboundMethodList;
	}

	public List<String> getInboundFeatureList() {
		return inboundFeatureList;
	}

	public void setInboundFeatureList(ArrayList<String> inboundFeatureList) {
		this.inboundFeatureList = inboundFeatureList;
	}

	public List<String> getOutboundClassList() {
		return outboundClassList;
	}

	public void setOutboundClassList(ArrayList<String> outboundClassList) {
		this.outboundClassList = outboundClassList;
	}

	public List<String> getOutboundAttributeList() {
		return outboundAttributeList;
	}

	public void setOutboundAttributeList(ArrayList<String> outboundAttributeList) {
		this.outboundAttributeList = outboundAttributeList;
	}

	public List<String> getOutboundMethodList() {
		return outboundMethodList;
	}

	public void setOutboundMethodList(ArrayList<String> outboundMethodList) {
		this.outboundMethodList = outboundMethodList;
	}

	public List<String> getOutboundFeatureList() {
		return outboundFeatureList;
	}

	public void setOutboundFeatureList(ArrayList<String> outboundFeatureList) {
		this.outboundFeatureList = outboundFeatureList;
	}

	public ArrayList<String> getExtract() {
		return extract;
	}

	public void setExtract(ArrayList<String> extract) {
		this.extract = extract;
	}

	public int getMethodlinesbe() {
		return methodlinesbe;
	}

	public void setMethodlinesbe(int methodlinesbe) {
		this.methodlinesbe = methodlinesbe;
	}

	public int getMethodlinesaf() {
		return methodlinesaf;
	}

	public void setMethodlinesaf(int methodlinesaf) {
		this.methodlinesaf = methodlinesaf;
	}

}
