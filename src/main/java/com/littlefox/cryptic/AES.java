package com.littlefox.cryptic;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 对称加密 AES
 * @author rockychen
 */
@Slf4j
public class AES extends AbstractCryptic implements CrypticInterface {

    private static final String ENCRYPT_TYPE="AES";
    private static final String ENCODING="UTF-8";

    public AES(String privateKey, String algorithm) {
        super(privateKey, algorithm);
    }

    @Override
    public String encryptSelf(String str) {
        try {
            Cipher encryptCipher = Cipher.getInstance(ENCRYPT_TYPE);
            encryptCipher.init(Cipher.ENCRYPT_MODE, generateMySQLAESKey(this.privateKey));
            return new String(Hex.encodeHex(encryptCipher.doFinal(str.getBytes(ENCODING)))).toUpperCase();
        } catch (Exception e) {
            log.error("AES encryptSelf error : {}",e);
            return super.decryptSelf(str);
        }
    }

    @Override
    public String decryptSelf(String str) {
        try {
            Cipher decryptChipher = Cipher.getInstance(ENCRYPT_TYPE);
            decryptChipher.init(Cipher.DECRYPT_MODE, generateMySQLAESKey(this.privateKey));
            return new String(decryptChipher.doFinal(Hex.decodeHex(str.toCharArray())));
        } catch (Exception e) {
            log.error("AES decryptSelf error : {}",e);
            return super.decryptSelf(str);
        }
    }

    /**
     * 产生mysql-aes_encrypt
     * @param key 加密的密盐
     * @return
     */
    public static SecretKeySpec generateMySQLAESKey(final String key) {
        try {
            final byte[] finalKey = new byte[16];
            int i = 0;
            for(byte b : Hex.decodeHex(key.toCharArray()))
                finalKey[i++ % 16] ^= b;
            return new SecretKeySpec(finalKey, ENCRYPT_TYPE);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
