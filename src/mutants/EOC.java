package mutants;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import components.ClassComponents;
import components.MemberMethod;
import components.Target;
import components.Token;
import utils.EMConstants;
import utils.*;

public class EOC extends MutantGenerating{
    
    public EOC(ClassComponents classComponents) {
        super(classComponents);
    }

    public void generateMutants() {
        ArrayList methods = classComponents.getMmList();
        int mutantNumber = 0;
        for (int m=0; m<methods.size(); m++){
            MemberMethod mMethod = (MemberMethod) methods.get(m);
            String methodName = mMethod.getMethodName();
            if (methodName.startsWith("toString") || methodName.startsWith("set") || methodName.startsWith("get")) {
                continue;
            }
            ArrayList code = super.getCodeExceptMutatedMethod(m);
            String codeBefore = (String) code.get(0);
            String codeAfter = (String) code.get(1);

            ArrayList mmTokens = mMethod.getMethodTokens();
            for(int mmt=0; mmt<mmTokens.size(); mmt++){
                Token token = (Token) mmTokens.get(mmt);
                System.out.println(token.getToken());
                if(token.getToken().equals("==") || token.getToken().equals("equals")){
                    String mCode = ""; int indentCount = 1; boolean mutated = false;
                    for (int mmt2 = 0; mmt2 < mmTokens.size(); mmt2++) {
                        Token token2 = (Token) mmTokens.get(mmt2);
                        System.out.println(token2.getDescription());
                        if(mmt2 == mmt){
                            if(token2.getToken().equals("==")){
                                if(mCode.endsWith(" ")){
                                    mCode = mCode.substring(0, mCode.length()-1) + ".equals( ";
                                }else{
                                    mCode += ".equals( ";
                                }
                                
                                token2 = (Token) mmTokens.get(++mmt2);
                                System.out.println("inside: " + token2.getToken());
                                while(!token2.getToken().equals(")")){
                                    mCode += token2.getToken();
                                    token2 = (Token)mmTokens.get(++mmt2);
                                }
                                mCode += " ) )";
                            }else{
                                mCode = mCode.substring(0,mCode.length()-1) + "==";
                                mmt2 += 2;
                                mCode += ((Token)mmTokens.get(mmt2)).getToken();
                                mmt2 += 2;
                                mCode += " )";
                            }
                            mutated = true;
                        }else{
                            Token prevToken;
                            if (mmt2 > 0) {
                                prevToken = (Token) mmTokens.get(mmt2 - 1);
                            } else {
                                prevToken = null;
                            }

                            if(token2.getDescription().equals("Relational Operator")){
                                if(mCode.endsWith(" ")){
                                    mCode = mCode.substring(0, mCode.length()-1) + token2.getToken();
                                }else{
                                    mCode += token2.getToken();
                                }
                            }else if (token2.getDescription().equals("Keyword")) {
                                if (prevToken == null) {
                                    mCode = mCode + addIndentation(token2, 4 * indentCount) + " ";
                                } else if (token2.getToken().equals("if") || token2.getToken().equals("else") || token2.getToken().equals("return")) {
                                    mCode = mCode + addIndentation(token2, 4 * indentCount) + " ";
                                } else {
                                    mCode = mCode + token2.getToken() + " ";
                                }
                            } else if (token2.getToken().equals(";") || token2.getToken().equals("{")) {
                                if (token2.getToken().equals("{")) {
                                    indentCount++;
                                    if(mutated){
                                        mCode = mCode + token2.getToken() + "//mutated with EOC" + "\n";
                                        mutated = false;
                                    }else{
                                        mCode = mCode + token2.getToken() + "\n";
                                    }
                                } else {
                                    mCode = mCode.substring(0, mCode.length() - 1) + token2.getToken() + "\n";
                                }
                            } else if(token2.getToken().equals(".")) {
                                if(mCode.endsWith(" ")){
                                    mCode = mCode.substring(0, mCode.length()-1) + token2.getToken();
                                }else{
                                    mCode += token2.getToken();
                                }
                            }else if (token2.getToken().equals("(") || token2.getToken().equals( "," )){
                                if(mCode.endsWith(" ")){
                                    mCode = mCode.substring(0, mCode.length()-1) + token2.getToken() + " ";
                                }else{
                                    mCode += token2.getToken() + " ";
                                }
                            }else if (token2.getToken().equals(")")){
                                if(mCode.endsWith("( ")){
                                    mCode = mCode.substring(0, mCode.length()-1) + token2.getToken() + " ";
                                }else{
                                    mCode += token2.getToken() + " ";
                                }

                            }else if (token2.getToken().equals("}")) {
                                indentCount--;
                                mCode = mCode + addIndentation(token2, 4 * indentCount) + "\n";
                            } else {
                                if (prevToken.getToken().equals(";") || prevToken.getToken().equals("{") || prevToken.getToken().equals("}")) {
                                    mCode = mCode + addIndentation(token2, 4 * indentCount) + " ";
                                } else {
                                    mCode = mCode + token2.getToken() + " ";
                                }
                            }
                        }
                    }
                    mCode += "\n";
                    mutantNumber++;
                    Target target = new Target();
                    target.setMutationOperator("EOC");
                    target.setMutantNumber(mutantNumber);
                    target.setClassName(classComponents.getClassHeader().getClassName());
                    EMConstants.TARGETS.add(target);
                    EMConstants.TOTAL_MUTANTS++;
                    File file = new File(EMConstants.PROJECT_LOCATION + "/assets/mutants/EOC/" + mutantNumber);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    try {
                        RandomAccessFile raf = new RandomAccessFile(
                                        EMConstants.PROJECT_LOCATION + "/assets/mutants/EOC/" + mutantNumber + "/"
                                        + classComponents.getClassHeader().getClassName() + ".java", "rw");
                        String fullCode = "//" + classComponents.getClassHeader().getClassName() + "." + methodName + "()\n" + 
                                            super.getCodeBeforeMethod() + codeBefore + mCode + codeAfter + "}";
                        raf.writeBytes(fullCode);
                    } catch (Exception e) {
                       
                    }
                }  
            }
        }
    }

    public static void main(String[] args) {
        String filePath="Programs/LinearStack/Stack.java";
        EMScanner scanner = new EMScanner(new File(filePath));
        ClassComponents cc = new ClassComponents(scanner.getTokenList(), filePath);
        cc.extractClassComponents(0);
        EOC eoc = new EOC(cc);
        eoc.generateMutants();
    }
}