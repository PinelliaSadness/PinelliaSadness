package com.summersadness.springfactorybean.factorybean;

import com.summersadness.springfactorybean.model.Author;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/4 10:46
 */
public class AuthorFactoryBean implements FactoryBean {

    private static final Author author = new Author();

    private Long id;

    private String name;

    private String email;

    @Override
    public Object getObject() throws Exception {
        author.setId(id);
        author.setName(name);
        author.setEmail(email);
        return author;
    }

    @Override
    public Class<?> getObjectType() {
        return Author.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
