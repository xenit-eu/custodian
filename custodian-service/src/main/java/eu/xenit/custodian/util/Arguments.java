package eu.xenit.custodian.util;

import org.springframework.lang.Nullable;

public final class Arguments {

    private Arguments() {
        throw new AssertionError("Instance not allowed");
    }

    /**
     * Assert that an argument is not {@code null}.
     *
     * @param object the object to check
     * @param name the name of the argument, that will be used in the exception message if the object is null.
     * @throws IllegalArgumentException if the argument is {@code null}
     */
    public static <T> T notNull(@Nullable T object, String name) {
        if (object == null) {
            String message = String.format("Argument '%s' is required", name);
            throw new IllegalArgumentException(message);
        }

        return object;
    }

    /**
     * Assert that the provided argument is an instance of the provided class.
     *
     * @param type the type to check against
     * @param argument the object to check
     * @param name is the name of the argument, to be used in the exception message
     * @throws IllegalArgumentException if the object is not an instance of type
     */
    public static <T> T isInstanceOf(Class<T> type, @Nullable Object argument, String name) {
        if (!type.isInstance(argument)) {
            String className = (argument != null ? argument.getClass().getName() : "null");
            String msg = String.format("Argument '%s' of class [%s] must be an instance of %s",
                    name, className, type);
            throw new IllegalArgumentException(msg);
        }

        return type.cast(argument);
    }

}
