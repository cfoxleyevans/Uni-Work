/****************************************************
 * Client_request.java                              *
 *                                                  *
 * This is the Client Request
 *
 *
 ****************************************************/

import java.io.*;

public class Client_request implements Serializable{
	private int uid;
	private int nonse;
	private long passcode;

    /*******************************************
     Note: nonse feild is just a random number
           So just generate a random number using
           any random nnumber generator
    *********************************************/


    /* used for key based authentication */
	public Client_request(int uid, int nonse){
		this.uid = uid;
		this.nonse = nonse;
		this.passcode = 0;
	}
	/* used for password based authentication */
	public Client_request(int uid, int nonse,long passcode){
		this.uid = uid;
		this.nonse = nonse;
		this.passcode = passcode;

	}
	public long get_passcode(){
		return passcode;
	}
	public int get_uid(){
		return uid;
	}
	public int get_nonse(){
		return nonse;
	}
}
