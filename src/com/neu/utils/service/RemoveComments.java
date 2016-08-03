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

package com.neu.utils.service;
/**
 * 这个类用于去除原文件的注释
 *  author 李端宝
 *  update LEEO date :2013-04-10
 */
import java.io.IOException;
import java.io.Reader;

public class RemoveComments{

	// COMMENCODES为普通代码模式,PRECOMMENTS为斜杠模式,MORELINECOMMENTS为多行注释模式,
	// STARMODEL为多行注释下星号模式，SINGLELINECOMMENTS为单行注释模式，STRINGMODEL为字符串模式，
	// TRANSFERMODEL为字符串转义模式
	private enum model {
		COMMENCODES, PRECOMMENTS, MORELINECOMMENTS, STARMODEL, SINGLELINECOMMENTS, STRINGMODEL, TRANSFERMODEL
	}
	
	//stats记录状态
	private model stats = model.COMMENCODES;	

	public String remove(Reader in) throws IOException {
		StringBuilder s = new StringBuilder();
		int n;
		while ((n = in.read()) != -1) {
			switch ((char) n) {
			case '/':
				if (stats == model.COMMENCODES) {// 如果当前位普通代码模式则转到斜杠模式
					stats = model.PRECOMMENTS;
				} else if (stats == model.PRECOMMENTS) {// 如果当前为斜杠模式则转到单行注释模式
					stats = model.SINGLELINECOMMENTS;
					s.append("  ");
				} else if (stats == model.MORELINECOMMENTS) {//
					s.append(" ");
				} else if (stats == model.STARMODEL) {// 如果当前为星号模式则转到普通代码模式
					stats = model.COMMENCODES;
					s.append(" ");
				} else if (stats == model.SINGLELINECOMMENTS) {
					s.append(" ");
				} else if (stats == model.STRINGMODEL) {
					s.append("/");
				} else if (stats == model.TRANSFERMODEL) {
					stats = model.STRINGMODEL;
					s.append("/");
				}
				break;
			case '*':
				if (stats == model.COMMENCODES) {
					s.append("*");
				} else if (stats == model.PRECOMMENTS) {// 如果为斜杠模式则转到多行注释模式
					stats = model.MORELINECOMMENTS;
					s.append("  ");
				} else if (stats == model.MORELINECOMMENTS) {// 如果当前为多行注释模式则转到星号模式
					stats = model.STARMODEL;
					s.append(" ");
				} else if (stats == model.STARMODEL) {
					s.append(" ");
				} else if (stats == model.SINGLELINECOMMENTS) {
					s.append(" ");
				} else if (stats == model.STRINGMODEL) {
					s.append("*");
				} else if (stats == model.TRANSFERMODEL) {
					s.append("*");
				}
				break;
			case '"':
				if (stats == model.COMMENCODES) {// 如果当前为普通代码模式则转到字符串模式
					stats = model.STRINGMODEL;
					s.append("\"");
				} else if (stats == model.PRECOMMENTS) {// 如果当前为斜杠模式则转到普通代码模式
					stats = model.COMMENCODES;
					s.append("/\"");
				} else if (stats == model.STARMODEL) {// 如果当前为星号模式则转到多行注释模式
					stats = model.MORELINECOMMENTS;
					s.append(" ");
				} else if (stats == model.SINGLELINECOMMENTS) {
					s.append(" ");
				} else if (stats == model.STRINGMODEL) {// 如果当前为字符串模式则转到普通代码模式
					stats = model.COMMENCODES;
					s.append("\"");
				} else if (stats == model.TRANSFERMODEL) {// 如果当前为转义模式则转到字符串格式
					stats = model.STRINGMODEL;
					s.append("\"");
				}
				break;
			case '\\':
				if (stats == model.COMMENCODES) {
					s.append("\\");
				} else if (stats == model.PRECOMMENTS) {// 如果当前为斜杠模式则转到普通代码格式
					stats = model.COMMENCODES;
					s.append("/\\");
				} else if (stats == model.MORELINECOMMENTS) {
					s.append(" ");
				} else if (stats == model.STARMODEL) {// 如果当前为星号模式则转到多行注释模式
					stats = model.MORELINECOMMENTS;
					s.append(" ");
				} else if (stats == model.SINGLELINECOMMENTS) {
					s.append(" ");
				} else if (stats == model.STRINGMODEL) {// 如果当前为字符串模式则转到字符串转移模式
					stats = model.TRANSFERMODEL;
					s.append("\\");
				} else if (stats == model.TRANSFERMODEL) {// 如果当前为字符串转义模式则转到字符串模式
					stats = model.STRINGMODEL;
					s.append("\\");
				}
				break;
			case '\n':
				if (stats == model.COMMENCODES) {
					s.append("\n");
				} else if (stats == model.PRECOMMENTS) {// 如果当前为斜杠模式则转到普通代码格式
					stats = model.COMMENCODES;
					s.append("/\n");
				} else if (stats == model.MORELINECOMMENTS) {
					s.append("\n");
				} else if (stats == model.STARMODEL) {// 如果当前为星号模式则转到多行注释模式
					stats = model.MORELINECOMMENTS;
					s.append("\n");
				} else if (stats == model.SINGLELINECOMMENTS) {// 如果当前为单行注释模式则转到普通代码格式
					stats = model.COMMENCODES;
					s.append("\n");
				} else if (stats == model.STRINGMODEL) {
					s.append("\n");
				} else if (stats == model.TRANSFERMODEL) {
					s.append("\\n");
				}
				break;
			default:
				if (stats == model.COMMENCODES) {
					s.append((char) n);
				} else if (stats == model.PRECOMMENTS) {// 如果当前为斜杠模式则转到普通代码格式
					stats = model.COMMENCODES;
					s.append("/" + (char) n);
				} else if (stats == model.STARMODEL) {// 如果当前为星号模式则转到多行注释模式
					stats = model.MORELINECOMMENTS;
					s.append(" ");
				} else if (stats == model.SINGLELINECOMMENTS) {
					s.append(" ");
				} else if (stats == model.STRINGMODEL) {
					s.append((char) n);
				} else if (stats == model.TRANSFERMODEL) {// 如果当前为字符串转义模式则转到字符串模式
					stats = model.STRINGMODEL;
					s.append((char) n);
				}
				break;
			}
		}

		String result = s.toString();
//		System.out.println(result);
		return result;
	}
}
