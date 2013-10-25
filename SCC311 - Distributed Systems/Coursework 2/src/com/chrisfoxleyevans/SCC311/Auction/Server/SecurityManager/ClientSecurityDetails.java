package com.chrisfoxleyevans.SCC311.Auction.Server.SecurityManager;


import java.io.Serializable;
import java.security.Key;

public class ClientSecurityDetails implements Serializable {

    public int clientID;
    public Key key;

    public ClientSecurityDetails(int id, Key key) {
        this.clientID = id;
        this.key = key;
    }
}
