package org.objectmapper4j;

class ParameterizedClass<T> {

    private Class<T> type;

    public static <T> ParameterizedClass<T> of(Class<T> type) {
        return new ParameterizedClass<>(type);
    }

    private ParameterizedClass(Class<T> type) {
        this.type = type;
    }
}
