package components;

import java.util.*;


public class ClassConstructor {
    private ArrayList constructorTokens;
    private ArrayList statementsList;

    public ClassConstructor(){
        constructorTokens = new ArrayList();
        statementsList = new ArrayList();
    }

    public String getConstructorHeader(){
        String header = "";
        for (int ct=0; ct<constructorTokens.size(); ct++){
            Token token = (Token) constructorTokens.get(ct);
            if(token.getToken().equals("{")){
                break;
            }
            if(token.getToken().equals("[") || token.getToken().equals("]") || 
               token.getToken().equals("(") || token.getToken().equals(")") || token.getToken().equals(".")){
                header = header + token.getToken();
            }else{
                header = header + " " + token.getToken();
            }
        }
        return header;
    }

    public void addToken(Token token){
        constructorTokens.add(token);
    }

    public ArrayList getConstructorTokens() {
        return constructorTokens;
    }

    public ArrayList getStatementsList(){
        return statementsList;
    }
}