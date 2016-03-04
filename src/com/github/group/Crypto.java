/**
 *  Crypto.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import java.security.DigestException;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.SecureRandom;

public class Crypto {
    private static Crypto instance = null;

    private static PrivateKey priv = null;
    public static PublicKey pub = null;

    /**
     * Constructor
     */
    protected Crypto() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(512, random);
            KeyPair pair = keyGen.generateKeyPair();

            priv = pair.getPrivate();
            pub = pair.getPublic();
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchProviderException e) {
        }
    }

    /**
     * Ensures that only one instance of Crypto is created.
     *
     * @return The instance of Crypto
     */
    public static Crypto getInstance() {
        if (instance == null) {
            instance = new Crypto();
        }

        return instance;
    }

    /**
     * Generates a Public and Private key and returns the public key as the user
     * ID.
     *
     * @return A public key in Hex encoding
     */
    public String getID() {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(pub.getEncoded());
            byte[] mdbytes = md.digest();

            //convert the byte to hex format method 1
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
        }

        return sb.toString();
    }

    /**
     * Returns the servers public key.
     *
     * @return This servers public key
     */
    public PublicKey getPublicKey() {
        return pub;
    }
}
