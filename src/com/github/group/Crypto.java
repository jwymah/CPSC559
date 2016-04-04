/**
 *  Crypto.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.SecureRandom;

public class Crypto {

    private final String CLASS_ID = "Crypto";
    private static Log log;

    private static Crypto instance = null;

    private static PrivateKey priv = null;
    public static PublicKey pub = null;

    /**
     * Constructor
     */
    protected Crypto() {

        // Get instance of Log
        log = Log.getInstance();

        // Generate pub/priv key pair
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(512, random);
            KeyPair pair = keyGen.generateKeyPair();

            priv = pair.getPrivate();
            pub = pair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Unknown Public Key algorithm");
        } catch (NoSuchProviderException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Unknown provider");
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
     * Returns a SHA hash of the input message
     *
     * @param msg The input message
     * @return The SHA hash of the message
     */
    public String hash(String msg) {
        // Get a string buffer
        StringBuffer sb = new StringBuffer();

        // Get a hash of the public key
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(msg.getBytes());
            byte[] mdbytes = md.digest();

            // Convert the bytes to hex format 
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Unknown Hashing algorithm");
        }

        return sb.toString();
    }

    /**
     * Generates a Public and Private key and returns the public key as the user
     * ID.
     *
     * @return A public key in Hex encoding
     */
    public String getID() {

        log.printLogMessage(Log.INFO, CLASS_ID, "Generating ID");

        return hash(new String(pub.getEncoded()));

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
