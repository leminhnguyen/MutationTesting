/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jBillu
 */

public class ElectricHeater {
    
    private String traceFile;
    private int temperature;
    private int status;
    
    public ElectricHeater() {
        temperature = 0;
        status  = 0;
    } //END ElectricHeater() CONSTRUCTOR
    
    public ElectricHeater( String tf ) {
        traceFile = tf;
    } //END ElectricHeater() CONSTRUCTOR

    public int turnOn( int t ) {
        if( t<0 ) {
            t = t * -1;
        } //END if STATEMENT
        if( status==0 ) {
            if( t<17 ) {
                status = 1;
                temperature = temperature + t;
            } //END if STATEMENT
        } //END if STATEMENT
        return temperature;
    } //END turnOn() METHOD
    
    public void method1() {
    } //END method1()
    
    public int turnOff() {
        int off = 0;
        if( status==1 ) {
            status = 0;
            temperature = 17;
            off = 1;
        } //END if STATEMENT
        return off;        
    } //END turnOff() METHOD
    
    public void method2() {
    } //END method1()
    
    public int increaseHead( int t ) {
        if( t<0 ) {
            t = t * -1;
        } //END if STATEMENT
        if( status==1 ) {
            if( t<17 ) {
                if( temperature<50 ) {
                    temperature = temperature + t;
                } //END if STATEMENT
            } //END if STATEMENT
        } //END if STATEMENT
        return temperature;
    } //END increaseHeat() METHOD
    
    public void method31() {
    } //END method1()
    
    public int reduceHeat( int t ) {
        if( t<0 ) {
            t = t * -1;
        } //END if STATEMENT
        if( status==1 ) {
            if( t<17 ) {
                if( temperature>17 ) {
                    temperature = temperature - t;
                } //END if STATEMENT
            } //END if STATEMENT
        } //END if STATEMENT
        return temperature;
    } //END reduceHead() METHOD
    
    public void method4() {
    } //END method1()
    
    public int roomTemperature() {
        int temp = 0;
        if( status==1 ) {
            temp = temperature;
        } //END if STATEMENT
        return temp;
    } //END roomTemperature() METHOD
    
    public void method5() {
    } //END method1()
    
    public void autoController( int current ) {
        if( temperature>=current ) {
            turnOff();
        } //END if STATEMENT
        if( temperature<current ) {
            turnOn( temperature );
        } //END if STATEMENT
    } //END autoController() METHOD
    
    public void method6() {
    } //END method1()
    
    public int isWorking() {
        int working = 0;
        if( status==1 ) {
            working = 1;
        } //END if STATEMENT
        return working;
    } //END isWorking() METHOD
    
} //END ElectricHeater CLASS