package org.example;

import org.example.application.MyApplicationContext;
import org.example.config.AppConfig;
import org.example.service.UserService;

import java.util.Objects;

/**
 * @author fanyufeng
 * @date 2023/11/22 19:41
 */
public class MainAppliation {
	public static void main(String[] args) {
		
		MyApplicationContext myApplicationContext = new MyApplicationContext(AppConfig.class);
		UserService userService = (UserService)myApplicationContext.getBean("userService");
		System.out.println("userService = " + userService);
		String userInfo = userService.getUserInfo();
		userService.test();
		System.out.println("userInfo = " + userInfo);
		System.out.println("Hello world!");
	}
}