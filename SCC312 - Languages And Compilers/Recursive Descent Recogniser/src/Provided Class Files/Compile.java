
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
		String prefix = "Programs Folder" + File.separator + "program" ;
		int fileNumber = -1 ;
		int exitFlag = 0 ;
		System.out.println("rggSTART") ;
		PrintStream out = null;
		String outputFile = new String("res.txt");
		boolean goon = true;

		try {
			out = new PrintStream(new FileOutputStream(outputFile));
		} catch (Exception e)
		{
			System.out.println("unable to open output file "+e);
			System.exit(0);
		};

		while (goon)
		{
			fileNumber++ ;
			fileName = prefix + fileNumber;
			goon = ((new File(fileName)).exists());
			if (goon)
			{
				System.out.println() ;
				System.out.println("rggFILE " + fileName) ;

				SyntaxAnalyser syn = new SyntaxAnalyser(fileName) ;
				syn.parse(out) ;
			} else System.out.println(fileName+" does not exist");
		}

		System.out.println() ;
		System.out.println("rggFINISH") ;
		out.flush();out.close();
		System.exit(exitFlag) ;
	} // end of main method

	public static void main(String args[]) throws IOException
	{
		Compile c = new Compile();
		c.go();
	};

} // end of class Compile
