import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class Driver {

    public static void main(String args[]) {

        Random random = new Random(); //the random number gen
        try {

            //locate the registry and get a ref to the server
            Registry registry = LocateRegistry.getRegistry("scc311-server.lancs.ac.uk");
            CW_server_interface server = (CW_server_interface) registry.lookup("CW_server");

            //construct a client request
            Client_request clientRequest = new Client_request(, random.nextInt());

            Server_response response =  server.getSpec(clientRequest.get_uid(), clientRequest);



        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

}
