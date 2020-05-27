/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jBillu
 *
 * @equvalent ROR 1, 34, 43, 48
 *
 */

public class AutoDoor {
    
    private String traceFile;
    private int angle;
    private int locked;
    
    public AutoDoor() {
        angle = 0;
        locked = 0;
    } //END AutoDoor() CONSTRUCTOR
    
    public AutoDoor( String tf ) {
        traceFile = tf;
        locked = 0;
    } //END AutoDoor() CONSTRUCTOR
    
    public void method2() {
    } //END method2() METHOD

    public int openDoor( int a ) {
    		if( a<0 ) {
    		   a = a * -1;
    		}
        int result = 0;
        if( locked==0 ) {
            if( angle<90 ) {
                if( a<45 && a>0 ) {
	                angle = angle + a;
	                result = result + angle;
                } //END if STATEMENT
            } //END if STATEMENT
        } //END if STATEMENT
        return result;
    } //END openDoor() METHOD
    
    public void method3() {
    } //END method2() METHOD

    public int closeDoor( int a ) {
        if( angle>90 && a>0 ) {
            angle = angle - a;
        } //END if STATEMENT
        return angle;
    } //END closeDoor() METHOD
    
    public void method5() {
    } //END method3() METHOD
    
    public int isOpen() {
        int result = 0;
        if( angle>0 ) {
            result = 1;
        } //END if STATEMENT
        return result;
    } //END isOpen() METHOD
    
    public void method8() {
    } //END method2() METHOD

    public int isClose() {
        int result = 0;
        if( angle==0 ) {
            result = 1;
        } //END if STATEMENT
        return result;
    } //END isClose() METHOD
        
    public void method10() {
    } //END method2() METHOD

    public int currentAngle() {
        return angle;
    } //END curruentAngle() METHOD
 
    public void method16() {
    } //END method2() METHOD

    public int lockDoor() {
        int result = 0;
        if( angle<=0 ) {
            result = result + 2;
            locked = 1;
        } //END if STATEMENT
        return result;
    } //END lockDoor() METHOD

    public void method18() {
    } //END method2() METHOD

    public int unlockDoor() {
        int result = 0;
        if( locked==1 ) {
            result = 1;
            locked = 0;
        } //END if STATEMENT
        return result;
    } //END unlockDoor() METHOD

} //END AutoDoor CLASS