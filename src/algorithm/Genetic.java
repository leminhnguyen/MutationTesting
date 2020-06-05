package algorithm;

import java.io.*;
import java.util.*;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import components.*;
import utils.EMConstants;
import utils.EMController;

public class Genetic {

    private ArrayList selectedPopulation;
    public static int TS_K = 5;     //This 'K' is for Tournament Selection
    public Target gaTarget;
    private ArrayList population;
    private int traceCount, iterationNumber, traceNumber;
    private ClassComponents classComponents;

    public Genetic(ClassComponents classComponents){
        population = new ArrayList();
        selectedPopulation = new ArrayList();
        traceCount = 1;
        iterationNumber = 0;
        this.classComponents = classComponents;
    }

    public void gaTarget(Target gaTarget) {
        this.gaTarget = gaTarget;
    }

    public void performTournamentSelection() {
        int currentPopulation = this.population.size();
        int twentyPercent = ( int )( currentPopulation * EMConstants.OLD_POPULATION_RATE );
        selectedPopulation.clear();
        
        // sort the the current population descendant
        for( int l=0; l<this.population.size(); l++ ) {
            for( int k=l+1; k<this.population.size(); k++ ) {
                TestCase testCase = ( TestCase )this.population.get( l );  
                TestCase testCase2 = ( TestCase )this.population.get( k );
                if( testCase.getWeight()<testCase2.getWeight() ) {
                    this.population.remove( k );
                    this.population.remove( l );
                    this.population.add( l, testCase2 );
                    this.population.add( k, testCase );
                }
            }
        }

        // select 20% best testCases in the population
        for( int t=0; t<twentyPercent; t++ ) {
            TestCase testCase = ( TestCase )this.population.get( 0 );
            this.population.remove( 0 );
            selectedPopulation.add( testCase );
        }
        
        ArrayList tournamentPopulation = new ArrayList();
        while( selectedPopulation.size() < currentPopulation ) {
            tournamentPopulation.clear();
            int ts_k = 1;
            //Pick k number of Chromosomes from Population
            while( ts_k <= Genetic.TS_K ) {
                int random = ( int )( Math.random()*this.population.size() );
                TestCase testCase = ( TestCase )this.population.get( random );
                tournamentPopulation.add( testCase );
                ts_k++;
            }
            
            //Find best Chromosome from k number of samples
            TestCase bestCase = ( TestCase )tournamentPopulation.get( 0 );
            for( int t=1; t<tournamentPopulation.size(); t++ ) {
                TestCase bCase = ( TestCase )tournamentPopulation.get( t );
                if( bCase.getWeight()>=bestCase.getWeight() ) {
                    bestCase = bCase;
                }
            }
            TestCase theBest = new TestCase( bestCase );
            selectedPopulation.add( theBest );
        }
        
        ArrayList newList = ( ArrayList )selectedPopulation.clone();
        int i=0, j=newList.size()-1;
        for( int n=0; n<twentyPercent; n++ ) {
            TestCase tCase = ( TestCase )newList.get( n );
            selectedPopulation.set( i++, tCase );
        }
        
        for( int n=twentyPercent; n<newList.size(); n++ ) {
            TestCase tCase = ( TestCase )newList.get( n );
            if( tCase.getStateFitness()==0.0 ) {
                selectedPopulation.set( i++, tCase );
            } else {
                selectedPopulation.set( j--, tCase );
            }
        }
    }
    
    public void crossoverPopulation() {
        for( int sp=0,sp2=(selectedPopulation.size()-1); sp<=sp2; sp++, sp2-- ) {
            TestCase testCase1 = ( TestCase )selectedPopulation.get( sp );
            TestCase testCase2 = ( TestCase )selectedPopulation.get( sp2 );
            
            if( testCase1.getTestCase().equals( testCase2.getTestCase() ) ) {
                int random = ( int )( selectedPopulation.size() * Math.random() );
                testCase2 = ( TestCase )selectedPopulation.get( random );
            }
                       
            StringTokenizer tk1 = new StringTokenizer( testCase1.getTestCase(), ";" );
            StringTokenizer tk2 = new StringTokenizer( testCase2.getTestCase(), ";" );
            String firstToken = tk1.nextToken();
            tk2.nextToken();
            ArrayList tl1 = new ArrayList();
            while( tk1.hasMoreTokens() ) {
                tl1.add( tk1.nextToken() );
            }
            String lastToken1 = ( String )tl1.get( tl1.size()-1 );
            tl1.remove( tl1.size()-1 );
            ArrayList tl2 = new ArrayList();
            while( tk2.hasMoreTokens() ) {
                tl2.add( tk2.nextToken() );
            }
            String lastToken2 = ( String )tl2.get( tl2.size()-1 );
            tl2.remove( tl2.size()-1 );
            
            int minimum = tl1.size();
            if( tl1.size()>tl2.size() ) {
                minimum = tl2.size();
            }
            int crossoverPoint = ( int )( minimum * Math.random() );
            
            String newTestCase1 = firstToken + ";";
            for( int i1=0; i1<crossoverPoint; i1++ ) {
                String s1 = ( String )tl1.get( i1 );
                newTestCase1 += s1 + ";";
            }
            for( int i1=crossoverPoint; i1<tl2.size(); i1++ ) {
                String s1 = ( String )tl2.get( i1 );
                newTestCase1 += s1 + ";";
            }
            newTestCase1 += lastToken2 + ";";
            
            String newTestCase2 = firstToken + ";";
            for( int i1=0; i1<crossoverPoint; i1++ ) {
                String s1 = ( String )tl2.get( i1 );
                newTestCase2 += s1 + ";";
            }
            for( int i1=crossoverPoint; i1<tl1.size(); i1++ ) {
                String s1 = ( String )tl1.get( i1 );
                newTestCase2 += s1 + ";";
            }
            newTestCase2 += lastToken1 + ";";
            
            testCase1.setTestCase( newTestCase1 );
            testCase2.setTestCase( newTestCase2 );
        }
        
        this.population.clear();
        for( int sp=0; sp<selectedPopulation.size(); sp++ ) {
            this.population.add( ( TestCase )selectedPopulation.get( sp ) );
        }
        selectedPopulation.clear();
    }

    public void mutatePopulation() {
        int twentyPercent = ( int )( EMConstants.POPULATION_SIZE * EMConstants.OLD_POPULATION_RATE );
        
        // get best TestCases
        for( int l=0; l<this.population.size(); l++ ) {
            for( int k=l+1; k<this.population.size(); k++ ) {
                TestCase testCase = ( TestCase )this.population.get( l );  
                TestCase testCase2 = ( TestCase )this.population.get( k );
                if( testCase.getWeight()<testCase2.getWeight() ) {
                    this.population.remove( k );
                    this.population.remove( l );
                    this.population.add( l, testCase2 );
                    this.population.add( k, testCase );
                }
            }
        }
        
        ArrayList tempPopulation = ( ArrayList )this.population.clone();
        this.population.clear();
        for( int tp=0; tp<twentyPercent ; tp++ ) {
            TestCase testCase = ( TestCase )tempPopulation.get( tp );
            population.add( testCase );
        }
        
        for( int tp=twentyPercent; ( tp<tempPopulation.size() && this.population.size()<EMConstants.POPULATION_SIZE ) ; ) {
            TestCase testCase = ( TestCase )tempPopulation.get( tp );
            tempPopulation.remove( testCase );
            // if fitness == 0 then we just need to mutate the parameters
            if( testCase.getStateFitness()==0.0 ) {
                ArrayList newTest = new ArrayList();
                StringTokenizer tokenizer1 = new StringTokenizer( testCase.getTestCase(), " " );
                while( tokenizer1.hasMoreTokens() ) {
                    String token = tokenizer1.nextToken();
                    if( !token.equals( " " ) ) {
                        newTest.add( token );
                    }
                }
                newTest.remove( newTest.size()-1 );
                String solution = "";
                for( int i=0; i<newTest.size(); i++ ) {
                    solution += ( String )newTest.get( i ) + " ";
                }
                String objectName = "";
                ClassComponents cComponents = classComponents;
                objectName = testCase.getObjectName();
                String methodUnderTest = testCase.getMethod();
                ArrayList mmList = cComponents.getMmList();
                FOR: for( int mm=0; mm<mmList.size(); mm++ ) {
                    MemberMethod mMethod = ( MemberMethod )mmList.get( mm );
                    if( mMethod.getMethodName().equals( methodUnderTest ) ) {
                        solution += objectName + "." + mMethod.getMethodName() + "(";
                        ArrayList mTokens = mMethod.getMethodTokens();
                        int mt = 0;
                        Token token = null;
                        do {
                            token = ( Token )mTokens.get( mt++ );
                        } while( !token.getToken().equals( "(" ) );
                        token = ( Token )mTokens.get( mt++ );
                        while( !token.getToken().equals( ")" ) ) {
                            if( token.getToken().equals( "byte" ) || token.getToken().equals( "short" ) ) {
                                int value = ( int )( Math.random() * EMConstants.INT_RANGE );
                                solution += value;
                            } else if( token.getToken().equals( "int" ) || token.getToken().equals( "long" ) ) {
                                int value = ( int )( Math.random() * EMConstants.INT_RANGE );
                                int signRandom = ( int )( Math.random() * 100 );
                                if( signRandom%5==0 ) {
                                    value = value * -1;
                                }
                                solution += value;
                            } else if( token.getToken().equals( "float" ) ) {
                                int value1 = ( int )( Math.random() * EMConstants.INT_RANGE );
                                int value2 = ( int )( Math.random() * 10 );
                                solution += value1 + "." + value2 + "f";
                            } else if( token.getToken().equals( "double" ) ) {
                                int value1 = ( int )( Math.random() * EMConstants.INT_RANGE );
                                int value2 = ( int )( Math.random() * 10 );
                                int signRandom = ( int )( Math.random() * 100 );
                                if( signRandom%5==0 ) {
                                    value1 = value1 * -1;
                                }
                                solution += value1 + "." + value2;
                            } else if( token.getToken().equals( "char" ) ) {
                                int value = ( int )( Math.random() * 26 );
                                int tvalue = ( int )( Math.random() * 2 );
                                if( tvalue==0 ) {
                                    value += 65;
                                } else {
                                    value += 97;
                                }
                                char ch = ( char )value;
                                solution += "\'" + ch + "\'";
                            } else if( token.getToken().equals( "String" ) ) {
                                int len = ( int )( Math.random() * 10 );
                                String value = "\"";
                                for( int l=0; l<len; l++ ) {
                                    int ivalue = ( int )( Math.random() * 26 );
                                    int tvalue = ( int )( Math.random() * 3 );
                                    if( tvalue==0 ) {
                                        ivalue += 65;
                                    } else if( tvalue==1 ) {
                                        ivalue += 97;
                                    } else {
                                        ivalue += 48;
                                    }
                                    char ch = ( char )ivalue;
                                    value += ch;
                                }
                                value += "\"";
                                solution += value;
                            }
                            mt++;
                            token = ( Token )mTokens.get( mt++ );
                            if( token.getToken().equals( "," ) ) {
                                solution += ",";
                                token = ( Token )mTokens.get( mt++ );
                            }
                        }
                        solution += ");";
                        break FOR;
                    }
                }

                testCase.setTestCase( solution );
                this.population.add( testCase );
            } else {
                String className = testCase.getClassName();
                String objectName = testCase.getObjectName();
                String solution = "";
                ClassComponents cComponents = null;
                cComponents = classComponents;
                String methodUnderTest = testCase.getMethod();
                solution += className + " " + objectName + " = new " + className + "(); ";
                int methodCallSeqCount = (int )( Math.random() * EMConstants.METHOD_CLASS_SEQUENCE_COUNT );
                if( methodCallSeqCount<=1 ) {
                    methodCallSeqCount += 2;
                }
                for( int m=1; m<=methodCallSeqCount; m++ ) {
                    int methodNumber = ( int )( Math.random() * cComponents.getMmList().size() );
                    MemberMethod mMethod = ( MemberMethod )cComponents.getMmList().get( methodNumber );
                    if( !mMethod.getMethodName().equals( methodUnderTest ) ) {
                        solution += objectName + "." + mMethod.getMethodName() + "(";
                        ArrayList mTokens = mMethod.getMethodTokens();
                        int mt = 0;
                        Token token = null;
                        do {
                            token = ( Token )mTokens.get( mt++ );
                        } while( !token.getToken().equals( "(" ) );
                        token = ( Token )mTokens.get( mt++ );
                        while( !token.getToken().equals( ")" ) ) {
                            if( token.getToken().equals( "byte" ) || token.getToken().equals( "short" ) ) {
                                int value = ( int )( Math.random() * EMConstants.INT_RANGE );
                                solution += value;
                            } else if( token.getToken().equals( "int" ) || token.getToken().equals( "long" ) ) {
                                int value = ( int )( Math.random() * EMConstants.INT_RANGE );
                                int signRandom = ( int )( Math.random() * 100 );
                                if( signRandom%5==0 ) {
                                    value = value * -1;
                                }
                                solution += value;
                            } else if( token.getToken().equals( "float" ) ) {
                                int value1 = ( int )( Math.random() * EMConstants.INT_RANGE );
                                int value2 = ( int )( Math.random() * 10 );
                                solution += value1 + "." + value2 + "f";
                            } else if( token.getToken().equals( "double" ) ) {
                                int value1 = ( int )( Math.random() * EMConstants.INT_RANGE );
                                int value2 = ( int )( Math.random() * 10 );
                                int signRandom = ( int )( Math.random() * 100 );
                                if( signRandom%5==0 ) {
                                    value1 = value1 * -1;
                                }
                                solution += value1 + "." + value2;
                            } else if( token.getToken().equals( "char" ) ) {
                                int value = ( int )( Math.random() * 26 );
                                int tvalue = ( int )( Math.random() * 2 );
                                if( tvalue==0 ) {
                                    value += 65;
                                } else {
                                    value += 97;
                                }
                                char ch = ( char )value;
                                solution += "\'" + ch + "\'";
                            } else if( token.getToken().equals( "String" ) ) {
                                int len = ( int )( Math.random() * 10 );
                                String value = "\"";
                                for( int l=0; l<len; l++ ) {
                                    int ivalue = ( int )( Math.random() * 26 );
                                    int tvalue = ( int )( Math.random() * 3 );
                                    if( tvalue==0 ) {
                                        ivalue += 65;
                                    } else if( tvalue==1 ) {
                                        ivalue += 97;
                                    } else {
                                        ivalue += 48;
                                    }
                                    char ch = ( char )ivalue;
                                    value += ch;
                                }
                                value += "\"";
                                solution += value;
                            }
                            mt++;
                            token = ( Token )mTokens.get( mt++ );
                            if( token.getToken().equals( "," ) ) {
                                solution += ",";
                                token = ( Token )mTokens.get( mt++ );
                            }
                        }
                        solution += "); ";
                    }
                }

                ArrayList mmList = cComponents.getMmList();
                FOR: for( int mm=0; mm<mmList.size(); mm++ ) {
                MemberMethod mMethod = ( MemberMethod )mmList.get( mm );
                if( mMethod.getMethodName().equals( methodUnderTest ) ) {
                    solution += objectName + "." + mMethod.getMethodName() + "(";
                    ArrayList mTokens = mMethod.getMethodTokens();
                    int mt = 0;
                    Token token = null;
                    do {
                        token = ( Token )mTokens.get( mt++ );
                    } while( !token.getToken().equals( "(" ) );
                    token = ( Token )mTokens.get( mt++ );
                    while( !token.getToken().equals( ")" ) ) {
                        if( token.getToken().equals( "byte" ) || token.getToken().equals( "short" ) ) {
                            int value = ( int )( Math.random() * EMConstants.INT_RANGE );
                            solution += value;
                        } else if( token.getToken().equals( "int" ) || token.getToken().equals( "long" ) ) {
                            int value = ( int )( Math.random() * EMConstants.INT_RANGE );
                            int signRandom = ( int )( Math.random() * 100 );
                            if( signRandom%5==0 ) {
                                value = value * -1;
                            }
                            solution += value;
                        } else if( token.getToken().equals( "float" ) ) {
                            int value1 = ( int )( Math.random() * EMConstants.INT_RANGE );
                            int value2 = ( int )( Math.random() * 10 );
                            solution += value1 + "." + value2 + "f";
                        } else if( token.getToken().equals( "double" ) ) {
                            int value1 = ( int )( Math.random() * EMConstants.INT_RANGE );
                            int value2 = ( int )( Math.random() * 10 );
                            int signRandom = ( int )( Math.random() * 100 );
                            if( signRandom%5==0 ) {
                                value1 = value1 * -1;
                            }
                            solution += value1 + "." + value2;
                        } else if( token.getToken().equals( "char" ) ) {
                            int value = ( int )( Math.random() * 26 );
                            int tvalue = ( int )( Math.random() * 2 );
                            if( tvalue==0 ) {
                                value += 65;
                            } else {
                                value += 97;
                            }
                            char ch = ( char )value;
                            solution += "\'" + ch + "\'";
                        } else if( token.getToken().equals( "String" ) ) {
                            int len = ( int )( Math.random() * 10 );
                            String value = "\"";
                            for( int l=0; l<len; l++ ) {
                                int ivalue = ( int )( Math.random() * 26 );
                                int tvalue = ( int )( Math.random() * 3 );
                                if( tvalue==0 ) {
                                    ivalue += 65;
                                } else if( tvalue==1 ) {
                                    ivalue += 97;
                                } else {
                                    ivalue += 48;
                                }
                                char ch = ( char )ivalue;
                                value += ch;
                            }
                            value += "\"";
                            solution += value;
                        }
                        mt++;
                        token = ( Token )mTokens.get( mt++ );
                        if( token.getToken().equals( "," ) ) {
                            solution += ",";
                            token = ( Token )mTokens.get( mt++ );
                        }
                    }
                    solution += ");";
                    break FOR;
                } 
            }
                testCase.setTestCase( solution );
                this.population.add( testCase );
            }
        } 
    }

    public void generatePopulation(Target target) {
        String path = EMConstants.PROJECT_LOCATION + "/assets/mutants/" + target.getMutationOperator() + "/" + target.getMutantNumber() + "/" + target.getClassName() + ".java";
        String methodUnderTest = retrieveMethodUnderTest(path);
        System.out.println("Method Test: " + methodUnderTest);
        for(int p=this.population.size(); p<EMConstants.POPULATION_SIZE; p++){
            TestCase testCase = new TestCase();
            String solution = "";
            String className = this.classComponents.getClassHeader().getClassName();
            String objectName = className + "1";
            testCase.setClassName(className);
            testCase.setObjectName(objectName);
            testCase.setMethod(methodUnderTest);

            solution += className + " " + objectName + " = new " + className + "();" ;
            int methodCallSeqCount = (int) (Math.random() * EMConstants.METHOD_CLASS_SEQUENCE_COUNT);

            // random calls sequence
            for(int m=1; m<=methodCallSeqCount; m++){
                int methodNumber = (int)(Math.random()*classComponents.getMmList().size());
                MemberMethod mMethod = (MemberMethod)classComponents.getMmList().get(methodNumber);
                if(!mMethod.getMethodName().equals(methodUnderTest)){
                    solution += objectName + "." + mMethod.getMethodName() + "(";
                    ArrayList mTokens = mMethod.getMethodTokens();
                    int mt=0;
                    Token token=null;
                    do {
                        token = (Token) mTokens.get(mt++);
                    } while (!token.getToken().equals("("));
                    
                    token = (Token) mTokens.get(mt);
                    while(!token.getToken().equals(")")){
                        if( token.getToken().equals( "byte" )  || token.getToken().equals( "short" ) ) {
                            int value = ( int )( Math.random() * EMConstants.INT_RANGE );
                            solution += value;
                        } else if( token.getToken().equals( "int" ) || token.getToken().equals( "long" ) ) {
                            int value = ( int )( Math.random() * EMConstants.INT_RANGE );
                            int signRandom = ( int )( Math.random() * 100 );
                            if( signRandom%5==0 ) {
                                value = value * -1;
                            }
                            solution += value;
                        } else if( token.getToken().equals( "float" ) ) {
                            int value1 = ( int )( Math.random() * EMConstants.INT_RANGE );
                            int value2 = ( int )( Math.random() * 10 );
                            solution += value1 + "." + value2 + "f";
                        } else if( token.getToken().equals( "double" ) ) {
                            int value1 = ( int )( Math.random() * EMConstants.INT_RANGE );
                            int value2 = ( int )( Math.random() * 10 );
                            int signRandom = ( int )( Math.random() * 100 );
                            if( signRandom%5==0 ) {
                                value1 = value1 * -1;
                            }
                            solution += value1 + "." + value2;
                        } else if( token.getToken().equals( "char" ) ) {
                            int value = ( int )( Math.random() * 26 );
                            int tvalue = ( int )( Math.random() * 2 );
                            if( tvalue==0 ) {
                                value += 65;
                            } else {
                                value += 97;
                            }
                            char ch = ( char )value;
                            solution += "\'" + ch + "\'";
                        } else if( token.getToken().equals( "String" ) ) {
                            int len = ( int )( Math.random() * 10 );
                            String value = "\"";
                            for( int l=0; l<len; l++ ) {
                                int ivalue = ( int )( Math.random() * 26 );
                                int tvalue = ( int )( Math.random() * 3 );
                                if( tvalue==0 ) {
                                    ivalue += 65;
                                } else if( tvalue==1 ) {
                                    ivalue += 97;
                                } else {
                                    ivalue += 48;
                                }
                                char ch = ( char )ivalue;
                                value += ch;
                            }
                            value += "\"";
                            solution += value;
                        }
                        mt++; 
                        token = ( Token )mTokens.get(mt++);
                        if( token.getToken().equals( "," ) ) {
                            solution += ",";
                            token = ( Token )mTokens.get( mt++ );
                        }
                    }
                    solution += "); ";
                }
            }
            
            // random method under test
            ArrayList mmList = classComponents.getMmList();
            for(int mm=0; mm<mmList.size(); mm++){
                MemberMethod mMethod = (MemberMethod) mmList.get(mm);
                if(mMethod.getMethodName().equals(methodUnderTest)){
                    solution += objectName + "." + mMethod.getMethodName() + "(";
                    ArrayList mTokens = mMethod.getMethodTokens();
                    int mt = 0;
                    Token token = null;
                    do {
                        token = ( Token )mTokens.get( mt++ );
                    } while( !token.getToken().equals( "(" ) );
                    token = ( Token )mTokens.get( mt++ );
                    while( !token.getToken().equals( ")" ) ) {
                        if( token.getToken().equals( "byte" )  || token.getToken().equals( "short" ) ) {
                            int value = ( int )( Math.random() * EMConstants.INT_RANGE );
                            solution += value;
                        } else if( token.getToken().equals( "int" ) || token.getToken().equals( "long" ) ) {
                            int value = ( int )( Math.random() * EMConstants.INT_RANGE );
                            int signRandom = ( int )( Math.random() * 100 );
                            if( signRandom%5==0 ) {
                                value = value * -1;
                            }
                            solution += value;
                        } else if( token.getToken().equals( "float" ) ) {
                            int value1 = ( int )( Math.random() * EMConstants.INT_RANGE );
                            int value2 = ( int )( Math.random() * 10 );
                            solution += value1 + "." + value2 + "f";
                        } else if( token.getToken().equals( "double" ) ) {
                            int value1 = ( int )( Math.random() * EMConstants.INT_RANGE );
                            int value2 = ( int )( Math.random() * 10 );
                            int signRandom = ( int )( Math.random() * 100 );
                            if( signRandom%5==0 ) {
                                value1 = value1 * -1;
                            }
                            solution += value1 + "." + value2;
                        } else if( token.getToken().equals( "char" ) ) {
                            int value = ( int )( Math.random() * 26 );
                            int tvalue = ( int )( Math.random() * 2 );
                            if( tvalue==0 ) {
                                value += 65;
                            } else {
                                value += 97;
                            }
                            char ch = ( char )value;
                            solution += "\'" + ch + "\'";
                        } else if( token.getToken().equals( "String" ) ) {
                            int len = ( int )( Math.random() * 10 );
                            String value = "\"";
                            for( int l=0; l<len; l++ ) {
                                int ivalue = ( int )( Math.random() * 26 );
                                int tvalue = ( int )( Math.random() * 3 );
                                if( tvalue==0 ) {
                                    ivalue += 65;
                                } else if( tvalue==1 ) {
                                    ivalue += 97;
                                } else {
                                    ivalue += 48;
                                }
                                char ch = ( char )ivalue;
                                value += ch;
                            }
                            value += "\"";
                            solution += value;
                        }
                        mt++;
                        token = ( Token )mTokens.get( mt++ );
                        if( token.getToken().equals( "," ) ) {
                            solution += ",";
                            token = ( Token )mTokens.get( mt++ );
                        }
                    }
                    solution += ");";
                    break;
                }
            }
            testCase.setTestCase(solution);
            this.population.add(testCase);
        }
    }

    public String retrieveMethodUnderTest(String path) {
        String line="";
        try {
            LineNumberReader lnr = new LineNumberReader(new FileReader(path));
            line = lnr.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String methodName = line.split(Pattern.quote("."))[1];
        return methodName.substring(0, methodName.length()-2);
    }

    public void executeTestCase(Target target) throws IOException{
        String targetPath1 = EMConstants.PROJECT_LOCATION + "/assets/instrument/" + target.getMutationOperator() + "/" + target.getMutantNumber();
        String targetPath2 = EMConstants.PROJECT_LOCATION + "/assets/oinstrument/" + target.getMutationOperator() + "/" + target.getMutantNumber();
        File file = new File(targetPath1);
        if(!file.exists()){
            file.mkdirs();
        }
        System.out.println("Program Test: " + classComponents.getFilePath());
        file = new File(targetPath2);
        if(!file.exists()){
            file.mkdirs();
        }
        EMController.copyDirectory(new File(classComponents.getFilePath()), new File(targetPath1 + "/" + classComponents.getClassHeader().getClassName() + ".java"));
        EMController.copyDirectory(new File(classComponents.getFilePath()), new File(targetPath2 + "/" + classComponents.getClassHeader().getClassName() + ".java"));

        try {
            for(int t=0; t<this.population.size(); t++){
                TestCase testCase = (TestCase) this.population.get(t);
                String driverString = "import java.io.*;\n";
                driverString += "public class Driver"+ t + " {\n";
                driverString += "public static void main( String[] args )throws Exception {\n";
                String[] testCaseStrings = this.breakTestCase(testCase.toString());
                driverString += testCaseStrings[ 0 ] + " args[0] " + testCaseStrings[ 1 ] + "\n";
                driverString += "}\n";
                driverString += "}\n";
                File driverI = new File( targetPath1 + "/Driver" + t + ".java" );
                RandomAccessFile rafi = new RandomAccessFile(driverI,"rw");
                rafi.writeBytes( driverString );
                rafi.close();
                File driverO = new File( targetPath2 + "/Driver" + t + ".java" );
                RandomAccessFile rafo = new RandomAccessFile(driverO,"rw");
                rafo.writeBytes( driverString );
                rafo.close();
            }

            EMController.execCmd("find " + targetPath1 + " -name *.java -exec javac -sourcepath " + targetPath1 + " {} +");
            System.out.println("Target Path: " + targetPath1);
            //System.out.println("find " + targetPath1 + " -name *.java -exec javac -sourcepath " + targetPath1 + " {} +");
            File d1Check = new File( targetPath1 + "/Driver0.class" );
            int timeout = 1;
            while( !d1Check.exists() && timeout<=EMConstants.GA_TIMEOUT ) {
                timeout++;
            }

            Runtime.getRuntime().exec("find " + targetPath2 + " -name *.java -exec javac -sourcepath " + targetPath2 + " {} +");
            File d2Check = new File( targetPath2 + "/Driver0.class" );
            timeout = 1;
            while( !d2Check.exists() && timeout<=EMConstants.GA_TIMEOUT ) {
                timeout++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] breakTestCase( String tc ) {
        String[] tcs = new String[ 2 ];
        String temp = "";
        for( int t=0; t<tc.length(); t++ ) {
            char ch = tc.charAt( t );
            if( ch!='(' ) {
                temp += ch;
            } else {
                temp += ch;
                tcs[ 0 ] = temp;
                tcs[ 1 ] = tc.substring( t+1 );
                break;
            }
        }
        return tcs;
    }

    public ArrayList getPopulation() {
        return this.population;
    }
}