package org.beancp;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class ObjectsReferenceImpl<S, D> implements ObjectsReference<S, D> {

    private final S sourceObject;

    private final D destinationObject;

    private boolean tracingEnabled;

    private List<Method> sourceCallers;

    private List<Method> destinationCallers;

    public ObjectsReferenceImpl(final S sourceObject, final D destinationObject) {
        this.sourceObject = sourceObject;
        this.destinationObject = destinationObject;
        this.tracingEnabled = false;
    }

    /**
     * Enables tracing. When tracing is enabled then <a href="#source">source()</a> and
     * <a href="#destination">destination()</a> methods registers its callers methods. Tracing
     * result can be obtained using <a href="#getSourceCallers">getSourceCallers()</a> and
     * <a href="#getSourceCallers">getSourceCallers()</a> methods.
     *
     * This operation puts restriction on <a href="#source">source()</a> and
     * <a href="#destination">destination()</a> caller methods - they have to be lambda expressions.
     */
    public void enableTracing() {
        if (tracingEnabled) {
            throw new IllegalStateException("Tracing is already enabled.");
        }

        tracingEnabled = true;
        resetTracking();
    }

    public void disableTracing() {
        if (!tracingEnabled) {
            throw new IllegalStateException("Tracing is already disabled.");
        }

        tracingEnabled = false;
        resetTracking();
    }

    public boolean isTracingEnabled() {
        return tracingEnabled;
    }

    public void resetTracking() {
        sourceCallers = new LinkedList<>();
        destinationCallers = new LinkedList<>();
    }

    public Iterable<Method> getSourceCallers() {
        return sourceCallers;
    }

    public Iterable<Method> getDestinationCallers() {
        return destinationCallers;
    }

    @Override
    public S source() {
        if (tracingEnabled) {
            sourceCallers.add(getCallerMethod());
        }

        return sourceObject;
    }

    @Override
    public D destination() {
        if (tracingEnabled) {
            destinationCallers.add(getCallerMethod());
        }

        return destinationObject;
    }

    private Method getCallerMethod() {
        StackTraceElement[] currentStackTrace = Thread.currentThread().getStackTrace();

        // 0 - getStackTrace
        // 1 - this method
        // 2 - method of this class which calls this method
        // 3 - this class client method
        StackTraceElement callerStackTraceElement = currentStackTrace[3];
        String callerClassName = callerStackTraceElement.getClassName();
        String callerMethodName = callerStackTraceElement.getMethodName();

        try {
            Class<?> callerClass = Class.forName(callerClassName);

            List<Method> methods = Arrays.stream(callerClass.getDeclaredMethods())
                    .filter(n -> n.getName().equals(callerMethodName))
                    .collect(Collectors.toList());

            // for lambda expression this should not happend
            if (methods.size() > 1) {
                throw new MapConfigurationException(
                        String.format("Failed identifiy %s class method %s. There is more that one "
                                + " method with that name. Caller is lambda expression?",
                                callerClassName, callerMethodName));
            }

            Method callerMethod = methods.get(0);

            return callerMethod;
        } catch (ClassNotFoundException ex) {
            throw new MapConfigurationException(
                    String.format("Failed to load load caller class: %s", callerClassName), ex);
        }
    }
}
