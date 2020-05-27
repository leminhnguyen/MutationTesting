/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jBillu
 * 
 * @equvalent
 * 
 */

public class HashTable {
    
    private int size;
    
    private int element;
    private int length;
    
    private String traceFile;
    
    public HashTable() {
        size = 0;
    } //END HashTable() CONSTRUCTOR
    
    public HashTable( String tf ) {
        traceFile = tf;
    } //END HashTable() CONSTRUCTOR
    
    public void setSize( int s ) {
        if( s>0 ) {
            size = s;
        } //END if STATEMENT
    } //END setSize() METHOD
    
    public void method1() {
    } //END method1() METHOD
    
    public void insert( int key ) {
        int hash = -1;
        if( key>50 && length<size ) {
            hash = hashCode( key );
            element = key;
            length = length + 1;
        } //END if STATEMENT
    } //END insert() METHOD
    
    public void method2() {
    } //END method1() METHOD
    
    public int delete( int key ) {
        int hash = -1;
        if( key>50 && length>2 ) {
            hash = hashCode( key );
            length = length - 1;
            element = hash * length;
        } //END if STATEMENT
        return element;
    } //END delete() METHOD
    
    public void method3() {
    } //END method1() METHOD
    
    public int contains( int key ) {
        int result = 0;
        if( length>2 ) {
            result = result + 2;
        } //END if STATEMENT
        return result;
    } //END contains() METHOD

    public void method4() {
    } //END method1() METHOD
    
    public int hashCode( int key ) {
        int hash = 0;
        hash = key + key * 5;
        if( hash<0 ) {
            hash = hash * -1;
        } //END if STATEMENT
        return hash;
    } //END hashCode() METHOD
    
    public void method5() {
    } //END method1() METHOD
    
    public void clear() {
        if( length>0 ) {
            length = 0;
            element = 0;
        } //END if STATEMETN
    } //END clear() METHOD
    
    public void method6() {
    } //END method1() METHOD
    
} //END HashTable CLASS