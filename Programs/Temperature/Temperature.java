/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jBillu
 */

public class Temperature {
    
    private String traceFile;

  private double temp;
  
  public Temperature() {
      temp = 0.0;
  } //END Temperature() CONSTRUCTOR

  public Temperature( String tf ) {
      temp = 0.0;
      traceFile = tf;
  } //END Temperature() CONSTRUCTOR

  public double cTOk( double c ) {
      temp = c + 273;
      return temp;
  } //END cTOk() METHOD
  
  public double cTOf( double c ) {
      temp = c * 9 / 5;
      temp = temp + 32;
      return 0.0;
  } //END cTOf() METHOD

  public double kTOc( double k ) {
      temp = k - 273;
      return temp;
  } //END kTOc() METHOD
  
  public double kTOf( double k ) {
      temp = k * 9 / 5;
      temp = temp - 459;
      return temp;
  } //END kTOf() METHOD
  
  public double fTOk( double f ) {
      temp = f + 459;
      temp = temp * 5 / 9;
      return temp;
  } //END fTOk() METHOD
  
  public double fTOc( double f ) {
      temp = f - 32;
      temp = temp * 5 / 9;
      return temp;
  } //END fTOc() METHOD

} //END Temperature CLASS