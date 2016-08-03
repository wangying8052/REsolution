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

package com.Refactor.Metrics;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.Refactor.NonInheritance.GenerateRefactoringSuggestions;
import com.Refactor.NonInheritance.tool;
import com.Refactor.classparser.ClassObject;
import com.neu.utils.listener.ProgressBarLisenter;


public class CompareBeforeExtend {
	public static MaintainabilityMetrics beforeMetric;
	public static OODMetrics beforeOODMetric;
	public static MaintainabilityMetrics extendMetric;
	public static OODMetrics extendOODMetric;
	public MaintainabilityMetrics compareMetric;
	public OODMetrics compareOODMetric;
	
	public CompareBeforeExtend(AuxiliaryMeasure measure, List<ClassObject> classesArrList, ProgressBarLisenter barLisenter){
		
		this.beforeMetric = new MaintainabilityMetrics(measure.getBeforeRefactor(),measure.getBeforeRefactorInheritance(),classesArrList);
		
		barLisenter.endFile();
		
		this.beforeOODMetric = new OODMetrics(measure.getBeforeRefactor(),measure.getBeforeRefactorInheritance(),classesArrList);
		
		barLisenter.endFile();
		
		this.beforeMetric.setMt(1);
		this.beforeOODMetric.setReusability(1);
		this.beforeOODMetric.setFlexibility(1);
		this.beforeOODMetric.setUnderstandability(-0.99);

		this.extendMetric = new MaintainabilityMetrics(measure.getExtendRefactor(), measure.getExtendRefactorInheritance(), classesArrList);
		
		barLisenter.endFile();
		
		this.extendOODMetric = new OODMetrics(measure.getExtendRefactor(), measure.getExtendRefactorInheritance(), classesArrList);
		
		barLisenter.endFile();
		
		
		double LCOM = this.extendMetric.getLCOM() / this.beforeMetric.getLCOM();
		double MPC = this.extendMetric.getMPC() / this.beforeMetric.getMPC();
		double WMC = this.extendMetric.getWMC() / this.beforeMetric.getWMC();
		double ANA = this.extendMetric.getANA() / this.beforeMetric.getANA();
		double NOC = this.extendMetric.getNOC() / this.beforeMetric.getNOC();
		double RFC = this.extendMetric.getRFC() / this.beforeMetric.getRFC();
		this.extendMetric.setMt(1 / (LCOM * MPC * WMC * ANA * NOC * RFC));
		tool.tidy();
		double CAM = this.extendOODMetric.getCAM() / this.beforeOODMetric.getCAM();
		double CIS = this.extendOODMetric.getCIS() / this.beforeOODMetric.getCIS();
		double DSC = this.extendOODMetric.getDSC() / this.beforeOODMetric.getDSC();
		double DAM = this.extendOODMetric.getDAM() / this.beforeOODMetric.getDAM();
		double MOA = this.extendOODMetric.getMOA() / this.beforeOODMetric.getMOA();
		double NOP = this.extendOODMetric.getNOP() / this.beforeOODMetric.getNOP();
		double NOM = this.extendOODMetric.getNOM() / this.beforeOODMetric.getNOM();
		this.extendOODMetric.setReusability(-0.25 * MPC + 0.25 * CAM + 0.5 * CIS + 0.5 * DSC);
		this.extendOODMetric.setFlexibility(0.25 * DAM - 0.25 * MPC + 0.5 * MOA + 0.5 * NOP);
		this.extendOODMetric.setUnderstandability(-0.33 * ANA + 0.33 * DAM - 0.33 * MPC + 0.33 * CAM - 0.33 * NOP -0.33 * NOM - 0.33 * DSC);
		
		this.compareMetric = new MaintainabilityMetrics();
		
		barLisenter.endFile();
		
		this.compareOODMetric = new OODMetrics();
		
		barLisenter.endFile();
		
		this.compareMetric.setANA((this.extendMetric.getANA() - this.beforeMetric.getANA()) / Math.abs(this.beforeMetric.getANA()));
		this.compareMetric.setLCOM((this.extendMetric.getLCOM() - this.beforeMetric.getLCOM()) / Math.abs(this.beforeMetric.getLCOM()));
		this.compareMetric.setMPC((this.extendMetric.getMPC() - this.beforeMetric.getMPC()) / Math.abs(this.beforeMetric.getMPC()));
		this.compareMetric.setCBO((this.extendMetric.getCBO() - this.beforeMetric.getCBO()) / Math.abs(this.beforeMetric.getCBO()));
		this.compareMetric.setNOC((this.extendMetric.getNOC() - this.beforeMetric.getNOC()) / Math.abs(this.beforeMetric.getNOC()));
		this.compareMetric.setRFC((this.extendMetric.getRFC() - this.beforeMetric.getRFC()) / Math.abs(this.beforeMetric.getRFC()));
		this.compareMetric.setWMC((this.extendMetric.getWMC() - this.beforeMetric.getWMC()) / Math.abs(this.beforeMetric.getWMC()));
		this.compareMetric.setMt((this.extendMetric.getMt() - this.beforeMetric.getMt()) / Math.abs(this.beforeMetric.getMt()));
		
		this.compareOODMetric.setANA((this.extendOODMetric.getANA() - this.beforeOODMetric.getANA()) / Math.abs(this.beforeOODMetric.getANA()));
		this.compareOODMetric.setCAM((this.extendOODMetric.getCAM() - this.beforeOODMetric.getCAM()) / Math.abs(this.beforeOODMetric.getCAM()));
		this.compareOODMetric.setCIS((this.extendOODMetric.getCIS() - this.beforeOODMetric.getCIS()) / Math.abs(this.beforeOODMetric.getCIS()));
		this.compareOODMetric.setDAM((this.extendOODMetric.getDAM() - this.beforeOODMetric.getDAM()) / Math.abs(this.beforeOODMetric.getDAM()));
		this.compareOODMetric.setDSC((this.extendOODMetric.getDSC() - this.beforeOODMetric.getDSC()) / Math.abs(this.beforeOODMetric.getDSC()));
		this.compareOODMetric.setMOA((this.extendOODMetric.getMOA() - this.beforeOODMetric.getMOA()) / Math.abs(this.beforeOODMetric.getMOA()));
		this.compareOODMetric.setMPC((this.extendOODMetric.getMPC() - this.beforeOODMetric.getMPC()) / Math.abs(this.beforeOODMetric.getMPC()));
		this.compareOODMetric.setCBO((this.extendOODMetric.getCBO() - this.beforeOODMetric.getCBO()) / Math.abs(this.beforeOODMetric.getCBO()));
		this.compareOODMetric.setNOM((this.extendOODMetric.getNOM() - this.beforeOODMetric.getNOM()) / Math.abs(this.beforeOODMetric.getNOM()));
		this.compareOODMetric.setNOP((this.extendOODMetric.getNOP() - this.beforeOODMetric.getNOP()) / Math.abs(this.beforeOODMetric.getNOP()));
		this.compareOODMetric.setReusability((this.extendOODMetric.getReusability() - this.beforeOODMetric.getReusability()) / Math.abs(this.beforeOODMetric.getReusability()));
		this.compareOODMetric.setFlexibility((this.extendOODMetric.getFlexibility() - this.beforeOODMetric.getFlexibility()) / Math.abs(this.beforeOODMetric.getFlexibility()));
		this.compareOODMetric.setUnderstandability((this.extendOODMetric.getUnderstandability() - this.beforeOODMetric.getUnderstandability()) / Math.abs(this.beforeOODMetric.getUnderstandability()));
	}

}
