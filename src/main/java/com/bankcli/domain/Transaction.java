package com.bankcli.domain;



import java.time.LocalDateTime;

public class Transaction {
    private int tran_id;
    private int from_acc_id;
    private int to_acc_id;
    private String tran_type;
    private double tran_amount;
    private LocalDateTime timestamp;

    public Transaction(int tran_id,int from_acc_id,int to_acc_id, String tran_type,double tran_amount,LocalDateTime timestamp){
        this.tran_id = tran_id;
        this.from_acc_id = from_acc_id;
        this.to_acc_id = to_acc_id;
        this.tran_type = tran_type;
        this.tran_amount = tran_amount;
        this.timestamp = timestamp;
    }
    @Override 
    public String toString(){
        return String.format("Transaction ID: %d | From Account: %d | To Account: %d | Transaction Type: %s | Transaction amount: %.2f | Transaction Time: %s",
          tran_id, from_acc_id, to_acc_id, tran_type, tran_amount, timestamp );
    }

    public int getTranId(){return tran_id;}
    public int getFromAccId(){return from_acc_id;}
    public int getToAccId(){return to_acc_id;}
    public String getTranType() { return tran_type; }
    public double getTranAmount() { return tran_amount;}
    public LocalDateTime getTimestamp() { return timestamp;}  
}


