package ir.ac.kntu.web.utils.tx;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TransactionAspect {
    private final TransactionManager manager;

    public TransactionAspect(TransactionManager manager) {
        this.manager = manager;
    }

    @Around("@annotation(Tx)")
    public Object manageTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        manager.doBegin();
        Object res = joinPoint.proceed();
        manager.doCommit();
        return res;
    }
}

