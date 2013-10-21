import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.Key;
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
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void simpleAuthentication(CW_server_interface server, int nonse) {
        try {
            //get a response object from the server and write it to a file
            server.getSpec(uid, new Client_request(uid, nonse, passkey))
                    .write_to(new FileOutputStream(new File("spec.docx")));
        } catch (Exception e) {
            System.out.println("Problem With Simple Auth: " + e.getMessage());
        }
    }

    public static void keyAuthentication(CW_server_interface server, int nonse) {
        try {
            //read in the key from the file
            Key key = (Key) new ObjectInputStream(new FileInputStream("32879415.key")).readObject();

            //construct a cipher object
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            //get a response from the server
            SealedObject sealedResponse = server.getSpec(uid,  new SealedObject(new Client_request(uid, nonse), cipher));

            //decrypt the response and write out to file
            cipher.init(Cipher.DECRYPT_MODE, key);
            Server_response response = (Server_response) sealedResponse.getObject(cipher);
            response.write_to(new FileOutputStream(new File("sealedSpec.docx")));
        } catch (Exception e) {
            System.out.println("Problem With Key Auth: " + e.getMessage());
        }
    }
}
