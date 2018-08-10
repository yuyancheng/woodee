package com.sf.kh.aop;

import code.ponfee.commons.constrain.Constraints;
import code.ponfee.commons.constrain.MethodValidator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Validator parameters before method invoke based spring aop
 *
 * @author Ponfee
 */
@Component
@Aspect
public class ApplicationValidator extends MethodValidator {

    @Override
    @Around(value = "execution(public * com.sf.kh.service.impl..*Impl..*(..)) && @annotation(cst)", argNames = "pjp,cst")
    public Object constrain(ProceedingJoinPoint pjp, Constraints cst) throws Throwable {
        return super.constrain(pjp, cst);
    }
}
