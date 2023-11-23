package org.example.application;

import org.example.annotaions.*;

import java.beans.Introspector;
import java.io.File;
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


    public MyApplicationContext(Class<org.example.config.AppConfig> configClass) {
        this.configClass = configClass;
	    
	    scan(configClass);
		

    }
	
	/**
	 * 配置文件 class 扫描 注册bean
	 * @param configClass config class
	 */
	private void scan(Class<? extends org.example.config.AppConfig> configClass) {
		System.out.println("scan file start");
		if(configClass.isAnnotationPresent(ComponentScan.class)){
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
			if(file.isDirectory()){
				for (File f: Objects.requireNonNull(file.listFiles())) {
					if(f.isDirectory()){
						for (File f1 : f.listFiles()) {
							if (!f1.isDirectory()) {
								fileArrayList.add(f1);
							}
						}
					}else {
						fileArrayList.add(f);
					}
					
				}
			}
			
			for(File cFile :fileArrayList){
				String absolutePath = cFile.getAbsolutePath();
				System.out.println("absolutePath = " + absolutePath);
				String className = absolutePath.substring(absolutePath.indexOf("org"), absolutePath.indexOf(".class")).replace("/",".");
				System.out.println("className = " + className);
				try {
					Class<?> clazz = classLoader.loadClass(className);
					if(!clazz.isAnnotationPresent(Component.class)){
						continue;
					}
					// 设置 BeanDefinition
					BeanDefinition beanDefinition = new BeanDefinition();
					beanDefinition.setType(clazz);
					beanDefinition.setLazy(clazz.isAnnotationPresent(Lazy.class));
					if(clazz.isAnnotationPresent(Scope.class)){
						beanDefinition.setScope(clazz.getAnnotation(Scope.class).value());
					}else {
						beanDefinition.setScope("singletion");
					}
					String beanName = clazz.getAnnotation(Component.class).value();
					if(beanName.isEmpty()){
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
}
