
/**
 *
 * Main Driver program for 254 exercise.
 * 
 * This class has been provided to students
 *
 * @Author: Roger Garside, John Mariani
 *
 *
 **/

import java.io.* ;

public class Compile
{

	public static String fileName ;

	/**
	 *
	 * main
	 *
	 **/

	private void go() throws IOException
	{
        //had to change this line as the file structure on a mac is different
        String prefix = "Programs/program" ;
        int fileNumber = -1 ;
		int exitFlag = 0 ;
		boolean files = true;

        PrintStream out = null;

        String outputFile = "res.txt";

        System.out.println("rggSTART") ;

        try {
			out = new PrintStream(new FileOutputStream(outputFile));
		}
        catch (Exception e) {
			System.out.println("unable to open output file " + e);
			System.exit(0);
		}

        //while there are files left to parse. create a new sa and write the output to a file
		while (files) {
			fileNumber++ ;
			fileName = prefix + fileNumber;
			files = ((new File(fileName)).exists());
			if (files) {
				System.out.println() ;
				System.out.println("rggFILE " + fileName) ;

				SyntaxAnalyser syn = new SyntaxAnalyser(fileName) ;
				syn.parse(out) ;
			}
            else {
                System.out.println(fileName +" does not exist");
            }
		}

		System.out.println() ;
		System.out.println("rggFINISH") ;

        out.flush();
        out.close();

        System.exit(exitFlag) ;
	} // end of main method

	public static void main(String args[]) throws IOException
	{
		Compile c = new Compile();
		c.go();
	}

} // end of class Compile
