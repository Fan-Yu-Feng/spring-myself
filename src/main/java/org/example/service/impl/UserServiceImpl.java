package org.example.service.impl;

import org.example.annotaions.Component;
import org.example.annotaions.Lazy;
import org.example.annotaions.Scope;
import org.example.service.UserService;

/**
 * @author fanyufeng
 * @date 2023/11/24 15:07
 * @description
 */

@Component("userService")
@Lazy(value = false)
// @Scope("singleton")
public class UserServiceImpl implements UserService {
	@Override
	public String getUserInfo() {
		System.out.println(" getUserInfo");
		return "userInfo";
	}
}
