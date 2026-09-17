package com.bankcli.business;

import java.util.List;

import com.bankcli.domain.Transaction;

public interface TransactionService {

    void deposit(int accountId, double amount);                    // add funds + log
    void withdraw(int accountId, double amount);                   // check funds! then subtract + log
    void transfer(int fromId, int toId, double amount);            // atomic move + log
    List<Transaction> getHistory(int accountId);                   // audit trail
    
}
