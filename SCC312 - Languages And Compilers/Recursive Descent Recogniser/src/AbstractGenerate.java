
/**
 * Abstract Generate methods for 254 exercise.  This class provides an interface for arbitrary code generators to accept syntax from the rest of the system.
 * <p/>
 * This class has been provided to students
 *
 * @Author: Roger Garside, John Mariani
 */

public abstract class AbstractGenerate {

    /**
     * insertTerminal
     */
    public void insertTerminal(Token token) {
        String tt = Token.getName(token.symbol);
        if ((token.symbol == Token.identifier) ||
                (token.symbol == Token.numberConstant) ||
                (token.symbol == Token.stringConstant))
            tt += " '" + token.text + "'";
        tt += " on line " + token.lineNumber;
        System.out.println("rggTOKEN " + tt);
    } // end of method insertTerminal

    /**
     * commenceNonterminal
     */
    public void commenceNonterminal(String name) {
        System.out.println("rggBEGIN " + name);
    } // end of method commenceNonterminal

    /**
     * finishNonterminal
     */
    public void finishNonterminal(String name) {
        System.out.println("rggEND " + name);
    } // end of method finishNonterminal

    /**
     * reportSuccess
     */
    public void reportSuccess() {
        System.out.println("rggSUCCESS");
    } // end of method reportSuccess

    /**
     * Report an error to the user.
     */
    public abstract void reportError(Token token, String explanatoryMessage)
            throws CompilationException;
} // end of class "AbstractGenerate"
