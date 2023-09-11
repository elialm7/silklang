package lexer;

import org.junit.Test;
import silklang.Lexer.SilkLexer;
import silklang.Lexer.Token;

import java.util.List;

public class LexerTest {
    @Test
    public void testVariableDeclaration(){
        String testcode = "var hola = \"hello\" ";
        System.out.println(testcode);
        SilkLexer lexer = new SilkLexer(testcode);
        List<Token> tokens = lexer.tokenize();
        for(Token tk: tokens){
            System.out.println(tk);
        }
    }

}
