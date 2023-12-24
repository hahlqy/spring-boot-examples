package com.neo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


@SpringBootTest
public class HelloApplicationTests {


	@Test
	void contextLoads() {
		String value = "3.2.1 新支付系统商户同步状态查询（REQ-SHTB-001，优先级：高）\t21";
	}

	@Test
	public void parseTxt() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\23523\\Desktop\\全网收单二期\\文件目录.txt")));
		String str = null;
		String head = null;
		while((str = bufferedReader.readLine())!= null){
			String[] s = str.split(" ");
			String[] s1 = s[0].split("\\.");
			if(s1.length == 2){
				head = s[1];
			}else{
				System.out.println(head+"\t"+s[1]);
			}

		}

	}
}
