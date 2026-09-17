package com.bankcli.repository;

import com.bankcli.domain.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAOImpl implements AccountDAO {

    private static final String INSERT_SQL = "INSERT INTO account (account_id,pin, current_balance) VALUES (?, ?, ?)";
    private static final String FIND_BY_ID_SQL = "SELECT account_id, pin, current_balance FROM account WHERE account_id = ?";
    private static final String UPDATE_SQL = "UPDATE account SET current_balance = ? WHERE account_id = ?";

    @Override
    public void addAccount(Account account) {
        try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
             statement.setInt(1, account.getAccId());
            statement.setString(2, account.getPin());
            statement.setDouble(3, account.getCurrBalance());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw databaseError("Could not add account", e);
        }
    }

    
    @Override 
    public Account getAccountById(int acc_id){
        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)){
                statement.setInt(1, acc_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapAccount(resultSet);
                }
            }
            return null;

                }
        catch(SQLException e){
            throw databaseError("Could not Display the Account", e);

        }
    }
   
    @Override 
    public void updateBalance(int to_acc_id,double newBalance){
        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
        PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)){
            statement.setDouble(1, newBalance);
            statement.setInt(2, to_acc_id);
            statement.executeUpdate();

        }
        catch(SQLException e){
            throw databaseError("Could not update balance", e);
        }

    }
    
    
    private Account mapAccount(ResultSet resultSet) throws SQLException {
        return new Account(
                resultSet.getInt("account_id"),
                resultSet.getString("pin"),
                resultSet.getDouble("current_balance"));
    }

        
    

    private IllegalStateException databaseError(String message, SQLException cause) {
        return new IllegalStateException(message, cause);
    }
}