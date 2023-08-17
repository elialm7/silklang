package shdlang.lexer;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Lexer {

    private boolean isAlphabetic(@NotNull String src){
        return !src.toUpperCase().equals(src.toLowerCase());
    }
    private boolean isInt(@NotNull String src){
        int c = src.codePointAt(0);
        int upperbound = "0".codePointAt(0);
        int lowerbound = "9".codePointAt(0);
        return c >= upperbound && c <= lowerbound;
    }
    private boolean isSkippable(@NotNull String src){
        return src.equals(" ") || src.equals("\n") || src.equals("\t");
    }


    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    private Token getToken(String value, TokenType type){
        return new Token(value, type);
    }

    public List<Token> tokenize(@NotNull String sourceCode){
        List<Token> tokens = new ArrayList<>();
        List<String> source = Arrays.asList(sourceCode.split(""));
        Iterator<String> sourceIterator = source.iterator();
        while(sourceIterator.hasNext()){
            String str = sourceIterator.next();
            if(str.equals("(")){
                tokens.add(getToken(str, TokenType.OPENPAREN));
                continue;
            }
            if(str.equals(")")){
                tokens.add(getToken(str, TokenType.ClOSEPAREN));
                continue;
            }
            if(checkbinaryoperator(str)){
                tokens.add(getToken(str, TokenType.BINARYOPERATOR));
                continue;
            }
            if(str.equals("=")){
                tokens.add(getToken(str, TokenType.EQUALS));
                continue;
            }
            if(isInt(str)){
                StringBuilder number = new StringBuilder();
                while(isInt(str) && sourceIterator.hasNext()){
                    number.append(str);
                    str = sourceIterator.next();
                }
                tokens.add(getToken(number.toString(), TokenType.NUMBER));
                continue;
            }
            if(isAlphabetic(str)){
                StringBuilder identifier = new StringBuilder();
                while(isAlphabetic(str) && sourceIterator.hasNext()){
                    identifier.append(str);
                    str = sourceIterator.next();
                }
                TokenType reserved = KeyWords.get(identifier.toString());
                tokens.add(getToken(identifier.toString(), Objects.requireNonNullElse(reserved, TokenType.IDENTIFIER)));
                continue;
            }
            if(isSkippable(str)){
                continue;
            }
            System.err.println("Unrecognized character found in source: " + str);
            System.exit(1);
        }
        return tokens;
    }

    private boolean checkbinaryoperator(@NotNull String str){
        return str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/");
    }

}







