package mutants;

import components.*;
import utils.EMScanner;

import java.io.*;
import java.util.*;

public class MutantGenerating {

    protected ClassComponents classComponents;

    public MutantGenerating(){

    }

    public MutantGenerating(ClassComponents classComponents){
        this.classComponents = classComponents;
    }

    // get code before mutation operator
    public String getCodeBeforeMethod() {
        String code = "";

        // get class imports
        ArrayList classImports = classComponents.classHeader.getImportsList();
        for (int i=0; i<classImports.size(); i++){
            Token token = (Token)classImports.get(i);
            if(token.getToken().equals(";")){
                code = code + token.getToken() + "\n";
            }else if(token.getToken().equals(".")){
                code = code.substring(0, code.length()-1) + token.getToken();
            }else{
                code = code + token.getToken() + " ";
            }
        }

        // get class headers
        ArrayList classHeaders = classComponents.classHeader.getHeaderList();
        for (int i=0; i<classHeaders.size(); i++){
            Token token = (Token) classHeaders.get(i);
            code = code + token.getToken() + " ";
        }
        code += "{\n\n";

        // data members
        ArrayList dataMembers = classComponents.getDmList();
        for(int i=0; i<dataMembers.size(); i++){
            Token prevToken = null;
            Token token = (Token) dataMembers.get(i);
            if(i>0){
                prevToken = (Token)dataMembers.get(i-1);
            }

            if(token.getToken().equals(";")){
                code = code + token.getToken() + "\n";
            }else{
                if(i==0 || prevToken.getToken().equals(";")){
                    code = code + addIndentation(token, 4) + " ";
                }else{
                    code = code + token.getToken() + " ";
                }
            }
        }
        code = code + "\n";

        // get class constructor
        ArrayList cConstructorList = classComponents.getCcList();
        for (int c=0; c<cConstructorList.size(); c++){
            ClassConstructor cConstructor = (ClassConstructor) cConstructorList.get(c);
            ArrayList cTokens = cConstructor.getConstructorTokens();
            Token prevToken; int indentCount=1;
            for (int i=0; i<cTokens.size(); i++){
                Token token = (Token)cTokens.get(i);
                if(i>0){
                    prevToken = (Token)cTokens.get(i-1);
                }else{
                    prevToken = null;
                }

                if(token.getDescription().equals("Keyword")){
                    code = code + addIndentation(token, 4*indentCount) + " ";
                }else if(token.getToken().equals(";") || token.getToken().equals("{")){
                    if (token.getToken().equals("{")){
                        code = code + token.getToken() + "\n";
                        indentCount++;
                    }else{
                        code = code.substring(0, code.length()-1) + token.getToken() + "\n";
                    }
                }else if(token.getToken().equals("}")){
                    indentCount--;
                    code = code + addIndentation(token, 4*indentCount) + "\n";
                }else{
                    if(prevToken.getToken().equals(";") || prevToken.getToken().equals("{")){
                        code = code + addIndentation(token, 4*indentCount) + " ";
                    }else{
                        code = code + token.getToken() + " ";
                    }
                }
            }
            code = code + "\n";
        }
        
        return code;
    }

    public ArrayList getCodeExceptMutatedMethod(int mNumber){
        ArrayList code = new ArrayList();
        ArrayList mmList = classComponents.getMmList();
        String codeBefore=""; String codeAfter="";
        Token prevToken;
        for (int mm=0; mm<mmList.size(); mm++){
            MemberMethod mMethod = ( MemberMethod )mmList.get( mm );
            ArrayList mmTokens = mMethod.getMethodTokens();
            int indentCount = 1;
            if(mm<mNumber){
                for( int mmt=0; mmt<mmTokens.size(); mmt++ ) {
                    Token token = ( Token )mmTokens.get(mmt);
                    if(mmt>0){
                        prevToken = (Token)mmTokens.get(mmt-1);
                    }else{
                        prevToken = null;
                    }
                    
                    if(token.getDescription().equals("Relational Operator")){
                        if(codeBefore.endsWith(" ")){
                            codeBefore = codeBefore.substring(0, codeBefore.length()-1) + token.getToken();
                        }else{
                            codeBefore += token.getToken();
                        }
                    }else if(token.getDescription().equals("Keyword")){
                        if (prevToken==null){
                            codeBefore = codeBefore + addIndentation(token, 4*indentCount) + " ";
                        }else if(token.getToken().equals("if") || token.getToken().equals("else") || token.getToken().equals("return")){
                            codeBefore = codeBefore + addIndentation(token, 4*indentCount) + " ";
                        }else{
                            codeBefore = codeBefore + token.getToken() + " " ;
                        }
                    }else if(token.getToken().equals(";") || token.getToken().equals("{")){
                        if(token.getToken().equals("{")){
                            indentCount++;
                            codeBefore = codeBefore + token.getToken() + "\n";
                        }else{
                            codeBefore = codeBefore.substring(0, codeBefore.length()-1) + token.getToken() + "\n";
                        }
                    } else if(token.getToken().equals(".")) {
                        if(codeBefore.endsWith(" ")){
                            codeBefore = codeBefore.substring(0, codeBefore.length()-1) + token.getToken();
                        }else{
                            codeBefore += token.getToken();
                        }
                    }else if (token.getToken().equals("(") || token.getToken().equals( "," )){
                        if(codeBefore.endsWith(" ")){
                            codeBefore = codeBefore.substring(0, codeBefore.length()-1) + token.getToken() + " ";
                        }else{
                            codeBefore += token.getToken() + " ";
                        }
                    }else if (token.getToken().equals(")")){
                        if(codeBefore.endsWith("( ")){
                            codeBefore = codeBefore.substring(0, codeBefore.length()-1) + token.getToken() + " ";
                        }else{
                            codeBefore += token.getToken() + " ";
                        }
                    }else if(token.getToken().equals("}")){
                        indentCount--;
                        codeBefore = codeBefore + addIndentation(token, 4*indentCount) + "\n";
                    }else{
                        if(prevToken.getToken().equals(";") || prevToken.getToken().equals("{") || prevToken.getToken().equals("}")){
                            codeBefore = codeBefore + addIndentation(token, 4*indentCount) + " ";
                        }else{
                            codeBefore = codeBefore + token.getToken() + " ";
                        }
                    }
                }
                codeBefore = codeBefore + "\n";
            }else if(mm==mNumber){
                continue;
            }else{
                indentCount = 1;
                for( int mmt=0; mmt<mmTokens.size(); mmt++ ) {
                Token token = ( Token )mmTokens.get(mmt);
                if(mmt>0){
                    prevToken = (Token)mmTokens.get(mmt-1);
                }else{
                    prevToken = null;
                }

                if(token.getDescription().equals("Relational Operator")){
                    if(codeAfter.endsWith(" ")){
                        codeAfter = codeAfter.substring(0, codeAfter.length()-1) + token.getToken();
                    }else{
                        codeAfter += token.getToken();
                    }
                }else if(token.getDescription().equals("Keyword")){
                    if (prevToken==null){
                        codeAfter = codeAfter + addIndentation(token, 4*indentCount) + " ";
                    }else if(token.getToken().equals("if") || token.getToken().equals("else") || token.getToken().equals("return")){
                        codeAfter = codeAfter + addIndentation(token, 4*indentCount) + " ";
                    }else{
                        codeAfter = codeAfter + token.getToken() + " " ;
                    }
                }else if(token.getToken().equals(";") || token.getToken().equals("{")){
                    if(token.getToken().equals("{")){
                        indentCount++;
                        codeAfter = codeAfter + token.getToken() + "\n";
                    }else{
                        codeAfter = codeAfter.substring(0, codeAfter.length()-1) + token.getToken() + "\n";
                    }
                } else if(token.getToken().equals(".")) {
                    if(codeAfter.endsWith(" ")){
                        codeAfter = codeAfter.substring(0, codeAfter.length()-1) + token.getToken();
                    }else{
                        codeAfter += token.getToken();
                    }
                }else if (token.getToken().equals("(") || token.getToken().equals( "," )){
                    if(codeAfter.endsWith(" ")){
                        codeAfter = codeAfter.substring(0, codeAfter.length()-1) + token.getToken() + " ";
                    }else{
                        codeAfter += token.getToken() + " ";
                    }
                }else if (token.getToken().equals(")")){
                    if(codeAfter.endsWith("( ")){
                        codeAfter = codeAfter.substring(0, codeAfter.length()-1) + token.getToken() + " ";
                    }else{
                        codeAfter += token.getToken() + " ";
                    }
                }
                
                else if(token.getToken().equals("}")){
                    indentCount--;
                    codeAfter = codeAfter + addIndentation(token, 4*indentCount) + "\n";
                }else{
                    if(prevToken.getToken().equals(";") || prevToken.getToken().equals("{") || prevToken.getToken().equals("}")){
                        codeAfter = codeAfter + addIndentation(token, 4*indentCount) + " ";
                    }else{
                        codeAfter = codeAfter + token.getToken() + " ";
                    }
                }
            }
                codeAfter = codeAfter + "\n";
            }
        }

        code.add(codeBefore);
        code.add(codeAfter);
        return code;
    }
        
    public String addIndentation(Token token, int num){
        String code = token.getToken();
        for(int i=0; i<num; i++){
            code = " " + code;
        }
        return code;
    }

    public static void main(String[] args) {
        EMScanner scanner = new EMScanner(new File("src/components/Token.java"));
        ClassComponents cc = new ClassComponents(scanner.getTokenList(), "src/components/Token.java");
        //scanner.printTokens();
        cc.extractClassComponents(0);
        MutantGenerating mg = new MutantGenerating(cc);
        System.out.println(mg.getCodeBeforeMethod());
        
    }
}