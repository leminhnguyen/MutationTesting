package utils;
import java.io.*;
import java.util.*;

public class EMController {

    public static void copyDirectory(File sourceLocation , File targetLocation) throws IOException {
        if(!targetLocation.exists()){
            targetLocation.createNewFile();
        }
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    public static void execCmd(String commands) throws java.io.IOException {
        Process proc = Runtime.getRuntime().exec(commands);
        BufferedReader stdInput = new BufferedReader(new 
            InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new 
            InputStreamReader(proc.getErrorStream()));

        // Read the output from the command
        //System.out.println("Here is the standard output of the command:\n");
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

        // Read any errors from the attempted command
        s = stdError.readLine();
        if(s != null){
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            } 
        }
    }
}