package com.cheng.test.model;

import java.util.ArrayList;
import java.util.TreeSet;

import com.cheng.parser.util.TextUtil;

public class TestOther {
	public static void main(String[] args){
		ArrayList<String> list = new ArrayList<>();
		list.add("afs");
		list.add("¦Å");
		list.add("asdf");
		list.add("");
		list.add("");
		list.add("");
		
		TreeSet<Character> set = new TreeSet<>();
		set.add('a');
		set.add('b');
		if (TextUtil.expressionToNull(set, "df")) {
			System.out.println(1);
		}
		else {
			System.out.println(0);
		}

	}
}
