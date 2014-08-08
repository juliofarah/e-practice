package models.analisadorLexico;

import java.util.HashMap;

public class IdentificadorDeToken {

	private HashMap<String, String> mapaDeTokens;
	
    public IdentificadorDeToken() {
    	mapaDeTokens = new HashMap<String, String>();
    	adicionaValores();
	}

	public String identifica(String token) {
        if (mapaDeTokens.containsKey(token))
        	return mapaDeTokens.get(token);
        
        if (tokenComecaETerminaComAspas(token)) 
            return "CONSTANTE_TIPO_STRING";

        if (tokenComecaComLetra(token)) 
            return "IDV";
        
        if (tokenComecaComNumero(token))
            return verificaNumeros(token);
        
        return "INVALIDO";
    }

    public boolean verificaSeTodasOsCaracteresSaoNumeros(String token){
        for (int i = 1; i < token.length(); i++) {
            if(!Character.isDigit(token.charAt(i))){
                return false;
            }
        }
        return true;
    }

	private String verificaNumeros(String token) {
		try{
			Integer.valueOf(token);
			return "NUMERO";
		}catch (NumberFormatException e){
			return "ERRO";
		}
	}

	private boolean tokenComecaComNumero(String token) {
		return Character.isDigit(token.charAt(0));
	}

	private boolean tokenComecaComLetra(String token) {
		return Character.isLetter(token.charAt(0));
	}

	private boolean tokenComecaETerminaComAspas(String token) {
		return token.charAt(0) == '\"' && token.charAt(token.length()-1) == '\"';
	}

    
    private void adicionaValores() {
		mapaDeTokens.put("var", "PALAVRA_RESERVADA");
        mapaDeTokens.put("String", "TIPO_DE_VARIAVEL");
        mapaDeTokens.put("varres", "PALAVRA_RESERVADA");
        mapaDeTokens.put("Inteiro", "TIPO_DE_VARIAVEL");
        mapaDeTokens.put("=", "IGUAL");
        mapaDeTokens.put(":", "DECLARACAO");
        mapaDeTokens.put("+", "ADICAO");
        mapaDeTokens.put("-", "SUBTRACAO");
        mapaDeTokens.put("*", "MULTIPLICACAO");
        mapaDeTokens.put("/", "DIVISAO");
        mapaDeTokens.put("(", "PARENTESES_ABERTO");
        mapaDeTokens.put(")", "PARENTESES_FECHADO");
        mapaDeTokens.put("<>", "CONCATENACAO");
	}
}
