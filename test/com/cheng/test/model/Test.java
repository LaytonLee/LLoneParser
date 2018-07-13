package com.cheng.test.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

import com.cheng.parser.model.AnalyseItem;
import com.cheng.parser.model.Analyser;
import com.cheng.parser.model.LL1GrammarModel;

public class Test {
	
	public void test(){
		
		ArrayList<String> productionList1 = new ArrayList<>();
		productionList1.add("D->*FD");
		productionList1.add("D->ε");
		productionList1.add("T->FD");
		productionList1.add("E->TC");
		productionList1.add("F->(E)");
		productionList1.add("F->i");
		productionList1.add("C->+TC");
		productionList1.add("C->ε");
		
//		ArrayList<String> productionList2 = new ArrayList<>();
//		productionList2.add("E->TH");
//		productionList2.add("H->+TH");
//		productionList2.add("H->ε");
//		productionList2.add("T->FJ");
//		productionList2.add("J->*FJ");
//		productionList2.add("J->ε");
//		productionList2.add("F->i");
//		productionList2.add("F->(E)");
		
		ArrayList<String> productionList2 = new ArrayList<>();
		productionList2.add("A->BCc");
		productionList2.add("A->gDB");
		productionList2.add("B->bCDE");
		productionList2.add("B->ε");
		productionList2.add("C->DaB");
		productionList2.add("C->ca");
		productionList2.add("D->dD");
		productionList2.add("D->ε");
		productionList2.add("E->gAf");
		productionList2.add("E->c");
		
		Character start = 'A';

		LL1GrammarModel grammar = new LL1GrammarModel(productionList2, start);
		
		HashMap<Character, TreeSet<Character>> firstMap = grammar.getFirstMap();  //first集
		HashMap<Character, TreeSet<Character>> followMap = grammar.getFollowMap();  //follow 集
		TreeMap<Character, HashMap<String, TreeSet<Character>>> selectMap = grammar.getSelectMap();
		TreeSet<Character> setVn = grammar.getVnSet();  // 非终结符 集
		TreeSet<Character> setVt = grammar.getVtSet();  // 终结符 集
		//TreeSet<Character> setVntoNull = grammar.getVnToNull();
		HashMap<Character, ArrayList<String>> expression = grammar.getExpressionMap();  // 表达式 集
		String[][] table = grammar.getAnalysisTable();
		
//		System.out.println("非终结符：" + setVn.toString() + "\n终结符：" + setVt.toString());
//		System.out.println("表达式集：" + expression.toString());
		System.out.println("First集：" + firstMap.toString());
		System.out.println("Follow集：" + followMap.toString());
		System.out.println("select集：" + selectMap.toString());
		
//		//打印预测分析表
//		for (int i = 0; i < table.length; i++) {
//			for (int j = 0; j < table[0].length; j++) {
//				if ( null != table[i][j] ) {
//					System.out.print(table[i][j] + "\t");
//				}
//				else {
//					System.out.print("\t");
//				}			
//			}
//			System.out.println("");
//		}
		
//		//分析表达式
//		Analyser analyser = new Analyser();
//		ArrayList<AnalyseItem> list = analyser.analyse(table, start, "i)+i#");
//		System.out.printf("%-25s%-60s%-32s%-20s\n", "步骤", "分析栈", "剩余串", "推导结果");
//		for (int i = 0; i < list.size(); i++) {
//			System.out.printf("%-10s", list.get(i).getIndex());
//			System.out.printf("%-25s", list.get(i).getAnalyseStackItem());
//			System.out.printf("%-15s", list.get(i).getLeftStr());
//			System.out.printf("%-15s", list.get(i).getUseExpStr());
//			System.out.println();
//		}
		
	}
	
	public static void  main(String[] args){
		Test testModel = new Test();
		testModel.test();
	}
}
