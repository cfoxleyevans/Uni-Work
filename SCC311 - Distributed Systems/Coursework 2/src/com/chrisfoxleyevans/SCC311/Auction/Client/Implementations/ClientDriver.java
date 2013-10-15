package com.chrisfoxleyevans.SCC311.Auction.Client.Implementations;

public class ClientDriver {
    public static void main(String args[]) {
        try {
            Client testclient = new Client("localhost");
            testclient.displayMenu();
      }
        catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
