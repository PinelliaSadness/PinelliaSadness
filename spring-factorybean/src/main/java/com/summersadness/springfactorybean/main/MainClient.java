package com.summersadness.springfactorybean.main;

import com.summersadness.springfactorybean.model.Author;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author PinelliaSadness@Gmail.com
 * @date 2019/3/4 11:41
 * @version 1.0.0
 */
public class MainClient {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        Author author = (Author) applicationContext.getBean("author");
        System.out.println(author);
    }

}
