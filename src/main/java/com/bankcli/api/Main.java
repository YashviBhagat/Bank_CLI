package com.bankcli.api;

import com.bankcli.repository.*;
import com.bankcli.business.*;
public class Main {
    public static void main(String[] args) {
        AccountDAO accountDAO = new AccountDAOImpl();
        TransactionDAO transactionDAO = new TransactionDAOImpl();
        AccountService accountService = new AccountServiceImpl(accountDAO);
        TransactionService transactionService = new TransactionServiceImpl(accountDAO, transactionDAO);

        new BankREPL(accountService, transactionService).run();
    }
}