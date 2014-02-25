import java.io.IOException;

/**
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class SyntaxAnalyser extends AbstractSyntaxAnalyser {

    public SyntaxAnalyser(String filename) {

        try {
            lex = new LexicalAnalyser(filename);

        } catch (IOException e) {
            System.out.println("ERROR: UNABLE TO CREATE TO LA");
            System.exit(0);
        }
    }

    //process the top level symbol
    @Override
    void _statementPart_() throws IOException, CompilationException {
        //accept the begin token
        myGenerator.commenceNonterminal("<statement part>");
        acceptTerminal(Token.beginSymbol);

        //parse the statement list
        statementList();

        acceptTerminal(Token.endSymbol);

        //tell the myGenerator that this non terminal is finished
        myGenerator.finishNonterminal("<statement part>");
    }

    //accept a terminal ask the myGenerator to print it to the listing
    @Override
    void acceptTerminal(int symbol) throws IOException, CompilationException {
        if (nextToken.symbol == symbol) {
            myGenerator.insertTerminal(nextToken);
            nextToken = lex.getNextToken();
        } else {
            myGenerator.reportError(nextToken,
                    "ERROR: Not expecting symbol: " + Token.getName(nextToken.symbol) + " at line: " + nextToken.lineNumber
                            + " Expecting: " + Token.getName(symbol));
        }
    }

    void statementList() throws IOException, CompilationException {
        myGenerator.commenceNonterminal("<statement list>");

        statement();

        while (nextToken.symbol == Token.semicolonSymbol) {
            //accept the semi colon
            acceptTerminal(Token.semicolonSymbol);

            //move to the next terminal and process
            statement();
        }
        myGenerator.finishNonterminal("<statement list>");
    }

    void statement() throws IOException, CompilationException {
        myGenerator.commenceNonterminal("<statement>");

        switch (nextToken.symbol) {
            case Token.identifier:
                assignmentStatement();
                break;
            case Token.ifSymbol:
                ifStatement();
                break;
            case Token.whileSymbol:
                whileStatement();
                break;
            case Token.callSymbol:
                procdeureStatment();
                break;
            case Token.untilSymbol:
                untilStatment();
                break;
            default:
                myGenerator.reportError(nextToken,
                        "ERROR: Not expecting symbol: " + Token.getName(nextToken.symbol) + " at line: "
                                + nextToken.lineNumber);
        }
        myGenerator.finishNonterminal("<statement>");
    }

    //process an assignment statement
    void assignmentStatement() throws IOException, CompilationException {
        myGenerator.commenceNonterminal("<assignment statement>");

        acceptTerminal(Token.identifier);

        acceptTerminal(Token.becomesSymbol);

        if (nextToken.symbol == Token.stringConstant) {
            acceptTerminal(Token.stringConstant);
        } else {
            expression();
        }

        myGenerator.finishNonterminal("<assignment statement>");
    }

    //process an if statment
    void ifStatement() throws IOException, CompilationException {
        myGenerator.commenceNonterminal("<if statement>");

        acceptTerminal(Token.ifSymbol);

        condition();

        acceptTerminal(Token.thenSymbol);

        statementList();

        if (nextToken.symbol == Token.elseSymbol) {
            statementList();

            acceptTerminal(Token.endSymbol);

            acceptTerminal(Token.ifSymbol);
        } else {
            acceptTerminal(Token.endSymbol);

            acceptTerminal(Token.ifSymbol);
        }

        myGenerator.finishNonterminal("<if statement>");
    }

    //process the sequence of tokens for a while statement
    void whileStatement() throws IOException, CompilationException {
        myGenerator.commenceNonterminal("<while statement>");

        acceptTerminal(Token.whileSymbol);

        condition();

        acceptTerminal(Token.loopSymbol);

        statementList();

        acceptTerminal(Token.endSymbol);
        acceptTerminal(Token.loopSymbol);

        myGenerator.finishNonterminal("<while statement>");
    }

    //process the procedure statment
    void procdeureStatment() throws IOException, CompilationException {
        myGenerator.commenceNonterminal("<procedure statement>");

        acceptTerminal(Token.callSymbol);

        acceptTerminal(Token.identifier);

        acceptTerminal(Token.leftParenthesis);

        argumentList();

        acceptTerminal(Token.rightParenthesis);

        myGenerator.commenceNonterminal("<procedure statement>");
    }

    //process the sequence of tokens for a until statment
    void untilStatment() throws IOException, CompilationException {
        myGenerator.commenceNonterminal("<unitl statement>");
        acceptTerminal(Token.doSymbol);


        statementList();


        acceptTerminal(Token.untilSymbol);


        condition();

        myGenerator.finishNonterminal("<unitl statement>");
    }

    //process an argument list
    void argumentList() throws IOException, CompilationException {
        myGenerator.commenceNonterminal("<argument list>");


        acceptTerminal(Token.identifier);


        while (nextToken.symbol == Token.commaSymbol) {
            acceptTerminal(Token.commaSymbol);


            argumentList();
        }

        myGenerator.finishNonterminal("<argument list>");
    }

    //process a condition
    void condition() throws IOException, CompilationException {
        myGenerator.commenceNonterminal("<condition>");

        acceptTerminal(Token.identifier);

        conditionalOperator();

        switch (nextToken.symbol) {
            case Token.identifier:
                acceptTerminal(Token.identifier);
                break;
            case Token.numberConstant:
                acceptTerminal(Token.numberConstant);
                break;
            case Token.stringConstant:
                acceptTerminal(Token.stringConstant);
                break;
            default:
                myGenerator.reportError(nextToken, "ERROR: Not expecting symbol: " + nextToken.symbol + " at line: "
                        + nextToken.lineNumber);

        }

        myGenerator.commenceNonterminal("<condition>");
    }

    //accept the terminals that are valid for the conditional operator
    //if the symbol is not found report an error
    void conditionalOperator() throws IOException, CompilationException {
        myGenerator.commenceNonterminal("<conditional operator>");

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
            case Token.notEqualSymbol:
                acceptTerminal(Token.notEqualSymbol);
                break;
            case Token.lessThanSymbol:
                acceptTerminal(Token.lessThanSymbol);
                break;
            case Token.lessEqualSymbol:
                acceptTerminal(Token.lessEqualSymbol);
                break;
            default:
                myGenerator.reportError(nextToken, "ERROR: Not expecting symbol: " + nextToken.symbol + " at line: "
                        + nextToken.lineNumber);
        }
        myGenerator.finishNonterminal("<conditional operator>");
    }

    //proces a expression
    void expression() throws IOException, CompilationException {
        myGenerator.commenceNonterminal("<expression>");

        term();

        while (nextToken.symbol == Token.plusSymbol || nextToken.symbol == Token.minusSymbol) {
            if (nextToken.symbol == Token.plusSymbol) {
                acceptTerminal(Token.plusSymbol);
                term();
            }
            if (nextToken.symbol == Token.minusSymbol) {
                acceptTerminal(Token.minusSymbol);
                term();
            }
        }

        myGenerator.commenceNonterminal("<expression>");
    }

    //process a term
    void term() throws IOException, CompilationException {
        myGenerator.commenceNonterminal("<term>");

        factor();

        while (nextToken.symbol == Token.timesSymbol || nextToken.symbol == Token.divideSymbol) {
            if (nextToken.symbol == Token.timesSymbol) {
                acceptTerminal(Token.timesSymbol);
                factor();
            }
            if (nextToken.symbol == Token.divideSymbol) {
                acceptTerminal(Token.divideSymbol);
                factor();
            }
        }


        myGenerator.commenceNonterminal("<term>");
    }

    //process a factor
    void factor() throws IOException, CompilationException {
        myGenerator.commenceNonterminal("<factor>");

        switch (nextToken.symbol) {
            case Token.identifier:
                acceptTerminal(Token.identifier);
                break;
            case Token.numberConstant:
                acceptTerminal(Token.numberConstant);
                break;
            case Token.leftParenthesis:
                acceptTerminal(Token.leftParenthesis);
                expression();
                acceptTerminal(Token.rightParenthesis);
                break;
            default:
                myGenerator.reportError(nextToken, "ERROR: Not expecting symbol: " +
                        Token.getName(nextToken.symbol) + " at line: " + nextToken.lineNumber + "Expecting: " +
                        Token.getName(Token.identifier) + ", " +
                        Token.getName(Token.numberConstant) + ", " + Token.getName(Token.leftParenthesis));
        }

        myGenerator.commenceNonterminal("<factor>");
    }
}