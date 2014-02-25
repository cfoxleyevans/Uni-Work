import java.io.IOException;

/**
 * This is the implementation of a basic Recursive Descent Parser For SCC312 Compilation CW
 * The Grammar for the language is set out in the CW document
 * @author Chris Foxley-Evans 32879415
 * @version 0.0.5
 */
public class SyntaxAnalyser extends AbstractSyntaxAnalyser {

    /**
     * This stores the name of the file to provide more detail in messages
     */
    public String filename;


    /**
     * This is the class constructor
     * @param filename This is the filename of the file that should be analysed
     */
    public SyntaxAnalyser(String filename) {
        //try to create a new lexical analyzer for the file
        try {
            lex = new LexicalAnalyser(filename);
            this.filename = filename;

        } catch (IOException e) {
            //if we have reached here print error and exit
            System.out.println("ERROR: UNABLE TO CREATE TO LA FOR FILE:" + filename);
            System.exit(-1);
        }
    }

    /**
     * This function processes the distinguished symbol for the language
     * @throws IOException
     * @throws CompilationException
     */
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

    /**
     * This function accepts the current symbol from the Lexical Analyzer
     * @param symbol This is the symbol that is expected
     * @throws IOException
     * @throws CompilationException
     */
    @Override
    void acceptTerminal(int symbol) throws IOException, CompilationException {
        //if the symbol is the one we expect
        if (nextToken.symbol == symbol) {
            //insert the symbol into the cod generator and get the next token
            myGenerator.insertTerminal(nextToken);
            nextToken = lex.getNextToken();
        } else {
            //generate a helpful error message and pass it to the code generator
            myGenerator.reportError(nextToken,
                    "ERROR:[FILE:\"" + filename + "\"] [LINE:" + nextToken.lineNumber + "]" +
                            "Found Token: " + nextToken.text + " (" + Token.getName(nextToken.symbol) +")" +
                            " Expecting: " + Token.getName(symbol)
            );
        }
    }

    /**
     * This function is used to parse a Statement List
     * @throws IOException
     * @throws CompilationException
     */
    void statementList() throws IOException, CompilationException {
        //tell the code generator we have begun a statement list
        myGenerator.commenceNonterminal("<statement list>");

        //parse the statement
        statement();

        //while there a semi colon in the input string
        while (nextToken.symbol == Token.semicolonSymbol) {
            //accept the semi colon
            acceptTerminal(Token.semicolonSymbol);

            //parse the statement
            statement();
        }

        //tell the code generator that we have finished the statement list
        myGenerator.finishNonterminal("<statement list>");
    }

    /**
     * This function is used to parse a Statement
     * @throws IOException
     * @throws CompilationException
     */
    void statement() throws IOException, CompilationException {
        //tell the code generator that we have begun a statement
        myGenerator.commenceNonterminal("<statement>");

        //check the next token from the lexical analyzer and call the appropriate parse method
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
                procedureStatement();
                break;
            case Token.untilSymbol:
                untilStatement();
                break;
            default:
                //generate a helpful error message and pass it to the code generator
                myGenerator.reportError(nextToken,
                        "ERROR:[FILE:\"" + filename + "\"] [LINE:" + nextToken.lineNumber + "]" +
                                "Found Token: " + nextToken.text + " (" + Token.getName(nextToken.symbol) +")" +
                                " Expecting: " + Token.getName(Token.identifier) +
                                " (" + Token.getName(Token.ifSymbol) +
                                " ," + Token.getName(Token.whileSymbol) +
                                " ," + Token.getName(Token.callSymbol) +
                                " ," + Token.getName(Token.untilSymbol) + ")"
                );
        }

        //tell the code generator that we have finished the statment
        myGenerator.finishNonterminal("<statement>");
    }

    /**
     * This function is used to parse an Assignment Statement
     * @throws IOException
     * @throws CompilationException
     */
    void assignmentStatement() throws IOException, CompilationException {
        //tell the code generator that we have begun an assignment statement
        myGenerator.commenceNonterminal("<assignment statement>");

        //accept the identifier token
        acceptTerminal(Token.identifier);

        //accept the becomes token
        acceptTerminal(Token.becomesSymbol);

        //if the next token is a string accept it if it is not parse the expression
        if (nextToken.symbol == Token.stringConstant) {
            acceptTerminal(Token.stringConstant);
        } else {
            expression();
        }

        //tell the code generator that we have finished the assignment statement
        myGenerator.finishNonterminal("<assignment statement>");
    }

    /**
     * This function is used to parse an If Statement
     * @throws IOException
     * @throws CompilationException
     */
    void ifStatement() throws IOException, CompilationException {
        //tell the code generator that we have begun an if statement
        myGenerator.commenceNonterminal("<if statement>");

        //accept the if token
        acceptTerminal(Token.ifSymbol);

        //parse the condition
        condition();

        //accept the the then token
        acceptTerminal(Token.thenSymbol);

        //parse the statement list
        statementList();

        //if the next token is an else token then parse the extra components
        if (nextToken.symbol == Token.elseSymbol) {

            //accept the else token
            acceptTerminal(Token.elseSymbol);

            //parse the statement list
            statementList();
        }

        //accept the end token
        acceptTerminal(Token.endSymbol);

        //accept the if token
        acceptTerminal(Token.ifSymbol);

        //tell the code generator that we have finished the if statement
        myGenerator.finishNonterminal("<if statement>");
    }

    /**
     * This function is used to parse a While Statement
     * @throws IOException
     * @throws CompilationException
     */
    void whileStatement() throws IOException, CompilationException {
        //tell the code generator that we have begun the while statment
        myGenerator.commenceNonterminal("<while statement>");

        //accept the while token
        acceptTerminal(Token.whileSymbol);

        //parse the condition
        condition();

        //accept the loop token
        acceptTerminal(Token.loopSymbol);

        //parse the statement list
        statementList();

        //accept the end token
        acceptTerminal(Token.endSymbol);

        //accept the loop token
        acceptTerminal(Token.loopSymbol);

        //tell the code generator that we have finished the while statement
        myGenerator.finishNonterminal("<while statement>");
    }

    /**
     * This function is used to parse a Procedure Statement
     * @throws IOException
     * @throws CompilationException
     */
    void procedureStatement() throws IOException, CompilationException {
        //tell the code generator that we have begun a procedure statement
        myGenerator.commenceNonterminal("<procedure statement>");

        //accept the call token
        acceptTerminal(Token.callSymbol);

        //accept the identifier
        acceptTerminal(Token.identifier);

        //accept the left paren
        acceptTerminal(Token.leftParenthesis);

        //parse the argument list
        argumentList();

        //accept the right paren
        acceptTerminal(Token.rightParenthesis);

        //tell the code generator that we have finished the procedure statement
        myGenerator.commenceNonterminal("<procedure statement>");
    }

    /**
     * This function is used to parse an Until Statement
     * @throws IOException
     * @throws CompilationException
     */
    void untilStatement() throws IOException, CompilationException {
        //tell the code generator that we have begun an until statement
        myGenerator.commenceNonterminal("<until statement>");

        //accept the do token
        acceptTerminal(Token.doSymbol);

        //parse the statement list
        statementList();

        //accept the until token
        acceptTerminal(Token.untilSymbol);

        //parse the condition
        condition();

        //tell the code generator that we have finished the until statement
        myGenerator.finishNonterminal("<until statement>");
    }

    /**
     * This function is used to parse an Argument List
     * @throws IOException
     * @throws CompilationException
     */
    void argumentList() throws IOException, CompilationException {
        //tell the code generator that we have begun an argument list
        myGenerator.commenceNonterminal("<argument list>");

        //accept the identifier token
        acceptTerminal(Token.identifier);

        //while there is a comma in the input string
        while (nextToken.symbol == Token.commaSymbol) {
            //accept the comma token
            acceptTerminal(Token.commaSymbol);

            //accept the next identifier token
            acceptTerminal(Token.identifier);
        }

        //tell the code generator that we have finished the argument list
        myGenerator.finishNonterminal("<argument list>");
    }

    /**
     * This function is used to parse a condition
     * @throws IOException
     * @throws CompilationException
     */
    void condition() throws IOException, CompilationException {
        //tell the code generator that we have begun a condition
        myGenerator.commenceNonterminal("<condition>");

        //accept the identifier token
        acceptTerminal(Token.identifier);

        //parse the conditional operator
        conditionalOperator();

        //check the next token from the lexical analyzer and call the appropriate accept method
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
                //generate a helpful error message and pass it to the code generator
                myGenerator.reportError(nextToken,
                        "ERROR:[FILE:\"" + filename + "\"] [LINE:" + nextToken.lineNumber + "]" +
                                "Found Token: " + nextToken.text + " (" + Token.getName(nextToken.symbol) +")" +
                                " Expecting: " + Token.getName(Token.identifier) +
                                " (" + Token.getName(Token.numberConstant) +
                                " ," + Token.getName(Token.stringConstant) +")"
                );
        }

        //tell the code generator that we have finished the condition
        myGenerator.commenceNonterminal("<condition>");
    }

    /**
     * This function is used to parse a condition operator
      * @throws IOException
     * @throws CompilationException
     */
    void conditionalOperator() throws IOException, CompilationException {
        //tell the code generator that we have begun a conditional operator
        myGenerator.commenceNonterminal("<conditional operator>");

        //check the next token from the lexical analyzer and call the appropriate accept method
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
                //generate a helpful error message and pass it to the code generator
                myGenerator.reportError(nextToken,
                        "ERROR:[FILE:\"" + filename + "\"] [LINE:" + nextToken.lineNumber + "]" +
                                "Found Token: " + nextToken.text + " (" + Token.getName(nextToken.symbol) +")" +
                                " Expecting: " + Token.getName(Token.greaterThanSymbol) +
                                " (" + Token.getName(Token.greaterEqualSymbol) +
                                " ," + Token.getName(Token.equalSymbol) +
                                " ," + Token.getName(Token.notEqualSymbol) +
                                " ," + Token.getName(Token.lessThanSymbol) +
                                " ," + Token.getName(Token.lessEqualSymbol) + ")"
                );
        }
        //tell the code generator that we have finished the conditional operator
        myGenerator.finishNonterminal("<conditional operator>");
    }

    /**
     * This function is used to parse an expression
     * @throws IOException
     * @throws CompilationException
     */
    void expression() throws IOException, CompilationException {
        //tell the code generator that we have begun and expression
        myGenerator.commenceNonterminal("<expression>");

        //parse the term
        term();

        //if there is a plus/minus token on the input string then check which and accept the token
        //then parse the term
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
        //tell the code generator that we have finished the expression
        myGenerator.commenceNonterminal("<expression>");
    }

    /**
     * This function is used to parse a term
     * @throws IOException
     * @throws CompilationException
     */
    void term() throws IOException, CompilationException {
        //tell the code generator that we have begun a term
        myGenerator.commenceNonterminal("<term>");

        //parse the factor
        factor();

        //if there if a times/divide symbol on the input string then check which and accept the token
        //then factor the term
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
        //tell the code generator that we have finished the term
        myGenerator.commenceNonterminal("<term>");
    }

    /**
     * This function is used to parse a factor
     * @throws IOException
     * @throws CompilationException
     */
    void factor() throws IOException, CompilationException {
        //tell the code generator that we have begun a factor
        myGenerator.commenceNonterminal("<factor>");

        //check the next token from the lexical analyzer and call the appropriate accept method
        //in the case of the left paren accept this then parse the expression then accept the right paren
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
                //generate a helpful error message and pass it to the code generator
                myGenerator.reportError(nextToken,
                        "ERROR:[FILE:\"" + filename + "\"] [LINE:" + nextToken.lineNumber + "]" +
                        "Found Token: " + nextToken.text + " (" + Token.getName(nextToken.symbol) +")" +
                        " Expecting: " + Token.getName(Token.identifier) +
                        " (" + Token.getName(Token.leftParenthesis) +
                        " ," + Token.getName(Token.rightParenthesis) + ")"
                );
        }
        //tell the code generator that we have finished the factor
        myGenerator.commenceNonterminal("<factor>");
    }
}