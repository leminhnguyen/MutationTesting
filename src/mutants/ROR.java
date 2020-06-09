package mutants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import components.*;
import java.io.*;
import utils.*;
import algorithm.*;

public class ROR extends MutantGenerating {

    public static ArrayList<String> MUTANT_OPERATORS = new ArrayList<>(Arrays.asList("<", "<=", ">", ">=", "==", "!="));

    public ROR(ClassComponents classComponents) {
        super(classComponents);
    }

    public ArrayList generateMutants() {
        ArrayList methods = classComponents.getMmList();
        int mutantNumber = 0;
        for (int m = 0; m < methods.size(); m++) {
            MemberMethod mMethod = (MemberMethod) methods.get(m);
            String methodName = mMethod.getMethodName();
            System.out.println(methodName);
            if (methodName.startsWith("toString") || methodName.startsWith("set") || methodName.startsWith("get")) {
                continue;
            }
            ArrayList code = super.getCodeExceptMutatedMethod(m);
            String codeBefore = (String) code.get(0);
            String codeAfter = (String) code.get(1);

            ArrayList mmTokens = mMethod.getMethodTokens();
            for (int mmt = 0; mmt < mmTokens.size(); mmt++) {
                Token token = (Token) mmTokens.get(mmt);
                if (token.getDescription().equals("Relational Operator")) {
                    ArrayList remains = getRemainedMO(token.getToken());
                    for (int rt = 0; rt < remains.size(); rt++) {
                        String mCode = ""; int indentCount = 1; boolean mutated = false;
                        for (int mmt2 = 0; mmt2 < mmTokens.size(); mmt2++) {
                            Token token2 = (Token) mmTokens.get(mmt2);
                            if (mmt2 == mmt) {
                                if(mCode.endsWith(" ")){
                                    mCode = mCode.substring(0, mCode.length()-1) + remains.get(rt);
                                }else{
                                    mCode = mCode + remains.get(rt);
                                }
                                mutated = true;
                            } else {
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
                                            mCode = mCode + token2.getToken() + "//mutated with ROR" + "\n";
                                            mutated = false;
                                        }else{
                                            mCode = mCode + token2.getToken() + "\n";
                                        }
                                    } else {
                                        mCode = mCode.substring(0, mCode.length() - 1) + token2.getToken() + "\n";
                                    }
                                } else if (token2.getToken().equals("(") || token2.getToken().equals( "," )){
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
                                    if (prevToken.getToken().equals(";") || prevToken.getToken().equals("{")
                                            || prevToken.getToken().equals("}")) {
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
                        target.setMutationOperator("ROR");
                        target.setMutantNumber(mutantNumber);
                        target.setClassName(classComponents.getClassHeader().getClassName());
                        EMConstants.TARGETS.add(target);
                        EMConstants.TOTAL_MUTANTS++;
                        File file = new File(EMConstants.PROJECT_LOCATION + "/assets/mutants/ROR/" + mutantNumber);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        try {
                            RandomAccessFile raf = new RandomAccessFile(
                                            EMConstants.PROJECT_LOCATION + "/assets/mutants/ROR/" + mutantNumber + "/"
                                            + classComponents.getClassHeader().getClassName() + ".java", "rw");
                            String fullCode = "//" + classComponents.getClassHeader().getClassName() + "." + methodName + "()\n" + 
                                                super.getCodeBeforeMethod() + codeBefore + mCode + codeAfter + "}";
                            raf.writeBytes(fullCode);
                        } catch (Exception e) {
                            ;
                        }
                    }
                }
            }
        }
        return null;
    }

    private ArrayList getRemainedMO(String token){
        ArrayList mutantOperators = ROR.MUTANT_OPERATORS;
        for(int i=0; i<mutantOperators.size(); i++){
            if( token.equals( "<" ) || token.equals( "<=" ) ) {
                mutantOperators.remove( "<" );
                mutantOperators.remove( "<=" );
            } else if( token.equals( ">" ) || token.equals( ">=" ) ) {
                mutantOperators.remove( ">" );
                mutantOperators.remove( ">=" );            
            } else if( token.equals( "==" ) ) {
                mutantOperators.remove( "==" );
            } else if( token.equals( "!=" ) ) {
                mutantOperators.remove( "!=" );
            }
        }
        return mutantOperators;
    }

    public static void main(String[] args){
        // String filePath="Programs/LinearStack/Stack.java";
        // EMScanner scanner = new EMScanner(new File(filePath));
        // ClassComponents cc = new ClassComponents(scanner.getTokenList(), filePath);
        // cc.extractClassComponents(0);
        // MutantGenerating mg = new MutantGenerating(cc);
        // ROR ror = new ROR(cc);
        // System.out.println(ror.generateMutants());
        //Genetic genetic = new Genetic(cc);
        // for (int t=0; t<EMConstants.TARGETS.size(); t++){
        //     Target target = (Target) EMConstants.TARGETS.get(t);
        //     System.out.println("------------------ Execute Target " + t + " ------------------------");
        //     genetic.generatePopulation(target);
        //     try {
        //         genetic.executeTestCase(target); 
        //         System.out.println("===>> run successfully\n");
        //     } catch (Exception e) {
        //         System.out.println("===>> Failed\n");
        //     }
        // }
        String str = "hello";
        System.out.println(str=="hello");
    }
}