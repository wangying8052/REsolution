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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Refactor.NonInheritance.Methodfeature;
import com.Refactor.NonInheritance.newclass;

public class ClassObject {
	public String name;
	public ArrayList<String> inboundClassList = new ArrayList<String>();
	public ArrayList<String> inboundFeatureList = new ArrayList<String>();
	public ArrayList<String> outboundClassList = new ArrayList<String>();
	public ArrayList<String> outboundOtherClassList = new ArrayList<String>();
	public boolean interfaceornot = false;
	public ArrayList<String> getOutboundOtherClassList() {
		return outboundOtherClassList;
	}

	public void setOutboundOtherClassList(ArrayList<String> outboundOtherClassList) {
		this.outboundOtherClassList = outboundOtherClassList;
	}

	public ArrayList<String> outboundFeatureList = new ArrayList<String>();
	public ArrayList<Methodfeature> methodlines = new ArrayList<Methodfeature>();
	public List<Feature> FeatureList = new ArrayList<Feature>();
	public List<Feature> getFeatureList() {
		return FeatureList;
	}

	public void setFeatureList(List<Feature> featureList) {
		FeatureList = featureList;
	}

	public Map<String, Feature> featureMap = new HashMap<String, Feature>();
	public ArrayList<String> Refactor = new ArrayList<String>();
	public ArrayList<newclass>  ExtractClass = new  ArrayList<newclass>();
	public boolean isLeaf = false;
	public String superclass = null;
	public double connectivity = 0;
	public double LCOM = 0;
	public double CBO = 0;
	public double MPC = 0;
	public double C3 = 0;

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

	public List<String> getOutboundFeatureList() {
		return outboundFeatureList;
	}

	public void setOutboundFeatureList(ArrayList<String> outboundFeatureList) {
		this.outboundFeatureList = outboundFeatureList;
	}

	public Map<String, Feature> getFeatureMap() {
		return featureMap;
	}

	public void setFeatureMap(Map<String, Feature> featureMap) {
		this.featureMap = featureMap;
	}



}
