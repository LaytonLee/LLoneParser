package com.cheng.parser.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.cheng.parser.util.TextUtil;
/**
 * LL1文法的POJO
 * 
 * @author 誠
 *
 */
public class LL1GrammarModel implements Serializable {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 590880309899892136L;
	
	/**
	 * 产生式链表
	 */
	private ArrayList<String> production;
	
	/**
	 * 表达式 <Vn,产生式链表>
	 */
	private HashMap<Character, ArrayList<String>> expressionMap;
	
	/**
	 * 开始符
	 */
	private Character start;
	
	/**
	 * first集
	 */
	private HashMap<Character, TreeSet<Character>> firstMap;
	
	/**
	 * follow集
	 */
	private HashMap<Character, TreeSet<Character>> followMap;
	
	/**
	 * select集
	 */
	private TreeMap<Character, HashMap<String, TreeSet<Character>>> selectMap;
	
	/**
	 * 非终结符集
	 */
	private TreeSet<Character> vnSet;
	
	/**
	 * 终结符集
	 */
	private TreeSet<Character> vtSet;
	
	/**
	 * 可推导到 ε 的非终结符集
	 */
	private TreeSet<Character> vnSetToNull;
	
	/**
	 * 预测分析表
	 */
	private String[][] analysisTable;
	
	/**
	 * 无参构造方法
	 */
	public LL1GrammarModel(){
		super();
	}
	
	/**
	 * 构造方法，并初始化此POJO
	 * 
	 * @param production
	 * 					文法产生式链表
	 * @param start
	 * 				开始符
	 */
	public LL1GrammarModel(ArrayList<String> production, Character start){
		super();
		vnSet = new TreeSet<>();
		vtSet = new TreeSet<>();
		vnSetToNull = new TreeSet<>();
		expressionMap = new HashMap<>();
		firstMap = new HashMap<>();
		followMap = new HashMap<>();
		selectMap = new TreeMap<>();
		
		this.production = production;
		this.start = start;
		
		//获取终结符与非终结符
		getVtVn();
		
		//初始化表达式
		initExpressionMaps();
		
		//获取可以推导到 ε 的非终结符
		getVnToNull();
		
		//获取FIRST集
		getFirst();
		
		//获取FOLLOW集
		getFollow();
		
		//获取SELECT集
		getSelect();
		
		//获取预测分析表
		getPredictingAnalysisTable();
	}
	
	public TreeSet<Character> getVnSetToNull() {
		return vnSetToNull;
	}

	public void setVnSetToNull(TreeSet<Character> vnSetToNull) {
		this.vnSetToNull = vnSetToNull;
	}

	/**
	 * 得出终结符与非终结符集合
	 */
	public void getVtVn(){
		//取得产生式右侧非终结符集合
		for (String productionItem : production) {
			String[] items = productionItem.split("->");    //切分字符串
			String vnString = items[0];
			//System.out.print(vnString + " ");
			char vn = vnString.charAt(0);
			vnSet.add(vn);
		}
		
		//取得产生式左侧终结符集合
		for (String productionItem : production) {
			String[] items = productionItem.split("->");    //切分字符串
			String vtString = items[1].trim();   //去掉首尾可能存在的空格
			//遍历右侧字符串
			for (int i = 0; i < vtString.length(); i++) {
				char vt = vtString.charAt(i);
				//若字符不在非终结符集中，将其加入非终结符集
				if (!vnSet.contains(vt)) {
					vtSet.add(vt);
				}
			}
		}
		//System.out.println("非终结符集：" + vnSet.toString() + "\n终结符集：" + vtSet.toString());
	}
	
	/**
	 * 初始化表达式
	 */
	public void initExpressionMaps(){
		expressionMap = new HashMap<Character, ArrayList<String>>();
		for (String productionItem : production) {
			String[] items = productionItem.split("->");
			String leftItem = items[0].trim();     //产生式左端
			String rightItem = items[1].trim();    //产生式右端
			char charItem = leftItem.charAt(0);    //左端非终结符
			//非终结符不在表达式集中，加入
			if (!expressionMap.containsKey(charItem)) {
				ArrayList<String> expression = new ArrayList<String>();
				expression.add(rightItem);
				expressionMap.put(charItem, expression);
			}
			//非终结符已在表达式集中，添加表达式
			else {
				ArrayList<String> expression = expressionMap.get(charItem);
				expression.add(rightItem);
				expressionMap.put(charItem, expression);
			}
		}
		//System.out.println("表达式：" + expressionMap.toString());
	}
	
	/**
	 * 获取可以推导到 ε 的非终结符
	 */
	public void getVnToNull(){
		/**
		 * 获取可删除的表达式，非终结符map
		 */
		HashMap<Character, ArrayList<String>> temExpressionMap = new HashMap<>();
		for (String productionItem : production) {
			String[] items = productionItem.split("->");
			String leftItem = items[0].trim();     //产生式左端
			String rightItem = items[1].trim();    //产生式右端
			char charItem = leftItem.charAt(0);    //左端非终结符
			//非终结符不在表达式集中，加入
			if (!temExpressionMap.containsKey(charItem)) {
				ArrayList<String> expression = new ArrayList<String>();
				expression.add(rightItem);
				temExpressionMap.put(charItem, expression);
			}
			//非终结符已在表达式集中，添加表达式
			else {
				ArrayList<String> expression = temExpressionMap.get(charItem);
				expression.add(rightItem);
				temExpressionMap.put(charItem, expression);
			}
		}
		
		//System.out.println("表达式Map：" + temExpressionMap.toString());
		
		Set<Character> keySet = temExpressionMap.keySet();  //非终结符表
		Iterator<Character> iterator = keySet.iterator();
		
		/**
		 * 1. 扫描，扫描 产生式中的终结符与ε
		 */
		while (iterator.hasNext()){
			Character keyItem = iterator.next();  //当前非终结符
			ArrayList<String> expressionArray = temExpressionMap.get(keyItem);
			
			for (String expression : expressionArray) {
				//当前表达式含有 ε ，向 vnSetToNull 添加值，清除当前 表达式链表 并添加 null值
				if ( expression.contains("ε") ) {
					vnSetToNull.add(keyItem);
					expressionArray.clear();
					expressionArray.add(null);
					break;
				}
				
				//当前表达式含有终结符 ，请链表中的当前项的值置为 ""
				if ( TextUtil.containsVt(vtSet, expression) ) {
					int position = expressionArray.indexOf(expression);
					expressionArray.set(position, "");
				}
			}
			
		}
		
		//System.out.println(temExpressionMap.toString());
		//System.out.println(vnSetToNull.toString());
		
		/**
		 * 2. 去掉链表中为 "" 或 null 的值
		 */
		ArrayList<String> arrayListNull = new ArrayList<>();
		arrayListNull.add(null);
		ArrayList<String> arrayListSizeZero = new ArrayList<>();
 		arrayListSizeZero.add("");
 		arrayListSizeZero.remove("");
 		
 		//去掉可以推导到 ε 的 temExpressionMap 中的项 <非终结符, 表达式>
 		for (Character keyItem : vnSetToNull) {
			temExpressionMap.remove(keyItem);
		}
 		
 		//删除 temExpressionMap <非终结符, 表达式> 表达式中的值为 "",既表达式含有非终结符的项
 		// 1) 去掉多余的""值
 		keySet = temExpressionMap.keySet();    //更新keySet
 		for (Character keyItem : keySet) {
			ArrayList<String> expressionArray = temExpressionMap.get(keyItem);
			for (int i = 0; i < expressionArray.size(); i++) {
				expressionArray.remove("");
			}
		}
 		//System.out.println(temExpressionMap.toString());
 		
 		// 2) 获取表达式中为 "" 的项 对应的key —— 非终结符
 		TreeSet<Character> temSet = new TreeSet<>();    //临时存储key
 		for (Character keyItem : keySet) {
 			ArrayList<String> expressionArray = temExpressionMap.get(keyItem);
			if (expressionArray.size() == 1 && expressionArray.get(0).equals("")) {
				temSet.add(keyItem);
			}
		}
 		// 3) 删除 temExpressionMap 中 value 均为 "" 的项
 		for (Character character : temSet) {
			temExpressionMap.remove(character);
		}
 		//System.out.println(temExpressionMap.toString());
 		
 		/**
 		 * 3. 若此时temExpressionMap非空，则剩下的为 <非终结符，只含非终结符的表达式>
 		 */
 		if (temExpressionMap.size() != 0) {
 			keySet = temExpressionMap.keySet();     //更新表达式Map temExpressionMap 的key集
 			Iterator<Character> iterator_expression = keySet.iterator();    //创建keySet的迭代器
 			//迭代器 iterator_expression 遍历 剩余的 temExpressionMap 中的元素
 			while(iterator_expression.hasNext()){
 				ArrayList<String> expressionArray = new ArrayList<>();    //表达式链表
 				Character keyItem = iterator_expression.next();    // 当前的 key
 				expressionArray = temExpressionMap.get(keyItem);    // 链表赋值
 				//遍历链表
 				for (String expression : expressionArray) {
 					//若表达式的非终结符均可推导到 ε，将表达式左侧的非终结符 加入 vnSetToNull
 					if (TextUtil.expressionToNull(vnSetToNull, expression)) {
 						vnSetToNull.add(keyItem);
 					}
				}
 			}	
		}
 		System.out.println("可以推导到 ε的非终结符：" + vnSetToNull.toString());
 		
	}
	
	/**
	 * 获取First集合
	 */
	public void getFirst(){
		//遍历非终结符集合，求First集
		Iterator<Character> iterator = vnSet.iterator();
		while (iterator.hasNext()) {
			Character charVn = (Character) iterator.next();
			//System.out.println("charVn:" + charVn);
			ArrayList<String> expression = expressionMap.get(charVn);
			for (String item : expression) {
				boolean isBreak = false;    //跳出循环的标志
				//遍历每一个表达式
				for (int i = 0; i < item.length(); i++) {
					char itemChar = item.charAt(i);
					char itemCharNext;
					//获取当前字符的后一个字符
					if ((i + 1) < item.length()) {
						itemCharNext = item.charAt(i+1);
					}
					else {
						itemCharNext = ' ';
					}
					TreeSet<Character> firstSet = firstMap.get(charVn);
					if (null == firstSet) {
						firstSet = new TreeSet<Character>();
					}
					boolean isContNull = false;    //是否包含 ε的标识
					isBreak = calculateFirst(firstSet, charVn, itemChar, itemCharNext, isContNull);
					if (isBreak) {
						break;
					}
				}
			}
			
		}
	}
	/**
	 * 计算First集
	 * 
	 * @param firstSet
	 * 				First集
	 * @param vnCharacter
	 * 				当前计算的非终结符
	 * @param itemChar
	 * 				当前判断的字符
	 * @param itemCharNext
	 * 				当前判断字符的下一个字符
	 * @param isContNull
	 * 				判断是否添加ε
	 * @return
	 */
	public boolean calculateFirst(TreeSet<Character> firstSet, Character vnCharacter, 
			char itemChar, char itemCharNext, boolean isContNull){
		//当前字符为ε或者终结符
		if (itemChar == 'ε' || vtSet.contains(itemChar)) {
			firstSet.add(itemChar);
			firstSet.remove('ε');
			if (isContNull) {
				firstSet.add('ε');
			}
			if (vnSetToNull.contains(vnCharacter)) {
				firstSet.add('ε');
			}
			firstMap.put(vnCharacter, firstSet);
			return true;
		} else if (vnSetToNull.contains(itemChar) && vnSet.contains(itemCharNext)) {   //当前字符为  可以推导到ε的非终结符
			isContNull = true;
			//得到当前字符的表达式
			ArrayList<String> expression_calFirst = expressionMap.get(itemChar);
			//遍历表达式求first集
			for (int i = 0; i < expression_calFirst.size(); i++) {
				String string = expression_calFirst.get(i);  //表达式
				//遍历表达式的字符
				for (int j = 0; j < string.length(); j++) {
					char temChar = string.charAt(j);
					char temCharNext;
					if ( (j+1) < string.length()) {
						temCharNext = string.charAt(j+1);
					}
					else {
						temCharNext = ' ';
					}
					calculateFirst(firstSet, vnCharacter, temChar, temCharNext, isContNull);    //递归调用
				}	
			}
			
			return false;  //可继续推导
		} else if (vnSet.contains(itemChar) )  {    //当前字符为   不可推导到ε的非终结符
			isContNull = false;
			//得到当前字符的表达式
			ArrayList<String> expression_calFirst = expressionMap.get(itemChar);
			//遍历表达式求first集
			for (int i = 0; i < expression_calFirst.size(); i++) {
				String string = expression_calFirst.get(i);    //表达式
				//遍历表达式的每一个字符
				for (int j = 0; j < string.length(); j++) {
					char temChar = string.charAt(j);
					char temCharNext;
					if ( (j+1) < string.length()) {
						temCharNext = string.charAt(j+1);
					}
					else {
						temCharNext = ' ';
					}
					//防止遇到非终结符的无限循环
					boolean isBreak = calculateFirst(firstSet, vnCharacter, temChar, temCharNext, isContNull);    //递归调用
					if (isBreak) {
						break;
					}
				}
				
			}
			return true;    //不可继续推导
		}
		return true;
	}
	
	/**
	 * 已知所有为终结符的First集，获取某表达式的First集
	 * 
	 * @param expression
	 * 					表达式
	 * @return
	 */
	public TreeSet<Character> getExpressionFirst(String expression ){
		TreeSet<Character> expressionFirstSet = new TreeSet<>();
		//开始字符为终结符
		if (TextUtil.isVtStart(vtSet, expression)) {
			expressionFirstSet.add(expression.charAt(0));
		} 
		//开始字符为非终结符
		if (TextUtil.isVnStart(vnSet, expression)) {
			//遍历表达式中的每一个字符
			for (int i = 0; i < expression.length(); i++) {
				char expressionChar = expression.charAt(i);   //当前处理的字符  
				char expressionCharNext;    //当前字符的后一个字符
				if ( (i+1) < expression.length()) {
					expressionCharNext = expression.charAt(i+1);
				}
				else {
					expressionCharNext = ' ';
				}
				//当前字符可以推导到ε
				if (vnSetToNull.contains(expressionChar)) {
					expressionFirstSet.addAll(firstMap.get(expressionChar));
					if (vtSet.contains(expressionCharNext)) {    //下个字符为终结符
						expressionFirstSet.remove('ε');    //去除ε
						break;    //跳出循环
					} else if (!vnSetToNull.contains(expressionCharNext)) {    //下个字符不可推导到ε
						expressionFirstSet.addAll(firstMap.get(expressionChar));    //加入下个字符的First集
						//若下个字符存在，不为' ',既expressionChar不为最后一个字符
						if (expressionCharNext != ' ') {
							expressionFirstSet.remove('ε');
						}
						//终止循环
						break;
					} else {    //下个字符可以推导到ε
						expressionFirstSet.add('ε');
					}
				}
				//首字符为非终结符但不可推导到ε
				else {
					expressionFirstSet.addAll(firstMap.get(expressionChar));
					expressionFirstSet.remove('ε');
					break;
				}
			}
			
		}
		//返回表达式的First集
		return expressionFirstSet;
	}
	
	/**
	 * 获取follow集
	 */
	public void getFollow(){
		//向follow集加入非终结符
		for (Character temKey : vnSet) {
			TreeSet<Character> temSet = new TreeSet<Character>();
			followMap.put(temKey, temSet);
		}
		
		//遍历非终结符，求出其follow集
		//倒序
		Iterator<Character> iterator = vnSet.descendingIterator();
		//迭代器迭代
		while (iterator.hasNext()){
			Character charItem = iterator.next();    //非终结符
			//System.out.println("charItem:" + charItem);
			Set<Character> keySet = expressionMap.keySet();    //表达式左端非终结符集
			for (Character keyCharItem : keySet) {
				ArrayList<String> charItemArray = expressionMap.get(keyCharItem);    //右端表达式
				for (String itemCharStr : charItemArray) {
					//System.out.println(keyCharItem + "->" + itemCharStr);
					TreeSet<Character> itemSet = followMap.get(charItem);     //follow集
					calculateFollow(charItem, charItem, keyCharItem, itemCharStr, itemSet);
				}
			}
		}
	}
	/**
	 * 计算Follow集
	 * 例：对 A -> αSβ，求Follow(S),
	 * 
	 * @param putCharItem
	 * 				正在计算的非终结符，既 S
	 * @param charItem
	 * 				对应递归查询， 0 次递归为 S
	 * 						  1 次递归为 A 
	 * @param keyCharItem
	 * 				表达式左侧节点字符， 既 A
	 * @param itemCharStr
	 * 				当前判断的表达式， 既 αSβ
	 * @param itemSet
	 * 				follow结果集合， 既 Follow(S)
	 * 
	 */
	public void calculateFollow(Character putCharItem, Character charItem, Character keyCharItem, 
			String itemCharStr, TreeSet<Character> itemSet){
		
		// 1) 开始符，加入 #
		if (charItem.equals(start)) {
			itemSet.add('#');
			followMap.put(putCharItem, itemSet);
		}
		
		//若表达式中含有当前所求Follow集的非终结符
		//将该非终结符右边表达式的First集加入Follow集
		//若右侧表达式可以推导到ε
		//加入表达式左侧非终结符的follow集
		if (itemCharStr.contains(charItem.toString())) {
			String partExpression = TextUtil.getANextstr(itemCharStr, charItem);  //该非终结符右边表达式
			if (null != partExpression && partExpression.length() != 0) {
				itemSet.addAll(getExpressionFirst(partExpression));  //右边表达式的First集
				//若右侧表达式可以推导到ε
				if (itemSet.contains('ε')) {
					//System.out.println(partExpression);
					itemSet.remove('ε');
					//非  S -> AS , 求 Follow(S) 形式
					if (!keyCharItem.equals(charItem)) {
						//获取表达式左侧集合
						Set<Character> keySet = expressionMap.keySet();
						for (Character keyCharItems : keySet) {
							//获取表达式
							ArrayList<String> charItemArray = expressionMap.get(keyCharItems);
							for (String itemCharStrs : charItemArray) {
								calculateFollow(putCharItem, keyCharItem, keyCharItems, itemCharStrs, itemSet);
							}
						}
					}
				}
			}
			// A -> AS , 将follow(A)加入
			if (TextUtil.isLike_AS(itemCharStr, charItem)) {
				//非  S -> AS , 求 Follow(S) 形式
				if (!keyCharItem.equals(charItem)) {
					//获取表达式左侧集合
					Set<Character> keySet = expressionMap.keySet();
					for (Character keyCharItems : keySet) {
						//获取表达式
						ArrayList<String> charItemArray = expressionMap.get(keyCharItems);
						for (String itemCharStrs : charItemArray) {
							calculateFollow(putCharItem, keyCharItem, keyCharItems, itemCharStrs, itemSet);
						}
					}
				}
			}
		}
		
		
		
	}
	
	/**
	 * 获取select集
	 * @return
	 */
	public void getSelect(){
		//遍历所有表达式
		Set<Character> keySet = expressionMap.keySet();
		for (Character selectKey : keySet) {
			//对应同一非终结符的表达式链表
			ArrayList<String> expressionList = expressionMap.get(selectKey);  
			//每一个表达式的select集 <表达式 , select 集>
			HashMap<String, TreeSet<Character>> selectItemSet = new HashMap<>();
			
			//遍历同一非终结符的表达式
			for (String expression : expressionList) {
				//存放 select集
				TreeSet<Character> selectSet = new TreeSet<>();
				//将右侧表达式的First集存入Select集
				selectSet.addAll(getExpressionFirst(expression));
				//若右侧表达式可以推导到ε，将ε移除，将Follow集加入
				if (selectSet.contains('ε')) {
					selectSet.remove('ε');
					selectSet.addAll(followMap.get(selectKey));
				}
				//组装 表达式，Select集
				selectItemSet.put(expression, selectSet);
				//将表达式 <开始字符, selectItemset> 组装进 selectMap
				selectMap.put(selectKey, selectItemSet);			
			}
		}
	}
	
	/**
	 * 获取预测分析表
	 * @throws Exception
	 */
	public void getPredictingAnalysisTable(){
		Object[] vtArray = vtSet.toArray();
		Object[] vnArray = vnSet.toArray();
		
		//初始化分析表
		analysisTable = new String[vnArray.length + 1][vtArray.length + 1];
		//在预测分析表 [0][0] 处存入 "Vn\\Vt"
		analysisTable[0][0] = "Vn\\Vt";
		
		//初始化分析表首行
		for (int i = 0; i < vtArray.length; i++) {
			if (vtArray[i].equals('ε')) {
				vtArray[i] = '#';
			}
			//存入终结符
			analysisTable[0][i+1] = vtArray[i].toString();
		}
		
		//初始化分析表首列
		for (int i = 0; i < vnArray.length; i++) {
			analysisTable[i+1][0] = vnArray[i].toString();
		}
		
		//向分析表赋值
		for (int i = 1; i <= vnArray.length; i++) {
			for (int j = 1; j <= vtArray.length; j++) {
				Character charColPresent = analysisTable[i][0].charAt(0);  //行，终结符
				char charRowPresent = analysisTable[0][j].charAt(0);  //列， 非终结符
				//得到对应的表达式
				String expression = TextUtil.findExp(selectMap, charColPresent, charRowPresent);
				//表达式不为空，赋值
				if ( null != expression ) {
					analysisTable[i][j] = expression;
				}
			}
			
		}
	}
	
	/**
	 * getter
	 * setter
	 */
	public ArrayList<String> getProduction() {
		return production;
	}

	public void setProduction(ArrayList<String> production) {
		this.production = production;
	}

	public HashMap<Character, ArrayList<String>> getExpressionMap() {
		return expressionMap;
	}

	public void setExpressionMap(HashMap<Character, ArrayList<String>> expressionMap) {
		this.expressionMap = expressionMap;
	}

	public Character getStart() {
		return start;
	}

	public void setStart(Character start) {
		this.start = start;
	}

	public HashMap<Character, TreeSet<Character>> getFirstMap() {
		return firstMap;
	}

	public void setFirstMap(HashMap<Character, TreeSet<Character>> firstMap) {
		this.firstMap = firstMap;
	}

	public HashMap<Character, TreeSet<Character>> getFollowMap() {
		return followMap;
	}

	public void setFollowMap(HashMap<Character, TreeSet<Character>> followMap) {
		this.followMap = followMap;
	}

	public TreeMap<Character, HashMap<String, TreeSet<Character>>> getSelectMap() {
		return selectMap;
	}

	public void setSelectMap(TreeMap<Character, HashMap<String, TreeSet<Character>>> selectMap) {
		this.selectMap = selectMap;
	}

	public TreeSet<Character> getVnSet() {
		return vnSet;
	}

	public void setVnSet(TreeSet<Character> vnSet) {
		this.vnSet = vnSet;
	}

	public TreeSet<Character> getVtSet() {
		return vtSet;
	}

	public void setVtSet(TreeSet<Character> vtSet) {
		this.vtSet = vtSet;
	}

	public String[][] getAnalysisTable() {
		return analysisTable;
	}

	public void setAnalysisTable(String[][] analysisTable) {
		this.analysisTable = analysisTable;
	}
	
	
}



















