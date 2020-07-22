package com.sgcai.android.aop.callback;

public interface StartActivityIntercept {
    /**
     * @param target 当前的执行方法的对象
     * @param args   方法的参数值
     */
    boolean isIntercept(Object target, Object[] args) throws Throwable;
}
