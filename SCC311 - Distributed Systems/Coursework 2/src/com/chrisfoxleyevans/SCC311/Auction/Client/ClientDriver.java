package com.chrisfoxleyevans.SCC311.Auction.Client;

import com.chrisfoxleyevans.SCC311.Auction.Client.Client;

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
