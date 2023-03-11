package io.boxhit.socket.users;

import java.security.SecureRandom;

public class SessionToken {

    public static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static int SECURE_TOKEN_LENGTH = 200;

    private static final SecureRandom random = new SecureRandom();

    private static final char[] symbols = CHARACTERS.toCharArray();

    /**
     * Generate the next secure random token in the series.
     */
    public static String generateToken(int length) {
        SECURE_TOKEN_LENGTH = length;
        final char[] buf = new char[SECURE_TOKEN_LENGTH];

        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);

    }

}
