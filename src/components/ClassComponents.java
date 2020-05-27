package components;

import java.util.*;

public class ClassComponents {
    public ClassHeader classHeader;
    private ClassConstructor classConstructor;
    private ArrayList tokenList;
    private ArrayList dmList; // data members
    private ArrayList mmList; // method members
    private ArrayList ccList; // constructors
    private String filePath;

    public ClassComponents(ArrayList tokenList, String filePath){
        this.tokenList = tokenList;
        dmList = new ArrayList();
        mmList = new ArrayList();
        ccList = new ArrayList();
        classHeader = new ClassHeader(tokenList);
        this.filePath = filePath;
    }

    public void extractClassComponents(int tokenNum) {

        for( ; tokenNum<this.tokenList.size(); tokenNum++){
            ArrayList tempList = new ArrayList();
            Token token = (Token) this.tokenList.get(tokenNum);

            if(token.getToken().equals("public")    || token.getToken().equals("private") ||
               token.getToken().equals("protected") || token.getToken().equals("static")  || token.getToken().equals("final")){
                tempList.add(token);
                token = (Token) this.tokenList.get(++tokenNum);
            }

            // if this is abstract method then do nothing
            if(token.getToken().equals("abstract")){
                do {
                    token = (Token) this.tokenList.get(++tokenNum);
                } while (!token.getToken().equals(";"));
                continue;
            }

            if(token.getToken().equals("void")){
                tempList.add(token);
                MemberMethod mMethod = new MemberMethod();
                for(int m=0; m<tempList.size(); m++){
                    mMethod.addToken((Token)tempList.get(m));
                }

                ArrayList mList = new ArrayList();
                do {
                    token = (Token) this.tokenList.get(++tokenNum);
                    mMethod.addToken(token);
                    if(token.getToken().equals("{")){    
                        mList.add(token);
                    }else if (token.getToken().equals("}")){
                        mList.remove(mList.size()-1);
                        if(mList.size()==0){
                            break;
                        }
                    }
                } while (true);
                mmList.add(mMethod);
                continue;
            }

            if(token.getDescription().equals("Data Type")){
                tempList.add(token);
                token = (Token) this.tokenList.get(++tokenNum);
                if(token.getDescription().equals("Identifier")){
                    tempList.add(token);
                    token = (Token) this.tokenList.get(++tokenNum);
                    if(token.getToken().equals("(")){ // this is a method
                        tempList.add(token);
                        MemberMethod mMethod = new MemberMethod();
                        for (int m=0; m<tempList.size(); m++){
                            mMethod.addToken((Token)tempList.get(m));
                        }
                        ArrayList mList = new ArrayList();
                        do {
                            token = (Token) this.tokenList.get(++tokenNum);
                            mMethod.addToken(token);
                            if(token.getToken().equals("{")){
                                mList.add(token);
                            }else if (token.getToken().equals("}")){
                                mList.remove(mList.size()-1);
                                if (mList.size()==0){
                                    break;
                                }
                            }
                        } while (true);
                        //System.out.println(mMethod.getMethodName());
                        mmList.add(mMethod);
                        continue;
                    }else{ // this is attribute of the class
                        tempList.add(token);
                        for (int m=0; m<tempList.size(); m++){
                            dmList.add(tempList.get(m));
                        }
                        if(!token.getToken().equals(";")){
                            do {
                                token = (Token)this.tokenList.get(++tokenNum);
                                dmList.add(token);
                            } while (!token.getToken().equals(";"));
                        }
                    }

                }else{
                    tempList.add(token);
                    for (int m=0; m<tempList.size(); m++){
                        dmList.add(tempList.get(m));
                    }
                    do {
                        token = (Token)this.tokenList.get(++tokenNum);
                        dmList.add(token);
                    } while (!token.getToken().equals(";"));
                }
            }

            if(token.getDescription().equals("Identifier")){
                tempList.add(token);
                token = (Token) this.tokenList.get(++tokenNum);
                if(token.getToken().equals("(")){ // constructor
                    tempList.add(token);
                    ClassConstructor cConstructor = new ClassConstructor();
                    for(int m=0; m<tempList.size(); m++){
                        cConstructor.addToken((Token)tempList.get(m));
                    }
                    ArrayList mList = new ArrayList();
                    do {
                        token = (Token) this.tokenList.get(++tokenNum);
                        cConstructor.addToken(token);
                        if(token.getToken().equals("{")){
                            mList.add(token);
                        }else if(token.getToken().equals("}")){
                            mList.remove(mList.size()-1);
                            if(mList.size()==0){
                                break;
                            }
                        }
                    } while (true);
                    ccList.add(cConstructor);
                }else if(token.getDescription().equals("Identifier")){
                    tempList.add(token);
                    token = (Token)this.tokenList.get(++tokenNum);
                    if(token.getToken().equals("(")){
                        tempList.add(token);
                        MemberMethod mMethod = new MemberMethod();
                        for (int m=0; m<tempList.size(); m++){
                            mMethod.addToken((Token)tempList.get(m));
                        }
                        ArrayList mList = new ArrayList();
                        do {
                            token = (Token) this.tokenList.get(++tokenNum);
                            mMethod.addToken(token);
                            if(token.getToken().equals("{")){
                                mList.add(token);
                            }else if(token.getToken().equals("}")){
                                mList.remove(mList.size()-1);
                                if(mList.size()==0){
                                    break;
                                }
                            }
                        } while (true);
                        //System.out.println(mMethod.getMethodName());
                        mmList.add(mMethod);
                    }else if(token.getToken().equals(";")){
                        tempList.add(token);
                        for(int m=0; m<tempList.size(); m++){
                            dmList.add((Token)tempList.get(m));
                        }
                    }else{
                        tempList.add(token);
                        for(int m=0; m<tempList.size(); m++){
                            dmList.add((Token)tempList.get(m));
                        }
                        do {
                            token = (Token)this.tokenList.get(++tokenNum);
                            dmList.add(token);
                        } while (!token.getToken().equals(";"));
                    }

                }else if(token.getToken().equals(";")){
                    tempList.add(token);
                    for(int m=0; m<tempList.size(); m++){
                        dmList.add(tempList.get(m));
                    }
                }
            }
        }
    }


    public ArrayList getTokenList() {
        return this.tokenList;
    }

    public ArrayList getDmList() {
        return this.dmList;
    }

    public ArrayList getMmList() {
        return this.mmList;
    }

    public ArrayList getCcList() {
        return this.ccList;
    }

    public ClassHeader getClassHeader() {
        return this.classHeader;
    }

    public String getFilePath() {
        return this.filePath;
    }

}