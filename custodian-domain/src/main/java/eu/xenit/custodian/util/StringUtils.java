package eu.xenit.custodian.util;

public final class StringUtils {

    private StringUtils() {
        throw new AssertionError("Instance not allowed");
    }

    /**
     * Check whether the given {@code String} contains actual text.
     *
     * More specifically, this method returns {@code true} if the {@code String} is not {@code null}, its length is
     * greater than 0, and it contains at least one non-whitespace character.
     *
     * @param str the {@code String} to check (may be {@code null})
     * @return {@code true} if the {@code String} is not {@code null}, its length is greater than 0, and it does not
     * contain whitespace only
     *
     * @see Character#isWhitespace
     */
    public static boolean hasText(String str) {
        return str != null && !str.isEmpty() && containsText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();

        for (int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }
}
