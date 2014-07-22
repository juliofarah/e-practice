package unitario.model.analizadorLexico;

import models.analisadorLexico.Lexer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TesteLexer {

    private Lexer lexer;
    private ArrayList<String> tokens;

    @Before
    public void setUp() throws Exception {
        lexer = new Lexer();
    }

    @After
    public void tearDown() throws Exception {
        tokens.clear();
    }

    @Test
    public void dadoUmaLinhaEmBrancoDeveRetornarUmaListaDeTokensVazia() throws Exception {
        tokens = lexer.tokenizar("");
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void dadaUmaLinhaComUmaUnicaPalavraDeveRetornarUmaListaComApenasUmToken() throws Exception {
        tokens = lexer.tokenizar("String");
        String firstToken = tokens.get(0);
        assertThat(tokens.size(), is(1));
        assertThat(firstToken, is("String"));
    }

    @Test
    public void dadaUmaLinhaComDuasPalavrasSeparadasPorEspacoDeveRetornarUmaListaComDoisTokens() throws Exception {
        tokens = lexer.tokenizar("String name");
        String firstToken = tokens.get(0);
        String secondToken = tokens.get(1);
        assertThat(tokens.size(), is(2));
        assertThat(firstToken, is("String"));
        assertThat(secondToken, is("name"));
    }

    @Test
    public void dadaUmaLinhaComPalavrasSeparadasPorEspacoDeveRetornarUmaListaComUmTokenParaCadaPalavra() throws Exception {
        tokens = lexer.tokenizar("var umNumero : Integer");
        assertThat(tokens.size(), is(4));
        assertThat(tokens.get(0), is("var"));
        assertThat(tokens.get(1), is("umNumero"));
        assertThat(tokens.get(2), is(":"));
        assertThat(tokens.get(3), is("Integer"));
    }

    @Test
    public void dadaUmaAtribuicaoSemEspacamentoDeveRetornarUmaListaComUmTokenParaCadaPalavra() throws Exception {
        tokens = lexer.tokenizar("var umNumero:Integer");
        assertThat(tokens.size(), is(4));
        assertThat(tokens.get(0), is("var"));
        assertThat(tokens.get(1), is("umNumero"));
        assertThat(tokens.get(2), is(":"));
        assertThat(tokens.get(3), is("Integer"));
    }

    @Test
    public void dadaUmaAtribuicaoDeStringComUmaUnicaPalavraDeveRetornarUmaListaComTresTokens() throws Exception {
        tokens = lexer.tokenizar("nome = \"Joao\"");
        assertThat(tokens.size(), is(3));
        assertThat(tokens.get(0), is("nome"));
        assertThat(tokens.get(1), is("="));
        assertThat(tokens.get(2), is("\"Joao\""));
    }

    @Test
    public void dadaUmaAtribuicaoDeStringComDuasPalavrasDeeveRetornarUmaListaComTresTokens() throws Exception {
        tokens = lexer.tokenizar("nome = \"Joao Henrique\"");
        assertThat(tokens.size(), is(3));
        assertThat(tokens.get(0), is("nome"));
        assertThat(tokens.get(1), is("="));
        assertThat(tokens.get(2), is("\"Joao Henrique\""));
    }

    @Test
    public void dadaUmaAtribuicaoDeStringComQuatroPalavrasDeveRetornarUmaListaComTresTokens() throws Exception {
        tokens = lexer.tokenizar("nome = \"Joao Henrique Stocker Pinto\"");
        assertThat(tokens.size(), is(3));
        assertThat(tokens.get(0), is("nome"));
        assertThat(tokens.get(1), is("="));
        assertThat(tokens.get(2), is("\"Joao Henrique Stocker Pinto\""));
    }

    @Test
    public void dadaUmaAtribuicaoDeStringComUmaPalavraMasSemEspacosDeveRetornarUmaListaComTresTokens() throws Exception {
        tokens = lexer.tokenizar("nome=\"Joao\"");
        assertThat(tokens.size(), is(3));
        assertThat(tokens.get(0), is("nome"));
        assertThat(tokens.get(1), is("="));
        assertThat(tokens.get(2), is("\"Joao\""));
    }

    @Test
    public void dadaUmaConcatenacaoDeDuasStringsDeveRetornarUmaListaComCincoTokens() throws Exception {
        tokens = lexer.tokenizar("nome = \"Joao\" <> \"Henrique\"");
        assertThat(tokens.size(), is(5));
        assertThat(tokens.get(0), is("nome"));
        assertThat(tokens.get(1), is("="));
        assertThat(tokens.get(2), is("\"Joao\""));
        assertThat(tokens.get(3), is("<>"));
        assertThat(tokens.get(4), is("\"Henrique\""));
    }

    @Test
    public void dadaUmaConcatenacaoDeDuasStringsMasSemEspacoNaConcatenacaoDeveRetornarUmaListaComCincoTokens() throws Exception {
        tokens = lexer.tokenizar("nome = \"Joao\"<>\"Henrique\"");
        assertThat(tokens.size(), is(5));
        assertThat(tokens.get(0), is("nome"));
        assertThat(tokens.get(1), is("="));
        assertThat(tokens.get(2), is("\"Joao\""));
        assertThat(tokens.get(3), is ("<>"));
        assertThat(tokens.get(4), is("\"Henrique\""));

    }

    @Test
    public void dadaUmaConcatenacaoDeDuasStringsMasSemEspacoNaAtribuicaoENaConcatenacaoDeveRetornarUmaListaComCincoTokens() throws Exception {
        tokens = lexer.tokenizar("nome=\"Joao\"<>\"Henrique\"");
        assertThat(tokens.size(), is(5));
        assertThat(tokens.get(0), is("nome"));
        assertThat(tokens.get(1), is("="));
        assertThat(tokens.get(2), is("\"Joao\""));
        assertThat(tokens.get(3), is("<>"));
        assertThat(tokens.get(4), is("\"Henrique\""));
    }
}