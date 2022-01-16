package com.example.springsecuritydemo.databind;

import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import javax.servlet.ServletRequest;

public class AliasModelAttributeMethodProcessor extends ServletModelAttributeMethodProcessor {

    private ApplicationContext applicationContext;

    public AliasModelAttributeMethodProcessor(ApplicationContext applicationContext) {
        super(true);
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getMethodAnnotation(AliasProcessor.class) != null;
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        AliasDataBinder aliasDataBinder = new AliasDataBinder(binder.getTarget(), binder.getObjectName());
        RequestMappingHandlerAdapter requestMappingHandlerAdapter =
                this.applicationContext.getBean(RequestMappingHandlerAdapter.class);
        requestMappingHandlerAdapter.getWebBindingInitializer().initBinder(aliasDataBinder);
        aliasDataBinder.bind(request.getNativeRequest(ServletRequest.class));
    }
}
