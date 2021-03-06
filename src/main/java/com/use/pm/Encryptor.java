package com.use.pm;

import com.use.exception.EncryptException;
import com.use.exception.ErrorCode;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by julius on 03/05/2018.
 */
public class Encryptor {

  private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
  private static final String AES = "AES";

  /**
   * Return an encrypted string using input,
   * key, and ivparameter (just like another key)
   *
   * @param value
   * @param key
   * @param ivParameter
   * @return
   */
  public static String encrypt(String value, String key, String ivParameter) {
    try {
      Cipher cipher = getCipherBy(Cipher.ENCRYPT_MODE, key, ivParameter);
      byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(encrypted);
    } catch (Exception ex) {
      throw new EncryptException(ErrorCode.ENCRYPTION_ERROR);
    }
  }

  /**
   * Return an raw string using input,
   * key, and ivparameter (just like another key)
   *
   * @param value
   * @param key
   * @param ivParameter
   * @return
   */
  public static String decrypt(String value, String key, String ivParameter) {
    try {
      Cipher cipher = getCipherBy(Cipher.DECRYPT_MODE, key, ivParameter);
      byte[] encrypted = cipher.doFinal(Base64.getDecoder().decode(value));
      return new String(encrypted, StandardCharsets.UTF_8);
    } catch (Exception ex) {
      throw new EncryptException(ErrorCode.DECRYPTION_ERROR);
    }
  }

  private static Cipher getCipherBy(int mode, String key, String ivParameter) throws Exception {
    IvParameterSpec ivParam = generateIvParameterSpec(ivParameter);
    SecretKeySpec secretKey = generateKey(key);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(mode, secretKey, ivParam);
    return cipher;
  }

  private static SecretKeySpec generateKey(String key) throws Exception {
    return new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES);
  }

  private static IvParameterSpec generateIvParameterSpec(String ivParameter) {
    return new IvParameterSpec(ivParameter.getBytes(StandardCharsets.UTF_8));
  }
}
