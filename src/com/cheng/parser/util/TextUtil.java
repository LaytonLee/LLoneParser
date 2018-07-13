package com.cheng.parser.util;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 对字符串进行操作的工具类
 * @author 誠
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
			if (charAt == 'ε') {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断： 形如 A -> AS , Follow(S), S 后为非终结符
	 * 操作： 将  Follow(A) 加入  Follow(S)
	 * 
	 * @param itemCharStr
	 * 					待查找的表达式，既 AS
	 * @param a
	 * 			所求的非终结符，既 S
	 */
	public static boolean isLike_AS(String itemCharStr, Character a){
		String aStr = a.toString();
		// itemCharStr 串中的最后字符
		String lastStr = itemCharStr.substring(itemCharStr.length() - 1);
		// 若 a 与  lastStr 相同， 返回  true
		if (lastStr.equals(aStr)) {
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * 获取字符串 itemCharStr 中 a 后面的一个字符
	 * 
	 * @param itemCharStr
	 * 					字符串
	 * @param a
	 * 			字符a
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
	 * 获取字符a后面的字符串
	 * @param expression
	 * 					表达式
	 * @param a
	 * 			字符a
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
	 * 表达式是否以 ε 开头
	 * 
	 * @param expression
	 * 					表达式
	 * @return
	 */
	public static boolean isNullStart(String expression){
		Character startCharacter = expression.charAt(0);
		if (startCharacter.equals('ε')) {
			return true;
		}
		return false;
	}
	
	/**
	 * 表达式中含有非终结符
	 * 
	 * @param vtSet
	 * 				终结符集
	 * @param expression
	 * 				表达式
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
	 * 表达式均为非终结符，若此时所有的非终结符均可推出ε，返回true
	 * 
	 * @param vnSetToNull
	 * 					当前得到的可推出ε的终结符集
	 * @param expression
	 * 					表达式
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
	 * 以非终结符开始
	 * 
	 * @param vnSet
	 * 				非终结符集
	 * @param expression
	 * 					表达式
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
	 * 表达式以终结符开始
	 * 
	 * @param vtSet
	 * 				终结符集
	 * @param expression
	 * 					表达式
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
	 * 构造预测分析表
	 * 得到预测分析表对应的表达式
	 * 
	 * @param selectMap
	 * 					select集
	 * @param peek
	 * 			 分析表当前匹配的列字符	，非终结符
	 * @param charAt
	 * 				分析表当前的行匹配字符  ，终结符		
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
	 * 从预测分析表匹配表达式
	 * 
	 * @param analysisTable
	 * 						预测分析表
	 * @param charColPresent
	 * 						0列元素匹配
	 * @param charRowPresent
	 * 						0行元素匹配
	 * @return
	 */
	public static String findExpFromTable(String[][] analysisTable, Character charColPresent, Character charRowPresent){
		for (int i = 0; i < analysisTable.length; i++) {    //遍历数组0列
			if (analysisTable[i][0].equals(charColPresent.toString())) {    //匹配列上的非终结符
				for (int j = 0; j < analysisTable[0].length; j++) {    //遍历数组0行
					if (analysisTable[0][j].equals(charRowPresent.toString())) {    //匹配行的的zhong'j
						return analysisTable[i][j];
					}
				}
			}
		}
		return null;
	}
	
	
	
	//main，测试函数 
	public static void main(String[] args){
		String string = "safssd";
		String[][] stringArray = {
				{"Vn\\Vt", "(" , ")", "*", "+", "i", "#"},
				{"E", "TH", "", "", "", "TH", ""},
				{"F", "(E)", "", "", "", "i", ""},
				{"H", "", "ε", "", "+TH", "", "ε"},
				{"J", "", "ε", "*FJ", "ε", "", "ε"},
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
