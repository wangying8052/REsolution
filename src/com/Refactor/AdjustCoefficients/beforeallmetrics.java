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

package com.Refactor.AdjustCoefficients;

import java.util.List;

import com.Refactor.NonInheritance.MergeMethodNonInheri;
import com.jeantessier.dependencyfinder.gui.SrcAction;

public class beforeallmetrics {
	double LCOM =0;
	double Connectivity =0;
	double CBO =0;
	double mpc =0;
	double LCOMavg =0;
	double Connectivityavg =0;
	double CBOavg =0;
	double mpcavg =0;
	
	
	/**
	 * get LCOM Connectivity CBO mpc before refactoring
	 */
	public static beforeallmetrics GetLcombeforeRefactoring(MergeMethodNonInheri rm){
		double LCOM =0;
		double Connectivity =0;
		double CBO =0;
		double mpc =0;
		for(int i = 0; i < rm.classnameListmerge.size();i++){
			
					LCOM = LCOM + SrcAction.classesMap.get(rm.classnameListmerge.get(i)).LCOM;
					Connectivity = Connectivity + SrcAction.classesMap.get(rm.classnameListmerge.get(i)).connectivity;
					CBO = CBO+ SrcAction.classesMap.get(rm.classnameListmerge.get(i)).CBO;
					mpc  = mpc  + SrcAction.classesMap.get(rm.classnameListmerge.get(i)).MPC;
			
		}
		
		beforeallmetrics bm = new beforeallmetrics();
		bm.CBO = CBO;
		bm.Connectivity = Connectivity;
		bm.LCOM = LCOM;
		bm.mpc = mpc;
		bm.CBOavg = CBO/(double)rm.classnameListmerge.size();
		bm.Connectivityavg = Connectivity/(double)rm.classnameListmerge.size() ;
		bm.LCOMavg = LCOM/(double)rm.classnameListmerge.size();
		bm.mpcavg = mpc/(double)rm.classnameListmerge.size();
		
//		System.out.println("  重构前 bm.CBO=="+bm.CBO );
//		System.out.println("  重构前 bm.Connectivity=="+bm.Connectivity );
//		System.out.println("  重构前 bm.LCOM=="+bm.LCOM );
//		System.out.println("  重构前 bm.mpc=="+bm.mpc );
//		System.out.println("  重构前 bm.CBOavg=="+bm.CBOavg +" rm.classnameListmerge.size()=="+rm.classnameListmerge.size());
//		System.out.println("  重构前 bm.Connectivityavg =="+bm.Connectivityavg  );
//		System.out.println("  重构前 bm.LCOMavg=="+bm.LCOMavg );
//		System.out.println("  重构前 bm.mpcavg=="+bm.mpcavg );
		return bm;
	}
}
