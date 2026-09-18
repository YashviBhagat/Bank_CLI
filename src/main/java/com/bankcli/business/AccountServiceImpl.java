package com.bankcli.business; 

import com.bankcli.repository.AccountDAO;
import com.bankcli.domain.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountServiceImpl implements AccountService{

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountDAO accountDAO;

    public AccountServiceImpl(AccountDAO accountDAO){
        this.accountDAO = accountDAO;

    }
    @Override 
    public  void register(int accountId, String pin){
        if(accountId < 1000 || accountId > 9999){
            logger.error("Registration failed: account ID {} is not 4 digits", accountId);
            throw new IllegalArgumentException("Account ID must be exactly 4 digits");

        }
        if(accountDAO.getAccountById(accountId)!=null){
            logger.info("Registration failed: account {} already exists!", accountId);
            throw new IllegalArgumentException("Account ID already exists!");
        }

        accountDAO.addAccount(new Account(accountId, pin, 0.00));
        logger.info("Account {} registered successfully", accountId);
        

    }
    @Override 
    public Account login(int accountId, String pin){
        Account account = accountDAO.getAccountById(accountId);
        if(account == null){
            logger.error("Login failed: account {} not found", accountId);
            throw new IllegalArgumentException("Account not found!");
        }
        if (!account.getPin().equals(pin)) {
            logger.error("Login failed: incorrect PIN for account {}", accountId);
            throw new IllegalArgumentException("Incorrect PIN");

        }
        logger.info("Account {} logged in successfully", accountId);
        return account;

        
    }
    @Override 
    public  double checkBalance(int accountId){
        return accountDAO.getAccountById(accountId).getCurrBalance();
    }

    
}
