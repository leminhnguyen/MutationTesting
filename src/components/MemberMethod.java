package components;

import java.util.*;

public class MemberMethod {
    private ArrayList methodTokens;
    private ArrayList statementsList;
    
    public MemberMethod() {
        methodTokens = new ArrayList();
        statementsList = new ArrayList();
    }

    public String getMethodHeader(){
        String header = "";
        for (int ct=0; ct<methodTokens.size(); ct++){
            Token token = (Token) methodTokens.get(ct);
            if(token.getToken().equals("{")){
                break;
            }
            if(token.getToken().equals("[") || token.getToken().equals("]") ||
               token.getToken().equals("(") || token.getToken().equals(".") ||
               token.getToken().equals(",")){
                header = header + token.getToken();
            }else if(token.getToken().equals(")")){
                if (header.endsWith("(")){
                    header = header + token.getToken();
                }else{
                    header = header + " " + token.getToken();
                }
            }else{
                header = header + " " + token.getToken();
            }
        }
        return header;
    }

    public int getMethodHeaderCount() {
        int count = 0;
        for ( ; count < methodTokens.size(); ){
            Token token = (Token) methodTokens.get(count++);
            if (token.getToken().equals("{")){
                break;
            }
        }
        return count;
    }

    public String getAnnotatedMethodHeader(){
        String header = "";
        for (int ct=0; ct<methodTokens.size(); ct++){
            Token token = (Token)methodTokens.get(ct);
            if(token.getToken().equals("{")){
                break;
            }
            if (token.getToken().equals("[") || token.getToken().equals("]") ||
                token.getToken().equals(")") || token.getToken().equals(".")){
                    header = header + token.getToken();
            } else if (token.getToken().equals("(")){
                header = header + "_T" + token.getToken();
            } else{
                header = header + " " + token.getToken();
            }
        }
        return header;
    }

    public void identifyStatements() {
        int ct=0;
        DO1: do {
            Token token = (Token) methodTokens.get(ct++);
            if(token.getToken().equals("{")){
                break DO1;
            }
        } while (ct < methodTokens.size());

        for ( ; ct<methodTokens.size()-1; ct++){
            Token token = (Token) methodTokens.get(ct);
            if(token.getToken().equals("if")){
                Statement statement = new Statement();
                do {
                    statement.addToken(token);
                    token = (Token) methodTokens.get(++ct);
                } while (!token.getToken().equals("{"));
                ct--;
                statement.setDescription("If Statement");
                statementsList.add(statement);
            }else if (token.getToken().equals("else")){
                Statement statement = new Statement();
                statement.addToken(token);
                statement.setDescription("Else Statement");
                statementsList.add(statement);
            }else if (token.getToken().equals("while")){
                Statement statement = new Statement();
                do {
                    statement.addToken(token);
                    token = (Token) methodTokens.get(++ct);
                } while (!token.getToken().equals("{"));
                ct--;
                statement.setDescription("While Statement");
                statementsList.add(statement);
            }else if (token.getToken().equals("this")){
                Statement statement = new Statement();
                do {
                    statement.addToken(token);
                    token = (Token) methodTokens.get(++ct);
                } while (!token.getToken().equals(";") );
                if( ( (Token)statement.getStatementTokens().get(3) ).getToken().equals("(") ){
                    statement.setDescription("Function Call");
                }else{
                    statement.setDescription("Data Member Initialization");
                }
                statementsList.add(statement);
            }else if (token.getDescription().equals("Data Type")){
                Statement statement = new Statement();
                do{
                    statement.addToken(token);
                    token = (Token)methodTokens.get(++ct);
                }while(!token.getToken().equals(";"));
                statement.addToken(token);
                statement.setDescription("Local Declaration");
                statementsList.add(statement);
            }else if (token.getDescription().equals("Identifier")){
                Statement statement = new Statement();
                System.out.println( "Token: " + token.getToken() );
                do {
                    statement.addToken(token);
                    token = (Token) methodTokens.get(++ct);
                    System.out.println( "Token: " + token.getToken() );
                } while (!token.getToken().equals(";"));
                statement.addToken(token);
                if( ((Token)statement.getStatementTokens().get(2)).getToken().equals("new") ){
                    statement.setDescription("Object Creation");
                }else if ( ( ( Token )statement.getStatementTokens().get( 3 )).getToken().equals("new")){
                    statement.setDescription("Object Creation");
                } else if( ( ( Token )statement.getStatementTokens().get( 1 ) ).getToken().equals( "(" ) ) {
                    statement.setDescription( "Function Call" );
                } else if( ( ( Token )statement.getStatementTokens().get( 3 ) ).getToken().equals( "(" ) ) {
                    statement.setDescription( "Function Call" );
                } else if( ( ( Token )statement.getStatementTokens().get( 3 ) ).getToken().equals( "=" ) ) {
                    statement.setDescription( "Data Member Initialization" );
                } else {
                    statement.setDescription("Assignment Statement");
                }
                statementsList.add(statement);
            }else if (token.getToken().equals("return")){
                Statement statement = new Statement();
                do {
                    statement.addToken(token);
                    token = (Token)methodTokens.get(++ct);
                } while (!token.getToken().equals(";"));
                statement.setDescription("Return Statement");
                statementsList.add(statement);
            }else if (token.getToken().equals("{")){
                Statement statement = new Statement();
                statement.setDescription("Starting Delemeter");
                statement.addToken(token);
                statementsList.add(statement);
            }else if (token.getToken().equals("}")){
                Statement statement = new Statement();
                statement.setDescription("Ending Statement");
                statement.addToken(token);
                statementsList.add(statement);
            }
        }
    }

    public String getMethodName() {
        String methodName = "";
        for (int mt=0; mt<methodTokens.size(); mt++){
            Token token = (Token) methodTokens.get(mt);
            if (token.getDescription().equals("Identifier")){
                Token nextToken = (Token)methodTokens.get(mt+1);
                if (nextToken.getToken().equals("(")){
                    methodName = token.getToken();
                    break;
                } 
            }
        }
        return methodName;
    }

    public void addToken(Token token) {
        methodTokens.add(token);
    }

    public ArrayList getMethodTokens() {
        return methodTokens;
    }

    public void printStatements(){
        for (int s=0; s<statementsList.size(); s++){
            Statement statement = (Statement) statementsList.get(s);
            System.out.println(statement.getDescription());;
            ArrayList tokens = statement.getStatementTokens();
            for (int t=0; t<tokens.size(); t++){
                System.out.print( ((Token)tokens.get(t)).getToken() + " " );
            }
            System.out.println();
        }
    }

}