package lexer;

import jdk.jfr.BooleanFlag;
import org.junit.Before;
import org.junit.Test;
import shdlang.lexer.ShdLexer;
import shdlang.lexer.Token;
import shdlang.lexer.TokenType;

import java.util.List;

public class LexerTest {
    @Test
    public void testVariableDeclaration(){
        String testcode = "var hola = \"hello\" ";
        System.out.println(testcode);
        ShdLexer lexer = new ShdLexer(testcode);
        List<Token> tokens = lexer.tokenize();
        for(Token tk: tokens){
            System.out.println(tk);
        }
    }

}
