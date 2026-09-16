package com.bankcli.domain;

public class Account {

    private int acc_id;
    private String pin;
    private double curr_balance;

    public Account(int acc_id,String pin,double curr_balance){
        this.acc_id = acc_id;
        this.pin = pin;
        this.curr_balance = curr_balance;

    } 

    @Override 
    public String toString(){
        return String.format("Account ID: %d | Current Balance: %.2f",acc_id , curr_balance);

    }
    public int getAccId(){
        return acc_id;
    }
    public String getPin(){
        return pin;
    }
    public double getCurrBalance(){
        return curr_balance;
    }

    
}






   

    