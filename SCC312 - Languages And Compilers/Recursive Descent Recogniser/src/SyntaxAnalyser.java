import java.io.IOException;

/**
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class SyntaxAnalyser extends AbstractSyntaxAnalyser {

    public SyntaxAnalyser(String filename) {

    }

    @Override
    void _statementPart_() throws IOException, CompilationException {

    }

    @Override
    void acceptTerminal(int symbol) throws IOException, CompilationException {
        if(nextToken.symbol == symbol) {
            myGenerate.insertTerminal(nextToken);
        }
        else {
            myGenerate.reportError(nextToken,
                    "ERROR: Found symbol " + nextToken + " expected " + symbol);

        }
    }

    void statementList() throws IOException, CompilationException {

    }

    void statment() throws IOException, CompilationException {

    }

    void ifStatement() throws IOException, CompilationException {

    }

    void whileStatement() throws  IOException, CompilationException{

    }

    void procdeureStatment() throws IOException, CompilationException {

    }

    void untilStatment() throws  IOException, CompilationException {

    }

    void argumentList() throws IOException, CompilationException {

    }

    void condition() throws IOException, CompilationException {

    }

    void conditionalOperator() throws IOException, CompilationException {

    }

    void expression() throws IOException, CompilationException {

    }

    void term() throws IOException, CompilationException {

    }

    void factor() throws  IOException, CompilationException {

    }




}
