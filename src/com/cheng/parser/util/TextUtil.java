package com.cheng.parser.util;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * ���ַ������в����Ĺ�����
 * @author �\
 *
 */
public class TextUtil {
	
	/**
	 * 
	 * @param expression
	 * @return
	 */
	public static boolean containsNull(String expression){
		for (int i = 0; i < expression.length(); i++) {
			char charAt = expression.charAt(i);
			if (charAt == '��') {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �жϣ� ���� A -> AS , Follow(S), S ��Ϊ���ս��
	 * ������ ��  Follow(A) ����  Follow(S)
	 * 
	 * @param itemCharStr
	 * 					�����ҵı��ʽ���� AS
	 * @param a
	 * 			����ķ��ս������ S
	 */
	public static boolean isLike_AS(String itemCharStr, Character a){
		String aStr = a.toString();
		// itemCharStr ���е�����ַ�
		String lastStr = itemCharStr.substring(itemCharStr.length() - 1);
		// �� a ��  lastStr ��ͬ�� ����  true
		if (lastStr.equals(aStr)) {
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * ��ȡ�ַ��� itemCharStr �� a �����һ���ַ�
	 * 
	 * @param itemCharStr
	 * 					�ַ���
	 * @param a
	 * 			�ַ�a
	 * @return
	 */
	public static Character getANextChar(String itemCharStr, Character a){
		String aStr = a.toString();
        if (itemCharStr.contains(aStr)) {
            int aIndex = itemCharStr.indexOf(aStr);
            String aNextStr = "";
            try {
            	aNextStr = itemCharStr.substring(aIndex + 1, aIndex + 2);
            } catch (Exception e) {
                return null;
            }
            return aNextStr.charAt(0);
        }
        return null;
	}
	
	/**
	 * ��ȡ�ַ�a������ַ���
	 * @param expression
	 * 					���ʽ
	 * @param a
	 * 			�ַ�a
	 * @return
	 */
	public static String getANextstr(String expression, Character a) {
		String aStr = a.toString();
		if (expression.contains(aStr)) {
			int aIndex = expression.indexOf(aStr);
			String aNextStr = "";
			try{
				aNextStr = expression.substring(aIndex + 1, expression.length());
			}catch (Exception e) {
                return null;
            }
            return aNextStr;
        }
        return null;
	}
	
	/**
	 * ���ʽ�Ƿ��� �� ��ͷ
	 * 
	 * @param expression
	 * 					���ʽ
	 * @return
	 */
	public static boolean isNullStart(String expression){
		Character startCharacter = expression.charAt(0);
		if (startCharacter.equals('��')) {
			return true;
		}
		return false;
	}
	
	/**
	 * ���ʽ�к��з��ս��
	 * 
	 * @param vtSet
	 * 				�ս����
	 * @param expression
	 * 				���ʽ
	 * @return
	 */
	public static boolean containsVt(TreeSet<Character> vtSet, String expression ){
		for (int i = 0; i < expression.length(); i++) {
			Character charExp = expression.charAt(i);
			if (vtSet.contains(charExp)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ���ʽ��Ϊ���ս��������ʱ���еķ��ս�������Ƴ��ţ�����true
	 * 
	 * @param vnSetToNull
	 * 					��ǰ�õ��Ŀ��Ƴ��ŵ��ս����
	 * @param expression
	 * 					���ʽ
	 * @return
	 */
	public static boolean expressionToNull(TreeSet<Character> vnSetToNull, String expression){
		Character charExpression;
		boolean judge = false;
		for (int i = 0; i < expression.length(); i++) {
			charExpression = expression.charAt(i);
			if (vnSetToNull.contains(charExpression)) {
				judge = true;
			}
			else {
				return false;
			}
		}
		return judge;
	}
	
	/**
	 * �Է��ս����ʼ
	 * 
	 * @param vnSet
	 * 				���ս����
	 * @param expression
	 * 					���ʽ
	 * @return
	 */
	public static boolean isVnStart(TreeSet<Character> vnSet, String expression){
		Character startCharater = expression.charAt(0);
		if (vnSet.contains(startCharater)) {
			return true;
		}
		return false;
	}
	
	/**
	 * ���ʽ���ս����ʼ
	 * 
	 * @param vtSet
	 * 				�ս����
	 * @param expression
	 * 					���ʽ
	 * @return
	 */
	public static boolean isVtStart(TreeSet<Character> vtSet, String expression){
		Character startCharater = expression.charAt(0);
		if (vtSet.contains(startCharater)) {
			return true;
		}
		return false;
	}
	
	/**
	 * ����Ԥ�������
	 * �õ�Ԥ��������Ӧ�ı��ʽ
	 * 
	 * @param selectMap
	 * 					select��
	 * @param peek
	 * 			 ������ǰƥ������ַ�	�����ս��
	 * @param charAt
	 * 				������ǰ����ƥ���ַ�  ���ս��		
	 * @return
	 */
	public static String findExp(TreeMap<Character, HashMap<String, TreeSet<Character>>> selectMap, 
			Character peek, char charAt) {
		try {
			HashMap<String, TreeSet<Character>> hashMap = selectMap.get(peek);
			Set<String> keySet = hashMap.keySet();
			for (String useExp : keySet) {
				TreeSet<Character> treeSet = hashMap.get(useExp);
				if (treeSet.contains(charAt)) {
					return useExp;
				}
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
	
	/**
	 * ��Ԥ�������ƥ����ʽ
	 * 
	 * @param analysisTable
	 * 						Ԥ�������
	 * @param charColPresent
	 * 						0��Ԫ��ƥ��
	 * @param charRowPresent
	 * 						0��Ԫ��ƥ��
	 * @return
	 */
	public static String findExpFromTable(String[][] analysisTable, Character charColPresent, Character charRowPresent){
		for (int i = 0; i < analysisTable.length; i++) {    //��������0��
			if (analysisTable[i][0].equals(charColPresent.toString())) {    //ƥ�����ϵķ��ս��
				for (int j = 0; j < analysisTable[0].length; j++) {    //��������0��
					if (analysisTable[0][j].equals(charRowPresent.toString())) {    //ƥ���еĵ�zhong'j
						return analysisTable[i][j];
					}
				}
			}
		}
		return null;
	}
	
	
	
	//main�����Ժ��� 
	public static void main(String[] args){
		String string = "safssd";
		String[][] stringArray = {
				{"Vn\\Vt", "(" , ")", "*", "+", "i", "#"},
				{"E", "TH", "", "", "", "TH", ""},
				{"F", "(E)", "", "", "", "i", ""},
				{"H", "", "��", "", "+TH", "", "��"},
				{"J", "", "��", "*FJ", "��", "", "��"},
				{"T", "FJ", "", "", "", "FJ", ""}
			};
		System.out.println(string.substring(1));
		int a = string.indexOf("s");
		//System.out.println(getANextstr(string, 'd') + getANextstr(string, 'd').length());
		TreeSet<Character> treeSet = new TreeSet<>();
		treeSet.add('a');
		treeSet.add('b');

	}
}
