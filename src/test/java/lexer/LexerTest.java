package lexer;

import org.junit.Test;
import shdlang.lexer.Lexer;
import shdlang.lexer.Token;

import java.util.List;

public class LexerTest {

    @Test
    public void testTokenizer(){

        Lexer lex = new Lexer();
        List<Token> results = lex.tokenize("let var = 45 ");

        for(Token tok: results){
            System.out.println(tok.toString());
        }

    }


}
