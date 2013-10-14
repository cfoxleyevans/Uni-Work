package com.chrisfoxleyevans.SCC311.Auction.Client.Implementations;

/**
 * The class serves a the entry point for the client application
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class ClientDriver {
    public static void main(String args[]) {
        try {
            Client testclient = new Client("localhost");

            testclient.registerAuction("This is a nice hat", 11.54);

            testclient.displayAuctions();
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
