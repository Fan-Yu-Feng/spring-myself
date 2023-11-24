package org.example.service.impl;

import org.example.annotaions.Autowired;
import org.example.annotaions.Component;
import org.example.annotaions.Lazy;
import org.example.application.MyApplicationContext;
import org.example.core.ApplicationContextAware;
import org.example.core.BeanNameAware;
import org.example.service.OrderService;
import org.example.service.UserService;

/**
 * @author fanyufeng
 * @date 2023/11/24 15:07
 * @description
 */

@Component("userService")
@Lazy(value = false)
// @Scope("singleton")
public class UserServiceImpl implements UserService, ApplicationContextAware, BeanNameAware {
	
	
	@Autowired
	private OrderService orderService;
	
	private MyApplicationContext myApplicationContext;
	
	
	private String beanName;
	
	public void test(){
		System.out.println(orderService);
		System.out.println(myApplicationContext);
		System.out.println(beanName);
	}
	
	
	@Override
	public String getUserInfo() {
		System.out.println(" getUserInfo");
		return "userInfo";
	}
	
	@Override
	public void setApplicationContext(MyApplicationContext myApplicationContext) {
		this.myApplicationContext = myApplicationContext;
	}
	
	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}
	
	
	
}
