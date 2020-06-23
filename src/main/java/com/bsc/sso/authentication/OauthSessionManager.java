package com.bsc.sso.authentication;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class OauthSessionManager {
    private static final String RANDOM_ALG_SHA1 = "SHA1PRNG";
    private static final String DIGEST_ALG_SHA256 = "SHA-256";

    /**
     * Generates a session state using the provided client id, client callback url and browser state cookie id
     *
     * @param clientId
     * @param rpCallBackUrl
     * @param opBrowserState
     * @return generated session state value
     */
    public static String getSessionStateParam(String clientId, String rpCallBackUrl, String opBrowserState) {

        try {
            String salt = generateSaltValue();
            String sessionStateDataString =
                    clientId + " " + getOrigin(rpCallBackUrl) + " " + opBrowserState + " " + salt;

            MessageDigest digest = MessageDigest.getInstance(DIGEST_ALG_SHA256);
            digest.update(sessionStateDataString.getBytes());
            return bytesToHex(digest.digest()) + "." + salt;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while calculating session state.", e);
        }
    }

    private static String generateSaltValue() throws NoSuchAlgorithmException {

        byte[] bytes = new byte[16];
        SecureRandom secureRandom = SecureRandom.getInstance(RANDOM_ALG_SHA1);
        secureRandom.nextBytes(bytes);
        return Base64.encodeBase64URLSafeString(bytes);
    }

    private static String bytesToHex(byte[] bytes) {

        StringBuilder result = new StringBuilder();
        for (byte byt : bytes) {
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    /**
     * Returns the origin of the provided url
     * <scheme>://<host>:<port>
     *
     * @param url
     * @return origin of the url
     */
    public static String getOrigin(String url) {

        try {
            URI uri = new URI(url);
            return uri.getScheme() + "://" + uri.getAuthority();
        } catch (URISyntaxException e) {
            log.error("Error while parsing URL origin of " + url + ". URL seems to be malformed.");
        }

        return null;
    }

    private static final Logger log = Logger.getLogger(OauthSessionManager.class);
}
