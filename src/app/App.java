package app;

import components.*;
import mutants.*;
import utils.*;

import java.io.File;

import algorithm.*;

public class App {

    public static void main(String[] args) throws Exception {

        // scan tokens
        String filePath="Programs/HashTable/HashTable.java";
        EMScanner scanner = new EMScanner(new File(filePath));
        ClassComponents cc = new ClassComponents(scanner.getTokenList(), filePath);
        cc.extractClassComponents(0);

        // generate mutant
        ROR ror = new ROR(cc);
        ror.generateMutants();
        scanner.printTokens();

        // add the traces to the mutants
        TraceBack traceBack = new TraceBack(cc);
        traceBack.trace("ROR");

        // execute GA algorithm to kill mutants
        
        int count=0;
        for (int t=0; t<EMConstants.TARGETS.size(); ){
            Target target = (Target) EMConstants.TARGETS.get(t);
            Genetic genetic = new Genetic(cc, target);
            System.out.println("------------------ Execute Target " + (count+1) + " ------------------------");
            boolean res = genetic.executeGA();
            if(res){
                System.out.println("Mutant has been killed\n");
            }else{
                System.out.println("Mutant alive\n");
                t++;
            }
            count++;
            System.out.println("Mutant Score: " + (float) EMConstants.ACHIEVED_TARGETS.size()/(EMConstants.ACHIEVED_TARGETS.size()+EMConstants.TARGETS.size()));
        }
        

        System.out.println("Mutant Score: " + (float) EMConstants.ACHIEVED_TARGETS.size()/(EMConstants.ACHIEVED_TARGETS.size()+EMConstants.TARGETS.size()));
    }
}