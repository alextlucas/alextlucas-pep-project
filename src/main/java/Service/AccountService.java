package Service;

import java.sql.SQLException;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    
    AccountDAO accountDAO = new AccountDAO();

    public Account register(String username, String password) throws Exception{
        if (username == null || username.isEmpty() || password.length() < 4) {
            throw new Exception("Invalid username or password");
        }
        return accountDAO.register(username, password);
    }

    public Account login(String username, String password) throws SQLException{
        return accountDAO.login(username, password);
    }
    
}