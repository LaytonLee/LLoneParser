package com.cheng.parser.model;

import java.io.Serializable;
/**
 * 本类是对表达式进行分析的每一项的pojo
 * @author 誠
 *
 */
public class AnalyseItem implements Serializable {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -5412436468603710202L;
	
	/**
	 * 分析步骤的序号
	 */
	private int index;  
	
	/**
	 * 分析栈的每一项
	 */
	private String analyseStackItem;
	
	/**
	 * 剩余输入串
	 */
	private String leftStr;
	
	/**
	 * 用到的表达式
	 */
	private String useExpStr;

	/**
	 * 构造函数
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















