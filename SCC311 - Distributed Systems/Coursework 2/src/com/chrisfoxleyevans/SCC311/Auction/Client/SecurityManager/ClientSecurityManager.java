package com.chrisfoxleyevans.SCC311.Auction.Client.SecurityManager;

import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Auction;
import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Bid;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;
import java.util.ArrayList;

/**
 * This class provides functionality for encrypting and decrypting objects that the client will deal with
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class ClientSecurityManager {

    //public methods
    public static Key generateKey(String password) {
        try {
            DESKeySpec desKeySpec = new DESKeySpec(password.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            return keyFactory.generateSecret(desKeySpec);
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<Auction> decryptAuctions(SealedObject auctions, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);



            return (ArrayList<Auction>) auctions.getObject(cipher);
        } catch (Exception e) {
            return null;
        }
    }

    public static SealedObject encryptAuction(Auction auction, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            return new SealedObject(auction, cipher);
        } catch (Exception e) {
            return null;
        }
    }

    public static Auction decryptAuction(SealedObject auction, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);

            return (Auction) auction.getObject(cipher);
        } catch (Exception e) {
            return null;
        }
    }

    public static SealedObject encryptBid(Bid bid, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            return new SealedObject(bid, cipher);
        } catch (Exception e) {
            return null;
        }
    }

    public static Bid decryptBid(SealedObject bid, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);

            return (Bid) bid.getObject(cipher);
        } catch (Exception e) {
            return null;
        }
    }

}



