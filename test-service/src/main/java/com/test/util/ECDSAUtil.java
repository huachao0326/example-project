package com.test.util;

/**
 * @description:
 * @author: huachao
 * @createDate: 2022/6/30
 */


import cn.hutool.core.util.HexUtil;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.BigIntegers;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * ECCDSA加签验签工具类
 *
 * @author Administrator
 */
public class ECDSAUtil {

    private static final String PROVIDER = "BC";
    private static final String ALGORITHM = "ECDSA";
    private static final String SECP256K1 = "secp256k1";
    private static final String MESSAGE = "e654d31e-7460-4102-be1e-e3379ad85386_1_1_abc";
    private static final String UN_COMPRESS_PUBLIC_KEY_STRING = "0463793c22d8f748b06e27f6ac274cbecc8ab9fb070ca1efb2e81148f6e318ec48f8adccbbc5c214fd5ef9fdf39bbe3784bc76a235c556b601beeb554da8d72179";
    private static final String COMPRESS_PUBLIC_KEY_STRING = "0363793c22d8f748b06e27f6ac274cbecc8ab9fb070ca1efb2e81148f6e318ec48";
    private static final String SIGNALGORITHMS = "SHA512withECDSA";
    private static final String PRIVATE_KEY = "458e89fffcbe42a9420dd79115a61640c83c5fb3b83c75076785559999d084b4";

    public static void main(String[] args) throws Exception {
        KeyPair keyPair = getKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("公钥：" + Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        System.out.println("私钥：" + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        System.out.println("======");
    }

    @Test
    public void veritySignByPublicKeyStr() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, SignatureException, InvalidKeyException {
//        PublicKey pubKey = rawToEncodedECPublicKey(UN_COMPRESS_PUBLIC_KEY_STRING);
        PublicKey pubKey = rawToEncodedECPublicKey(COMPRESS_PUBLIC_KEY_STRING);
        boolean result = verifyByPublic(pubKey, "f4c94313f946be1173f8504b57cad25d5f8234584dc49b8e91aa4993953fea5a749a54221ef2109be68f80e53977f27a5731360928eeb0714203e05d72a5b626", MESSAGE);
        System.out.println("验证结果：" + result);
    }

    @Test
    public void veritySignByPublicKeyString() {
        boolean result = verifyByPublicString(UN_COMPRESS_PUBLIC_KEY_STRING, "f4c94313f946be1173f8504b57cad25d5f8234584dc49b8e91aa4993953fea5a749a54221ef2109be68f80e53977f27a5731360928eeb0714203e05d72a5b626", MESSAGE);
        System.out.println("验证结果：" + result);
    }

    @Test
    public void signByPrivateStrAndVerityByPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, SignatureException, InvalidKeyException, IOException {
        PrivateKey privateKey = getPrivateKeyFromECBigIntAndCurve(PRIVATE_KEY);
        PublicKey publicKey = getPublicKeyFromPrivateKey(PRIVATE_KEY);
        String sign = signByPrivate(privateKey, MESSAGE);
        boolean result = verifyByPublic(publicKey, sign, MESSAGE);
        System.out.println("私钥：" + Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        System.out.println("签名结果：" + sign);
        System.out.println("公钥：" + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        System.out.println("验证结果：" + result);
    }

    @Test
    public void signByPrivateStrAndVerityByPublicString() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        PrivateKey privateKey = getPrivateKeyFromECBigIntAndCurve(PRIVATE_KEY);
        PublicKey publicKey = getPublicKeyFromPrivateKey(PRIVATE_KEY);
        String sign = signByPrivateString(PRIVATE_KEY, MESSAGE);
        boolean result = verifyByPublicString(COMPRESS_PUBLIC_KEY_STRING, sign, MESSAGE);
        System.out.println("私钥：" + Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        System.out.println("签名结果：" + sign);
        System.out.println("公钥：" + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        System.out.println("验证结果：" + result);
    }

    /**
     * @Description: 进行keccak256编码
     * @param: message
     * @return: java.lang.String
     * @Author: huaChao
     * @Date: 2022/6/30
     **/
    public static byte[] keccak256String(String message) {
        return Hash.sha3(message.getBytes());
    }

    /**
     * 根据私钥PrivateKehy加签
     *
     * @param privateKey 私钥
     * @param data       数据
     * @return
     */
    public static String signByPrivate(PrivateKey privateKey, String data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //执行签名
        Signature signature = Signature.getInstance("SHA256withPlain-ECDSA");
        signature.initSign(privateKey);
        signature.update(keccak256String(data));
        byte[] sign = signature.sign();
        return HexUtil.encodeHexStr(sign);
    }

    /**
     * @Description: 根据私钥字符串签名
     * @param: privateKey
     * @param: data
     * @return: java.lang.String
     * @Author: huaChao
     * @Date: 2022/7/5
     **/
    public static String signByPrivateString(String privateKey, String data) {
        ECDSASigner signer = new ECDSASigner();
        ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec(SECP256K1);
        ECDomainParameters domain = new ECDomainParameters(spec.getCurve(), spec.getG(), spec.getN());
        signer.init(true, new ECPrivateKeyParameters(new BigInteger(privateKey, 16), domain));
        BigInteger[] bi = signer.generateSignature(keccak256String(data));
        byte[] signature = new byte[64];
        System.arraycopy(BigIntegers.asUnsignedByteArray(32, bi[0]), 0, signature, 0, 32);
        System.arraycopy(BigIntegers.asUnsignedByteArray(32, bi[1]), 0, signature, 32, 32);
        return HexUtil.encodeHexStr(signature);
    }

    /**
     * 根据公钥PublicKey验签
     *
     * @param publicKey 公钥
     * @param signed    签名
     * @param data      数据
     * @return
     */
    public static boolean verifyByPublic(PublicKey publicKey, String signed, String data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //验证签名
        Signature signature = Signature.getInstance("SHA256withPlain-ECDSA");
        signature.initVerify(publicKey);
        signature.update(keccak256String(data));
        byte[] hex = HexUtil.decodeHex(signed);
        return signature.verify(hex);
    }

    /**
     * @Description: 根据公钥字符串验签
     * @param: publicKey
     * @param: signed
     * @param: data
     * @return: boolean
     * @Author: huaChao
     * @Date: 2022/7/5
     **/
    public static boolean verifyByPublicString(String publicKey, String signed, String data) {
        ECDSASigner signer = new ECDSASigner();
        ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec(SECP256K1);
        ECDomainParameters domain = new ECDomainParameters(spec.getCurve(), spec.getG(), spec.getN());
        byte[] aaa = HexUtil.decodeHex(publicKey);
        ECPublicKeyParameters publicKeyParams =
                new ECPublicKeyParameters(spec.getCurve().decodePoint(aaa), domain);
        signer.init(false, publicKeyParams);
        byte[] bbb = HexUtil.decodeHex(signed);
        BigInteger r = BigIntegers.fromUnsignedByteArray(bbb, 0, 32);
        BigInteger s = BigIntegers.fromUnsignedByteArray(bbb, 32, 32);
        return signer.verifySignature(keccak256String(data), r, s);
    }


    /**
     * @Description: 生成密钥对
     * @param:
     * @return: java.security.KeyPair
     * @Author: huaChao
     * @Date: 2022/7/1
     **/
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(SECP256K1);
        keyPairGenerator.initialize(ecGenParameterSpec);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: privateKey String 转PrivateKey
     * @param: privateKeyStr
     * @return: java.security.PrivateKey
     * @Author: huaChao
     * @Date: 2022/7/1
     **/
    public static PrivateKey getPrivateKeyFromECBigIntAndCurve(String privateKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        ECNamedCurveParameterSpec ecParameterSpec = ECNamedCurveTable.getParameterSpec(SECP256K1);
        BigInteger priv = new BigInteger(privateKeyStr, 16);
        ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(priv, ecParameterSpec);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
        return keyFactory.generatePrivate(privateKeySpec);

    }

    /**
     * @Description: PrivateKey转PublicKey
     * @param: privateKey
     * @return: java.security.PublicKey
     * @Author: huaChao
     * @Date: 2022/7/1
     **/
    public static PublicKey getPublicKeyFromPrivateKey(String privateKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(SECP256K1);
        BigInteger priv = new BigInteger(privateKey, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(priv);
        ECPoint Q = Sign.publicPointFromPrivate(ecKeyPair.getPrivateKey());
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(Q, ecSpec);
        return keyFactory.generatePublic(pubSpec);
    }

    /**
     * @Description: public string to PublicKey
     * @param: publicKeyStr
     * @return: java.security.PublicKey
     * @Author: huaChao
     * @Date: 2022/7/4
     **/
    public static PublicKey rawToEncodedECPublicKey(String publicKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(SECP256K1);
        BigInteger bigInteger = new BigInteger(publicKeyStr, 16);
        byte[] bbb = bigInteger.toByteArray();
        ECPoint pubPoint = ecSpec.getCurve().decodePoint(bbb);
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(pubPoint, ecSpec);
        return keyFactory.generatePublic(pubSpec);
    }


}
