/***********************************************************************
 * Server_response.java                                                *
 *                                                                     *
 * This represents the response of the CW server                       *
 *
 ***********************************************************************/
import java.io.*;

public class Server_response implements Serializable{
	private byte[] spec;
	private int nonse;

	public Server_response(String fn,int nonse){
		read_in(fn);
		this.nonse = nonse;
	}

    public int get_nonse(){
		return nonse;
	}
    /* Given a output stream this method will write the array of
       bytes stored in the 'spec' feild of this object
     */
	public void write_to( OutputStream out ){
		try{
			out.write( spec );
		}catch( Exception e ){
			System.out.println("Exception while writing to file \n");
			e.printStackTrace();
		}
   	}
   	 /* Given a file name, this method will read and store the
	       file as a array of bytes in this object
	  */
	private void read_in(String fn){
		try{
			File file = new File(fn);
			spec = new byte[ (int)(file.length()) ];
			(new FileInputStream( file )).read( spec );
		}catch( Exception e ){
			System.out.println("Exception while reading the spec file \n");
			e.printStackTrace();
		}
	}

}