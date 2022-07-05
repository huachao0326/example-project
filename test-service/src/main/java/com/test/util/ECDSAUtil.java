package com.test.util;

/**
 * @description:
 * @author: huachao
 * @createDate: 2022/6/30
 */


import cn.hutool.core.util.HexUtil;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;

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
    private static final String SIGNED_MESSAGE = "4a812b78a1b313974ece94a376834777532631e7f39db0376e360aa8afbcfc19135eb32dbe8da667164144104835c0809e2bfb38dfd4f47e2fcc5d40f0142041";
    private static final String SIGNALGORITHMS = "SHA512withECDSA";

    public static void main(String[] args) throws Exception {
        KeyPair keyPair = getKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("公钥：" + Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        System.out.println("私钥：" + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        System.out.println("======");
    }

    @Test
    public void veritySignByPublicKeyStr() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
//        PublicKey pubKey = rawToEncodedECPublicKey(UN_COMPRESS_PUBLIC_KEY_STRING);
        PublicKey pubKey = rawToEncodedECPublicKey(COMPRESS_PUBLIC_KEY_STRING);
        boolean result = verify(pubKey, "30450221009382ba6655255a62474a8b5978a2e4b52c38f54719a67860ece78f0fa61c043c022003d10363b507d266e46d48bbb9b9a09611ea206d137eaf8ef2c541c1bbf5c6e6", MESSAGE);
        System.out.println("验证结果：" + result);
    }

    @Test
    public void signByPrivateStrAndVerity() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, SignatureException, InvalidKeyException {
        PrivateKey privateKey = getPrivateKeyFromECBigIntAndCurve("458e89fffcbe42a9420dd79115a61640c83c5fb3b83c75076785559999d084b4");
        PublicKey publicKey = getPublicKeyFromPrivateKey("458e89fffcbe42a9420dd79115a61640c83c5fb3b83c75076785559999d084b4");
        String sign = sign(privateKey, MESSAGE);
        boolean result = verify(publicKey, sign, MESSAGE);
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
     * 加签
     *
     * @param privateKey 私钥
     * @param data       数据
     * @return
     */
    public static String sign(PrivateKey privateKey, String data) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        //执行签名
        Signature signature = Signature.getInstance("SHA256withPlain-ECDSA");
        signature.initSign(privateKey);
        signature.update(keccak256String(data));
        byte[] sign = signature.sign();
        return HexUtil.encodeHexStr(sign);
//        ECDSASigner signer = new ECDSASigner();
//        X9ECParameters curve = NISTNamedCurves.getByName("P-256");
//        ECDomainParameters domain = new ECDomainParameters(curve.getCurve(), curve.getG(), curve.getN(), curve.getH());
//        signer.init(true, new ECPrivateKeyParameters(new BigInteger("458e89fffcbe42a9420dd79115a61640c83c5fb3b83c75076785559999d084b4", 16), domain));
//        BigInteger[] signature = signer.generateSignature(keccak256String(data));
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        DERSequenceGenerator seq = new DERSequenceGenerator(baos);
//        seq.addObject(new ASN1Integer(signature[0]));
//        seq.addObject(new ASN1Integer(signature[1]));
//        seq.close();
//        return cn.hutool.core.util.HexUtil.encodeHexStr(baos.toByteArray());
    }

    /**
     * 验签
     *
     * @param publicKey 公钥
     * @param signed    签名
     * @param data      数据
     * @return
     */
    public static boolean verify(PublicKey publicKey, String signed, String data) {
        try {
            //验证签名
            Signature signature = Signature.getInstance("SHA256withPlain-ECDSA");
            signature.initVerify(publicKey);
            signature.update(keccak256String(data));
            byte[] hex = HexUtil.decodeHex(signed);
//            byte[] hex = HexUtil.decode("55add3d139d64c0f901eca6994229bb8b6133fad058afa0f8667192d0c18df13cac5949beff70d212071bc45ef6f288d7169b33b3f2553c1c1d7f95ef010b3b0");
            return signature.verify(hex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
