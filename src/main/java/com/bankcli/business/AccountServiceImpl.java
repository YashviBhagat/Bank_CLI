package com.bankcli.business; 

import com.bankcli.repository.AccountDAO;
import com.bankcli.domain.Account;

public class AccountServiceImpl implements AccountService{
    private final AccountDAO accountDAO;

    public AccountServiceImpl(AccountDAO accountDAO){
        this.accountDAO = accountDAO;

    }
    @Override 
    public  void register(int accountId, String pin){
        if(accountId < 1000 || accountId > 9999){
            throw new IllegalArgumentException("Account ID must be exactly 4 digits");

        }
        if(accountDAO.getAccountById(accountId)!=null){
            throw new IllegalArgumentException("Account ID already exists!");
        }
        accountDAO.addAccount(new Account(accountId, pin, 0.00));
        

    }
    @Override 
    public Account login(int accountId, String pin){
        Account account = accountDAO.getAccountById(accountId);
        if(account == null){
            throw new IllegalArgumentException("Account not found!");
        }
        if (!account.getPin().equals(pin)) {
        throw new IllegalArgumentException("Incorrect PIN");

    }
     return account;

        
    }
    @Override 
    public  double checkBalance(int accountId){
        return accountDAO.getAccountById(accountId).getCurrBalance();
    }

    
}
