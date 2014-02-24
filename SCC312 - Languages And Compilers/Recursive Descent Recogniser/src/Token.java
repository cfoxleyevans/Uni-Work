/**
 *
 * Represents a lexical token for 254 exercise. 
 * 
 * This class has been provided to students
 *
 * @Author: Roger Garside
 *
 *
 **/
public class Token
{

	public static final int becomesSymbol = 1 ;
	public static final int beginSymbol = 2 ;
	public static final int callSymbol = 3 ;
	public static final int colonSymbol = 4 ;
	public static final int commaSymbol = 5 ;
	public static final int divideSymbol = 6 ;
	public static final int doSymbol = 7 ;
	public static final int endSymbol = 8 ;
	public static final int elseSymbol = 9 ;
	public static final int eofSymbol = 10 ;
	public static final int equalSymbol = 11 ;
	public static final int errorSymbol = 12 ;
	public static final int floatSymbol = 13 ;
	public static final int greaterEqualSymbol = 14 ;
	public static final int greaterThanSymbol = 15 ;
	public static final int identifier = 16 ;
	public static final int ifSymbol = 17 ;
	public static final int integerSymbol = 18 ;
	public static final int isSymbol = 19 ;
	public static final int leftParenthesis = 20 ;
	public static final int lessEqualSymbol = 21 ;
	public static final int lessThanSymbol = 22 ;
	public static final int loopSymbol = 23 ;
	public static final int minusSymbol = 24 ;
	public static final int notEqualSymbol = 25 ;
	public static final int numberConstant = 26 ;
	public static final int plusSymbol = 27 ;
	public static final int procedureSymbol = 28 ;
	public static final int rightParenthesis = 29 ;
	public static final int semicolonSymbol = 30 ;
	public static final int stringConstant = 31 ;
	public static final int stringSymbol = 32 ;
	public static final int timesSymbol = 33 ;
	public static final int thenSymbol = 34 ;
	public static final int untilSymbol = 35 ;
	public static final int whileSymbol = 36 ;

	private static final String[] names = {
		":=",         "begin",   "call",      ":",     ",",
		"/",          "do",      "end",       "else",  "EOF",
		"=",          "ERROR",   "float",     ">=",    ">",
		"IDENTIFIER", "if",      "integer",   "is",    "(",
		"<=",         "<",       "loop",      "-",     "/=",
		"NUMBER",     "+",       "procedure", ")",     ";",
		"STRING",     "string",  "*",         "then",  "until",
		"while"
	} ;

	/** The symbol this token instance represents */
	public int symbol ;
	/** The original text. */
	public String text ;
	/** The line number of the original text in the source file. */
	public int lineNumber ;

	/** Constructs a new token with a given token type and line number.

	  @param s The type of symbol, typically as a class constant from Token.
	  @param t The original string recognised from the source file.
	  @param l The line number of the original string.
	 */
	public Token(int s, String t, int l)
	{
		symbol = s ;
		text = t ;
		lineNumber = l ;
	} // end of constructor method

	/** Constructs a new token from a StringBuffer, given type and line number.


	  @param s The type of symbol, typically as a class constant from Token.
	  @param t The original string recognised from the source file.
	  @param l The line number of the original string.
	 */
	public Token(int s, StringBuffer t, int l)
	{
		symbol = s ;
		text = new String(t) ;
		lineNumber = l ;
	} // end of constructor method

	/** Returns a string representation of a symbol type.

	  @param i The value of a symbol, typically as a class constant from Token.
	  @return The name of this symbol.
	 */
	public static String getName(int i)
	{
		if ((i < 1) || (i > names.length))
			return "UNKNOWN" ;
		else
			return names[i - 1] ;
	} // end of method getName

	/** @see Object.toString */
	public String toString()
	{
		String tt = "token " + getName(symbol) ;
		if ((symbol == identifier) ||
				(symbol == numberConstant) ||
				(symbol == stringConstant))
			tt += ": " + text ;
		tt += " (line " + lineNumber + ")" ;
		return tt ;
	} // end of method toString
} // end of class Token

