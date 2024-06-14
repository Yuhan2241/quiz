package com.example.quiz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

//@SpringBootTest
class QuizApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void test() {
		List<String> list = List.of(";C;",";A;", ";B;", ";AB;", ";D;");
		String str = ";AB;A;C;BA;B";
		String str1 = ";AB;A;C;B";
		String str2 = ";C;BA;B";
		String str3 = str + str1 + str2;
		System.out.println(str3);

		Map<String,Integer> map = new HashMap<>();
		for(String item : list) {		
			String newStr = str3.replace(item, "");
			int count = ((str3.length() - newStr.length())/ item.length());
			map.put(item,count);
		}
		System.out.println(map);
	}
	
}
