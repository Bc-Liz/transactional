package com.videojj.vjtransaction.intercept;

import com.videojj.vjtransaction.transactional.VjTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author videopls
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String groupId = request.getHeader("groupId");
        String transactionCount = request.getHeader("transactionCount");
        VjTransactionManager.setCurrentGroupId(groupId);
        VjTransactionManager.setTransactionCount(Integer.valueOf(transactionCount == null ? "0" : transactionCount));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
