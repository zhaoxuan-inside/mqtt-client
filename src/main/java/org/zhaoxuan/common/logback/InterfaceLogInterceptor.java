package org.zhaoxuan.common.logback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

 @Slf4j
@Aspect
@Component
public class InterfaceLogInterceptor {

    @Pointcut("execution(* org.zhaoxuan.controller.*.*(..)) || " +
            "execution(* org.zhaoxuan.biz.*.*(..)) || " +
            "execution(* org.zhaoxuan.service.*.*(..))" +
            "@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void syslog() {
    }

    @Around(value = "syslog()")
    @Order(0)
    public Object interceptorAppLogic(ProceedingJoinPoint pj) throws Throwable {

        long start = System.currentTimeMillis();

        Object[] args = pj.getArgs();
        MethodSignature signature = (MethodSignature) pj.getSignature();
        Method method = signature.getMethod();
        String declaringName = method.getDeclaringClass().getName();
        String methodName = declaringName.substring(declaringName.lastIndexOf(".") + 1) + "." + method.getName();

        List<Object> requestArgs = new ArrayList<>();
        if (!ObjectUtils.isEmpty(args)) {
            for (Object arg : args) {
                if (ObjectUtils.isEmpty(arg)) {
                    requestArgs.add(arg);
                    continue;
                }
                if (arg instanceof HttpServletResponse || arg instanceof MultipartFile || arg instanceof HttpServletRequest) {
                    continue;
                }
                requestArgs.add(arg);

            }
        }
        String requestParam = JSON.toJSONString(requestArgs, SerializerFeature.WriteNullStringAsEmpty);
        Object proceed = null;
        String requestUri = null;

        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                if (!ObjectUtils.isEmpty(request)) {
                    requestUri = request.getRequestURI();
                }
            }
            log.info("RequestUri:[{}], MethodName:[{}], RequestParams:[{}]", requestUri, methodName, requestParam);
            proceed = pj.proceed(args);
        } finally {
            long end = System.currentTimeMillis();
            String response = JSON.toJSONString(proceed, SerializerFeature.WriteNullStringAsEmpty);
            log.info("RequestUri:[{}], MethodName:[{}], RequestParams:[{}], ResponseBody:[{}], 请求耗时[{}ms]",
                    requestUri, methodName, requestParam, response, (end - start));
        }
        return proceed;
    }


}
