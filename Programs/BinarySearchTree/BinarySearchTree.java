/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jBillu
 *
 * @equvalent AOR 1-8 ROR 2,4,6,8,11-18,23,24,25,28,29 UOI 5,7,9,11,13,15,17,19,21,23
 *
 */

public class BinarySearchTree {
    
    private String traceFile;
    
    private int root;
    private int left;
    private int right;
    private int lSize;
    private int rSize;
    
    public BinarySearchTree() {
        root = 0;
        left = 0;
        right = 0;
        lSize = 0;
        rSize = 0;
    } //END BinarySearchTree() CONSTRUCTOR
    
    public BinarySearchTree( String tf ) {
        root = 0;
        left = 0;
        right = 0;
        lSize = 0;
        rSize = 0;
        traceFile = tf;
    } //END BinarySearchTree() CONSTRUCTOR
    
    public void method1() {
    }
    
    public void insert( int i ) {
        if( root==0 ) {
            root = i;
        } //END if STATEMENT
        if( root!=i ) {
            if( i<root ) {
                left = i;
                lSize = lSize + 2;
            } //END if STATEMENT
            if( i>root ) {
                right = i;
                rSize = rSize + 2;
            } //END if-else STATEMENT
        } //END if-else STATEMENT
    } //END insert() METHOD
    
    public void method2() {
    }
    
    public int delete( int i ) {
        int size = lSize + rSize;
        int element = 0;
        if( size>4 ) {
            if( lSize==0 && rSize==0 ) {
                element = root;
                root = 0;
            } //END if STATEMENT
            if( i<root ) {
                if( lSize>0 ) {
                    lSize = lSize - 2;
                    element = lSize * i;
                } //END if STATEMENT
            } //END if STATEMENT
            if( i>root ) {
                if( rSize>0 ) {
                    rSize = rSize - 2;
                    element = rSize * i;
                } //END if STATEMENT                    
            } //END if STATEMENT
        } //END if STATEMENT
        return element;
    } //END delete() METHOD
    
    public void method3() {
    }
    
    public int search( int i ) {
        int result = 0;
        if( root!=0 && i>0 ) {
            if( lSize>0 && rSize>0 ) {
                result = 1;
            } //END if STATEMENT
        } //END if STATEMENT
        return result;
    } //END search() METHOD
    
    public void method4() {
    }
    
    public int findSuccessor( int i ) {
        int parent = 0;
        if( root!=0 ) {
            if( i<root && lSize>0 ) {
                parent = left;
            } //END if STATEMENT
            if( i>root && rSize>0 ) {
                parent = right;
            } //END if STATEMENT
        } //END if STATEMENT
        return parent;
    } //END findSuccessor() METHOD
    
    public void method5() {
    }
    
} //END BinarySearchTree CLASS