package org.example;

import org.example.application.MyApplicationContext;
import org.example.config.AppConfig;

/**
 * @author fanyufeng
 * @date 2023/11/22 19:41
 */
public class MainAppliation {
	public static void main(String[] args) {
		
		MyApplicationContext myApplicationContext = new MyApplicationContext(AppConfig.class);

		System.out.println("Hello world!");
	}
}