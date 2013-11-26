package com.chrisfoxleyevans.SCC311.Auction.Client.Implementations;

/**
<<<<<<< HEAD:SCC311 - Distributed Systems/Coursework 2/src/com/chrisfoxleyevans/SCC311/Auction/Client/ClientDriver.java
 * This class creates a client and runs it
=======
 * The class server as an entry point to the client app
>>>>>>> SCC311Security:SCC311 - Distributed Systems/Coursework 2/src/com/chrisfoxleyevans/SCC311/Auction/Client/Implementations/ClientDriver.java
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class ClientDriver {

    //main method
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
