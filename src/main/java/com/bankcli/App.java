package com.bankcli;

import com.bankcli.business.*;
import com.bankcli.repository.*;
import com.bankcli.domain.*;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // wire up the layers (constructor injection)
        AccountDAO accountDAO = new AccountDAOImpl();
        TransactionDAO transactionDAO = new TransactionDAOImpl();
        AccountService accountService = new AccountServiceImpl(accountDAO);
        TransactionService txService = new TransactionServiceImpl(accountDAO, transactionDAO);

        // 1. register a new account (4-digit)
        System.out.println("--- Register 4444 ---");
        accountService.register(4444, "0000");
        System.out.println("Balance: " + accountService.checkBalance(4444));

        // 2. deposit
        System.out.println("--- Deposit 200 into 4444 ---");
        txService.deposit(4444, 200.00);
        System.out.println("Balance: " + accountService.checkBalance(4444));

        // 3. withdraw
        System.out.println("--- Withdraw 50 from 4444 ---");
        txService.withdraw(4444, 50.00);
        System.out.println("Balance: " + accountService.checkBalance(4444));

        // 4. transfer to an existing account (use 1001 or 2002 from earlier)
        System.out.println("--- Transfer 30 from 4444 to 1001 ---");
        txService.transfer(4444, 1001, 30.00);
        System.out.println("4444 balance: " + accountService.checkBalance(4444));
        System.out.println("1001 balance: " + accountService.checkBalance(1001));

        // 5. history
        System.out.println("--- History for 4444 ---");
        List<Transaction> history = txService.getHistory(4444);
        history.forEach(System.out::println);
    }
}
