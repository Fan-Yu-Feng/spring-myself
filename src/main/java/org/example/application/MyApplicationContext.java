package org.example.application;

import org.example.annotaions.*;
import org.example.core.ApplicationContextAware;
import org.example.core.BeanDefinition;
import org.example.core.BeanNameAware;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author fanyufeng
 * @date 2023/11/22 19:42
 */
public class MyApplicationContext {
	
	
	private Class<? extends org.example.config.AppConfig> configClass;
	private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
	
	
	/**
	 * 单例bean Object
	 */
	private final Map<String, Object> singletonObjects = new HashMap<>();
	
	
	public MyApplicationContext(Class<org.example.config.AppConfig> configClass) {
		this.configClass = configClass;
		scan(configClass);
		for (String beanName : beanDefinitionMap.keySet()) {
			BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
			// 单例bean且非懒加载则直接初始化
			if (!beanDefinition.isLazy() && beanDefinition.getScope().equals("singleton")) {
				Object bean = createBean(beanName);
				singletonObjects.put(beanName, bean);
			}
		}
	}
	
	
	/**
	 * 通过beanName 创建Bean
	 * 实例化 Bean
	 *
	 * @param beanName beanName
	 *
	 * @return 返回Objects
	 */
	private Object createBean(String beanName) {
		BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
		Class clazz = beanDefinition.getType();
		
		try {
			Object object = clazz.newInstance();
			
			// 类字段实现依赖注入
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field field : declaredFields) {
				if (field.isAnnotationPresent(Autowired.class)) {
					String fieldBeanName = field.getName();
					Object bean = getBean(fieldBeanName);
					// 设置访问限制。私有字段可以访问
					field.setAccessible(true);
					// 将指定的值设置给字段
					field.set(object, bean);
				}
			}
			
			
			if (object instanceof BeanNameAware) {
				((BeanNameAware)object).setBeanName(beanName);
			}
			
			if (object instanceof ApplicationContextAware) {
				((ApplicationContextAware)object).setApplicationContext(this);
			}
			return object;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 配置文件 class 扫描 注册bean
	 *
	 * @param configClass config class
	 */
	private void scan(Class<? extends org.example.config.AppConfig> configClass) {
		System.out.println("scan file start");
		if (configClass.isAnnotationPresent(ComponentScan.class)) {
			ComponentScan componentScan = configClass.getAnnotation(ComponentScan.class);
			// 扫描路径
			String path = componentScan.value();
			
			path = path.replace(".", "/");
			System.out.println("path = " + path);
			ClassLoader classLoader = this.getClass().getClassLoader();
			URL resource = classLoader.getResource(path);
			System.out.println("resource = " + resource.getPath());
			File file = new File(resource.getFile());
			ArrayList<File> fileArrayList = new ArrayList<>();
			if (file.isDirectory()) {
				for (File f : Objects.requireNonNull(file.listFiles())) {
					if (f.isDirectory()) {
						for (File f1 : f.listFiles()) {
							if (!f1.isDirectory()) {
								fileArrayList.add(f1);
							}
						}
					} else {
						fileArrayList.add(f);
					}
					
				}
			}
			
			for (File cFile : fileArrayList) {
				String absolutePath = cFile.getAbsolutePath();
				System.out.println("absolutePath = " + absolutePath);
				String className = absolutePath.substring(absolutePath.indexOf("org"), absolutePath.indexOf(".class")).replace(File.separatorChar, '.');
				System.out.println("className = " + className);
				try {
					Class<?> clazz = classLoader.loadClass(className);
					if (!clazz.isAnnotationPresent(Component.class)) {
						continue;
					}
					// 设置 BeanDefinition
					BeanDefinition beanDefinition = new BeanDefinition();
					beanDefinition.setType(clazz);
					beanDefinition.setLazy(clazz.isAnnotationPresent(Lazy.class));
					if (clazz.isAnnotationPresent(Scope.class)) {
						beanDefinition.setScope(clazz.getAnnotation(Scope.class).value());
					} else {
						beanDefinition.setScope("singletion");
					}
					String beanName = clazz.getAnnotation(Component.class).value();
					if (beanName.isEmpty()) {
						beanName = Introspector.decapitalize(clazz.getSimpleName());
						System.out.println("beanName = " + beanName);
					}
					
					System.out.println("aClass = " + clazz);
					beanDefinitionMap.put(beanName, beanDefinition);
					
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					System.out.println("e.getMessage() = " + e.getMessage());
				}
				
				
			}
			
			
		}
		
		
		System.out.println("scan file end");
	}
	
	
	/**
	 * 通过 bean name 拿到对应的记录
	 *
	 * @param beanName bean 的名称
	 *
	 * @return 返回bean
	 */
	public Object getBean(String beanName) {
		BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
		if (beanDefinition == null) {
			throw new NullPointerException();
		}
		// 单例bean
		if ("singleton".equals(beanDefinition.getScope())) {
			Object result = singletonObjects.get(beanName);
			if (result == null) {
				result = createBean(beanName);
				singletonObjects.put(beanName, result);
			}
			return result;
		}
		return createBean(beanName);
	}
}
