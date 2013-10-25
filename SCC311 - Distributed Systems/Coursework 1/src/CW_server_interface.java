/*****************************************************
 *CW_server_interface.java                           *
 *                                                   *
 *Interface to the CourseWork Spec Retreival Server  *
 *                                                   *
 *****************************************************/

import javax.crypto.SealedObject;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CW_server_interface extends Remote {
    public static final int GRADE_NONE = 0; /* as it says */
    public static final int GRADE_HALF = 1; /* password authentication */
    public static final int GRADE_FULL = 2; /* key based authentication */

    /* returns a Server_Response which contains the spec file
       will throw a RemoteException or returns null, if the uid doesnt exists
     */
    public Server_response getSpec (int uid, Client_request req) throws RemoteException;

    /* Given a sealed Client_request and a uid,
       returns a Server_Response (sealed) --encrypted by the key corresponding
       to the uid[passed as the first arg]-- will throw a RemoteException if
       the uid doesnt exists. It will return null if the decryption fails at the
       server end
     */
    public SealedObject getSpec (int uid, SealedObject req) throws RemoteException;

    /* returns   GRADE_NONE, GRADE_HALF, GRAGE_FULL depending on your
       authentication mode
     */
    public int getStatus (int uid) throws RemoteException;
}