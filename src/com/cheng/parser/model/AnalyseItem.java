package com.cheng.parser.model;

import java.io.Serializable;
/**
 * �����ǶԱ��ʽ���з�����ÿһ���pojo
 * @author �\
 *
 */
public class AnalyseItem implements Serializable {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -5412436468603710202L;
	
	/**
	 * ������������
	 */
	private int index;  
	
	/**
	 * ����ջ��ÿһ��
	 */
	private String analyseStackItem;
	
	/**
	 * ʣ�����봮
	 */
	private String leftStr;
	
	/**
	 * �õ��ı��ʽ
	 */
	private String useExpStr;

	/**
	 * ���캯��
	 */
	public AnalyseItem(){
		super();
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getAnalyseStackItem() {
		return analyseStackItem;
	}

	public void setAnalyseStackItem(String analyseStackItem) {
		this.analyseStackItem = analyseStackItem;
	}

	public String getLeftStr() {
		return leftStr;
	}

	public void setLeftStr(String leftStr) {
		this.leftStr = leftStr;
	}

	public String getUseExpStr() {
		return useExpStr;
	}

	public void setUseExpStr(String useExpStr) {
		this.useExpStr = useExpStr;
	}

	@Override
	public String toString() {
		return "AnalyseItem [index=" + index + ", analyseStackItem=" + analyseStackItem + ", leftStr=" + leftStr
				+ ", useExpStr=" + useExpStr + "]";
	}
	
	
}















