package org.example.annotaions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author fanyufeng
 * @date 2023/11/22 20:19
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
	
	String value() default "";
}
