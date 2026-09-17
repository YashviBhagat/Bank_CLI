package com.bankcli.business;
import java.util.List;


import com.bankcli.repository.*;
import com.bankcli.domain.*;

public class TransactionServiceImpl implements TransactionService {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public TransactionServiceImpl(AccountDAO accountDAO,TransactionDAO transactionDAO){
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    @Override 
    public void deposit(int accountId, double amount){ 
        Account account = accountDAO.getAccountById(accountId);

        if(account == null){
            throw new IllegalArgumentException("Account not exists");
        }
        if(amount <= 0){
            throw new IllegalArgumentException("Amount is not valid");
        }
        double newBalance = account.getCurrBalance() + amount;

        accountDAO.updateBalance(accountId,newBalance);

        transactionDAO.addTransaction(new Transaction(0, accountId, accountId, "DEPOSIT", amount, null));
       
    }
    @Override 
    public void withdraw(int accountId, double amount){
        Account account = accountDAO.getAccountById(accountId);

        if(account == null){
            throw new IllegalArgumentException("Account not exists");
        }
        if (amount <= 0) {
        throw new IllegalArgumentException("Amount must be positive");
        }
        if(amount > account.getCurrBalance()){
            throw new IllegalArgumentException("Account does not have enough balance");

        }
        double newBalance = account.getCurrBalance() - amount;

        accountDAO.updateBalance(accountId,newBalance);

        transactionDAO.addTransaction(new Transaction(0, accountId, accountId, "WITHDRAWAL", amount, null));


    }
    @Override 
    public void transfer(int fromId, int toId, double amount){
       
        Account fromAccount = accountDAO.getAccountById(fromId);
        Account toAccount = accountDAO.getAccountById(toId);


        if(fromAccount == null || toAccount == null){
            throw new IllegalArgumentException("One or both accounts not found");
        }
        if (fromId == toId) {
        throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        if (amount <= 0) {
        throw new IllegalArgumentException("Amount must be positive");
        }
        if (amount > fromAccount.getCurrBalance()) {
        throw new IllegalArgumentException("Insufficient funds");
        }
        // move the money
        accountDAO.updateBalance(fromId, fromAccount.getCurrBalance() - amount);   // deduct
        accountDAO.updateBalance(toId, toAccount.getCurrBalance() + amount);       // add

        transactionDAO.addTransaction(new Transaction(0, fromId, toId, "TRANSFER", amount, null));


    }
    @Override
    public List<Transaction> getHistory(int accountId) {
        Account account = accountDAO.getAccountById(accountId);

        if(account == null){
            throw new IllegalArgumentException("Account not exists");
        }
        return transactionDAO.getTransactionByAccId(accountId);

    }
    
}
