package com.example.interceptor;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.jboss.logging.Logger;

// This class contains audit logic that should run around every method annotated with @StarfleetAudit.
@Interceptor
@StarfleetAudit
public class StarfleetAuditInterceptor {

    private static final Logger LOG = Logger.getLogger(StarfleetAuditInterceptor.class);

    @AroundInvoke
    Object audit(InvocationContext context) throws Exception {
        String className = context.getTarget().getClass().getSimpleName();
        String methodName = context.getMethod().getName();
        LOG.infof("[STARFLEET AUDIT] Invoking: %s.%s", className, methodName);
        Object result = context.proceed();
        LOG.infof("[STARFLEET AUDIT] Completed: %s.%s", className, methodName);
        return result;
    }
}
