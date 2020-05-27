/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jBillu
 */

public class BankAccount {
    
    private String traceFile;
    
    private int balance;
    private int tax;
    private int profit;
    
    public BankAccount() {
    } //END BankAccount() CONSTRUCTOR
    
    public BankAccount( String tf ) {
        traceFile = tf;
    } //END BankAccount() CONSTRUCTOR
    
    public void deposit( int amount ) {
        if( amount>0 ) {
            balance = balance + amount;
        } //END if STATEMENT
    } //END depost() CONSTRUCTOR

    public int withdraw( int amount ) {
        if( amount>0 ) {
            if( balance>amount ) {
                balance = balance - amount;
            } //END if STATEMENT
        } //END if STATEMENT
        return balance;
    } //END withdraw() METHOD
    
    public int calculateTax( int rate ) {
        if( rate>0 && rate<25 ) {
            tax = balance * rate;
            tax = tax / 100;
            balance = balance - tax;
        } //END if STATEMENT
        return balance;
    } //END calculateTax() METHOD

    public int calculateProfit( int rate ) {
        if( rate>0 && rate<25 ) {
            profit = balance * rate;
            profit = profit / 100;
            balance = balance + profit;
        } //END if STATEMENT
        return balance;        
    } //END calculateProfit() METHOD
    
    public int closeAccount() {
        if( balance>0 ) {
            tax = balance * 2;
            tax = tax / 100;
            balance = balance - tax;
        } //END if STATEMENT
        return balance;
    } //END closeAccount() METHOD

    public int openAccount( int amount ) {
        if( amount>0 ) {
            balance = balance + amount;
            tax = 0;
            profit = 0;
        } //END if STATEMENT
        return balance;
    } //END openAccount() METHOD

} //EDN BankAccount CLASS