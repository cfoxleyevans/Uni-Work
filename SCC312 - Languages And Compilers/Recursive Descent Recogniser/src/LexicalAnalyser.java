
/**
 *
 * lexical analyser for 254 exercise.
 *
 * This class has been provided to students
 *
 * Author: Roger Garside
 *
 *
 **/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LexicalAnalyser {

    /**
     * Represents a textual and symbolic reserved word.
     */
    class ReservedWord {
        /**
         * The text used in source.
         */
        public String text;
        /**
         * The type (one of the class constants from Token) of this word.
         */
        public int symbol;

        /**
         * Creates a new reserved word from the observed text and a given type.
         *
         * @param t The text as seen in source.
         * @param s The type of this word, typically a class constant from Token
         */
        public ReservedWord(String t, int s) {
            text = t;
            symbol = s;
        } // end of constructor method
    } // end of class ReservedWord

    /**
     * The maximum number of identifiers to hold during compilation.
     */
    private static final int maxTableSize = 200;
    /**
     * The EOF character.
     */
    private static final char EOF = '\000';

    /**
     * A list of symbols.
     */
    private ReservedWord[] symbols = new ReservedWord[maxTableSize];
    /**
     * Holds the current number of symbols held.
     */
    private int noOfSymbols;

    /**
     * Is this the first time we have been called?
     */
    private boolean firstCall;

    /**
     * An input stream from the filename mentioned above.
     */
    private BufferedReader sourceFile;

    /* State-change character and offset counts. */
    private char currentCharacter;
    private String currentLine;
    private int currentOffset,
            currentLineNumber;

    /* input buffer */
    private StringBuffer currentText = new StringBuffer();

    /**
     * Adds a reserved word to the internal symbol table.
     *
     * @param t The text as seen in source.
     * @param s The type of this word, typically a class constant from Token
     */
    private void setReservedWord(String t, int s) {
        symbols[noOfSymbols] = new ReservedWord(t, s);
        noOfSymbols++;
    } // end of method setReservedWord


    /**
     * Sets all initial variables and adds the language's reserved words to the symbol table.
     */
    private void initialiseScanner() {
        noOfSymbols = 0;
        setReservedWord("begin", Token.beginSymbol);
        setReservedWord("call", Token.callSymbol);
        setReservedWord("do", Token.doSymbol);
        setReservedWord("else", Token.elseSymbol);
        setReservedWord("end", Token.endSymbol);
        setReservedWord("float", Token.floatSymbol);
        setReservedWord("if", Token.ifSymbol);
        setReservedWord("integer", Token.integerSymbol);
        setReservedWord("is", Token.isSymbol);
        setReservedWord("loop", Token.loopSymbol);
        setReservedWord("procedure", Token.procedureSymbol);
        setReservedWord("string", Token.stringSymbol);
        setReservedWord("then", Token.thenSymbol);
        setReservedWord("until", Token.untilSymbol);
        setReservedWord("while", Token.whileSymbol);
    } // end of method initialiseScanner


    /**
     * Creates a new LexicalAnalyser which will run over the given file.
     *
     * @param fileName The file to read.
     * @throws IOException if any read errors occur during parsing.
     */
    public LexicalAnalyser(String fileName) throws IOException {
        initialiseScanner();

        sourceFile = new BufferedReader(new FileReader(fileName));
        currentLine = sourceFile.readLine();
        currentOffset = 0;
        firstCall = true;
        currentLineNumber = 0;
    } // end of constructor method

    /**
     * Loads the next character of the input into the buffer.
     *
     * @throws IOException in the event that something like velociraptor attack happens to the input stream.
     */
    private void getNextCharacter() throws IOException {
        if (currentLine == null)
            currentCharacter = EOF;
        else if (currentOffset >= currentLine.length()) {
            currentLine = sourceFile.readLine();
            currentOffset = 0;
            currentCharacter = '\n';
        } else {
            currentCharacter = currentLine.charAt(currentOffset);
            currentOffset++;
        }
    } // end of method getNextCharacter

    /**
     * Returns the next token from the source file.  Repeatedly calling this
     * will return each token in the file, and eventually null.
     *
     * @return the next token from the source file.
     * @throws IOException in the event that the file cannot be read.
     */
    public Token getNextToken() throws IOException {
        if (firstCall) {
            getNextCharacter();
            firstCall = false;
        }

        while ((currentCharacter == ' ') || (currentCharacter == '\t') ||
                (currentCharacter == '\n') || (currentCharacter == '-')) {
            if (currentCharacter == '-') {
                getNextCharacter();
                if (currentCharacter == '-') {
                    while (currentCharacter != '\n')
                        getNextCharacter();
                } else
                    return new Token(Token.minusSymbol, "-", currentLineNumber);
            }

            if (currentCharacter == '\n')
                currentLineNumber++;
            getNextCharacter();
        }

        if (Character.isLetter(currentCharacter)) {
            currentText.setLength(0);
            while ((Character.isLetter(currentCharacter)) ||
                    (Character.isDigit(currentCharacter))) {
                currentText.append(currentCharacter);
                getNextCharacter();
            }

            int i = 0;
            String t = (new String(currentText)).toLowerCase();
            while ((i < noOfSymbols) &&
                    (!t.equals(symbols[i].text)))
                i++;

            if (i < noOfSymbols)
                return new Token(symbols[i].symbol, currentText, currentLineNumber);
            else
                return new Token(Token.identifier, currentText, currentLineNumber);
        } else if (Character.isDigit(currentCharacter)) {
            currentText.setLength(0);
            while (Character.isDigit(currentCharacter)) {
                currentText.append(currentCharacter);
                getNextCharacter();
            }
            if (currentCharacter == '.') {
                currentText.append(currentCharacter);
                getNextCharacter();
                while (Character.isDigit(currentCharacter)) {
                    currentText.append(currentCharacter);
                    getNextCharacter();
                }
            }
            return new Token(Token.numberConstant, currentText, currentLineNumber);
        } else if (currentCharacter == '"') {
            getNextCharacter();
            currentText.setLength(0);
            while (currentCharacter != '"') {
                currentText.append(currentCharacter);
                getNextCharacter();
            }
            getNextCharacter();
            return new Token(Token.stringConstant, currentText, currentLineNumber);
        } else if (currentCharacter == ':') {
            getNextCharacter();
            if (currentCharacter == '=') {
                getNextCharacter();
                return new Token(Token.becomesSymbol, ":=", currentLineNumber);
            } else
                return new Token(Token.colonSymbol, ":", currentLineNumber);
        } else if (currentCharacter == '>') {
            getNextCharacter();
            if (currentCharacter == '=') {
                getNextCharacter();
                return new Token(Token.greaterEqualSymbol, ">=", currentLineNumber);
            } else
                return new Token(Token.greaterThanSymbol, ">", currentLineNumber);
        } else if (currentCharacter == '<') {
            getNextCharacter();
            if (currentCharacter == '=') {
                getNextCharacter();
                return new Token(Token.lessEqualSymbol, "<=", currentLineNumber);
            } else
                return new Token(Token.lessThanSymbol, "<", currentLineNumber);
        } else if (currentCharacter == '/') {
            getNextCharacter();
            if (currentCharacter == '=') {
                getNextCharacter();
                return new Token(Token.notEqualSymbol, "/=", currentLineNumber);
            } else
                return new Token(Token.divideSymbol, "/", currentLineNumber);
        } else if (currentCharacter == '=') {
            getNextCharacter();
            return new Token(Token.equalSymbol, "=", currentLineNumber);
        } else if (currentCharacter == ',') {
            getNextCharacter();
            return new Token(Token.commaSymbol, ",", currentLineNumber);
        } else if (currentCharacter == ';') {
            getNextCharacter();
            return new Token(Token.semicolonSymbol, ";", currentLineNumber);
        } else if (currentCharacter == '+') {
            getNextCharacter();
            return new Token(Token.plusSymbol, "+", currentLineNumber);
        } else if (currentCharacter == '*') {
            getNextCharacter();
            return new Token(Token.timesSymbol, "*", currentLineNumber);
        } else if (currentCharacter == '(') {
            getNextCharacter();
            return new Token(Token.leftParenthesis, "(", currentLineNumber);
        } else if (currentCharacter == ')') {
            getNextCharacter();
            return new Token(Token.rightParenthesis, ")", currentLineNumber);
        } else if (currentCharacter == EOF) {
            return new Token(Token.eofSymbol, "", currentLineNumber);
        } else {
            currentText = new StringBuffer(currentCharacter);
            getNextCharacter();        // added 21st January 2005
            return new Token(Token.errorSymbol, currentText, currentLineNumber);
        }
    } // end of method getNextToken

    /**
     * Entry point to text Lexer
     */
    public static void main(String[] args) throws IOException {
        BufferedReader din = new BufferedReader(new InputStreamReader(System.in));
        System.err.print("file? ");
        System.err.flush();
        String fileName = din.readLine().trim();
        LexicalAnalyser lex = new LexicalAnalyser(fileName);
        Token t = null;
        do {
            t = lex.getNextToken();
            System.out.println(t);
        }
        while (t.symbol != Token.eofSymbol);
    } // end of main method
} // end of class LexicalAnalyser
