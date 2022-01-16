package com.example.springsecuritydemo.databind;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfiguration implements ApplicationContextAware {

    private RequestMappingHandlerAdapter adapter;

    private ApplicationContext applicationContext;

    @Autowired
    public void setAdapter(RequestMappingHandlerAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    protected void injectSelfMethodArgumentResolver() {
        List<HandlerMethodArgumentResolver> argumentResolverList = new ArrayList<>();
        argumentResolverList.add(new AliasModelAttributeMethodProcessor(applicationContext));
        argumentResolverList.addAll(adapter.getArgumentResolvers());
        adapter.setArgumentResolvers(argumentResolverList);
    }
}
