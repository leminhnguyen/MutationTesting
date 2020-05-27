package components;

import java.util.*;

public class ClassHeader {

    private int index;
    private ArrayList tokenList;
    private ArrayList importsList;
    private ArrayList headerList;
    private String className;
    private String classParent;

    public ClassHeader(ArrayList tokenList) {
        this.tokenList = tokenList;
        this.index = 0;
        this.importsList = extractImports();
        this.headerList = extractHeader();
        this.className = extractName();
        // this.classParent = extractParent();
    }

    public ArrayList extractImports(){
        ArrayList imports = new ArrayList();
        for( ; index<this.tokenList.size(); index++){
            Token token = (Token) this.tokenList.get(index);
            if(token.getToken().equals("import") || token.getToken().equals("package")){
                imports.add(token);
                do{
                    token = (Token) this.tokenList.get(++index);
                    imports.add(token);
                }while(!token.getToken().equals(";"));
            }else{
                return imports;
            }  
        }
        return imports;
    }

    public ArrayList extractHeader(){
        ArrayList headerList = new ArrayList();
        for ( ; index<this.tokenList.size(); index++){
            Token token = (Token)this.tokenList.get(index);
            if(!token.getToken().equals("{")){
                headerList.add(token);
            }else{
                return headerList;
            }
        }
        return headerList;
    }

    // difference
    public String extractName(){
        String className = "";
        for(int h=0; h<this.headerList.size(); h++){
            Token token = (Token) headerList.get(h);
            if(token.getDescription().equals("Identifier")){
                className = ( (Token) headerList.get(h) ).getToken();
                return className;
            }
        }
        return className;
    }

    public String extractParent(){
        String classParent = "";
        for(int h=0; h<this.headerList.size(); h++){
            Token token = (Token) headerList.get(h);
            if(token.getToken().equals("extends")){
                classParent = ((Token) headerList.get(h+1)).getToken();
                return classParent;
            }
        }
        return classParent;
    }

    public int getIndex(){
        return index;
    }

    public ArrayList getTokenList() {
        return this.tokenList;
    }

    public ArrayList getImportsList() {
        return this.importsList;
    }

    public ArrayList getHeaderList() {
        return this.headerList;
    }

    public String getClassName() {
        return this.className;
    }

    public String getClassParent() {
        return this.classParent;
    }


    
}