package com.chrisfoxleyevans.SCC311.Auction.Server.SecurityManager;


import java.io.Serializable;
import java.security.Key;

/**
 * This class models the details about the client that the server stores
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class ClientSecurityDetails implements Serializable {

    //instance vars
    public int clientID;
    public Key key;

    //constructor
    public ClientSecurityDetails(int id, Key key) {
        this.clientID = id;
        this.key = key;
    }
}
