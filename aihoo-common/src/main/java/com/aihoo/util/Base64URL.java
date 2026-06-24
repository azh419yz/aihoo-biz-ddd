package com.aihoo.util;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;

public class Base64URL {

    public static byte[] base64EncodeUrl(byte[] input) {
        byte[] base64 = Base64.encodeBase64(input);
        for (int i = 0; i < base64.length; ++i) {
            switch (base64[i]) {
                case '+':
                    base64[i] = '*';
                    break;
                case '/':
                    base64[i] = '-';
                    break;
                case '=':
                    base64[i] = '_';
                    break;
            }
        }
        return base64;
    }

    public static byte[] base64DecodeUrl(byte[] input) throws IOException {
        byte[] base64 = input.clone();
        for (int i = 0; i < base64.length; ++i) {
            switch (base64[i]) {
                case '*':
                    base64[i] = '+';
                    break;
                case '-':
                    base64[i] = '/';
                    break;
                case '_':
                    base64[i] = '=';
                    break;
            }
        }
        return Base64.decodeBase64(new String(base64));
    }
}