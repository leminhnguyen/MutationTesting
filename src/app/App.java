package app;

import components.*;
import mutants.*;
import utils.*;

import java.io.File;
import java.io.IOException;

import algorithm.*;

public class App {

    public static void setup() {
        try{
            Runtime.getRuntime().exec(new String[] { System.getenv("SHELL"), "-c", "rm -r assets/instrument/*" });
            Runtime.getRuntime().exec(new String[] { System.getenv("SHELL"), "-c", "rm -r assets/oinstrument/*" });
            Runtime.getRuntime().exec(new String[] { System.getenv("SHELL"), "-c", "rm -r assets/mutants/*" });
            Runtime.getRuntime().exec(new String[] { System.getenv("SHELL"), "-c", "rm -r assets/traces/*" });
        }catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public static void main(String[] args) throws Exception {

        // setup project
        App.setup();

        long startTime = System.currentTimeMillis();

        // scan tokens
        String filePath="Programs/LinearStack/Stack.java";
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
        int count=0; float mutationScore=0;
        for (int t=0; t<EMConstants.TARGETS.size(); ){
            Target target = (Target) EMConstants.TARGETS.get(t);
            Genetic genetic = new Genetic(cc, target);
            System.out.println("------------------ Try to kill Target " + (count+1) + " ------------------------");
            boolean res = genetic.executeGA();
            if(res){
                System.out.println("Mutant has been killed");
            }else{
                System.out.println("Mutant alive");
                t++;
            }
            count++;
            mutationScore = (float) EMConstants.ACHIEVED_TARGETS.size()/(EMConstants.ACHIEVED_TARGETS.size()+EMConstants.TARGETS.size());
            System.out.println("Mutant Score: " + mutationScore + "\n");
        }

        long endTime = System.currentTimeMillis();

        System.out.println("\n------------Result----------------");
        System.out.println("Total Time: " + (float)(endTime-startTime)/1000 + "s");
        System.out.println("The numbers of testcases: " + EMConstants.POPULATION_SIZE);
        System.out.println("Max iterations: " + EMConstants.MAX_ITERATIONS);
        System.out.println("Mutation rate: " + (float) (EMConstants.MAX_ITERATIONS/EMConstants.MUTATION_RATE)*10 + "%");
        System.out.println("Final mutant score: " + mutationScore);

    }
}