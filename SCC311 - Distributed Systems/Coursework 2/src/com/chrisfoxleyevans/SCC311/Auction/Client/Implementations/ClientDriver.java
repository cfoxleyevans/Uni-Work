package com.chrisfoxleyevans.SCC311.Auction.Client.Implementations;

/**
 * The class server as an entry point to the client app
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class ClientDriver {

    //main method
    public static void main(String args[]) {
        try {
           Client testClient = new Client("localhost", 2000, "AuctionServerZero");

//            Client testClient = new Client("localhost", 2001, "AuctionServerOne");

            if (testClient.server != null) {
                while (true) {
                    testClient.displayMenu();
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
