package com.littlefox.cryptic;

public interface CrypticInterface {
    public <T> void selectField(T t,String type);
    public <T> void updateField(T t,String type);

    public String encryptSelf(String str);
    public String decryptSelf(String str);
}
