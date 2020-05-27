/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jBillu
 */

public class Triangle {

   private double s;
   private double area;
   private int type = 0;
   private int a;
   private int b;
   private int c;
   
   private String traceFile;
   
   public Triangle() {
   } //END Triangle() CONSTRUCTOR
   
   public Triangle( String tf ) {
       traceFile = tf;
   } //END Triangle() CONSTRUCTOR

   public void setA( int _a ) {
       a = _a;
       if( a<0 ) {
           a = a * -1;
       } //END if STATEMENT
   } //END setA() METHOD
   
   public void setB( int _b ) {
       b = _b;
       if( b<0 ) {
           b = b * -1;
       } //END if STATEMENT
   } //END setB() METHOD
   
   public void setC( int _c ) {
       c = _c;
       if( c<0 ) {
           c = c * -1;
       } //END if STATEMENT
   } //END setC() METHOD

   public int triangle() {
       int d = a + b;
       int ee = a + c;
       int f = b + c;
       
       if( d > c ) {
           if( ee > b ) {
               if( f > a ) {
                   if( a > 0 ) {
                       if( b > 0 ) {
                           if( c > 0 ) {
                               int g = a + b + c;
                               s = g / 2.0;
                               area = s * s - a * s - b * s - c;
                               
                               if( a==b && b==c ) {
                                   type = 1;
                               } //END if STATEMENT
                               if( type==0 ) {
                                   if( a==b ) {
                                       type = 2;
                                   } //END if STATEMENT
                                   if( b==c ) {
                                       type = 2;
                                   } //END if STATEMENT
                                   if( a==c ) {
                                       type = 2;
                                   } //END if STATEMENT
                               } //END if STATEMENT
                               if( type==0 ) {
                                   type = 3;
                               } //END if STATEMENT
                               
                           } //END if STATEMENT
                       } //END if STATEMENT
                   } //END if STATEMENT
               } //END if STATEMENT
           } //END if STATEMENT
       } //END if STATEMENT
       
       return type;
       
   } //END triangle() METHOD   

} //END Triangle CLASS