package shdlang.lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {

    private boolean isAlphabetic(String src){
        return !src.toUpperCase().equals(src.toLowerCase());
    }
    private boolean isInt(String src){
        int c = src.codePointAt(0);
        int upperbound = "0".codePointAt(0);
        int lowerbound = "9".codePointAt(0);
        return c >= upperbound && c <= lowerbound;
    }
    private boolean isSkippable(String src){
        return src.equals(" ") || src.equals("\n") || src.equals("\t");
    }


    private Token token(String value, TokenType type){
        return new Token(value, type);
    }

    public List<Token> tokenize(String sourceCode){
        List<Token> tokens = new ArrayList<>();
        String[] source = sourceCode.split("");

        while(source.length > 0){
            // we parse one character variables
            if (source[0].equals("(")) {
                // need to create a array shift implementation for this code section.
            }
        }

        return null;
    }

}
