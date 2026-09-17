package com.bankcli.business;

import com.bankcli.domain.Account;

public interface AccountService {
    void register(int accountId, String pin);   // validate 4-digit + uniqueness, then create
    Account login(int accountId, String pin);   // check the account exists AND the PIN matches
    double checkBalance(int accountId);          // get the balance (via the account)
    
}
