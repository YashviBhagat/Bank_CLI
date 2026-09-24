import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.bankcli.business.TransactionServiceImpl;
import com.bankcli.domain.Account;
import com.bankcli.repository.AccountDAO;
import com.bankcli.repository.TransactionDAOImpl;
public class TransactionServiceImplTest {

    //POSITIVE TEST - withdraw works when there's enough money
    @Test 
    void withdraw_succeeds_whenSufficientFunds(){
        AccountDAO accountDAO = mock(AccountDAO.class);
        TransactionDAOImpl transactionDAO = mock(TransactionDAOImpl.class);
        TransactionServiceImpl service = new TransactionServiceImpl(accountDAO, transactionDAO);

        when(accountDAO.getAccountById(9000)).thenReturn(new Account(9000,"1234",500.0));

        service.withdraw(9000,100.0 );

        verify(accountDAO).updateBalance(9000,400.0);


    }

    // Negative test - withdraw is rejected when funds are insufficient 
    @Test void withdraw_throws_whenInsufficientFunds(){

        AccountDAO accountDAO = mock(AccountDAO.class);
        TransactionDAOImpl transactionDAO = mock(TransactionDAOImpl.class);
        TransactionServiceImpl service = new TransactionServiceImpl(accountDAO, transactionDAO);

        when(accountDAO.getAccountById(9000)).thenReturn(new Account(9000,"1234",50.0));

        assertThrows(IllegalArgumentException.class,()-> service.withdraw(9000,10.0 ));

        

    }
    
}
