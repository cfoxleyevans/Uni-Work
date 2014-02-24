import sun.io.ByteToCharEUC_KR;

import java.io.IOException;

/**
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class SyntaxAnalyser extends AbstractSyntaxAnalyser {

    public SyntaxAnalyser(String filename) {

        try{
            lex = new LexicalAnalyser(filename);

        }
        catch (IOException e) {
            System.out.println("ERROR: UNABLE TO CREATE TO LA");
            System.exit(0);
        }
}

    //process the top level symbol
    @Override
    void _statementPart_() throws IOException, CompilationException {
        //accept the begin token
        generator.commenceNonterminal("<statement part>");
        acceptTerminal(Token.beginSymbol);

        //parse the statement list
        statementList();

        lex.getNextToken();
        acceptTerminal(Token.endSymbol);

        //tell the generator that this non terminal is finished
        generator.finishNonterminal("<statement part>");
        //grab the next token before returning
        lex.getNextToken();
    }

    //accept a terminal ask the generator to print it to the listing
    @Override
    void acceptTerminal(int symbol) throws IOException, CompilationException {
        if(nextToken.symbol == symbol) {
            generator.insertTerminal(nextToken);
        }
        else {
            generator.reportError(nextToken,
                    "ERROR: Not expecting symbol: " + nextToken.symbol + "at line: " + nextToken.lineNumber);
        }
    }

    void statementList() throws IOException, CompilationException {
        generator.commenceNonterminal("<statement list>");
        generator.finishNonterminal("<statement list>");
    }

    void statment() throws IOException, CompilationException {
        lex.getNextToken();

        switch (nextToken.symbol) {
            case Token.identifier:
                assignmentStatement();
            case Token.ifSymbol:
                ifStatement();
                break;
            case Token.whileSymbol:
                whileStatement();
                break;
            case Token.procedureSymbol:
                procdeureStatment();
                break;
            case Token.untilSymbol:
                untilStatment();
                break;
            default:
                generator.reportError(nextToken, "ERROR: Not expecting symbol: " + nextToken.symbol + " at line: "
                        + nextToken.lineNumber);
        }
    }

    //process an assignment statement
    void assignmentStatement() throws IOException, CompilationException {
        generator.commenceNonterminal("<assignment statement>");
        acceptTerminal(Token.identifier);

        lex.getNextToken();

        acceptTerminal(Token.equalSymbol);

        lex.getNextToken();

        if(nextToken.symbol == Token.stringConstant) {
            acceptTerminal(Token.stringConstant);
        }
        else {
            expression();
        }
        generator.finishNonterminal("<assignment statement>");
    }

    void ifStatement() throws IOException, CompilationException {
        generator.commenceNonterminal("<if statement>");
        generator.finishNonterminal("<if statement>");
    }

    //process the sequence of tokens for a while statement
    void whileStatement() throws  IOException, CompilationException{
        generator.commenceNonterminal("<while statement>");
        acceptTerminal(Token.whileSymbol);

        lex.getNextToken();

        condition();

        lex.getNextToken();

        acceptTerminal(Token.loopSymbol);

        lex.getNextToken();

        statementList();

        lex.getNextToken();

        acceptTerminal(Token.endSymbol);
        generator.finishNonterminal("<while statement>");
    }

    void procdeureStatment() throws IOException, CompilationException {
        generator.commenceNonterminal("<procedure statement>");
        generator.commenceNonterminal("<procedure statement>");
    }

    //process the sequence of tokens for a until statment
    void untilStatment() throws  IOException, CompilationException {
        generator.commenceNonterminal("<unitl statement>");
        acceptTerminal(Token.doSymbol);

        lex.getNextToken();

        statementList();

        lex.getNextToken();

        acceptTerminal(Token.untilSymbol);

        lex.getNextToken();

        condition();

        generator.finishNonterminal("<unitl statement>");
    }

    void argumentList() throws IOException, CompilationException {
        generator.commenceNonterminal("<argument list>");
        generator.finishNonterminal("<argument list>");
    }

    void condition() throws IOException, CompilationException {
        generator.commenceNonterminal("<condition>");
        generator.commenceNonterminal("<condition>");
    }

    //accept the terminals that are valid for the conditional operator
    //if the symbol is not found report an error
    void conditionalOperator() throws IOException, CompilationException {
        lex.getNextToken();

        switch (nextToken.symbol) {
            case Token.greaterThanSymbol:
                acceptTerminal(Token.greaterThanSymbol);
                break;
            case Token.greaterEqualSymbol:
                acceptTerminal(Token.greaterEqualSymbol);
                break;
            case Token.equalSymbol:
                acceptTerminal(Token.equalSymbol);
                break;
            case Token.divideSymbol:
                acceptTerminal(Token.divideSymbol);
                break;
            case Token.lessThanSymbol:
                acceptTerminal(Token.lessThanSymbol);
                break;
            case Token.lessEqualSymbol:
                acceptTerminal(Token.lessEqualSymbol);
                break;
            default:
                generator.reportError(nextToken, "ERROR: Not expecting symbol: " + nextToken.symbol + " at line: "
                        + nextToken.lineNumber);
        }
    }

    void expression() throws IOException, CompilationException {
        generator.commenceNonterminal("<expression>");
        generator.commenceNonterminal("<expression>");
    }

    void term() throws IOException, CompilationException {
        generator.commenceNonterminal("<term>");
        generator.commenceNonterminal("<term>");
    }

    void factor() throws  IOException, CompilationException {
        generator.commenceNonterminal("<factor>");
        generator.commenceNonterminal("<factor>");
    }




}
