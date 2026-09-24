package com.bankcli.repository;



import com.bankcli.domain.Transaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;


public class TransactionDAOImpl implements TransactionDAO {

    private static final String INSERT_SQL =
    "INSERT INTO transaction (from_account_id, to_account_id, transaction_type, transaction_amount) VALUES (?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL = """
        SELECT transaction_id, from_account_id, to_account_id, transaction_type, transaction_amount, created_at
        FROM transaction
        WHERE from_account_id = ? OR to_account_id = ?
        """;
    @Override 
    public  void addTransaction(Transaction transaction){ // perform 3 types of transaction:Deposit/Withdraw/Transfer
        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
        PreparedStatement statement = connection.prepareStatement(INSERT_SQL)){
            statement.setInt(1,transaction.getFromAccId());
            statement.setInt(2,transaction.getToAccId());
            statement.setString(3,transaction.getTranType());
            statement.setDouble(4,transaction.getTranAmount());
            
            statement.executeUpdate();


        }
        catch(SQLException e){
            throw databaseError("Could not add transaction", e);
        }
    
    }

    @Override
    public void addTransaction(Connection connection, Transaction transaction) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setInt(1, transaction.getFromAccId());
            statement.setInt(2, transaction.getToAccId());
            statement.setString(3, transaction.getTranType());
            statement.setDouble(4, transaction.getTranAmount());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw databaseError("Could not add transaction", e);
        }
    }
    @Override
    public List<Transaction> getTransactionByAccId(int acc_id){
        List<Transaction> transactions = new ArrayList<>();
    
        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)){
            statement.setInt(1, acc_id);
            statement.setInt(2, acc_id);
            try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {           // while, not if — many rows
                transactions.add(mapTransaction(resultSet));
            }
        }
        return transactions;

        }

        catch(SQLException e){
            throw databaseError("Could not add transaction", e);
        }
    
    }
    private Transaction mapTransaction(ResultSet rs) throws SQLException {
    return new Transaction(
        rs.getInt("transaction_id"),
        rs.getInt("from_account_id"),
        rs.getInt("to_account_id"),
        rs.getString("transaction_type"),
        rs.getDouble("transaction_amount"),
        rs.getTimestamp("created_at").toLocalDateTime());
}
    

    

    private IllegalStateException databaseError(String message, SQLException cause) {
        return new IllegalStateException(message, cause);
    }
}
