package com.chrisfoxleyevans.SCC311.Auction.Client;

public class ClientBuyerDriver {
    public static void main(String args[]) {
        try {
            ClientBuyer testClient = new ClientBuyer("localhost");
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
