package com.example.interceptor;

import jakarta.interceptor.InvocationContext;
import org.jboss.logging.Logger;

// This class contains audit logic that should run around every method annotated with @StarfleetAudit.
//
// TODO: Turn this into a CDI interceptor bound to the @StarfleetAudit interceptor binding.
//       Steps:
//         1. Add @Interceptor on the class.
//         2. Add @StarfleetAudit on the class (to bind it to the interceptor binding).
//         3. Add @Priority(Interceptor.Priority.APPLICATION) to enable it globally.
//         4. Add @AroundInvoke on the audit() method so CDI knows it is the interceptor method.
public class StarfleetAuditInterceptor {

    private static final Logger LOG = Logger.getLogger(StarfleetAuditInterceptor.class);

    Object audit(InvocationContext context) throws Exception {
        String className = context.getTarget().getClass().getSimpleName();
        String methodName = context.getMethod().getName();
        LOG.infof("[STARFLEET AUDIT] Invoking: %s.%s", className, methodName);
        Object result = context.proceed();
        LOG.infof("[STARFLEET AUDIT] Completed: %s.%s", className, methodName);
        return result;
    }
}
