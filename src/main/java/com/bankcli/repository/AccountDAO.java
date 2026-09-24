package com.bankcli.repository;
import com.bankcli.domain.Account;
import java.sql.Connection;



public interface AccountDAO {
    void addAccount (Account account); // add the new account
    Account getAccountById(int acc_id); // get the current balance using account ID
    
    void updateBalance(int acc_id, double newBalance); // update the current balance according  to transaction type 
    void updateBalance(Connection conn, int acc_id, double newBalance); 
    
}
