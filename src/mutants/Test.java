package mutants;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;

public class Test {
    public static void main(String[] args) {
        try {
            File file = new File("/home/nguyenlm/Documents/Projects/Projects-DriverD/Java/MutationTesting2/trace.txt");
            LineNumberReader lnr = new LineNumberReader(new FileReader(file));
            String lineI = lnr.readLine();
            System.out.println(lineI);
            String lines[] = lineI.split("/n");
            File file2 = new File("/home/nguyenlm/Documents/Projects/Projects-DriverD/Java/MutationTesting2/test.txt");
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.setLength(0);
            for(int l=0; l<lines.length; l++){
                System.out.println(lines[l]);
                raf.writeBytes(lines[l] + "\n");
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
}