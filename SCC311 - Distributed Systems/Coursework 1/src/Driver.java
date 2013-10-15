import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

            //perform simple auth
            simpleAuthentication(server, random.nextInt());

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
            //get a response object from the server and write it to a file
            server.getSpec(uid, new Client_request(uid, nonse, passkey))
                    .write_to(new FileOutputStream(new File("spec.docx")));
        }
        catch (Exception e) {
            System.out.println("Problem With Simple Auth: " + e.getMessage());
        }
    }

    public static void keyAuthentication(CW_server_interface server, int nonse){
        try {
            //construct a client request
            Client_request request = new Client_request(uid, nonse);

            Path path = Paths.get("32879415.key");
            byte[] data = Files.readAllBytes(path);

            DESKeySpec desKeySpec = new DESKeySpec(data);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey skey = keyFactory.generateSecret(desKeySpec);

            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            cipher.doFinal();

            SealedObject sealedObject = new SealedObject(request, cipher);

            SealedObject resp = server.getSpec(uid, sealedObject);
            System.out.println(resp);
}
        catch (Exception e){
            System.out.println("Problem With Key Auth: " + e.getMessage());
        }
    }
}
