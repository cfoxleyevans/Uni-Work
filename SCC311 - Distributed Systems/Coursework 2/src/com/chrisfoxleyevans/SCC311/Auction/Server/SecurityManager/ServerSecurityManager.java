package com.chrisfoxleyevans.SCC311.Auction.Server.SecurityManager;

import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Auction;
import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Bid;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import java.security.Key;

public class ServerSecurityManager {

    public static Auction decryptAuction(SealedObject auction, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);

            return (Auction) auction.getObject(cipher);
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

    public static Bid decryptBid(SealedObject bid, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);

            return (Bid) bid.getObject(cipher);
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
}
