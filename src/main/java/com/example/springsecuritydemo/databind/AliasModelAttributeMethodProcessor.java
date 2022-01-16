package com.example.springsecuritydemo.databind;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import javax.servlet.ServletRequest;

public class AliasModelAttributeMethodProcessor extends ServletModelAttributeMethodProcessor {

    /** 日志 */
    private final Log log = LogFactory.getLog(this.getClass());

    /** Spring应用程序上下文 */
    private final ApplicationContext applicationContext;

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
        WebBindingInitializer webBindingInitializer = requestMappingHandlerAdapter.getWebBindingInitializer();
        if (webBindingInitializer == null) {
            log.error("WebBindingInitializer is null.");
            return;
        }
        webBindingInitializer.initBinder(aliasDataBinder);
        ServletRequest nativeRequest = request.getNativeRequest(ServletRequest.class);
        if (nativeRequest == null) {
            log.error("Got null when execute: NativeWebRequest.getNativeRequest(ServletRequest.class)");
            return;
        }
        aliasDataBinder.bind(nativeRequest);
    }
}
