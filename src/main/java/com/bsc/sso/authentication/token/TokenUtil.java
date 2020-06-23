package com.bsc.sso.authentication.token;

import com.auth0.jwt.algorithms.Algorithm;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class TokenUtil {

    public TokenUtil() {
    }

    public static Algorithm getAlgWithRSA512(String key) throws Exception {
        try {
            return Algorithm.RSA512(toRSAKey(key));
        } catch (Exception e) {
            throw e;
        }
    }

    public static Algorithm getAlgWithRSA256(String key) throws Exception {
        try {
            return Algorithm.RSA256(toRSAKey(key));
        } catch (Exception e) {
            throw e;
        }
    }

    private static RSAKey toRSAKey(String keyPem) throws Exception {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            if (keyPem.indexOf("BEGIN PRIVATE KEY") != -1) {
                keyPem = keyPem.replace("-----BEGIN PRIVATE KEY-----", "");
                keyPem = keyPem.replace("-----END PRIVATE KEY-----", "");
                return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getMimeDecoder().decode(keyPem)));
            } else if (keyPem.indexOf("BEGIN PUBLIC KEY") != -1) {
                keyPem = keyPem.replace("-----BEGIN PUBLIC KEY-----", "");
                keyPem = keyPem.replace("-----END PUBLIC KEY-----", "");
                return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(Base64.getMimeDecoder().decode(keyPem)));
            } else if (keyPem.indexOf("BEGIN CERTIFICATE") != -1) {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(keyPem.getBytes(StandardCharsets.UTF_8.name())));
                return (RSAPublicKey) x509Certificate.getPublicKey();
            } else {
                throw new Exception("String is not a RSA key format");
            }
        } catch (GeneralSecurityException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }
}
