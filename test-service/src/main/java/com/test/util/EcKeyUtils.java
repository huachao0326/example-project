package com.test.util;


import org.apache.tomcat.util.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NullCipher;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;

/**
 * @Description: ec工具类
 * @Author: huaChao
 * @Date: 2022/6/29
 * @Version: 1.0.0
 **/
public class EcKeyUtils {
    public static String R1 = "secp256r1";
    public static String K1 = "secp256k1";

    /**
     * 生成EC密钥对
     *
     * @param ecCurveName R1 K1
     * @return
     */
    public static KeyPair generatorEcKeyPair(String ecCurveName) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(ecCurveName);
            keyPairGenerator.initialize(ecGenParameterSpec);
//            KeyPair ecKeyPair = keyPairGenerator.generateKeyPair();
//            ECPublicKey ecPublicKey = (ECPublicKey) ecKeyPair.getPublic();
//            ECPrivateKey ecPrivateKey = (ECPrivateKey) ecKeyPair.getPrivate();
//            String publicKeyStr = new String(Base64.encodeBase64(ecPublicKey.getEncoded()));
//            String privateKeyStr = new String(Base64.encodeBase64(ecPrivateKey.getEncoded()));
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用EC密钥对数据进行签名
     *
     * @param src    原文数据
     * @param priKey 私钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static String sign(String src, PrivateKey priKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        byte[] srcData = src.getBytes();
        signature.initSign(priKey);
        signature.update(srcData);
        byte[] signBytes = signature.sign();
        return bytesToHex(signBytes);
    }

    /**
     * 验签
     *
     * @param src     原文
     * @param signSrc 签名数据
     * @param pubKey  公钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static Boolean verify(String src, String signSrc, PublicKey pubKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] srcData = src.getBytes();
        Signature signatureV = Signature.getInstance("SHA256withECDSA");
        signatureV.initVerify(pubKey);
        signatureV.update(srcData);
        byte[] signBytes = hexToByteArray(signSrc);
        return signatureV.verify(signBytes);
    }

    /**
     * 利用公钥进行对数据进行加密
     *
     * @param src
     * @param pubKey
     * @return
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String encrypt(String src, ECPublicKey pubKey) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] srcData = src.getBytes();
        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(pubKey.getW(), pubKey.getParams());
        Cipher cipher = new NullCipher();
        cipher.init(Cipher.ENCRYPT_MODE, pubKey, ecPublicKeySpec.getParams());
        byte[] encBytes = cipher.doFinal(srcData);
        return bytesToHex(encBytes);
    }

    /**
     * 利用私钥对数据进行解密
     *
     * @param encStr
     * @param priKey
     * @return
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    private static String decrypt(String encStr, ECPrivateKey priKey) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] encBytes = hexToByteArray(encStr);
        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(priKey.getS(), priKey.getParams());
        Cipher cipher = new NullCipher();
        cipher.init(Cipher.DECRYPT_MODE, priKey, ecPrivateKeySpec.getParams());
        byte[] decBites = cipher.doFinal(encBytes);
        return new String(decBites);
    }

    /**
     * 字符串转公钥
     *
     * @param publicKeyString
     * @return
     */
    public static PublicKey strToPublicKey(String publicKeyString) {
        PublicKey publicKey = null;
        try {
            X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(
                    new BASE64Decoder().decodeBuffer(publicKeyString));
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            publicKey = keyFactory.generatePublic(bobPubKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    /**
     * 字符串转私钥
     *
     * @param privateKeyString
     * @return
     */
    public static PrivateKey strToPrivateKey(String privateKeyString) {
        PrivateKey privateKey = null;
        try {
            byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(privateKeyString);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return privateKey;
    }


    /**
     * 字节数组转16进制
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * hex字符串转byte数组
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            //奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            //偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * Hex字符串转byte
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    public static void main(String[] args) throws Exception {
        forTest(R1);
        System.out.println();
        forTest(K1);
    }

    public static void forTest(String ecCurveName) throws Exception {
        System.out.println("1-1 开始生成EC密钥对，椭圆曲线数字签名算法的参数为：" + ecCurveName);
        KeyPair keyPair = generatorEcKeyPair(ecCurveName);
        ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
        ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();
        String publicKeyStr = new String(Base64.encodeBase64(ecPublicKey.getEncoded()));
        String privateKeyStr = new String(Base64.encodeBase64(ecPrivateKey.getEncoded()));
        System.out.println("1-2 密钥对生成完成：publicKeyStr='" + publicKeyStr + "',\nprivateKeyStr='" + privateKeyStr + "'");

        String src = "1234567890";
        System.out.println("2 原文：" + src);

        ECPrivateKey priKey = (ECPrivateKey) strToPrivateKey(privateKeyStr);
        String singResult = sign(src, priKey);
        System.out.println("3-1 使用EC密钥对中的私钥对数据进行签名：singResult=" + singResult);

        ECPublicKey pubKey = (ECPublicKey) strToPublicKey(publicKeyStr);
        Boolean verifyResult = verify(src, singResult, pubKey);
        System.out.println("3-2 验签完成：verifyResult=" + verifyResult);

        String encStr = encrypt(src, pubKey);
        System.out.println("4-1 使用EC公钥对中的私钥对数据进行加密：encStr=" + encStr);

        String decStr = decrypt(encStr, priKey);
        System.out.println("4-2 利用私钥对数据进行解密：decStr=" + decStr);

    }
}