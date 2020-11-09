package it.uniba.magr.misurapp.util;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * In this util class you have to find digest methods.
 */
public final class DigestUtil {

    public static final String MD5_DIGEST = "MD5";

    public static final int MD5_POSITIVE_SIGNUM_DIGEST = 1;
    public static final int MD5_CHAR_LENGTH            = 16;
    public static final int MD5_LENGTH                 = 32;

    private DigestUtil() {
        throw new IllegalStateException("This is a static class");
    }

    /**
     * @param text the text that must be converted into md5.
     * @return The 32 chars md5 string of argument text.
     * @throws NoSuchAlgorithmException If MD5 digest doesn't be found.
     */
    @NotNull
    public static String getMD5(@NotNull String text) throws NoSuchAlgorithmException {

        MessageDigest md5Digest = MessageDigest.getInstance(MD5_DIGEST);
        byte[] digest = md5Digest.digest(text.getBytes());

        BigInteger bigInteger = new BigInteger(MD5_POSITIVE_SIGNUM_DIGEST, digest);
        StringBuilder result = new StringBuilder(bigInteger.toString(MD5_CHAR_LENGTH));

        while (result.length() < MD5_LENGTH) {
            result.insert(0, "0");
        }

        return result.toString();

    }

}
