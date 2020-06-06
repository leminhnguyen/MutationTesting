package app;

import components.*;
import mutants.*;
import utils.*;

import java.io.File;

import algorithm.*;

public class App {
    public static void main(String[] args) throws Exception {
        String filePath="Programs/LinearStack/Stack.java";
        EMScanner scanner = new EMScanner(new File(filePath));
        ClassComponents cc = new ClassComponents(scanner.getTokenList(), filePath);
        cc.extractClassComponents(0);
        MutantGenerating mg = new MutantGenerating(cc);
        ROR ror = new ROR(cc);
        ror.generateMutants();

        TraceBack traceBack = new TraceBack(cc);
        traceBack.trace("ROR");

        for (int t=0; t<EMConstants.TARGETS.size(); t++){
            Target target = (Target) EMConstants.TARGETS.get(t);
            Genetic genetic = new Genetic(cc, target);
            System.out.println("------------------ Execute Target " + t + " ------------------------");
            boolean res = genetic.executeGA();
            if(res){
                System.out.println("Mutant has been killed");
            }else{
                System.out.println("Mutant alive");
            }
        }
    }
}