import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class Driver {
    //constants used for auth
    static final int uid = 32879415;
    static final long passkey = 1052368102;

    public static void main(String args[]) {
        Random random = new Random(); //the random number gen

        try {
            //locate the registry and get a ref to the remote server object
            Registry registry = LocateRegistry.getRegistry("scc311-server.lancs.ac.uk");
            CW_server_interface server = (CW_server_interface) registry.lookup("CW_server");

            //check current status
            //System.out.println("Current Status: " + server.getStatus(uid));

            //perform simple auth
           // simpleAuthentication(server, random.nextInt());

            //perform key based auth
            keyAuthentication(server, random.nextInt());

            //check current status
            System.out.println("Current Status: " + server.getStatus(uid));
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void simpleAuthentication(CW_server_interface server, int nonse) {
        try {
            //construct a client request
            Client_request request = new Client_request(uid, nonse, passkey);

            //get a response object from the server
            Server_response response = server.getSpec(request.get_uid(), request);

            //unpack the speck from the server
            response.write_to(new FileOutputStream(new File("spec.docx")));
        }
        catch (Exception e) {
            System.out.println("Problem With Simple Auth: " + e.getMessage());
        }
    }

    public static void keyAuthentication(CW_server_interface server, int nonse){
        try {
            //construct a client request
            Client_request request = new Client_request(uid, nonse);

            DESKeySpec desKeySpec = new DESKeySpec("1052368102".getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey skey = keyFactory.generateSecret(desKeySpec);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            cipher.doFinal("1052368102".getBytes());

            SealedObject sealedObject = new SealedObject(request, cipher);

            System.out.println(server.getSpec(uid, sealedObject));



        }
        catch (Exception e){
            System.out.println("Problem With Key Auth: " + e.getMessage());
        }
    }
}
