package models.parser;

public class IdentificadorDeToken {


    public String identifica(String token) {

        if (token.equals("var")) {
            return "PALAVRA_RESERVADA";
        } else if (token.equals("String")){
            return "TIPO_DE_VARIAVEL";
        } else if (token.equals("Inteiro")){
            return "TIPO_DE_VARIAVEL";
        } else if (token.equals("=")){
            return "IGUAL";
        } else if (Character.isLetter(token.charAt(0))){
            return "IDV";
        } else if (Character.isDigit(token.charAt(0))) {
            if(!verificaSeTodasOsCaracteresSaoNumeros(token)) return "ERRO";
            else return "NUMERO";
        } else return "";
    }

    public boolean verificaSeTodasOsCaracteresSaoNumeros(String token){
        for (int i = 1; i < token.length(); i++) {
            if(!Character.isDigit(token.charAt(i))){
                return false;
            }
        }
        return true;
    }
}
