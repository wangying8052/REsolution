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

import java.util.ArrayList;

public class Metrics {
	 ArrayList<Double> MoJOFMSUM =new ArrayList<Double>();
	 ArrayList<Double> LCOMSbefore =new ArrayList<Double>();
	 ArrayList<Double> LCOMSGod =new ArrayList<Double>();
	 ArrayList<Double> LCOMSRefa =new ArrayList<Double>();
	 ArrayList<Double> Conbefore =new ArrayList<Double>();
	 ArrayList<Double> ConGod =new ArrayList<Double>();
	 ArrayList<Double> ConRefa =new ArrayList<Double>();
	 ArrayList<Double> CBObefore =new ArrayList<Double>();
	 ArrayList<Double> CBOGod =new ArrayList<Double>();
	 ArrayList<Double> CBORefa =new ArrayList<Double>();
	 ArrayList<Double> MPCbefore =new ArrayList<Double>();
	 ArrayList<Double> MPCGod =new ArrayList<Double>();
	 ArrayList<Double> MPCRefa =new ArrayList<Double>();
	 public void Metrics(){
		 MoJOFMSUM =new ArrayList<Double>();
		 LCOMSbefore =new ArrayList<Double>();
		 LCOMSGod =new ArrayList<Double>();
		 LCOMSRefa =new ArrayList<Double>();
		 Conbefore =new ArrayList<Double>();
		 ConGod =new ArrayList<Double>();
		 ConRefa =new ArrayList<Double>();
		 CBObefore =new ArrayList<Double>();
		 CBOGod =new ArrayList<Double>();
		 CBORefa =new ArrayList<Double>();
		 MPCbefore =new ArrayList<Double>();
		 MPCGod =new ArrayList<Double>();
		 MPCRefa =new ArrayList<Double>();
	 }
}
