package models;

import models.analisadorLexico.IdentificadorDeToken;
import models.analisadorLexico.Lexer;
import models.analisadorSintatico.ValidadorDeDeclaracaoDeVariavel;

import java.util.ArrayList;

/**
 * Created by alunos3 on 10/07/14.
 */
public class FeedBacker {

    Lexer lexer;
    ValidadorDeDeclaracaoDeVariavel declaracaoDeVariavel;
    IdentificadorDeToken identificadorDeToken;
    TabelaDeSimbolos tabelaDeSimbolos;
    String erros;

    public FeedBacker() {
        lexer = new Lexer();
        identificadorDeToken = new IdentificadorDeToken();
        tabelaDeSimbolos = new TabelaDeSimbolos();
    }

    private void validaAtribuicaoDeVariavel(String solucaoDoUsuario){
        ArrayList<String> tokens = lexer.tokenizar(solucaoDoUsuario);
        declaracaoDeVariavel = new ValidadorDeDeclaracaoDeVariavel(tokens, tabelaDeSimbolos);
        if(!declaracaoDeVariavel.valida())
            erros = declaracaoDeVariavel.capturaMensagensDeErro();
    }

    public String feedBackDoCodigoDoUsuario(String solucaoDoUsuario) {
        validaAtribuicaoDeVariavel(solucaoDoUsuario);
        return erros;
    }
}
