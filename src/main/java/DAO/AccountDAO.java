package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;


public class AccountDAO {
    public Account register(String username, String password) throws SQLException{
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, username);
            stmt.setString(2, password);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0){
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()){
                    return new Account(keys.getInt(1), username, password);
                }
            }
            throw new SQLException("Failed to create account");
        }
    }

    public Account login(String username, String password) throws SQLException{
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "select * from Account where username = ? and password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return new Account(rs.getInt("account_id"), username, password);
            }
        }
        throw new SQLException("Login failed");
    }

    public boolean userExists(int id) throws SQLException{
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "select 1 from Account where account_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
        catch(SQLException e){
            return false;
        }
        
    }
}