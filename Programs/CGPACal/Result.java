public class Result {

  private double cgpa;
  private int totPoints, totCredits;
  private int credits;
  private char grade;
  private int result;
  private String traceFile;
     
  public Result() {
      cgpa = 3.0;
      totPoints = 30;
      totCredits = 12;
      credits = 0;
      grade = 'I';
  } //END Result() METHOD

  public Result( String tf ) {
      traceFile = tf;
  } //END Calculator() CONSTRUCTOR
  
  public void method1() {
  }

  public void method2() {
  }

  public void setEarnedGrade(char grade, int credits) {
      if( grade=='A' || grade=='a' ) {
          this.grade = 'A';
      } else if( grade=='B' || grade=='b' ) {
          this.grade = 'B';
      } else if( grade=='C' || grade=='c' ) {
          this.grade = 'C';
      } else if( grade=='D' || grade=='d' ) {
          this.grade = 'D';
      } else if( grade=='E' || grade=='e' || grade=='F' || grade=='f' ) {
          this.grade = 'F';
      } else {
          int gValue = 0;
          if( grade<=90 ) {
              gValue = ( 90 - ( grade - 65 ) ) % 6;
          } else {
              gValue = ( 122 - ( grade - 97 ) ) % 6;
          } //END if-else STATEMENT          
          this.grade = ( char )gValue;
      } //END if-else STATEMENT

      credits = Math.abs( credits );
      this.credits = credits % 5;
      if( this.credits==0 ) {
          this.credits++;
      } //END if STATEMENT
  } //END setEarnedGrade() METHOD

  public void method3() {
  }

  public void method4() {
  }

  public int calcCGPA() {
      int points = 0;
      if( grade=='A' ) {
          points = 4 * credits;
      } //END if STATEMENT
      if( grade=='B' ) {
          points = 3 * credits;
      } //END if STATEMENT
      if( grade=='C' ) {
          points = 2 * credits;
      } //END if STATEMENT
      if( grade=='D' ) {
          points = 1 * credits;
      } //END if STATEMENT
      if( grade=='F' ) {
          points = 0;
      } //END if STATEMENT

      totPoints = totPoints + points;
      totCredits = totCredits + credits;

      if( totCredits>0 ) {
          cgpa = totPoints / totCredits;
      } //END if STATEMENT
      if( cgpa>=2.5 ) {
          result = 1;
      } else {
          result = 0;
      } //END if-else STATEMENT
      return result;
  } //END calcCGPA() METHOD

} //END Result CLASS
