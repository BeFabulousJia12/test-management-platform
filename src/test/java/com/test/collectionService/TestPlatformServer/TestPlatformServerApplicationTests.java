package com.test.collectionService.TestPlatformServer;



import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestPlatformServerApplicationTests {
	private final byte[] DESIV = new byte[] { 0x12, 0x34, 0x56, 120, (byte) 0x90, (byte) 0xab, (byte) 0xcd, (byte) 0xef };// 向量
	private AlgorithmParameterSpec iv = null;// 加密算法的参数接口
	private Key key = null;
	private String charset = "utf-8";

	@Test
	public void contextLoads() {
	}

	@Test
	public void getMD5String() throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update("admin99%".getBytes());
		String adminPw = new BigInteger(1, md.digest()).toString();
		log.debug(adminPw);
		md.update("user99%".getBytes());
		String userPw = new BigInteger(1, md.digest()).toString();
		log.debug(userPw);
	}

	/**

	 * 初始化

	 * @param deSkey	密钥

	 * @throws Exception

	 */

	public void encryptUtil(String deSkey, String charset) throws Exception {

		if (StringUtils.isNotBlank(charset)) {

			this.charset = charset;

		}

		DESKeySpec keySpec = new DESKeySpec(deSkey.getBytes(this.charset));// 设置密钥参数

		iv = new IvParameterSpec(DESIV);// 设置向量

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂

		key = keyFactory.generateSecret(keySpec);// 得到密钥对象

	}
	//DES加密
	public String encode(String data) throws Exception {

		Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");// 得到加密对象Cipher

		enCipher.init(Cipher.ENCRYPT_MODE, key, iv);// 设置工作模式为加密模式，给出密钥和向量

		byte[] pasByte = enCipher.doFinal(data.getBytes("utf-8"));

		BASE64Encoder base64Encoder = new BASE64Encoder();

		return base64Encoder.encode(pasByte);

	}
	//DES解密
	public String decode(String data) throws Exception {

		Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		deCipher.init(Cipher.DECRYPT_MODE, key, iv);

		BASE64Decoder base64Decoder = new BASE64Decoder();

		byte[] pasByte = deCipher.doFinal(base64Decoder.decodeBuffer(data));

		return new String(pasByte, "UTF-8");

	}
	@Test
	public void DESEncrypt(){
		try {

			String targetString = "admin99%";

			String key = "9ba45bfd50061212328ec03adfef1b6e75";// 自定义密钥
			encryptUtil(key,"utf-8");

			System.out.println("加密前的字符：" + targetString);

			System.out.println("加密后的字符：" + encode(targetString));

			System.out.println("解密后的字符：" + decode(encode(targetString)));

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}