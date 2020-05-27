package utils;
import components.*;
import java.io.*;
import java.util.*;

public class EMScanner {

    private File file;
    private LineNumberReader lineReader;
    private ArrayList lineList;
    private ArrayList tokenList;
    private boolean isInteger, isFloat, isIdentifier;
    private boolean isChar, isString, isStart, isEnd;
    private Object em;

    public static String parentDirectory = "";

    public EMScanner() {
    } 

    public EMScanner( File codeFile ) {
        scanFile( codeFile );
    }
	
    public void scanFile( File codeFile ) {
        try {
            lineReader = new LineNumberReader( new FileReader( codeFile ) );
        } catch( IOException except ) {
            except.printStackTrace();
        }
        lineList = new ArrayList();
        tokenList = new ArrayList();
        readLinesFromFile();
        readTokenFromLines();
    }

    public void readLinesFromFile() {
        try {
            String line = null;
            if( lineReader != null ) {
                line = lineReader.readLine();
                while( line != null ) {
                    lineList.add( line );
                    line = lineReader.readLine();
                } 
            }
        } catch( IOException ioException ) {
            ioException.printStackTrace();
        } 
    }

    public void readTokenFromLines() {
        System.out.println(lineList.size());
        for( int lineNumber=0; lineNumber<lineList.size(); lineNumber++ ) {
            String line = ( String )lineList.get( lineNumber );
            findTokens( line,lineNumber+1 );
        }
    }

    public void findTokens( String line, int lineNumber ) {
        String token="";
        String tokenBreaker="";
        char character = '0';
        line += " ";
        boolean shouldExecute = true;
        for( int index=0; index<line.length(); index++ ) {
            character = line.charAt( index );
            shouldExecute = true;
            
            if( EMScanner.isTokenBreaker( character ) ) {

/////////////////////////Code to Check the type of Token///////////////////

                if( !token.equals( "" ) ) {
                    if( EMScanner.isDataType( token ) ) {
                        Token newToken = new Token();
                        newToken.setToken( token );
                        newToken.setDescription( "Data Type" );
                        newToken.setLineNumber( lineNumber );
                        tokenList.add( newToken );
                        token = "";
                    } else if( EMScanner.isKeyword( token ) ) {
                        Token newToken = new Token();
                        newToken.setToken( token );
                        newToken.setDescription( "Keyword" );
                        newToken.setLineNumber( lineNumber );
                        tokenList.add( newToken );
                        token = "";
                    } else if( EMScanner.isIdentifier( token ) ) {
                        Token newToken = new Token();
                        newToken.setToken( token );
                        newToken.setDescription( "Identifier" );
                        newToken.setLineNumber( lineNumber );
                        tokenList.add( newToken );
                        token = "";
                    } else if( EMScanner.isInteger( token ) ) {
                        Token newToken = new Token();
                        newToken.setToken( token );
                        newToken.setDescription( "Integer" );
                        newToken.setLineNumber( lineNumber );
                        tokenList.add( newToken );
                        token = "";
                    } //END if-else STATEMENT
                }

/////////////////////End checking code the type of token///////////////////

/////////////////////Code to check the type of token breaker////////////////

                if( shouldExecute ) {
                    tokenBreaker += character;
                    if( EMScanner.isArithematicOperator( tokenBreaker ) ) {
                        if( tokenBreaker.equals( "+" ) ) {
                            String tempString = ( tokenBreaker + line.charAt( index+1 ) );
                            if( tempString.equals( "++" ) ) {
                                Token newToken = new Token();
                                newToken.setToken( tempString );
                                newToken.setDescription( "Unary Operator" );
                                newToken.setLineNumber( lineNumber );
                                tokenList.add( newToken );
                                tokenBreaker = "";
                                index++;
                            } else {
                                Token newToken = new Token();
                                newToken.setToken( tokenBreaker );
                                newToken.setDescription( "Arithmatic Operator" );
                                newToken.setLineNumber( lineNumber );
                                tokenList.add( newToken );
                                tokenBreaker = "";
                            } //END if-else STATEMENTS                            
                        } else if( tokenBreaker.equals( "-" ) ) {
                            String tempString = line.charAt( index+1 ) + "";
                            if( tempString.equals( "-" ) ) {
                                Token newToken = new Token();
                                newToken.setToken( "--" );
                                newToken.setDescription( "Unary Operator" );
                                newToken.setLineNumber( lineNumber );
                                tokenList.add( newToken );
                                tokenBreaker = "";
                                index++;
                            } else if( EMScanner.isInteger( tempString ) ) {
                                String tempString2 = tokenBreaker + "";
                                do {
                                    index++;
                                    tempString2 += tempString;
                                    tempString = line.charAt( index+1 ) + "";
                                } while( EMScanner.isInteger( tempString ) );
                                Token newToken = new Token();
                                newToken.setToken( tempString2 );
                                newToken.setDescription( "Integer" );
                                newToken.setLineNumber( lineNumber );
                                tokenList.add( newToken );
                                tokenBreaker = "";
                            } else {
                                Token newToken = new Token();
                                newToken.setToken( tokenBreaker );
                                newToken.setDescription( "Arithmatic Operator" );
                                newToken.setLineNumber( lineNumber );
                                tokenList.add( newToken );
                                tokenBreaker = "";
                            } //END if-else STATEMENTS                            
                        } else {
                            Token newToken = new Token();
                            newToken.setToken( tokenBreaker );
                            newToken.setDescription( "Arithmatic Operator" );
                            newToken.setLineNumber( lineNumber );
                            tokenList.add( newToken );
                            tokenBreaker = "";
                        }

                    } else if( EMScanner.isRelationalOperator( tokenBreaker ) || tokenBreaker.equals( "!" ) ) {
                        if( tokenBreaker.equals( "<" ) || tokenBreaker.equals( ">" ) || tokenBreaker.equals( "!" ) ) {
                            if( line.length() > ( index+1 ) ) {
                                String tempString = ( tokenBreaker + line.charAt( index+1 ) );
                                if( EMScanner.isRelationalOperator( tempString ) ) {
                                    Token newToken = new Token();
                                    newToken.setToken( tempString );
                                    newToken.setDescription( "Relational Operator" );
                                    newToken.setLineNumber( lineNumber );
                                    tokenList.add( newToken );
                                    tokenBreaker = "";
                                    index++;
                                } else {
                                    Token newToken = new Token();
                                    newToken.setToken( tokenBreaker );
                                    newToken.setDescription( "Relational Operator" );
                                    newToken.setLineNumber( lineNumber );
                                    tokenList.add( newToken );
                                    tokenBreaker = "";
                                } //END if-else STATEMENTS
                            } else {
                                Token newToken = new Token();
                                newToken.setToken( tokenBreaker );
                                newToken.setDescription( "Relational Operator" );
                                newToken.setLineNumber( lineNumber );
                                tokenList.add( newToken );
                                tokenBreaker = "";
                            } //END if-else STATEMENTS
                        } else {
                            Token newToken = new Token();
                            newToken.setToken( tokenBreaker );
                            newToken.setDescription( "Relational Operator" );
                            newToken.setLineNumber( lineNumber );
                            tokenList.add( newToken );
                            tokenBreaker = "";
                        } //END if-else STATEMENTS
                    } else if( EMScanner.isAssignmentOperator( tokenBreaker ) ) {
                        if( line.length() > ( index+1 ) ) {
                            String tempString = ( tokenBreaker + line.charAt( index+1 ) );
                            if( EMScanner.isRelationalOperator( tempString ) ) {
                                Token newToken = new Token();
                                newToken.setToken( tempString );
                                newToken.setDescription( "Relational Operator" );
                                newToken.setLineNumber( lineNumber );
                                tokenList.add( newToken );
                                tokenBreaker = "";
                                index++;
                            } else {
                                Token newToken = new Token();
                                newToken.setToken( tokenBreaker );
                                newToken.setDescription( "Assignment Operator" );
                                newToken.setLineNumber( lineNumber );
                                tokenList.add( newToken );
                                tokenBreaker = "";
                            } //END if-else STATEMENTS
                        } else {
                            Token newToken = new Token();
                            newToken.setToken( tokenBreaker );
                            newToken.setDescription( "Assigment Operator" );
                            newToken.setLineNumber( lineNumber );
                            tokenList.add( newToken );
                            tokenBreaker = "";
                        } //END if-else STATEMENT
                    } else if( EMScanner.isSpecialSymbol( tokenBreaker ) ) {
                        Token newToken = new Token();
                        newToken.setToken( tokenBreaker );
                        newToken.setDescription( "Special Symbol" );
                        newToken.setLineNumber( lineNumber );
                        tokenList.add( newToken );
                        tokenBreaker = "";
                    } else if( tokenBreaker.equals( "&" ) ) {
                        Token newToken = new Token();
                        newToken.setToken( "&&" );
                        newToken.setDescription( "Logical Operator" );
                        newToken.setLineNumber( lineNumber );
                        tokenList.add( newToken );
                        tokenBreaker = "";
                        index++;
                    } else if( tokenBreaker.equals( "|" ) ) {
                        Token newToken = new Token();
                        newToken.setToken( "||" );
                        newToken.setDescription( "Logical Operator" );
                        newToken.setLineNumber( lineNumber );
                        tokenList.add( newToken );
                        tokenBreaker = "";
                        index++;
                    } else if( tokenBreaker.equals( "!" ) ) {
                        Token newToken = new Token();
                        newToken.setToken( "!" );
                        newToken.setDescription( "Logical Operator" );
                        newToken.setLineNumber( lineNumber );
                        tokenList.add( newToken );
                        tokenBreaker = "";
                    } else if( tokenBreaker.equals( "\"" ) ) {
                        String literal = "" + tokenBreaker;
                        index++;
STRFOR:			for( ; index<line.length(); index++ ) {
                            char ch = line.charAt( index );
                            literal += ch;
                            if( ch=='\"' ) {
                                break STRFOR;
                            } //END if STATEMENT
                        } //END for LOOP
                        Token newToken = new Token();
                        newToken.setToken( literal );
                        newToken.setDescription( "String Literal" );
                        newToken.setLineNumber( lineNumber );
                        tokenList.add( newToken );
                        tokenBreaker = "";
                    } else if( tokenBreaker.equals( "\'" ) ) {
                        String literal = "" + tokenBreaker;
                        index++;
STRFOR:			for( ; index<line.length(); index++ ) {
                            char ch = line.charAt( index );
                            literal += ch;
                            if( ch=='\'' ) {
                                break STRFOR;
                            } //END if STATEMENT
                        } //END for LOOP
                        Token newToken = new Token();
                        newToken.setToken( literal );
                        newToken.setDescription( "Character Literal" );
                        newToken.setLineNumber( lineNumber );
                        tokenList.add( newToken );
                        tokenBreaker = "";
                    } else {
                        tokenBreaker = "";
                    }
                } //check the possibility of .,+,- in real values

///////////////End checking code the type of token breaker ////////////////

            } else {
                token += character;
            } 
        } 
    }

    public static boolean isTokenBreaker( char character ) {
        int asciiCode = ( int )character;
        // 9-tab, 10-new line, 13-carriage return
        if( asciiCode==9 || asciiCode==13 || asciiCode==10 ) { 
                return true;
        }
        switch( character ) {
                case ' ':
                    return true;
                case '+':
                    return true;
                case '-':
                    return true;
                case '*':
                    return true;
                case '/':
                    return true;
                case '(':
                    return true;
                case ')':
                    return true;
                case '{':
                    return true;
                case '}':
                    return true;
                case '[':
                    return true;
                case ']':
                    return true;
                case '=':
                    return true;
                case '<':
                    return true;
                case '>':
                    return true;
                case ';':
                    return true;
                case '.':
                    return true;
                case ',':
                    return true;
                case '&':
                    return true;
                case '%':
                    return true;
                case '|':
                    return true;
                case '!':
                    return true;
                case '\'':
                    return true;
                case '\"':
                    return true;
                default:
                    return false;
            } //END swtich STATEMENT
        }

    public static boolean isKeyword( String token ) {
        if( token.equals( "class" ) ) {
            return true;
        } else if( token.equals( "if" ) ) {
                return true;
        } else if( token.equals( "else" ) ) {
                return true;
        } else if( token.equals( "private" ) ) {
                return true;
        } else if( token.equals( "public" ) ) {
                return true;
        } else if( token.equals( "protected" ) ) {
                return true;
        } else if( token.equals( "abstract" ) ) {
                return true;
        } else if( token.equals( "extends" ) ) {
                return true;
        } else if( token.equals( "implements" ) ) {
                return true;
        } else if( token.equals( "return" ) ) {
                return true;
        } else if( token.equals( "void" ) ) {
                return true;
        } else if( token.equals( "static" ) ) {
                return true;
        } else if( token.equals( "import" ) ) {
                return true;
        } else if( token.equals( "continue" ) ) {
                return true;
        } else if( token.equals( "break" ) ) {
                return true;
        } else if( token.equals( "final" ) ) {
                return true;
        } else if( token.equals( "extends" ) ) {
                return true;
        } else if( token.equals( "new" ) ) {
                return true;
        } else if( token.equals( "this" ) ) {
                return true;
        } else {
                return false;
        }
    }

    public static boolean isDataType( String token ) {
        if( token.equals( "byte" ) ) {
            return true;
	} else if( token.equals( "boolean" ) ) {
            return true;
	} else if( token.equals( "short" ) ) {
            return true;
	} else if( token.equals( "int" ) ) {
            return true;
	} else if( token.equals( "long" ) ) {
            return true;
	} else if( token.equals( "float" ) ) {
            return true;
        } else if( token.equals( "double" ) ) {
            return true;
	} else if( token.equals( "char" ) ) {
            return true;
	} else {
            return false;
	} //END if-else STATEMENTS
    } //END isDataType() METHOD
    
    public static boolean isIdentifier( String token ) {
        if( Character.isLetter( token.charAt( 0 ) ) || token.charAt( 0 )=='_' ) {
            for( int index=1; index<token.length(); index++ ) {
                if( !( ( Character.isLetterOrDigit( token.charAt( index ) ) ) ) ) {
                    return false;
		}
            } 
            return true;
	    } 
	    return false;
    } 

    public static boolean isInteger( String token ) {
        for( int index=0; index<token.length(); index++ ) {
            if( !Character.isDigit( token.charAt( index ) ) ) {
                return false;
            } //END if STATEMENT
	} //END for LOOP
	return true;
    } //END isInteger() METHOD

    public static boolean isArithematicOperator( String operator ) {
        if( operator.equals( "+" ) ) {
            return true;
	} else if( operator.equals( "-" ) ) {
            return true;
	} else if( operator.equals( "*" ) ) {
            return true;
	} else if( operator.equals( "/" ) ) {
            return true;
	} else if( operator.equals( "%" ) ) {
            return true;
	} else {
            return false;
	} //END if-else STATEMENTS
    } //END isArithematicOperator() METHOD

    public static boolean isRelationalOperator( String operator ) {
        if( operator.equals( "<" ) ) {
            return true;
        } else if( operator.equals( ">" ) ) {
            return true;
	} else if( operator.equals( "<=" ) ) {
            return true;
	} else if( operator.equals( ">=" ) ) {
            return true;
	} else if( operator.equals( "!=" ) ) {
            return true;
	} else if( operator.equals( "==" ) ) {
            return true;
	} else {
            return false;
	} //END if-else STATEMENTS
    } //END isRelationalOperator() METHOD

    public static boolean isAssignmentOperator( String operator ) {
        if( operator.equals( "=" ) ) {
            return true;
        } else {
            return false;
	} //END if-else STATEMENT
    } //END isAssignmentOperator() METHOD

    public static boolean isSpecialSymbol( String operator ) {
        char symbol = operator.charAt( 0 );
        switch( symbol ) {
            case '(':
                return true;
            case ')':
                return true;
            case '{':
                return true;
            case '}':
                return true;
            case '[':
                return true;
            case ']':
                return true;
            case ';':
                return true;
            case '.':
                return true;
            case ',':
                return true;
            default:
                return false;
	    } 
    } 

    public ArrayList getTokenList() {
        return tokenList;
    }

    public void printTokens() {
        for( int i=0; i<tokenList.size(); i++ ) {
            Token token = ( Token )tokenList.get( i );
            System.out.print( token);
	    }
    }

}
