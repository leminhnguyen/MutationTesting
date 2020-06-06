package algorithm;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.transform.Source;

import components.*;
import utils.*;

import components.ClassComponents;
import utils.EMConstants;

public class TraceBack{

    private ClassComponents classComponents;

    public TraceBack(){

    }

    public TraceBack(ClassComponents classComponents){
        this.classComponents = classComponents;
    }

    public void trace(String mutationOperator) {
        try {
            File mutantDir = new File( EMConstants.PROJECT_LOCATION + "/assets/mutants/" + mutationOperator );
            File[] mutantsDir = mutantDir.listFiles();
            File instDir = new File( EMConstants.PROJECT_LOCATION + "/assets/instrument/" + mutationOperator );
            if( !instDir.exists() ) {
                instDir.mkdir();
            }
            File oinstDir = new File( EMConstants.PROJECT_LOCATION + "/assets/oinstrument/" + mutationOperator );
            if( !oinstDir.exists() ) {
                oinstDir.mkdir();
            }

            for( int md=0; md<mutantsDir.length; md++ ) {
                File mutantFile = new File( EMConstants.PROJECT_LOCATION + "/assets/mutants/" + mutationOperator + "/" + (md+1) );
                File[] mutantFiles = mutantFile.listFiles();
                ClassComponents cComponents = classComponents;
                for( int mf=0; mf<mutantFiles.length; mf++ ) {
                    int methodCount = 1;
                    int controlCount = 1;
                    int methodEndCount = 0;
                    boolean isMutatedMethod = false;
                    File codeFile = mutantFiles[ mf ];
                    File ocodeFile = new File( EMConstants.PROJECT_LOCATION + "/assets/omutants/" + codeFile.getName() );
                    String instrumentedCode = "";
                    String oinstrumentedCode = "";
                    String mutatedMethod = "";
                    LineNumberReader lnr = new LineNumberReader( new FileReader( codeFile ) );
                    LineNumberReader olnr = new LineNumberReader( new FileReader( ocodeFile ) );
                    File instDirs = new File( EMConstants.PROJECT_LOCATION + "/assets/instrument/" + mutationOperator + "/" + (md+1) );
                    File oinstDirs = new File( EMConstants.PROJECT_LOCATION + "/assets/oinstrument/" + mutationOperator + "/" + (md+1) );
                    if( !instDirs.exists() ) {
                        instDirs.mkdirs();
                    }
                    if( !oinstDirs.exists() ) {
                        oinstDirs.mkdirs();
                    }
                    RandomAccessFile raf = new RandomAccessFile( instDirs.getAbsolutePath() + "/" + codeFile.getName(), "rw" );
                    String line = lnr.readLine();
                    RandomAccessFile oraf = new RandomAccessFile( oinstDirs.getAbsolutePath() + "/" + ocodeFile.getName(), "rw" );
                    String oline = "";
                    if( line.startsWith( "//" ) ) {
                        System.err.print( "Line: " + line );
                        mutatedMethod = findMutatedMethod( line );
                        System.err.print( "Method: " + mutatedMethod );
                        mutatedMethod = findMethodHeader( mutatedMethod, cComponents );
                        do {
                            if( line.contains( mutatedMethod ) ) {
                                //Now we are in the method containing mutation
                                isMutatedMethod = true;
                                ArrayList tempList = new ArrayList();
                                ArrayList otempList = new ArrayList();
                                boolean mutation = false;
                                int conditionCount = 0;
                                ArrayList conditions = new ArrayList();
                                ArrayList predicates = new ArrayList();
                                int approach_level = 1;
                                boolean ifWhileMutation = false;
                                do {
                                    if( line.contains( "//mutated with" ) && ( line.contains( "if(" ) || line.contains( "while(" ) ) ) {
                                        conditionCount++;
                                        predicates.add( line );
                                        ifWhileMutation = true;
                                        if( line.contains( "if(" ) ) {
                                            conditions.add( "if" );
                                        } else {
                                            conditions.add( "while" );                                                
                                        }
                                    } 
                                    if( line.contains( "//mutated with" ) ) {
                                        if( conditionCount==0 ) {
                                            String instrumentedString = "try {\n";
                                            instrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                            instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                            instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                            instrumentedString += "raf.writeBytes( \"R: 0.0 0 0.0\" );\n";
                                            instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                            instrumentedString += "raf.close();\n";
                                            instrumentedString += "} catch( Exception e ) { }\n";
                                            tempList.add( instrumentedString );
                                            instrumentedString = "try {\n";
                                            instrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                            instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                            instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                            String oinstrumentedString = "try {\n";
                                            oinstrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                            oinstrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                            oinstrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                            if( line.contains( "mutated with ABS" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( line ) + ");\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( oline ) + ");\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with AOR" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( line ) + ");\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( oline ) + ");\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with ROR" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: true\" );\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: true\" );\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with LCR" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: true\" );\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: true\" );\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with UOI" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( line ) + ");\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( oline ) + ");\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with JID" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( line ) + ");\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( oline ) + ");\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with OMD" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( line ) + ");\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( oline ) + ");\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with IOP" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( line ) + ");\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( oline ) + ");\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with PNC" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: \" + " + getObject( line ) + ".toString() );\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: \" + " + getObject( oline ) + ".toString() );\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with EOC" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: true\" );\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: true\" );\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            }

                                        } else {
                                            String instrumentedString = "try {\n";
                                            instrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                            instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                            instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                            instrumentedString += "raf.writeBytes( \"R: 0.0 0 0.0\" );\n";
                                            instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                            instrumentedString += "raf.close();\n";
                                            instrumentedString += "} catch( Exception e ) { }\n";
                                            tempList.add( instrumentedString );
                                            instrumentedString = "try {\n";
                                            instrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                            instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                            instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                            String oinstrumentedString = "try {\n";
                                            oinstrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                            oinstrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                            oinstrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                            if( line.contains( "mutated with ABS" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( line ) + ");\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( oline ) + ");\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with AOR" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( line ) + ");\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( oline ) + ");\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with ROR" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: true\" );\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: true\" );\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with LCR" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: true\" );\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: true\" );\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with UOI" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( line ) + ");\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( oline ) + ");\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with JID" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( line ) + ");\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( oline ) + ");\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with OMD" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( line ) + ");\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( oline ) + ");\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with IOP" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( line ) + ");\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: \" + " + getIdentifier( oline ) + ");\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with PNC" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: \" + " + getObject( line ) + ".toString() );\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: \" + " + getObject( oline ) + ".toString() );\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            } else if( line.contains( "mutated with EOC" ) ) {
                                                instrumentedString += "raf.writeBytes( \"N: true\" );\n";
                                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                instrumentedString += "raf.close();\n";
                                                instrumentedString += "} catch( Exception e ) { }\n";
                                                tempList.add( line );
                                                tempList.add( instrumentedString );
                                                oinstrumentedString += "raf.writeBytes( \"N: true\" );\n";
                                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                oinstrumentedString += "raf.close();\n";
                                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                                otempList.add( oline );
                                                otempList.add( oinstrumentedString );
                                            }     
                                            while( conditionCount>0 ) {
                                                line = lnr.readLine();
                                                oline = olnr.readLine();
                                                if( line.contains( "if(" ) || line.contains( "while(" ) ) {
                                                    tempList.add( line );
                                                    otempList.add( oline );
                                                    String innerCondSuffString = "S: " + codeFile.getName().substring( 0, codeFile.getName().length()-5 ) + " " + this.isMethodDeclaration( mutatedMethod, cComponents );
                                                    instrumentedString = "try {\n";
                                                    instrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                                    instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                                    instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                                    oinstrumentedString = "try {\n";
                                                    oinstrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                                    oinstrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                                    oinstrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                                    if( line.contains( "if(" ) ) {
                                                        innerCondSuffString += " if " + controlCount;
                                                        instrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                                        instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                        instrumentedString += "raf.close();\n";
                                                        instrumentedString += "} catch( Exception e ) { }\n";
                                                        oinstrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                                        oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                        oinstrumentedString += "raf.close();\n";
                                                        oinstrumentedString += "} catch( Exception e ) { }\n";
                                                        tempList.add( instrumentedString );
                                                        otempList.add( oinstrumentedString );
                                                    } else if( line.contains( "while(" ) ) {
                                                        innerCondSuffString += " while " + controlCount;
                                                        instrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                                        instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                        instrumentedString += "raf.close();\n";
                                                        instrumentedString += "} catch( Exception e ) { }\n";
                                                        oinstrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                                        oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                        oinstrumentedString += "raf.close();\n";
                                                        oinstrumentedString += "} catch( Exception e ) { }\n";
                                                        tempList.add( instrumentedString );
                                                        otempList.add( oinstrumentedString );
                                                    } else if( line.contains( "} else {" ) ) {
                                                        innerCondSuffString += " else " + controlCount;
                                                        instrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                                        instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                        instrumentedString += "raf.close();\n";
                                                        instrumentedString += "} catch( Exception e ) { }\n";
                                                        oinstrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                                        oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                        oinstrumentedString += "raf.close();\n";
                                                        oinstrumentedString += "} catch( Exception e ) { }\n";
                                                        tempList.add( instrumentedString );
                                                        otempList.add( oinstrumentedString );
                                                    }                 
                                                    controlCount++;
                                                    int innerConditions = 1;
                                                    do {
                                                        line = lnr.readLine();
                                                        oline = olnr.readLine();
                                                        innerCondSuffString = "S: " + codeFile.getName().substring( 0, codeFile.getName().length()-5 ) + " " + this.isMethodDeclaration( mutatedMethod, cComponents );
                                                        instrumentedString = "try {\n";
                                                        instrumentedString += "java.io.File myFile = new java.io.File( traceFile);\n";
                                                        instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                                        instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                                        oinstrumentedString = "try {\n";
                                                        oinstrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                                        oinstrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                                        oinstrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                                        if( line.contains( "if(" ) ) {
                                                            tempList.add( line );
                                                            otempList.add( oline );
                                                            innerCondSuffString += " if "  + controlCount;
                                                            instrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                                            instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                            instrumentedString += "raf.close();\n";
                                                            instrumentedString += "} catch( Exception e ) { }\n";
                                                            oinstrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                                            oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                            oinstrumentedString += "raf.close();\n";
                                                            oinstrumentedString += "} catch( Exception e ) { }\n";
                                                            tempList.add( instrumentedString );
                                                            otempList.add( oinstrumentedString );
                                                            innerConditions++;
                                                            controlCount++;
                                                        } else if( line.contains( "while(" ) ) {
                                                            tempList.add( line );
                                                            otempList.add( oline );
                                                            innerCondSuffString += " while "  + controlCount;
                                                            instrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                                            instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                            instrumentedString += "raf.close();\n";
                                                            instrumentedString += "} catch( Exception e ) { }\n";
                                                            oinstrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                                            oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                            oinstrumentedString += "raf.close();\n";
                                                            oinstrumentedString += "} catch( Exception e ) { }\n";
                                                            tempList.add( instrumentedString );
                                                            otempList.add( oinstrumentedString );
                                                            innerConditions++;
                                                            controlCount++;
                                                        } else if( line.contains( "} else {" ) ) {
                                                            tempList.add( line );
                                                            otempList.add( oline );
                                                            innerCondSuffString += " else " + controlCount;
                                                            instrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                                            instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                            instrumentedString += "raf.close();\n";
                                                            instrumentedString += "} catch( Exception e ) { }\n";
                                                            oinstrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                                            oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                            oinstrumentedString += "raf.close();\n";
                                                            oinstrumentedString += "} catch( Exception e ) { }\n";
                                                            tempList.add( instrumentedString );
                                                            otempList.add( oinstrumentedString );
                                                            controlCount++;
                                                        } else if( line.contains( "}" ) ) {
                                                            tempList.add( line );
                                                            otempList.add( oline );
                                                            innerConditions--;
                                                        } else {
                                                            tempList.add( line );
                                                            otempList.add( oline );
                                                        } 
                                                    } while( innerConditions>0 );

                                                } else if( line.contains( "} else {" ) ) {
                                                    tempList.add( line );
                                                    otempList.add( oline );
                                                    String condition = ( String )predicates.get( predicates.size()-1 );
                                                    predicates.remove( predicates.size()-1 );
                                                    conditions.remove( conditions.size()-1 );

                                                    instrumentedString = "try {\n";
                                                    instrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                                    instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                                    instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                                    instrumentedString += "float state_fitness = 0.0f;\n";
                                                    instrumentedString += "float local_fitness = 0.0f;\n";
                                                    instrumentedString += "float fitness = 0.0f;\n";
                                                    instrumentedString += "int state_count = 0;\n";
                                                    instrumentedString += "int local_count = 0;\n";
                                                    oinstrumentedString = "try {\n";
                                                    oinstrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                                    oinstrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                                    oinstrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                                    if( condition.contains( "this" ) ) {
                                                        String[] predComps = this.getReferenceConditionOperands( condition );
                                                        instrumentedString += "int stringWeight1 = 0;\n";
                                                        instrumentedString += "int stringWeight2 = 0;\n";
                                                        instrumentedString += "String _string1 = " + predComps[ 0 ] + ".toString();\n";
                                                        instrumentedString += "String _string2 = " + predComps[ 1 ] + ".toString();\n";
                                                        instrumentedString += "for( int _s1=0; _s1<_string1.length(); _s1++ ) {\n";
                                                        instrumentedString += "stringWeight1 += _string1.charAt( _s1 );\n";
                                                        instrumentedString += "}\n";
                                                        instrumentedString += "for( int _s2=0; _s2<_string2.length(); _s2++ ) {\n";
                                                        instrumentedString += "stringWeight2 += _string2.charAt( _s2 );\n";
                                                        instrumentedString += "}\n";
                                                        instrumentedString += "state_fitness = ( float)( stringWeight1 - stringWeight2 ) / ( stringWeight1 + stringWeight2 );\n";
                                                        instrumentedString += "state_fitness = Math.abs( state_fitness );\n";
                                                    } else {
                                                        ArrayList predicateList = getPredicateList( condition );
                                                        int stateCounter = 0;
                                                        int localCounter = 0;
                                                        for( int p=0; p<predicateList.size(); p++ ) {
                                                            String predicate = ( String )predicateList.get( p );
                                                            String[] predComps = getPredicateComponents( predicate );
                                                            String operand1 = predComps[ 0 ];
                                                            String operator = predComps[ 1 ];
                                                            String operand2 = predComps[ 2 ];
                                                            instrumentedString += "if( !(" + predicate + ") ) { \n";
                                                            if( operator.equals( "<=" ) ) {
                                                                instrumentedString += "fitness = ( float )( " + operand1 + " - " + operand2 + " ) / ( float )( " + operand1 + " + " + operand2 + "); \n";
                                                                instrumentedString += "fitness = Math.abs( fitness );\n";
                                                            } else if( operator.equals( "<" ) ) {
                                                                instrumentedString += "fitness = ( float )( (" + operand1 + "+1) - " + operand2 + " ) / ( float )( (" + operand1 + "+1) + " + operand2 + "); \n";
                                                                instrumentedString += "fitness = Math.abs( fitness );\n";
                                                            } else if( operator.equals( ">=" ) ) {
                                                                instrumentedString += "fitness = ( float )( " + operand2 + " - " + operand1 + " ) / ( float )( " + operand2 + " + " + operand1 + "); \n";
                                                                instrumentedString += "fitness = Math.abs( fitness );\n";
                                                            } else if( operator.equals( ">" ) ) {
                                                                instrumentedString += "fitness = ( float )( (" + operand2 + "+1) - " + operand1 + " ) / ( float )( (" + operand2 + "+1) + " + operand1 + "); \n";
                                                                instrumentedString += "fitness = Math.abs( fitness );\n";
                                                            } else if( operator.equals( "==" ) ) {
                                                                instrumentedString += "fitness = ( float )( " + operand2 + " - " + operand1 + " ) / ( float )( " + operand2 + " + " + operand1 + "); \n";
                                                                instrumentedString += "fitness = Math.abs( fitness );\n";
                                                            } else if( operator.equals( "!=" ) ) {
                                                                instrumentedString += "fitness = 0.5f;\n";
                                                            } 
                                                            if( isStateVariable( operand1, cComponents ) ) {
                                                                instrumentedString += "state_fitness += fitness;\n";
                                                                instrumentedString += "state_count++;\n";
                                                                stateCounter++;
                                                            } else {
                                                                instrumentedString += "local_fitness += fitness;\n";
                                                                instrumentedString += "local_count++;\n";
                                                                localCounter++;
                                                            } 
                                                            instrumentedString += "}\n";
                                                        } 
                                                        if( stateCounter>1 ) {
                                                             instrumentedString += "if( state_count>1 ) {\n";
                                                             instrumentedString += "state_fitness *= 0.5f;\n";
                                                             instrumentedString += "}\n";
                                                        } 
                                                        if( localCounter>1 ) {
                                                            instrumentedString += "if( local_count>1 ) {\n";
                                                            instrumentedString += "local_fitness *= 0.5f;\n";
                                                            instrumentedString += "}\n";
                                                        } 
                                                    } 
                                                    
                                                    instrumentedString += "raf.writeBytes( \"R: \" + state_fitness + \" " + approach_level + " \" + local_fitness );\n";
                                                    instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                    if( ifWhileMutation ) {
                                                        instrumentedString += "raf.writeBytes( \"N: false\" );\n";
                                                        instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                        oinstrumentedString += "raf.writeBytes( \"N: false\" );\n";
                                                        oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                    } else {
                                                        instrumentedString += "raf.writeBytes( \"N: c\" );\n";
                                                        instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                        instrumentedString += "raf.writeBytes( \"S: c Normal\" );\n";
                                                    } 
                                                    instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                    instrumentedString += "raf.close();\n";
                                                    instrumentedString += "} catch( Exception e ) { }\n";
                                                    tempList.add( instrumentedString );
                                                    oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                    oinstrumentedString += "raf.close();\n";
                                                    oinstrumentedString += "} catch( Exception e ) { }\n";
                                                    otempList.add( oinstrumentedString );
                                                    
                                                    approach_level++;
                                                    conditions.add( "else" );
                                                    predicates.add( line );
                                                    conditionCount--;
                                                } else if( line.contains( "}" ) ) {
                                                    String condition1 = ( String )conditions.get( conditions.size()-1 );
                                                    String condition = ( String )predicates.get( predicates.size()-1 );
                                                    System.out.println( "CONDITION: " + condition );
                                                    System.out.println( "PREDICATE: " + condition1 );
                                                    if( condition1.equals( "if" ) ) {
                                                        predicates.remove( predicates.size()-1 );
                                                        conditions.remove( conditions.size()-1 );
                                                        tempList.add( "} else {" );
                                                        otempList.add( "} else {" );
                                                    } else if( condition1.equals( "while" ) ) {
                                                        tempList.add( "}" );
                                                        otempList.add( "}" );
                                                        String whilePredicate = getWhilePredicate( condition );
                                                        System.out.println( "WP: " + whilePredicate );
                                                        tempList.add( "if( !(" + whilePredicate + ") ) {" );
                                                        otempList.add( "if( !(" + whilePredicate + ") ) {" );
                                                    } else {
                                                        predicates.remove( predicates.size()-1 );
                                                        conditions.remove( conditions.size()-1 );
                                                        tempList.add( "}" );
                                                        otempList.add( "}" );
                                                        condition = ( String )predicates.get( predicates.size()-1 );
                                                        tempList.add( condition );
                                                        otempList.add( condition );
                                                    } 

                                                    instrumentedString = "try {\n";
                                                    instrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                                    instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                                    instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                                    instrumentedString += "float state_fitness = 0.0f;\n";
                                                    instrumentedString += "float local_fitness = 0.0f;\n";
                                                    instrumentedString += "float fitness = 0.0f;\n";
                                                    instrumentedString += "int state_count = 0;\n";
                                                    instrumentedString += "int local_count = 0;\n";
                                                    oinstrumentedString = "try {\n";
                                                    oinstrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                                    oinstrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                                    oinstrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                                    if( condition.contains( "this" ) ) {
                                                        String[] predComps = this.getReferenceConditionOperands( condition );
                                                        instrumentedString += "int stringWeight1 = 0;\n";
                                                        instrumentedString += "int stringWeight2 = 0;\n";
                                                        instrumentedString += "String _string1 = " + predComps[ 0 ] + ".toString();\n";
                                                        instrumentedString += "String _string2 = " + predComps[ 1 ] + ".toString();\n";
                                                        instrumentedString += "for( int _s1=0; _s1<_string1.length(); _s1++ ) {\n";
                                                        instrumentedString += "stringWeight1 += _string1.charAt( _s1 );\n";
                                                        instrumentedString += "}\n";
                                                        instrumentedString += "for( int _s2=0; _s2<_string2.length(); _s2++ ) {\n";
                                                        instrumentedString += "stringWeight2 += _string2.charAt( _s2 );\n";
                                                        instrumentedString += "}\n";
                                                        instrumentedString += "state_fitness = ( float)( stringWeight1 - stringWeight2 ) / ( stringWeight1 + stringWeight2 );\n";
                                                        instrumentedString += "state_fitness = Math.abs( state_fitness );\n";
                                                    } else {
                                                        ArrayList predicateList = getPredicateList( condition );
                                                        int stateCounter = 0;
                                                        int localCounter = 0;
                                                        for( int p=0; p<predicateList.size(); p++ ) {
                                                            String predicate = ( String )predicateList.get( p );
                                                            System.out.println( "PREDICATE: " + predicate );
                                                            String[] predComps = getPredicateComponents( predicate );
                                                            String operand1 = predComps[ 0 ];
                                                            String operator = predComps[ 1 ];
                                                            String operand2 = predComps[ 2 ];
                                                            instrumentedString += "if( !(" + predicate + ") ) { \n";
                                                            if( operator.equals( "<=" ) ) {
                                                                instrumentedString += "fitness = ( float )( " + operand1 + " - " + operand2 + " ) / ( float )( " + operand1 + " + " + operand2 + "); \n";
                                                                instrumentedString += "fitness = Math.abs( fitness );\n";
                                                            } else if( operator.equals( "<" ) ) {
                                                                instrumentedString += "fitness = ( float )( (" + operand1 + "+1) - " + operand2 + " ) / ( float )( (" + operand1 + "+1) + " + operand2 + "); \n";
                                                                instrumentedString += "fitness = Math.abs( fitness );\n";
                                                            } else if( operator.equals( ">=" ) ) {
                                                                instrumentedString += "fitness = ( float )( " + operand2 + " - " + operand1 + " ) / ( float )( " + operand2 + " + " + operand1 + "); \n";
                                                                instrumentedString += "fitness = Math.abs( fitness );\n";
                                                            } else if( operator.equals( ">" ) ) {
                                                                instrumentedString += "fitness = ( float )( (" + operand2 + "+1) - " + operand1 + " ) / ( float )( (" + operand2 + "+1) + " + operand1 + "); \n";
                                                                instrumentedString += "fitness = Math.abs( fitness );\n";
                                                            } else if( operator.equals( "==" ) ) {
                                                                instrumentedString += "fitness = ( float )( " + operand2 + " - " + operand1 + " ) / ( float )( " + operand2 + " + " + operand1 + "); \n";
                                                                instrumentedString += "fitness = Math.abs( fitness );\n";
                                                            } else if( operator.equals( "!=" ) ) {
                                                                instrumentedString += "fitness = 0.5f;\n";                                                                    
                                                            } 
                                                            if( isStateVariable( operand1, cComponents ) ) {
                                                                instrumentedString += "state_fitness += fitness;\n";
                                                                instrumentedString += "state_count++;\n";
                                                                stateCounter++;
                                                            } else {
                                                                instrumentedString += "local_fitness += fitness;\n";
                                                                instrumentedString += "local_count++;\n"; 
                                                                localCounter++;
                                                            } 
                                                            instrumentedString += "}\n";
                                                        } 
                                                        if( stateCounter>1 ) {
                                                             instrumentedString += "if( state_count>1 ) {\n";
                                                             instrumentedString += "state_fitness *= 0.5f;\n";
                                                             instrumentedString += "}\n";
                                                        } 
                                                        if( localCounter>1 ) {
                                                            instrumentedString += "if( local_count>1 ) {\n";
                                                            instrumentedString += "local_fitness *= 0.5f;\n";
                                                            instrumentedString += "}\n";
                                                        } 
                                                    } 
                                                    
                                                    instrumentedString += "raf.writeBytes( \"R: \" + state_fitness + \" " + approach_level + " \" + local_fitness );\n";
                                                    instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                    if( ifWhileMutation ) {
                                                        instrumentedString += "raf.writeBytes( \"N: false\" );\n";
                                                        instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                        oinstrumentedString += "raf.writeBytes( \"N: false\" );\n";
                                                        oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                    } else {
                                                        instrumentedString += "raf.writeBytes( \"N: c\" );\n";
                                                        instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                        instrumentedString += "raf.writeBytes( \"S: c Normal\" );\n";
                                                    } 
                                                    instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                    instrumentedString += "raf.close();\n";
                                                    instrumentedString += "} catch( Exception e ) { }\n";
                                                    tempList.add( instrumentedString );
                                                    oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                                    oinstrumentedString += "raf.close();\n";
                                                    oinstrumentedString += "} catch( Exception e ) { }\n";
                                                    otempList.add( oinstrumentedString );
                                                    
                                                    tempList.add( "}" );
                                                    otempList.add( "}" );
                                                    approach_level++;
                                                    conditionCount--;
                                                } else {
                                                    tempList.add( line );
                                                    otempList.add( oline );
                                                }
                                            }
                                        }
                                        
                                        mutation = true;
                                    } else if( line.contains( "if(" ) ) {
                                        tempList.add( line );
                                        otempList.add( oline );
                                        conditionCount++;
                                        conditions.add( "if" );
                                        predicates.add( line );
                                        if( mutation ) {
                                            String innerCondSuffString = "S: " + codeFile.getName().substring( 0, codeFile.getName().length()-5 ) + " " + this.isMethodDeclaration( mutatedMethod, cComponents ) + " if " + controlCount;
                                            String instrumentedString = "try {\n";
                                            instrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                            instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                            instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                            instrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                            instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                            instrumentedString += "raf.close();\n";
                                            instrumentedString += "} catch( Exception e ) { }\n";
                                            String oinstrumentedString = "try {\n";
                                            oinstrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                            oinstrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                            oinstrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                            oinstrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                            oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                            oinstrumentedString += "raf.close();\n";
                                            oinstrumentedString += "} catch( Exception e ) { }\n";
                                            tempList.add( instrumentedString );
                                            otempList.add( oinstrumentedString );             
                                            controlCount++;
                                        } 
                                    } else if( line.contains( "while(" ) ) {
                                        tempList.add( line );
                                        otempList.add( oline );
                                        conditionCount++;
                                        conditions.add( "while" );
                                        predicates.add( line );
                                        if( mutation ) {
                                            String innerCondSuffString = "S: " + codeFile.getName().substring( 0, codeFile.getName().length()-5 ) + " " + this.isMethodDeclaration( mutatedMethod, cComponents ) + " while " + controlCount;
                                            String instrumentedString = "try {\n";
                                            instrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                            instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                            instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                            instrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                            instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                            instrumentedString += "raf.close();\n";
                                            instrumentedString += "} catch( Exception e ) { }\n";
                                            String oinstrumentedString = "try {\n";
                                            oinstrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                            oinstrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                            oinstrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                            oinstrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                            oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                            oinstrumentedString += "raf.close();\n";
                                            oinstrumentedString += "} catch( Exception e ) { }\n";
                                            tempList.add( instrumentedString );
                                            otempList.add( oinstrumentedString );        
                                            controlCount++;
                                        } 
                                    } else if( line.contains( "} else {" ) ) {
                                        tempList.add( line );
                                        otempList.add( oline );
                                        conditions.add( "else" );
                                        predicates.add( line );
                                        if( mutation ) {
                                            String innerCondSuffString = "S: " + codeFile.getName().substring( 0, codeFile.getName().length()-5 ) + " " + this.isMethodDeclaration( mutatedMethod, cComponents ) + " else " + controlCount;
                                            String instrumentedString = "try {\n";
                                            instrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                            instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                            instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                            instrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                            instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                            instrumentedString += "raf.close();\n";
                                            instrumentedString += "} catch( Exception e ) { }\n";
                                            String oinstrumentedString = "try {\n";
                                            oinstrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                            oinstrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                            oinstrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                            oinstrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                            oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                            oinstrumentedString += "raf.close();\n";
                                            oinstrumentedString += "} catch( Exception e ) { }\n";
                                            tempList.add( instrumentedString );
                                            otempList.add( oinstrumentedString );    
                                            controlCount++;
                                        } 
                                    } else if( line.contains( "}" ) ) {
                                        if( conditionCount==0 || conditionCount==1 ) {
                                            tempList.add( line );
                                            otempList.add( oline );
                                            for( int t=0; t<tempList.size(); t++ ) {
                                                String tempLine = ( String )tempList.get( t );
                                                instrumentedCode += tempLine;
                                                instrumentedCode += "\n";
                                            } 
                                            tempList.clear();
                                            for( int t=0; t<otempList.size(); t++ ) {
                                                String otempLine = ( String )otempList.get( t );
                                                oinstrumentedCode += otempLine;
                                                oinstrumentedCode += "\n";
                                            } 
                                            otempList.clear();
                                        } else {
                                            tempList.add( line );
                                            otempList.add( oline );
                                        }
                                        if( conditionCount==0 ) {
                                            mutation = true;
                                        } 
                                        conditionCount--;
                                    } else {
                                        if( conditionCount==0 ) {
                                            instrumentedCode += line;
                                            instrumentedCode += "\n";
                                            oinstrumentedCode += oline;
                                            oinstrumentedCode += "\n";
                                            if( line.contains( "return ") ) {
                                                System.out.println( "Return: " + line );
                                            } 
                                        } else {
                                            tempList.add( line );
                                            otempList.add( oline );
                                        } 
                                    } 
                                    if( !mutation ) {
                                        line = lnr.readLine();
                                        oline = olnr.readLine();
                                    } 
                                } while( line!=null && !mutation );
                                while( mutation ) {
                                    for( int t=0; t<tempList.size(); t++ ) {
                                        String tempLine = ( String )tempList.get( t );
                                        instrumentedCode += tempLine;
                                        instrumentedCode += "\n";
                                    } 
                                    for( int t=0; t<otempList.size(); t++ ) {
                                        String otempLine = ( String )otempList.get( t );
                                        oinstrumentedCode += otempLine;
                                        oinstrumentedCode += "\n";
                                    } 
                                    mutation = false;
                                }
                            } else {
                                String methodName = isMethodDeclaration( line, cComponents );
                                if( isMutatedMethod && line.contains( "if(" ) ) {
                                    instrumentedCode += line;
                                    instrumentedCode += "\n";
                                    oinstrumentedCode += oline;
                                    oinstrumentedCode += "\n";
                                    String innerCondSuffString = "S: " + codeFile.getName().substring( 0, codeFile.getName().length()-5 ) + " " + this.isMethodDeclaration( mutatedMethod, cComponents ) + " if " + controlCount;
                                    String instrumentedString = "try {\n";
                                    instrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                    instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                    instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                    instrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                    instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                    instrumentedString += "raf.close();\n";
                                    instrumentedString += "} catch( Exception e ) { }\n";
                                    String oinstrumentedString = "try {\n";
                                    oinstrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                    oinstrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                    oinstrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                    oinstrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                    oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                    oinstrumentedString += "raf.close();\n";
                                    oinstrumentedString += "} catch( Exception e ) { }\n";
                                    instrumentedCode += instrumentedString;
                                    oinstrumentedCode += oinstrumentedString;
                                    controlCount++;
                                    methodEndCount++;
                                } else if( isMutatedMethod && line.contains( "else" ) ) {
                                    instrumentedCode += line;
                                    instrumentedCode += "\n";
                                    oinstrumentedCode += oline;
                                    oinstrumentedCode += "\n";
                                    String innerCondSuffString = "S: " + codeFile.getName().substring( 0, codeFile.getName().length()-5 ) + " " + this.isMethodDeclaration( mutatedMethod, cComponents ) + " else " + controlCount;
                                    String instrumentedString = "try {\n";
                                    instrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                    instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                    instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                    instrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                    instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                    instrumentedString += "raf.close();\n";
                                    instrumentedString += "} catch( Exception e ) { }\n";
                                    String oinstrumentedString = "try {\n";
                                    oinstrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                    oinstrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                    oinstrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                    oinstrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                    oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                    oinstrumentedString += "raf.close();\n";
                                    oinstrumentedString += "} catch( Exception e ) { }\n";
                                    instrumentedCode += instrumentedString;
                                    oinstrumentedCode += oinstrumentedString;
                                    controlCount++;
                                    methodEndCount++;
                                } else if( isMutatedMethod && line.contains( "while(" ) ) {
                                    instrumentedCode += line;
                                    instrumentedCode += "\n";
                                    oinstrumentedCode += oline;
                                    oinstrumentedCode += "\n";
                                    String innerCondSuffString = "S: " + codeFile.getName().substring( 0, codeFile.getName().length()-5 ) + " " + this.isMethodDeclaration( mutatedMethod, cComponents ) + " while " + controlCount;
                                    String instrumentedString = "try {\n";
                                    instrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                    instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                    instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                    instrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                    instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                    instrumentedString += "raf.close();\n";
                                    instrumentedString += "} catch( Exception e ) { }\n";
                                    String oinstrumentedString = "try {\n";
                                    oinstrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                    oinstrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                    oinstrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                    oinstrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" );\n";
                                    oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                    oinstrumentedString += "raf.close();\n";
                                    oinstrumentedString += "} catch( Exception e ) { }\n";
                                    instrumentedCode += instrumentedString;
                                    oinstrumentedCode += oinstrumentedString;
                                    controlCount++;
                                    methodEndCount++;
                                } else if( methodName!=null && line.contains( "{" ) ) {
                                    instrumentedCode += line;
                                    instrumentedCode += "\n";
                                    oinstrumentedCode += oline;
                                    oinstrumentedCode += "\n";
                                    String className = codeFile.getName().substring( 0, codeFile.getName().length()-5 );
                                    String sufficiencyString = "S: " + className + " " + methodName + " " + methodCount;
                                    methodCount++;
                                    String instrumentedString = "try {\n";
                                    instrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                    instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                    instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                    instrumentedString += "raf.writeBytes( \"" + sufficiencyString + "\" );\n";
                                    instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                    instrumentedString += "raf.close();\n";
                                    instrumentedString += "} catch( Exception e ) { }\n";
                                    instrumentedCode += instrumentedString;
                                    instrumentedCode += "\n";
                                    String oinstrumentedString = "try {\n";
                                    oinstrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                    oinstrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                    oinstrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                    oinstrumentedString += "raf.writeBytes( \"" + sufficiencyString + "\" );\n";
                                    oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                    oinstrumentedString += "raf.close();\n";
                                    oinstrumentedString += "";
                                    oinstrumentedString += "} catch( Exception e ) { }\n";
                                    oinstrumentedCode += oinstrumentedString;
                                    oinstrumentedCode += "\n";
                                    isMutatedMethod = false;
                                } else if( isMutatedMethod && line.contains( "return" ) ) {
                                    String identifier = this.getReturnIdentifier( line );
                                    System.out.println( "Return 2: " + identifier );
                                    String innerCondSuffString = "S: " + codeFile.getName().substring( 0, codeFile.getName().length()-5 ) + " " + this.isMethodDeclaration( mutatedMethod, cComponents );
                                    String instrumentedString = "try {\n";
                                    instrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                    instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                    instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                    instrumentedString += "raf.writeBytes( \"" + innerCondSuffString + " \" + " + identifier + " );\n";
                                    instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                    instrumentedString += "raf.close();\n";
                                    instrumentedString += "} catch( Exception e ) { }\n";
                                    String oinstrumentedString = "try {\n";
                                    oinstrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                    oinstrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                    oinstrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                    oinstrumentedString += "raf.writeBytes( \"" + innerCondSuffString + " \" + " + identifier + " );\n";
                                    oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                    oinstrumentedString += "raf.close();\n";
                                    oinstrumentedString += "} catch( Exception e ) { }\n";
                                    instrumentedCode += instrumentedString;
                                    oinstrumentedCode += oinstrumentedString;
                                    instrumentedCode += line;
                                    instrumentedCode += "\n";
                                    oinstrumentedCode += oline;
                                    oinstrumentedCode += "\n";
                                    isMutatedMethod = false;
                                } else if( isMutatedMethod && line.contains( "}" ) ) {
                                    if( methodEndCount==0 ) {
                                        String innerCondSuffString = "S: " + codeFile.getName().substring( 0, codeFile.getName().length()-5 ) + " " + this.isMethodDeclaration( mutatedMethod, cComponents ) ;
                                        String instrumentedString = "try {\n";
                                        instrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                        instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                        instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                        instrumentedString += "raf.writeBytes( \"" + innerCondSuffString + " \" + this.toString() );\n";
                                        instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                        instrumentedString += "raf.close();\n";
                                        instrumentedString += "} catch( Exception e ) { }\n";
                                        String oinstrumentedString = "try {\n";
                                        oinstrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                        oinstrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                        oinstrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                        oinstrumentedString += "raf.writeBytes( \"" + innerCondSuffString + "\" + this.toString() );\n";
                                        oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                        oinstrumentedString += "raf.close();\n";
                                        oinstrumentedString += "} catch( Exception e ) { }\n";
                                        instrumentedCode += instrumentedString;
                                        oinstrumentedCode += oinstrumentedString;
                                        System.out.println();
                                    } 
                                    instrumentedCode += line;
                                    instrumentedCode += "\n";
                                    oinstrumentedCode += oline;
                                    oinstrumentedCode += "\n";                                            
                                    methodEndCount--;
                                } else {
                                    instrumentedCode += line;
                                    instrumentedCode += "\n";
                                    oinstrumentedCode += oline;
                                    oinstrumentedCode += "\n";
                                } 
                            } 
                            line = lnr.readLine();
                            oline = olnr.readLine();
                            System.out.println(line);
                        } while( line!=null );
                        raf.writeBytes( instrumentedCode );
                        raf.close();
                        oraf.writeBytes( oinstrumentedCode );
                        oraf.close();
                    } else {
                        oline = olnr.readLine();
                        do {
                            raf.writeBytes( line );
                            raf.writeBytes( "\n" );
                            oraf.writeBytes( oline );
                            oraf.writeBytes( "\n" );
                            String methodName = isMethodDeclaration( line, cComponents );
                            if( methodName!=null && line.contains( "{" ) ) {
                                String className = codeFile.getName().substring( 0, codeFile.getName().length()-5 );
                                String sufficiencyString = "S: " + className + " " + methodName + " " + methodCount;
                                methodCount++;
                                String instrumentedString = "try {\n";
                                instrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                instrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                instrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";
                                instrumentedString += "raf.writeBytes( \"" + sufficiencyString + "\" );\n";
                                instrumentedString += "raf.writeBytes( \"/n\" );\n";
                                instrumentedString += "raf.close();\n";
                                instrumentedString += "} catch( Exception e ) { }\n";
                                String oinstrumentedString = "try {\n";
                                oinstrumentedString += "java.io.File myFile = new java.io.File( traceFile );\n";
                                oinstrumentedString += "java.io.RandomAccessFile raf = new java.io.RandomAccessFile( myFile, \"rw\" );\n";
                                oinstrumentedString += "raf.skipBytes( ( int )myFile.length() );\n";                                    
                                oinstrumentedString += "raf.writeBytes( \"" + sufficiencyString + "\" );\n";
                                oinstrumentedString += "raf.writeBytes( \"/n\" );\n";
                                oinstrumentedString += "raf.close();\n";
                                oinstrumentedString += "";
                                oinstrumentedString += "} catch( Exception e ) { }\n";
                                raf.writeBytes( instrumentedString );
                                raf.writeBytes( "\n" );
                                oraf.writeBytes( oinstrumentedString );
                                oraf.writeBytes( "\n" );
                            } 
                            line = lnr.readLine();
                            oline = olnr.readLine();
                        } while( line!=null );
                        raf.close();
                        oraf.close();
                    } 
                } 
            }

        } catch( Exception exception ) {
            exception.printStackTrace();
        }
    }

    public String findMutatedMethod( String line ) {
        String methodName = "";
        int index = 0;
        char ch = ' ';
        do {
            ch = line.charAt( index++ );
        } while( ch!='.' );
        do {
            ch = line.charAt( index++ );
            if( ch!='(' ) {
                methodName += ch;
            } 
        } while( ch!='(' );
        return methodName;
    }

    public String getIdentifier( String line ) {
        String identifier = "";
        char ch = ' ';
        int index = 0;
        line = line.trim();
        System.out.println( line );
        if( line.startsWith( "this." ) ) {
            index = 5;
        } else if( line.startsWith( "int" ) ) {
            index = 3;
        } else if( line.startsWith( "long" ) ) {
            index = 4;
        } else if( line.startsWith( "float" ) ) {
            index = 5;
        } else if( line.startsWith( "double" ) ) {
            index = 6;
        } else if( line.startsWith( "char" ) ) {
            index = 4;
        } else if( line.startsWith( "byte" ) ) {
            index = 4;
        } else if( line.startsWith( "boolean" ) ) {
            index = 7;
        }
        if( line.contains( "=" ) ) {
            do {
                if( ch!=' ' && ch!='/' ) {
                    identifier += ch;
                } 
                ch = line.charAt( index++ );
            } while( ch!='=' );            
        } else {
            int rand = ( int )( Math.random() * 100 );
            identifier += rand;
        }
        return identifier;
    } 

    public String findMethodHeader( String methodName, ClassComponents classComponents ) {
        ArrayList mmList = classComponents.getMmList();
        for( int mm=0; mm<mmList.size(); mm++ ) {
            MemberMethod mMethod = ( MemberMethod )mmList.get( mm );
            if( methodName.equals( mMethod.getMethodName() ) ) {
                return mMethod.getMethodHeader();
            } 
        }
        return "";
    }

    public String isMethodDeclaration( String line, ClassComponents cComponents ) {
        ArrayList mmList = cComponents.getMmList();
        for( int m=0; m<mmList.size(); m++ ) {
            MemberMethod mm = ( MemberMethod )mmList.get( m );
            if( line.contains( mm.getMethodName() ) ) {
                return mm.getMethodName();
            } 
        }
        return null;
    } 

    public String[] getPredicateComponents( String predicate ) {
        String []comps = new String[ 3 ];
        String temp = "";
        for( int p=0; p<predicate.length(); p++ ) {
            char ch = predicate.charAt( p );
            if( ch=='<' || ch=='>' ) {
                comps[ 0 ] = temp;
                char ch1 = predicate.charAt( ++p );
                if( ch1=='=' ) {
                    comps[ 1 ] = ch + "=";
                } else {
                    comps[ 1 ] = ch + "";
                    p--;
                }
                temp = "";
            } else if( ch=='=' || ch=='!' ) {
                comps[ 0 ] = temp;
                comps[ 1 ] = ch + "=";
                p++;
                temp = "";
            } else {
                temp += ch;
            } 
        } 
        comps[ 2 ] = temp;
        return comps;
    }

    
    public String[] getReferenceConditionOperands( String condition ) {
        String[] operands = new String[ 2 ];
        operands[ 0 ] = "";
        operands[ 1 ] = "";
        
        StringTokenizer tokenizer = new StringTokenizer( condition, "." );
        int index=0;
        while( tokenizer.hasMoreTokens() ) {
            index++;
            String token = tokenizer.nextToken();
            System.out.println( "TOKEN: " + token );
            if( condition.contains( "equals" ) && index==2 ) {
                operands[ 0 ] = token;
            } //END if CONDITION
            if( condition.contains( "==" ) && index==2 ) {
                char ch = ' ';
                int t = 1;
                ch = token.charAt( 0 );
                do {
                operands[ 0 ] += ch;
                ch = token.charAt( t++ );
            } while( ch!='=' );
            operands[ 0 ] = operands[ 0 ].trim();
        }
        if( ( condition.contains( "equals" ) && index==4 ) || ( condition.contains( "==" ) && index==3 ) ) {
            char ch = ' ';
            int t = 1;
            ch = token.charAt( 0 );
            do {
                operands[ 1 ] += ch;
                ch = token.charAt( t++ );
            } while( ch!=')' );
            operands[ 1 ] = operands[ 1 ].trim();
        }
    }
        return operands;
    }

    public ArrayList getPredicateList( String condition ) {
        ArrayList predicateList = new ArrayList();
        String predicate = "";
        int p = 0;
        char ch = ' ';
        do {
            ch = condition.charAt( p++ );
        } while( ch!='(' );
        for( ; p<condition.length(); p++ ) {
            ch = condition.charAt( p );
            if( ch=='&' || ch=='|' ) {
                p++;
                predicateList.add( predicate );
                predicate = "";
            } else if( ch==')' ) {
                predicateList.add( predicate );
                predicate = "";
            } else if( ch!=' ' ) {
                predicate += ch;
            }
        }
        return predicateList;
    }
    
    public boolean isStateVariable( String operand1, ClassComponents cComponents ) {
        ArrayList dmList = cComponents.getDmList();
        for( int d=0; d<dmList.size(); d++ ) {
            Token token = ( Token )dmList.get( d );
            if( token.getToken().equals( operand1 ) ) {
                return true;
            } 
        }
        
        return false;
    }
    
    public String getWhilePredicate( String condition ) {
        String predicate = "";
        int p = 0;
        char ch = ' ';
        do {
            ch = condition.charAt( p++ );
        } while( ch!='(' );
        for( ; p<condition.length(); p++ ) {
            ch = condition.charAt( p );
            if( ch!=')' ) {
                predicate += ch;
            } else {
                return predicate;
            }
        }
        
        return predicate;
    }

    public String getObject( String line ) {
        String object = "";
        StringTokenizer tokens = new StringTokenizer( line, " " );
        WHILE: while( tokens.hasMoreTokens() ) {
            String temp = tokens.nextToken();
            if( temp.equals( "=" ) ) {
                return object;
            } else {
                object = temp;
            }
        }
        return object;
    }
    
    public String getReturnIdentifier( String line ) {
        String identifier = "";
        StringTokenizer tokenizer = new StringTokenizer( line, " " );
        WHILE: while( tokenizer.hasMoreTokens() ) {
            identifier = tokenizer.nextToken();
            if( identifier.contains( ";" ) ) {
                break WHILE;
            } 
        }
        return identifier.substring( 0, identifier.length()-1 );
    }
    
    public static void main(String[] args) {
        String filePath="Programs/LinearStack/Stack.java";
        EMScanner scanner = new EMScanner(new File(filePath));
        ClassComponents cc = new ClassComponents(scanner.getTokenList(), filePath);
        cc.extractClassComponents(0);
        TraceBack traceBack = new TraceBack(cc);
        traceBack.trace("ROR");
    }

}