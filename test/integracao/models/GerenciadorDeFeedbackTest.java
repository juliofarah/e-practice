package integracao.models;

import models.GerenciadorDeFeedback;
import models.TabelaDeSimbolos;
import models.analisadorLexico.IdentificadorDeToken;
import models.analisadorLexico.Lexer;
import models.analisadorLexico.QuebradorDeCodigoEmLinhas;
import models.analisadorSemantico.GerenciadorSemantico;
import models.analisadorSintatico.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

public class GerenciadorDeFeedbackTest {


    private GerenciadorSintatico gerenciadorSintatico;
    private GerenciadorSemantico gerenciadorSemantico;
    private QuebradorDeCodigoEmLinhas quebradorDeCodigo;
    private GerenciadorDeFeedback gerenciadorDeFeedback;
    private TabelaDeSimbolos tabelaDeSimbolos;

    @Before
    public void setUp() throws Exception {
        models.analisadorSintatico.GerenciadorBuilder builderSintatico = new GerenciadorBuilder();
        models.analisadorSemantico.GerenciadorBuilder builderSemantico = new models.analisadorSemantico.GerenciadorBuilder();
        Lexer lexer = new Lexer();
        IdentificadorDeToken identificadorDeToken = new IdentificadorDeToken();
        tabelaDeSimbolos = new TabelaDeSimbolos();

        ValidadorDeDeclaracaoDeVariavel validadorDeDeclaracaoDeVariavelSintatico = new ValidadorDeDeclaracaoDeVariavel(tabelaDeSimbolos);
        ValidadorDeAtribuicao validadorDeAtribuicaoSintatico = new ValidadorDeAtribuicao(identificadorDeToken);
        ValidadorDeOperacoesAritmeticas validadorDeOperacoesAritmeticasSintatico = new ValidadorDeOperacoesAritmeticas();
        ValidadorDeConcatenacaoDeStrings validadorDeConcatenacaoDeStringSintatico = new ValidadorDeConcatenacaoDeStrings();

        models.analisadorSemantico.ValidadorDeDeclaracaoDeVariavel validadorDeDeclaracaoDeVariavelSemantico = new models.analisadorSemantico.ValidadorDeDeclaracaoDeVariavel(tabelaDeSimbolos);
        models.analisadorSemantico.ValidadorDeAtribuicao validadorDeAtribuicaoSemantico = new models.analisadorSemantico.ValidadorDeAtribuicao(tabelaDeSimbolos);
        models.analisadorSemantico.ValidadorDeOperacoesAritmeticas validadorDeOperacoesAritmeticasSemantico = new models.analisadorSemantico.ValidadorDeOperacoesAritmeticas(tabelaDeSimbolos);
        models.analisadorSemantico.ValidadorDeConcatenacao validadorDeConcatenacaoDeStringSemantico = new models.analisadorSemantico.ValidadorDeConcatenacao(tabelaDeSimbolos);


        gerenciadorSintatico = builderSintatico.com(lexer)
                .com(identificadorDeToken)
                .com(validadorDeDeclaracaoDeVariavelSintatico)
                .com(validadorDeAtribuicaoSintatico)
                .com(validadorDeOperacoesAritmeticasSintatico)
                .com(validadorDeConcatenacaoDeStringSintatico)
                .geraGerenciador();

        gerenciadorSemantico = builderSemantico.com(validadorDeDeclaracaoDeVariavelSemantico)
                .com(validadorDeAtribuicaoSemantico)
                .com(validadorDeOperacoesAritmeticasSemantico)
                .com(validadorDeConcatenacaoDeStringSemantico)
                .geraGerenciador();

        quebradorDeCodigo = new QuebradorDeCodigoEmLinhas();
    }

    @Test
    public void dadoQueReceboUmaDeclaracaoDeVariavelInvalidaRetornoUmaMensagemDeErro() throws Exception {
        String codigo = "var x = Inteiro";
        gerenciadorDeFeedback = new GerenciadorDeFeedback(codigo, gerenciadorSintatico, gerenciadorSemantico, quebradorDeCodigo);

        String mensagemDeErro = gerenciadorDeFeedback.pegaFeedback();

        assertThat(mensagemDeErro, is(notNullValue()));
        assertThat(mensagemDeErro, is(not("")));
    }

    @Test
    public void dadoQueReceboUmaDeclaracaoDeVariavelValidaRetornoStringVazia() throws Exception {
        String codigo = "var x : Inteiro";
        gerenciadorDeFeedback = new GerenciadorDeFeedback(codigo, gerenciadorSintatico, gerenciadorSemantico, quebradorDeCodigo);

        String mensagemDeErro = gerenciadorDeFeedback.pegaFeedback();

        assertThat(mensagemDeErro, is(""));
    }

    @Test
    public void dadoQueReceboDuasDeclaracoesDeVariaveisRetornoMensagemDeErroVazia() throws Exception {
        String codigo = "var x : Inteiro\nvar y : Inteiro";
        gerenciadorDeFeedback = new GerenciadorDeFeedback(codigo, gerenciadorSintatico, gerenciadorSemantico, quebradorDeCodigo);

        String mensagemDeErro = gerenciadorDeFeedback.pegaFeedback();

        assertThat(mensagemDeErro, is(""));
    }

    @Test
    public void dadoQueReceboDuasAtribuicoesDeValoresNumericosValidosRetornoMensagemDeErroVazia() throws Exception {
        String codigo = "var x : Inteiro\nvar y : Inteiro\nx = 4\ny = 5";
        gerenciadorDeFeedback = new GerenciadorDeFeedback(codigo, gerenciadorSintatico, gerenciadorSemantico, quebradorDeCodigo);

        String mensagemDeErro = gerenciadorDeFeedback.pegaFeedback();

        assertThat(mensagemDeErro, is(""));
    }

    @Test
    public void dadoQueReceboUmaConcatenacaoDeStringsValidasRetornoUmaMensagemDeErroVazia() throws Exception {
        String codigo = "var nome : String\n" +
                "nome = \"Bernardo\"\n" +
                "nome = \"nome\" <> \"José\"";
        gerenciadorDeFeedback = new GerenciadorDeFeedback(codigo, gerenciadorSintatico, gerenciadorSemantico, quebradorDeCodigo);

        String mensagemDeErro = gerenciadorDeFeedback.pegaFeedback();

        assertThat(mensagemDeErro, is(""));
    }

    @Test
    public void dadoQueReceboUmaOPeracaoDeAritmeticaValidaRetornoUmaMensagemDeErroVazia() throws Exception {
        String codigo = "var numero : Inteiro\n" +
                "numero = (2 + 2) * 5";
        gerenciadorDeFeedback = new GerenciadorDeFeedback(codigo, gerenciadorSintatico, gerenciadorSemantico, quebradorDeCodigo);

        String mensagemDeErro = gerenciadorDeFeedback.pegaFeedback();

        assertThat(mensagemDeErro, is(""));
    }

    @Test
    public void dadoQueReceboDuasDeclaracoesInvalidasRetornoUmaMensagemDeErro() throws Exception {
        String codigo = "var x = Inteiro\nvar \"erro\" : String";
        gerenciadorDeFeedback = new GerenciadorDeFeedback(codigo, gerenciadorSintatico, gerenciadorSemantico, quebradorDeCodigo);

        String mensagemDeErro = gerenciadorDeFeedback.pegaFeedback();

        assertThat(mensagemDeErro, is(not("")));
    }

    @Test
    public void dadoQueReceboDuasAtribuicoesInvalidasRetornoUmaMensagemDeErro() throws Exception {
        String codigo = "x : 2\ny : +3";
        gerenciadorDeFeedback = new GerenciadorDeFeedback(codigo, gerenciadorSintatico, gerenciadorSemantico, quebradorDeCodigo);

        String mensagemDeErro = gerenciadorDeFeedback.pegaFeedback();

        assertThat(mensagemDeErro, is(not("")));
    }

    @Test
    public void dadoQueReceboDuasOperacoesAritmeticasInvalidasRetornoUmaMensagemDeErro() throws Exception {
        String codigo = "numero = 2( +2";
        gerenciadorDeFeedback = new GerenciadorDeFeedback(codigo, gerenciadorSintatico, gerenciadorSemantico, quebradorDeCodigo);

        String mensagemDeErro = gerenciadorDeFeedback.pegaFeedback();

        assertThat(mensagemDeErro, is(not("")));
    }

    @Test
    public void dadoQueReceboUmaConcatenacaoDeStringsValidasComUmEspacoAntesDasStringsRetornoUmaMensagemDeErroVazia() throws Exception {
        String codigo = "var nome : String\n" +
                "nome =   \"nome:\" <>     \"José\"";
        gerenciadorDeFeedback = new GerenciadorDeFeedback(codigo, gerenciadorSintatico, gerenciadorSemantico, quebradorDeCodigo);

        String mensagemDeErro = gerenciadorDeFeedback.pegaFeedback();

        assertThat(mensagemDeErro, is(""));
    }

    @Test
    public void dadoQueReceboUmaConcatenacaoDeNumerosInvalidaEntaoRetornoUmaMensagemDeErro() throws Exception {
        String codigo = "10 <> 20";
        gerenciadorDeFeedback = new GerenciadorDeFeedback(codigo, gerenciadorSintatico, gerenciadorSemantico, quebradorDeCodigo);

        String mensagemDeErro = gerenciadorDeFeedback.pegaFeedback();

        assertThat(mensagemDeErro, is(not("")));
    }

    @Test
    public void dadoQueReceboUmaConcatenacaoDeStringsUtilizandoSimboloMaiorEMenorEMaisValidasRetornoUmaMensagemDeErroDiferenteDeVazio() throws Exception {
        String codigo = "\"nome:\" <> \"Bernardo\" + \"José\"";
        gerenciadorDeFeedback = new GerenciadorDeFeedback(codigo, gerenciadorSintatico, gerenciadorSemantico, quebradorDeCodigo);

        String mensagemDeErro = gerenciadorDeFeedback.pegaFeedback();

        assertThat(mensagemDeErro, is(not("")));
    }


    @Test
    public void dadoQueReceboUmaOperacaoAritmeticaInvalidaRetornoUmaMensagemDeErro() throws Exception {
        String codigo = "numero = 2 + 2)";
        gerenciadorDeFeedback = new GerenciadorDeFeedback(codigo, gerenciadorSintatico, gerenciadorSemantico, quebradorDeCodigo);

        String mensagemDeErro = gerenciadorDeFeedback.pegaFeedback();

        assertThat(mensagemDeErro, is(not("")));
    }


    @Test
    public void dadoQueReceboUmaOperacaoAritmeticaInvalidaUtilizandoDoisOperadoresJuntosRetornoUmaMensagemDeErro() throws Exception {
        String codigo = "numero = 2 * + 2";
        gerenciadorDeFeedback = new GerenciadorDeFeedback(codigo, gerenciadorSintatico, gerenciadorSemantico, quebradorDeCodigo);

        String mensagemDeErro = gerenciadorDeFeedback.pegaFeedback();

        assertThat(mensagemDeErro, is(not("")));
    }

    @Test
    public void dadoQueReceboUmaExpressaoSemEspacoNaoRetornaNenhumErro() throws Exception {
        String codigo = "var numero : Inteiro\n" +
                "numero=2+2";
        gerenciadorDeFeedback = new GerenciadorDeFeedback(codigo, gerenciadorSintatico, gerenciadorSemantico, quebradorDeCodigo);

        String mensagemDeErro = gerenciadorDeFeedback.pegaFeedback();

        assertThat(mensagemDeErro, is(""));
    }

}
