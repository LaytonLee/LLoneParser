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
 * LL1�ķ���POJO
 * 
 * @author �\
 *
 */
public class LL1GrammarModel implements Serializable {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 590880309899892136L;
	
	/**
	 * ����ʽ����
	 */
	private ArrayList<String> production;
	
	/**
	 * ���ʽ <Vn,����ʽ����>
	 */
	private HashMap<Character, ArrayList<String>> expressionMap;
	
	/**
	 * ��ʼ��
	 */
	private Character start;
	
	/**
	 * first��
	 */
	private HashMap<Character, TreeSet<Character>> firstMap;
	
	/**
	 * follow��
	 */
	private HashMap<Character, TreeSet<Character>> followMap;
	
	/**
	 * select��
	 */
	private TreeMap<Character, HashMap<String, TreeSet<Character>>> selectMap;
	
	/**
	 * ���ս����
	 */
	private TreeSet<Character> vnSet;
	
	/**
	 * �ս����
	 */
	private TreeSet<Character> vtSet;
	
	/**
	 * ���Ƶ��� �� �ķ��ս����
	 */
	private TreeSet<Character> vnSetToNull;
	
	/**
	 * Ԥ�������
	 */
	private String[][] analysisTable;
	
	/**
	 * �޲ι��췽��
	 */
	public LL1GrammarModel(){
		super();
	}
	
	/**
	 * ���췽��������ʼ����POJO
	 * 
	 * @param production
	 * 					�ķ�����ʽ����
	 * @param start
	 * 				��ʼ��
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
		
		//��ȡ�ս������ս��
		getVtVn();
		
		//��ʼ�����ʽ
		initExpressionMaps();
		
		//��ȡ�����Ƶ��� �� �ķ��ս��
		getVnToNull();
		
		//��ȡFIRST��
		getFirst();
		
		//��ȡFOLLOW��
		getFollow();
		
		//��ȡSELECT��
		getSelect();
		
		//��ȡԤ�������
		getPredictingAnalysisTable();
	}
	
	public TreeSet<Character> getVnSetToNull() {
		return vnSetToNull;
	}

	public void setVnSetToNull(TreeSet<Character> vnSetToNull) {
		this.vnSetToNull = vnSetToNull;
	}

	/**
	 * �ó��ս������ս������
	 */
	public void getVtVn(){
		//ȡ�ò���ʽ�Ҳ���ս������
		for (String productionItem : production) {
			String[] items = productionItem.split("->");    //�з��ַ���
			String vnString = items[0];
			//System.out.print(vnString + " ");
			char vn = vnString.charAt(0);
			vnSet.add(vn);
		}
		
		//ȡ�ò���ʽ����ս������
		for (String productionItem : production) {
			String[] items = productionItem.split("->");    //�з��ַ���
			String vtString = items[1].trim();   //ȥ����β���ܴ��ڵĿո�
			//�����Ҳ��ַ���
			for (int i = 0; i < vtString.length(); i++) {
				char vt = vtString.charAt(i);
				//���ַ����ڷ��ս�����У����������ս����
				if (!vnSet.contains(vt)) {
					vtSet.add(vt);
				}
			}
		}
		//System.out.println("���ս������" + vnSet.toString() + "\n�ս������" + vtSet.toString());
	}
	
	/**
	 * ��ʼ�����ʽ
	 */
	public void initExpressionMaps(){
		expressionMap = new HashMap<Character, ArrayList<String>>();
		for (String productionItem : production) {
			String[] items = productionItem.split("->");
			String leftItem = items[0].trim();     //����ʽ���
			String rightItem = items[1].trim();    //����ʽ�Ҷ�
			char charItem = leftItem.charAt(0);    //��˷��ս��
			//���ս�����ڱ��ʽ���У�����
			if (!expressionMap.containsKey(charItem)) {
				ArrayList<String> expression = new ArrayList<String>();
				expression.add(rightItem);
				expressionMap.put(charItem, expression);
			}
			//���ս�����ڱ��ʽ���У���ӱ��ʽ
			else {
				ArrayList<String> expression = expressionMap.get(charItem);
				expression.add(rightItem);
				expressionMap.put(charItem, expression);
			}
		}
		//System.out.println("���ʽ��" + expressionMap.toString());
	}
	
	/**
	 * ��ȡ�����Ƶ��� �� �ķ��ս��
	 */
	public void getVnToNull(){
		/**
		 * ��ȡ��ɾ���ı��ʽ�����ս��map
		 */
		HashMap<Character, ArrayList<String>> temExpressionMap = new HashMap<>();
		for (String productionItem : production) {
			String[] items = productionItem.split("->");
			String leftItem = items[0].trim();     //����ʽ���
			String rightItem = items[1].trim();    //����ʽ�Ҷ�
			char charItem = leftItem.charAt(0);    //��˷��ս��
			//���ս�����ڱ��ʽ���У�����
			if (!temExpressionMap.containsKey(charItem)) {
				ArrayList<String> expression = new ArrayList<String>();
				expression.add(rightItem);
				temExpressionMap.put(charItem, expression);
			}
			//���ս�����ڱ��ʽ���У���ӱ��ʽ
			else {
				ArrayList<String> expression = temExpressionMap.get(charItem);
				expression.add(rightItem);
				temExpressionMap.put(charItem, expression);
			}
		}
		
		//System.out.println("���ʽMap��" + temExpressionMap.toString());
		
		Set<Character> keySet = temExpressionMap.keySet();  //���ս����
		Iterator<Character> iterator = keySet.iterator();
		
		/**
		 * 1. ɨ�裬ɨ�� ����ʽ�е��ս�����
		 */
		while (iterator.hasNext()){
			Character keyItem = iterator.next();  //��ǰ���ս��
			ArrayList<String> expressionArray = temExpressionMap.get(keyItem);
			
			for (String expression : expressionArray) {
				//��ǰ���ʽ���� �� ���� vnSetToNull ���ֵ�������ǰ ���ʽ���� ����� nullֵ
				if ( expression.contains("��") ) {
					vnSetToNull.add(keyItem);
					expressionArray.clear();
					expressionArray.add(null);
					break;
				}
				
				//��ǰ���ʽ�����ս�� ���������еĵ�ǰ���ֵ��Ϊ ""
				if ( TextUtil.containsVt(vtSet, expression) ) {
					int position = expressionArray.indexOf(expression);
					expressionArray.set(position, "");
				}
			}
			
		}
		
		//System.out.println(temExpressionMap.toString());
		//System.out.println(vnSetToNull.toString());
		
		/**
		 * 2. ȥ��������Ϊ "" �� null ��ֵ
		 */
		ArrayList<String> arrayListNull = new ArrayList<>();
		arrayListNull.add(null);
		ArrayList<String> arrayListSizeZero = new ArrayList<>();
 		arrayListSizeZero.add("");
 		arrayListSizeZero.remove("");
 		
 		//ȥ�������Ƶ��� �� �� temExpressionMap �е��� <���ս��, ���ʽ>
 		for (Character keyItem : vnSetToNull) {
			temExpressionMap.remove(keyItem);
		}
 		
 		//ɾ�� temExpressionMap <���ս��, ���ʽ> ���ʽ�е�ֵΪ "",�ȱ��ʽ���з��ս������
 		// 1) ȥ�������""ֵ
 		keySet = temExpressionMap.keySet();    //����keySet
 		for (Character keyItem : keySet) {
			ArrayList<String> expressionArray = temExpressionMap.get(keyItem);
			for (int i = 0; i < expressionArray.size(); i++) {
				expressionArray.remove("");
			}
		}
 		//System.out.println(temExpressionMap.toString());
 		
 		// 2) ��ȡ���ʽ��Ϊ "" ���� ��Ӧ��key ���� ���ս��
 		TreeSet<Character> temSet = new TreeSet<>();    //��ʱ�洢key
 		for (Character keyItem : keySet) {
 			ArrayList<String> expressionArray = temExpressionMap.get(keyItem);
			if (expressionArray.size() == 1 && expressionArray.get(0).equals("")) {
				temSet.add(keyItem);
			}
		}
 		// 3) ɾ�� temExpressionMap �� value ��Ϊ "" ����
 		for (Character character : temSet) {
			temExpressionMap.remove(character);
		}
 		//System.out.println(temExpressionMap.toString());
 		
 		/**
 		 * 3. ����ʱtemExpressionMap�ǿգ���ʣ�µ�Ϊ <���ս����ֻ�����ս���ı��ʽ>
 		 */
 		if (temExpressionMap.size() != 0) {
 			keySet = temExpressionMap.keySet();     //���±��ʽMap temExpressionMap ��key��
 			Iterator<Character> iterator_expression = keySet.iterator();    //����keySet�ĵ�����
 			//������ iterator_expression ���� ʣ��� temExpressionMap �е�Ԫ��
 			while(iterator_expression.hasNext()){
 				ArrayList<String> expressionArray = new ArrayList<>();    //���ʽ����
 				Character keyItem = iterator_expression.next();    // ��ǰ�� key
 				expressionArray = temExpressionMap.get(keyItem);    // ����ֵ
 				//��������
 				for (String expression : expressionArray) {
 					//�����ʽ�ķ��ս�������Ƶ��� �ţ������ʽ���ķ��ս�� ���� vnSetToNull
 					if (TextUtil.expressionToNull(vnSetToNull, expression)) {
 						vnSetToNull.add(keyItem);
 					}
				}
 			}	
		}
 		System.out.println("�����Ƶ��� �ŵķ��ս����" + vnSetToNull.toString());
 		
	}
	
	/**
	 * ��ȡFirst����
	 */
	public void getFirst(){
		//�������ս�����ϣ���First��
		Iterator<Character> iterator = vnSet.iterator();
		while (iterator.hasNext()) {
			Character charVn = (Character) iterator.next();
			//System.out.println("charVn:" + charVn);
			ArrayList<String> expression = expressionMap.get(charVn);
			for (String item : expression) {
				boolean isBreak = false;    //����ѭ���ı�־
				//����ÿһ�����ʽ
				for (int i = 0; i < item.length(); i++) {
					char itemChar = item.charAt(i);
					char itemCharNext;
					//��ȡ��ǰ�ַ��ĺ�һ���ַ�
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
					boolean isContNull = false;    //�Ƿ���� �ŵı�ʶ
					isBreak = calculateFirst(firstSet, charVn, itemChar, itemCharNext, isContNull);
					if (isBreak) {
						break;
					}
				}
			}
			
		}
	}
	/**
	 * ����First��
	 * 
	 * @param firstSet
	 * 				First��
	 * @param vnCharacter
	 * 				��ǰ����ķ��ս��
	 * @param itemChar
	 * 				��ǰ�жϵ��ַ�
	 * @param itemCharNext
	 * 				��ǰ�ж��ַ�����һ���ַ�
	 * @param isContNull
	 * 				�ж��Ƿ���Ӧ�
	 * @return
	 */
	public boolean calculateFirst(TreeSet<Character> firstSet, Character vnCharacter, 
			char itemChar, char itemCharNext, boolean isContNull){
		//��ǰ�ַ�Ϊ�Ż����ս��
		if (itemChar == '��' || vtSet.contains(itemChar)) {
			firstSet.add(itemChar);
			firstSet.remove('��');
			if (isContNull) {
				firstSet.add('��');
			}
			if (vnSetToNull.contains(vnCharacter)) {
				firstSet.add('��');
			}
			firstMap.put(vnCharacter, firstSet);
			return true;
		} else if (vnSetToNull.contains(itemChar) && vnSet.contains(itemCharNext)) {   //��ǰ�ַ�Ϊ  �����Ƶ����ŵķ��ս��
			isContNull = true;
			//�õ���ǰ�ַ��ı��ʽ
			ArrayList<String> expression_calFirst = expressionMap.get(itemChar);
			//�������ʽ��first��
			for (int i = 0; i < expression_calFirst.size(); i++) {
				String string = expression_calFirst.get(i);  //���ʽ
				//�������ʽ���ַ�
				for (int j = 0; j < string.length(); j++) {
					char temChar = string.charAt(j);
					char temCharNext;
					if ( (j+1) < string.length()) {
						temCharNext = string.charAt(j+1);
					}
					else {
						temCharNext = ' ';
					}
					calculateFirst(firstSet, vnCharacter, temChar, temCharNext, isContNull);    //�ݹ����
				}	
			}
			
			return false;  //�ɼ����Ƶ�
		} else if (vnSet.contains(itemChar) )  {    //��ǰ�ַ�Ϊ   �����Ƶ����ŵķ��ս��
			isContNull = false;
			//�õ���ǰ�ַ��ı��ʽ
			ArrayList<String> expression_calFirst = expressionMap.get(itemChar);
			//�������ʽ��first��
			for (int i = 0; i < expression_calFirst.size(); i++) {
				String string = expression_calFirst.get(i);    //���ʽ
				//�������ʽ��ÿһ���ַ�
				for (int j = 0; j < string.length(); j++) {
					char temChar = string.charAt(j);
					char temCharNext;
					if ( (j+1) < string.length()) {
						temCharNext = string.charAt(j+1);
					}
					else {
						temCharNext = ' ';
					}
					//��ֹ�������ս��������ѭ��
					boolean isBreak = calculateFirst(firstSet, vnCharacter, temChar, temCharNext, isContNull);    //�ݹ����
					if (isBreak) {
						break;
					}
				}
				
			}
			return true;    //���ɼ����Ƶ�
		}
		return true;
	}
	
	/**
	 * ��֪����Ϊ�ս����First������ȡĳ���ʽ��First��
	 * 
	 * @param expression
	 * 					���ʽ
	 * @return
	 */
	public TreeSet<Character> getExpressionFirst(String expression ){
		TreeSet<Character> expressionFirstSet = new TreeSet<>();
		//��ʼ�ַ�Ϊ�ս��
		if (TextUtil.isVtStart(vtSet, expression)) {
			expressionFirstSet.add(expression.charAt(0));
		} 
		//��ʼ�ַ�Ϊ���ս��
		if (TextUtil.isVnStart(vnSet, expression)) {
			//�������ʽ�е�ÿһ���ַ�
			for (int i = 0; i < expression.length(); i++) {
				char expressionChar = expression.charAt(i);   //��ǰ������ַ�  
				char expressionCharNext;    //��ǰ�ַ��ĺ�һ���ַ�
				if ( (i+1) < expression.length()) {
					expressionCharNext = expression.charAt(i+1);
				}
				else {
					expressionCharNext = ' ';
				}
				//��ǰ�ַ������Ƶ�����
				if (vnSetToNull.contains(expressionChar)) {
					expressionFirstSet.addAll(firstMap.get(expressionChar));
					if (vtSet.contains(expressionCharNext)) {    //�¸��ַ�Ϊ�ս��
						expressionFirstSet.remove('��');    //ȥ����
						break;    //����ѭ��
					} else if (!vnSetToNull.contains(expressionCharNext)) {    //�¸��ַ������Ƶ�����
						expressionFirstSet.addAll(firstMap.get(expressionChar));    //�����¸��ַ���First��
						//���¸��ַ����ڣ���Ϊ' ',��expressionChar��Ϊ���һ���ַ�
						if (expressionCharNext != ' ') {
							expressionFirstSet.remove('��');
						}
						//��ֹѭ��
						break;
					} else {    //�¸��ַ������Ƶ�����
						expressionFirstSet.add('��');
					}
				}
				//���ַ�Ϊ���ս���������Ƶ�����
				else {
					expressionFirstSet.addAll(firstMap.get(expressionChar));
					expressionFirstSet.remove('��');
					break;
				}
			}
			
		}
		//���ر��ʽ��First��
		return expressionFirstSet;
	}
	
	/**
	 * ��ȡfollow��
	 */
	public void getFollow(){
		//��follow��������ս��
		for (Character temKey : vnSet) {
			TreeSet<Character> temSet = new TreeSet<Character>();
			followMap.put(temKey, temSet);
		}
		
		//�������ս���������follow��
		//����
		Iterator<Character> iterator = vnSet.descendingIterator();
		//����������
		while (iterator.hasNext()){
			Character charItem = iterator.next();    //���ս��
			//System.out.println("charItem:" + charItem);
			Set<Character> keySet = expressionMap.keySet();    //���ʽ��˷��ս����
			for (Character keyCharItem : keySet) {
				ArrayList<String> charItemArray = expressionMap.get(keyCharItem);    //�Ҷ˱��ʽ
				for (String itemCharStr : charItemArray) {
					//System.out.println(keyCharItem + "->" + itemCharStr);
					TreeSet<Character> itemSet = followMap.get(charItem);     //follow��
					calculateFollow(charItem, charItem, keyCharItem, itemCharStr, itemSet);
				}
			}
		}
	}
	/**
	 * ����Follow��
	 * ������ A -> ��S�£���Follow(S),
	 * 
	 * @param putCharItem
	 * 				���ڼ���ķ��ս������ S
	 * @param charItem
	 * 				��Ӧ�ݹ��ѯ�� 0 �εݹ�Ϊ S
	 * 						  1 �εݹ�Ϊ A 
	 * @param keyCharItem
	 * 				���ʽ���ڵ��ַ��� �� A
	 * @param itemCharStr
	 * 				��ǰ�жϵı��ʽ�� �� ��S��
	 * @param itemSet
	 * 				follow������ϣ� �� Follow(S)
	 * 
	 */
	public void calculateFollow(Character putCharItem, Character charItem, Character keyCharItem, 
			String itemCharStr, TreeSet<Character> itemSet){
		
		// 1) ��ʼ�������� #
		if (charItem.equals(start)) {
			itemSet.add('#');
			followMap.put(putCharItem, itemSet);
		}
		
		//�����ʽ�к��е�ǰ����Follow���ķ��ս��
		//���÷��ս���ұ߱��ʽ��First������Follow��
		//���Ҳ���ʽ�����Ƶ�����
		//������ʽ�����ս����follow��
		if (itemCharStr.contains(charItem.toString())) {
			String partExpression = TextUtil.getANextstr(itemCharStr, charItem);  //�÷��ս���ұ߱��ʽ
			if (null != partExpression && partExpression.length() != 0) {
				itemSet.addAll(getExpressionFirst(partExpression));  //�ұ߱��ʽ��First��
				//���Ҳ���ʽ�����Ƶ�����
				if (itemSet.contains('��')) {
					//System.out.println(partExpression);
					itemSet.remove('��');
					//��  S -> AS , �� Follow(S) ��ʽ
					if (!keyCharItem.equals(charItem)) {
						//��ȡ���ʽ��༯��
						Set<Character> keySet = expressionMap.keySet();
						for (Character keyCharItems : keySet) {
							//��ȡ���ʽ
							ArrayList<String> charItemArray = expressionMap.get(keyCharItems);
							for (String itemCharStrs : charItemArray) {
								calculateFollow(putCharItem, keyCharItem, keyCharItems, itemCharStrs, itemSet);
							}
						}
					}
				}
			}
			// A -> AS , ��follow(A)����
			if (TextUtil.isLike_AS(itemCharStr, charItem)) {
				//��  S -> AS , �� Follow(S) ��ʽ
				if (!keyCharItem.equals(charItem)) {
					//��ȡ���ʽ��༯��
					Set<Character> keySet = expressionMap.keySet();
					for (Character keyCharItems : keySet) {
						//��ȡ���ʽ
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
	 * ��ȡselect��
	 * @return
	 */
	public void getSelect(){
		//�������б��ʽ
		Set<Character> keySet = expressionMap.keySet();
		for (Character selectKey : keySet) {
			//��Ӧͬһ���ս���ı��ʽ����
			ArrayList<String> expressionList = expressionMap.get(selectKey);  
			//ÿһ�����ʽ��select�� <���ʽ , select ��>
			HashMap<String, TreeSet<Character>> selectItemSet = new HashMap<>();
			
			//����ͬһ���ս���ı��ʽ
			for (String expression : expressionList) {
				//��� select��
				TreeSet<Character> selectSet = new TreeSet<>();
				//���Ҳ���ʽ��First������Select��
				selectSet.addAll(getExpressionFirst(expression));
				//���Ҳ���ʽ�����Ƶ����ţ������Ƴ�����Follow������
				if (selectSet.contains('��')) {
					selectSet.remove('��');
					selectSet.addAll(followMap.get(selectKey));
				}
				//��װ ���ʽ��Select��
				selectItemSet.put(expression, selectSet);
				//�����ʽ <��ʼ�ַ�, selectItemset> ��װ�� selectMap
				selectMap.put(selectKey, selectItemSet);			
			}
		}
	}
	
	/**
	 * ��ȡԤ�������
	 * @throws Exception
	 */
	public void getPredictingAnalysisTable(){
		Object[] vtArray = vtSet.toArray();
		Object[] vnArray = vnSet.toArray();
		
		//��ʼ��������
		analysisTable = new String[vnArray.length + 1][vtArray.length + 1];
		//��Ԥ������� [0][0] ������ "Vn\\Vt"
		analysisTable[0][0] = "Vn\\Vt";
		
		//��ʼ������������
		for (int i = 0; i < vtArray.length; i++) {
			if (vtArray[i].equals('��')) {
				vtArray[i] = '#';
			}
			//�����ս��
			analysisTable[0][i+1] = vtArray[i].toString();
		}
		
		//��ʼ������������
		for (int i = 0; i < vnArray.length; i++) {
			analysisTable[i+1][0] = vnArray[i].toString();
		}
		
		//�������ֵ
		for (int i = 1; i <= vnArray.length; i++) {
			for (int j = 1; j <= vtArray.length; j++) {
				Character charColPresent = analysisTable[i][0].charAt(0);  //�У��ս��
				char charRowPresent = analysisTable[0][j].charAt(0);  //�У� ���ս��
				//�õ���Ӧ�ı��ʽ
				String expression = TextUtil.findExp(selectMap, charColPresent, charRowPresent);
				//���ʽ��Ϊ�գ���ֵ
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



















