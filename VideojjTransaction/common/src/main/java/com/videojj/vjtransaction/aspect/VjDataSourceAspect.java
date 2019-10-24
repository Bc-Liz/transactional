package com.videojj.vjtransaction.aspect;

import com.videojj.vjtransaction.connection.VjConnection;
import com.videojj.vjtransaction.transactional.VjTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * @author videopls
 */
@Aspect
@Component
public class VjDataSourceAspect {

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection around(ProceedingJoinPoint point) throws Throwable {
        if (VjTransactionManager.getCurrent() != null) {
            return new VjConnection((Connection) point.proceed(), VjTransactionManager.getCurrent());
        } else {
            return (Connection) point.proceed();
        }
    }
}
