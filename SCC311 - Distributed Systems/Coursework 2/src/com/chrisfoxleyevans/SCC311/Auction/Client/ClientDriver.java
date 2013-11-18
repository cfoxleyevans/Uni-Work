package com.chrisfoxleyevans.SCC311.Auction.Client;

/**
 * This class creates a client and runs it
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class ClientDriver {
    public static void main(String args[]) {
        try {
            Client testClient = new Client("localhost");
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
