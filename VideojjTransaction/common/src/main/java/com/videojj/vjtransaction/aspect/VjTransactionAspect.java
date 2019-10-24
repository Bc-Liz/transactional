package com.videojj.vjtransaction.aspect;

import com.videojj.vjtransaction.annotation.VjTransactional;
import com.videojj.vjtransaction.transactional.VjTransaction;
import com.videojj.vjtransaction.transactional.VjTransactionManager;
import com.videojj.vjtransaction.transactional.TransactionType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author videopls
 */
@Aspect
@Component
public class VjTransactionAspect implements Ordered {


    @Around("@annotation(com.videojj.vjtransaction.annotation.VjTransactional)")
    public void invoke(ProceedingJoinPoint point) {
        // 打印出这个注解所对应的方法
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        VjTransactional lbAnnotation = method.getAnnotation(VjTransactional.class);

        String groupId = "";
        if (lbAnnotation.isStart()) {
            groupId = VjTransactionManager.createLbTransactionGroup();
        } else {
            groupId = VjTransactionManager.getCurrentGroupId();
        }

        VjTransaction vjTransaction = VjTransactionManager.createLbTransaction(groupId);

        try {
            // spring会开启mysql事务
            point.proceed();
            VjTransactionManager.addLbTransaction(vjTransaction, lbAnnotation.isEnd(), TransactionType.commit);
        } catch (Exception e) {
            VjTransactionManager.addLbTransaction(vjTransaction, lbAnnotation.isEnd(), TransactionType.rollback);
            e.printStackTrace();
        } catch (Throwable throwable) {
            VjTransactionManager.addLbTransaction(vjTransaction, lbAnnotation.isEnd(), TransactionType.rollback);
            throwable.printStackTrace();
        }
    }


    @Override
    public int getOrder() {
        return 10000;
    }
}
