package com.cheng.parser.model;

import java.util.ArrayList;
import java.util.Stack;

import com.cheng.parser.util.TextUtil;

/**
 * 分析表达式的
 * @author 誠
 *
 */
public class Analyser {
	
	/**
	 * 对表达式进行分析
	 * @param analysisTable
	 * 						预测分析表
	 * @param startChar
	 * 					开始字符
	 * @param expression
	 * 				需要分析的字符串
	 */
	public ArrayList<AnalyseItem> analyse(String[][] analysisTable, Character startChar, String expression){
		
		ArrayList<AnalyseItem> analyseList = new ArrayList<>();    //存储分析过程的链表
		int index = 0;    //序号
		Stack<Character> analyseStack = new Stack<>();    //分析栈
		String leftStr = expression;    //剩余表达式，赋初值
		
		analyseStack.push('#');    // # 入栈
		analyseStack.push(startChar);    // 开始符入栈
		
		//分析过程
		while (!analyseStack.isEmpty()){
			index++;    //序号++
			// 栈顶元素 与 剩余串首字符 不同
			if (analyseStack.peek() != leftStr.charAt(0)){
				// 查表
				String nowUseExpStr = TextUtil.findExpFromTable(analysisTable, analyseStack.peek(), leftStr.charAt(0));
				// 存储此时的
				AnalyseItem analyseItem = new AnalyseItem();
				analyseItem.setIndex(index);
				analyseItem.setAnalyseStackItem(analyseStack.toString());
				analyseItem.setLeftStr(leftStr);
				//System.out.println(nowUseExpStr);
				// 预测分析表中无匹配项
				if (null == nowUseExpStr) {
					analyseItem.setUseExpStr("无法匹配");
					analyseList.add(analyseItem);
					return analyseList;
				}
				// 预测分析表中含匹配项
				else {
					analyseItem.setUseExpStr(analyseStack.peek() + "->" + nowUseExpStr);
				}
				analyseList.add(analyseItem);
				
				analyseStack.pop();
				// 匹配的产生式倒序入栈
				if (null != nowUseExpStr && nowUseExpStr.charAt(0) != 'ε') {
					for (int j = nowUseExpStr.length()-1; j >= 0; j--) {
						char temChar = nowUseExpStr.charAt(j);
						analyseStack.push(temChar);
					}
				}
				continue;
			}
			
			// 栈顶字符 与 剩余串首字符 相同，匹配
			if (analyseStack.peek() == leftStr.charAt(0)) {
				// 存储此时的匹配信息
				AnalyseItem analyseItem = new AnalyseItem();
				analyseItem.setIndex(index);
				analyseItem.setAnalyseStackItem(analyseStack.toString());
				analyseItem.setLeftStr(leftStr);
				if (leftStr.charAt(0) == '#') {
					analyseItem.setUseExpStr("接受" );
				}
				else {
					analyseItem.setUseExpStr("'" + leftStr.charAt(0) + "' 匹配" );
				}	
				analyseList.add(analyseItem);
				analyseStack.pop();    //栈顶出站
				leftStr = leftStr.substring(1);    //剩余串删去首字符
				continue;
			}
		}
		
		return analyseList;
	}

}













