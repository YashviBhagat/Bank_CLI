package com.bankcli.repository;
import com.bankcli.domain.Transaction;

import java.util.List;

public interface TransactionDAO {
    void addTransaction(Transaction transaction); // perform 3 types of transaction:Deposit/Withdraw/Transfer
    List<Transaction> getTransactionByAccId(int acc_id); // get the transaction History using account ID
    
}
