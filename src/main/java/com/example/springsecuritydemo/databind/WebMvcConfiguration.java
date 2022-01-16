package com.example.springsecuritydemo.databind;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    /** 日志 */
    private final Log log = LogFactory.getLog(this.getClass());

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
        List<HandlerMethodArgumentResolver> argumentResolvers = adapter.getArgumentResolvers();
        if (argumentResolvers != null) {
            argumentResolverList.addAll(argumentResolvers);
        } else {
            log.warn("Got null resolver from RequestMappingHandlerAdapter.");
        }
        adapter.setArgumentResolvers(argumentResolverList);
    }
}
