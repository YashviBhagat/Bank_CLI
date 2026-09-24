package com.bankcli.business;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


import com.bankcli.repository.*;
import com.bankcli.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionServiceImpl implements TransactionService {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    public TransactionServiceImpl(AccountDAO accountDAO,TransactionDAO transactionDAO){
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    @Override 
    public void deposit(int accountId, double amount){ 
        Account account = accountDAO.getAccountById(accountId);

        if(account == null){
            logger.error("deposit failed: account {} not found", accountId);
            throw new IllegalArgumentException("Account not exists");
            
        }
        if(amount <= 0){
            logger.error("deposit failed: account {} has insufficient funds", accountId);
            throw new IllegalArgumentException("Amount is not valid");
        }
        double newBalance = account.getCurrBalance() + amount;

        accountDAO.updateBalance(accountId,newBalance);

        transactionDAO.addTransaction(new Transaction(0, accountId, accountId, "DEPOSIT", amount, null));
        logger.info("deposit success: account {} deposit money successfully", accountId);
    }
    @Override 
    public void withdraw(int accountId, double amount){
        Account account = accountDAO.getAccountById(accountId);

        if(account == null){
            logger.error("withdraw failed: account {} not found", accountId);
            throw new IllegalArgumentException("Account not exists");
            
        }
        if (amount <= 0) {
            logger.error("withdraw failed: account {} has insufficient funds", accountId);
            throw new IllegalArgumentException("Amount must be positive");
        }
        if(amount > account.getCurrBalance()){
            throw new IllegalArgumentException("Account does not have enough balance");

        }
        double newBalance = account.getCurrBalance() - amount;
        

        accountDAO.updateBalance(accountId,newBalance);
        
        transactionDAO.addTransaction(new Transaction(0, accountId, accountId, "WITHDRAWAL", amount, null));
        logger.info("withdraw success: account {} withdraw money successfully", accountId);


    }
    @Override 
    public void transfer(int fromId, int toId, double amount){
       
        Account fromAccount = accountDAO.getAccountById(fromId);
        Account toAccount = accountDAO.getAccountById(toId);


        if(fromAccount == null || toAccount == null){
            logger.error("transfer failed: account {} or {} not found", fromId, toId);
            throw new IllegalArgumentException("One or both accounts not found");
        }
        if (fromId == toId) {

            logger.error("transfer failed: account {} cannot transfer in same account", fromId);
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        if (amount <= 0) {
        throw new IllegalArgumentException("Amount must be positive");
        }
        if (amount > fromAccount.getCurrBalance()) {

            logger.info("transfer failed: account {} has insufficient funds", fromId);
            throw new IllegalArgumentException("Insufficient funds");
        }

        // move the money
        Connection conn = null;
    try {
        conn = ConnectionFactory.getConnectionFactory().getConnection();
        conn.setAutoCommit(false);                                              // begin transaction

        accountDAO.updateBalance(conn, fromId, fromAccount.getCurrBalance() - amount);   // deduct
        accountDAO.updateBalance(conn, toId, toAccount.getCurrBalance() + amount);       // add
        transactionDAO.addTransaction(conn, new Transaction(0, fromId, toId, "TRANSFER", amount, null));  // log

        conn.commit();                                                         // all succeeded → permanent
        logger.info("transfer success: {} from account {} to {}", amount, fromId, toId);
        } catch (Exception e) {
        if (conn != null) {
            try { conn.rollback(); }                                           // any failed → undo all
            catch (SQLException ex) { logger.error("Rollback failed: {}", ex.getMessage()); }
        }
        logger.error("transfer failed, rolled back: {}", e.getMessage());
        throw new IllegalStateException("Transfer failed", e);
    } finally {
        if (conn != null) {
            try { conn.setAutoCommit(true); conn.close(); }                    // reset + release
            catch (SQLException ex) { logger.error("Closing connection failed: {}", ex.getMessage()); }
        }
    }
}



    @Override
    public List<Transaction> getHistory(int accountId) {
        Account account = accountDAO.getAccountById(accountId);

        if(account == null){
            logger.error("history failed: account {} not found", accountId);
            throw new IllegalArgumentException("Account not exists");
        }
        return transactionDAO.getTransactionByAccId(accountId);

    }
    
}
