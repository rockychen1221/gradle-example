package com.littlefox.utils.aes;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    private static final String ENCRYPT_TYPE = "AES";
    private static final String ENCODING = "UTF-8";

    public static String encrypt(String key, String str) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, generateMySQLAESKey(key));
        return new String(Hex.encodeHex(cipher.doFinal(str.getBytes(ENCODING)))).toUpperCase();

    }

    public static String decrypt(String key, String str) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, generateMySQLAESKey(key));
        return new String(cipher.doFinal(Hex.decodeHex(str.toCharArray())));

    }

    /**
     * 产生mysql-aes_encrypt
     *
     * @param key 加密的密盐
     * @return
     */
    private static SecretKeySpec generateMySQLAESKey(final String key) throws DecoderException {
        final byte[] finalKey = new byte[16];
        int i = 0;
        for (byte b : Hex.decodeHex(key.toCharArray()))
            finalKey[i++ % 16] ^= b;
        return new SecretKeySpec(finalKey, ENCRYPT_TYPE);
    }

}
