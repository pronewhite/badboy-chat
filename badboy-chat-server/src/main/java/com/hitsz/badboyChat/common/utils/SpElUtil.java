package com.hitsz.badboyChat.common.utils;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/2 20:40
 */
@Component
public class SpElUtil {

    private static final ExpressionParser PARSE = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERERA = new DefaultParameterNameDiscoverer();

    public static String parseSpEl(Method method, Object[] args, String spEl) {
        String[] params = Optional.ofNullable(PARAMETER_NAME_DISCOVERERA.getParameterNames(method)).orElse(new String[]{});//解析参数名
        EvaluationContext context = new StandardEvaluationContext();//el解析需要的上下文对象
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);//所有参数都作为原材料扔进去
        }
        Expression expression = PARSE.parseExpression(spEl);
        return expression.getValue(context, String.class);
    }

    public static String getMethodKey(Method method){
        return method.getDeclaringClass() + "#" + method.getName();
    }
}
