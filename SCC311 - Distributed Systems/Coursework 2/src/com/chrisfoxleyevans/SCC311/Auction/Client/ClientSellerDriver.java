package com.chrisfoxleyevans.SCC311.Auction.Client;

/**
 * This class creates a ClientSeller and runs it
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class ClientSellerDriver {
    public static void main(String args[]) {
        try {
            ClientSeller testClient = new ClientSeller("localhost");
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
