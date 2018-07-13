package com.cheng.parser.model;

import java.util.ArrayList;
import java.util.Stack;

import com.cheng.parser.util.TextUtil;

/**
 * �������ʽ��
 * @author �\
 *
 */
public class Analyser {
	
	/**
	 * �Ա��ʽ���з���
	 * @param analysisTable
	 * 						Ԥ�������
	 * @param startChar
	 * 					��ʼ�ַ�
	 * @param expression
	 * 				��Ҫ�������ַ���
	 */
	public ArrayList<AnalyseItem> analyse(String[][] analysisTable, Character startChar, String expression){
		
		ArrayList<AnalyseItem> analyseList = new ArrayList<>();    //�洢�������̵�����
		int index = 0;    //���
		Stack<Character> analyseStack = new Stack<>();    //����ջ
		String leftStr = expression;    //ʣ����ʽ������ֵ
		
		analyseStack.push('#');    // # ��ջ
		analyseStack.push(startChar);    // ��ʼ����ջ
		
		//��������
		while (!analyseStack.isEmpty()){
			index++;    //���++
			// ջ��Ԫ�� �� ʣ�മ���ַ� ��ͬ
			if (analyseStack.peek() != leftStr.charAt(0)){
				// ���
				String nowUseExpStr = TextUtil.findExpFromTable(analysisTable, analyseStack.peek(), leftStr.charAt(0));
				// �洢��ʱ��
				AnalyseItem analyseItem = new AnalyseItem();
				analyseItem.setIndex(index);
				analyseItem.setAnalyseStackItem(analyseStack.toString());
				analyseItem.setLeftStr(leftStr);
				//System.out.println(nowUseExpStr);
				// Ԥ�����������ƥ����
				if (null == nowUseExpStr) {
					analyseItem.setUseExpStr("�޷�ƥ��");
					analyseList.add(analyseItem);
					return analyseList;
				}
				// Ԥ��������к�ƥ����
				else {
					analyseItem.setUseExpStr(analyseStack.peek() + "->" + nowUseExpStr);
				}
				analyseList.add(analyseItem);
				
				analyseStack.pop();
				// ƥ��Ĳ���ʽ������ջ
				if (null != nowUseExpStr && nowUseExpStr.charAt(0) != '��') {
					for (int j = nowUseExpStr.length()-1; j >= 0; j--) {
						char temChar = nowUseExpStr.charAt(j);
						analyseStack.push(temChar);
					}
				}
				continue;
			}
			
			// ջ���ַ� �� ʣ�മ���ַ� ��ͬ��ƥ��
			if (analyseStack.peek() == leftStr.charAt(0)) {
				// �洢��ʱ��ƥ����Ϣ
				AnalyseItem analyseItem = new AnalyseItem();
				analyseItem.setIndex(index);
				analyseItem.setAnalyseStackItem(analyseStack.toString());
				analyseItem.setLeftStr(leftStr);
				if (leftStr.charAt(0) == '#') {
					analyseItem.setUseExpStr("����" );
				}
				else {
					analyseItem.setUseExpStr("'" + leftStr.charAt(0) + "' ƥ��" );
				}	
				analyseList.add(analyseItem);
				analyseStack.pop();    //ջ����վ
				leftStr = leftStr.substring(1);    //ʣ�മɾȥ���ַ�
				continue;
			}
		}
		
		return analyseList;
	}

}













