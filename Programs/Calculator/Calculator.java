public class Calculator {

   private double result;
   private String traceFile;

   public Calculator() {
      result = 0.0;
   } //END Calculator() CONSTRUCTOR

   public Calculator( String tf ) {
      result = 0.0;
      traceFile = tf;
   } //END Calculator() CONSTRUCTOR

   public double add( double a, double b ) {
      if( a!=0 || b!=0 ) {
         result = a+b;
      } //END if STATEMENT
      return result;
   } //END add() METHOD
   
   public double difference( double a, double b ) {
      result = a - b;
      if( result<0.0 ) {
         result = result * -1;
      } //END if STATEMENT
      return result;
   } //END difference() METHOD
   
   public double product( double a, double b ) {
      result = a * b;
      return result;
   } //END product() METHOD

   public int divisor( int a, int b ) {
      int div = 0;
      if( b>0 ) {
         div = a / b;
      } //END if STATEMENT
      return div;
   } //END divistor() METHOD
   
   public int remainder( int a, int b ) {
      int rem = 0;
      if( b>0 ) {
         rem = a % b;
      } //END if STATEMENT
      return rem;
   } //END remainder() METHOD

} //END Calculator CLASS