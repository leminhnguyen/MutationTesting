public class HashTable {
    
    private int size;
    private int element;
    private int length;
    
    private String traceFile;
    
    public HashTable() {
        size = 0;
    } 
    
    public HashTable( String tf ) {
        traceFile = tf;
    } 
    
    public void setSize( int s ) {
        if( s>0 ) {
            size = s;
        } 
    } 
    
    public void method1() {
    } 
    
    public void insert( int key ) {
        int hash = -1;
        if( key>50 && length<size ) {
            hash = hashCode( key );
            element = key;
            length = length + 1;
        } 
    } 
    
    public void method2() {
    } 
    
    public int delete( int key ) {
        int hash = -1;
        if( key>50 && length>2 ) {
            hash = hashCode( key );
            length = length - 1;
            element = hash * length;
        } 
        return element;
    }
    
    public void method3() {
    } 
    
    public int contains( int key ) {
        int result = 0;
        if( length>2 ) {
            result = result + 2;
        } 
        return result;
    } 

    public void method4() {
    } 
    
    public int hashCode( int key ) {
        int hash = 0;
        hash = key + key * 5;
        if( hash<0 ) {
            hash = hash * -1;
        } 
        return hash;
    } 
    
    public void method5() {
    } 
    
    public void clear() {
        if( length>0 ) {
            length = 0;
            element = 0;
        } 
    } 
    
    public void method6() {
    } 
    
} 