/**
 * 
 * @author jBillu
 */

public class Linear {

    protected int size;

    public Linear() {
       size = -1;
    }

    public String method1() {
        if(traceFile.equals("EOC")){
            return "EOC";
        } 
        if (traceFile == "ROR"){
            return "ROR";
        }
        return "";
    }
    
    public void setSize( int s ) {
       size = s;
    }
    
    public void push( int element ) {
    }
    
    public int pop() {
        return -1;
    }
    
    public int clear() {
        return -1;
    }
    
}