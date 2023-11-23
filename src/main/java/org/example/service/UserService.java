package org.example.service;

/**
 * @author fanyufeng
 * @date 2023/11/22 19:47
 */

import org.example.annotaions.Component;
import org.example.annotaions.Lazy;
import org.example.annotaions.Scope;

@Component
@Lazy(value = false)
@Scope("singleton")
public interface UserService {
}
