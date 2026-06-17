package com.aihoo.util;

import cn.hutool.core.util.HexUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * MD5 Password Encoder for compatibility with legacy Shiro hashing.
 * Logic: MD5(salt + password) -> iterated 3 times?
 * Replicating: SimpleHash("MD5", password, salt, hashIterations).toHex()
 * Default salt: "easyweb", iterations: 3
 */
public class Md5PasswordEncoder implements PasswordEncoder {

    private static final String DEFAULT_SALT = "easyweb";
    private static final int ITERATIONS = 3;

    @Override
    public String encode(CharSequence rawPassword) {
        return encrypt(rawPassword.toString(), DEFAULT_SALT, ITERATIONS);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(encode(rawPassword));
    }

    /**
     * Replicates Shiro's SimpleHash behavior for MD5 with salt and iterations.
     * Shiro logic:
     * 1. start with plain bytes.
     * 2. if salt exists, prepend salt bytes to data.
     * 3. hash.
     * 4. for remaining iterations, hash requests.
     */
    public static String encrypt(String password, String salt, int iterations) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = password.getBytes(StandardCharsets.UTF_8);

            if (salt != null) {
                byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);
                digest.reset();
                digest.update(saltBytes);
                digest.update(bytes);
                bytes = digest.digest();
            } else {
                bytes = digest.digest(bytes);
            }

            for (int i = 0; i < iterations - 1; i++) {
                digest.reset();
                bytes = digest.digest(bytes);
            }
            return HexUtil.encodeHexStr(bytes);
        } catch (Exception e) {
            throw new RuntimeException("MD5 hashing failed", e);
        }
    }
}
