package com.bankcli.api;
import java.util.List;
import java.util.Scanner;


import com.bankcli.business.*;
import com.bankcli.domain.*;


public class BankREPL {
    private final AccountService accountService;
    private final TransactionService transactionService;

    private final Scanner scanner = new Scanner(System.in);

    private Account currentAccount = null;

    public BankREPL(AccountService accountService,TransactionService transactionService){
        this.accountService = accountService;
        this.transactionService = transactionService;
    }
    public void run(){
        while(true){
            if(currentAccount == null){
                loggedOutMenu();
            }
            else{
                logInMenu();
            }

        }
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine().trim());
    }
    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    private double readDouble(String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(scanner.nextLine().trim());
    }

    public void loggedOutMenu(){
        System.out.println("\n=== Bank of CLI ===");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");

        String choice = readLine("Select an option:");

        switch (choice) {
            case "1" -> register();
            case "2" -> login();
            case "3" -> {
                System.out.println("Goodbye!");
                System.exit(0);
            }
            default -> System.out.println("Invalid option, try again.");
        }
    }
    private void register(){
        try {
        int id = readInt("Choose a 4-digit account ID: ");
        String pin = readLine("Set a PIN: ");
        accountService.register(id, pin);
        System.out.println("Account " + id + " created! Please log in.");
        } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
        }
    }
    private void login(){
        try {
            int id = readInt("Account ID: ");
            String pin = readLine("PIN: ");
            currentAccount = accountService.login(id, pin);
            System.out.println("Welcome, account " + id + "!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }   
    
    public void logInMenu(){
        System.out.println("\n=== Bank of CLI ===");
        System.out.println("1. Check Balance");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. History");
        System.out.println("6. Logout");

        String choice = readLine("Select an option:");

        switch (choice) {
            case "1" -> checkBalance();
            case "2" -> withdraw();
            case "3" -> deposit();
            case "4" -> transfer();
            case "5" -> history();

            case "6" -> {
                System.out.println("Logged Out");
                 currentAccount = null; 
            }
            default -> System.out.println("Invalid option, try again.");
        }
    }
    private void checkBalance(){
        double balance = accountService.checkBalance(currentAccount.getAccId());
        System.out.println("Your balance: $" + balance);

    }
    private void withdraw() {
    try {
        double amount = readDouble("Enter the amount: $");
        transactionService.withdraw(currentAccount.getAccId(), amount);
        System.out.println("Current Balance: $" + accountService.checkBalance(currentAccount.getAccId()));
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
    }
    private void deposit() {
    try {
        double amount = readDouble("Amount to deposit: $");
        transactionService.deposit(currentAccount.getAccId(), amount);
        System.out.println("Current Balance: $" + accountService.checkBalance(currentAccount.getAccId()));
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
    }
    private void transfer() {
    try {
        double amount = readDouble("Amount to transfer: $");
        int toId = readInt("Transfer to account ID: ");
        transactionService.transfer(currentAccount.getAccId(), toId, amount);
        System.out.println("Transferred " + amount + " to account " + toId);
        System.out.println("Your current balance: $" + accountService.checkBalance(currentAccount.getAccId()));
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}
private String formatTime(java.time.LocalDateTime time) {
    if (time == null) return "N/A";
    return time.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
}

private void history() {
    try {
        List<Transaction> transactions = transactionService.getHistory(currentAccount.getAccId());
        if (transactions.isEmpty()) {
            System.out.println("Transaction history is not available.");
            return;
        } 
        System.out.printf("%-6s %-8s %-8s %-12s %-12s %-20s%n",
            "ID", "From", "To", "Type", "Amount", "Time");
        System.out.println("-".repeat(70));

        // rows
        for (Transaction t : transactions) {
            System.out.printf("%-6d %-8d %-8d %-12s $%-11.2f %-20s%n",
                t.getTranId(), t.getFromAccId(), t.getToAccId(),
                t.getTranType(), t.getTranAmount(), formatTime(t.getTimestamp()));
        }
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}
    
    
    


        
    }
