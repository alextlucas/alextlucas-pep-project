package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    public Message createMessage(Message message) throws SQLException{
        int postedBy = message.getPosted_by();
        String messageText = message.getMessage_text();
        Long timePostedEpoch = message.getTime_posted_epoch();
       
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "insert into Message (posted_by, message_text, time_posted_epoch) values (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, postedBy);
            stmt.setString(2, messageText);
            stmt.setLong(3, timePostedEpoch);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0){
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()){
                    return new Message(keys.getInt(1), postedBy, messageText, timePostedEpoch);
                }
            }
        }
        throw new SQLException("Failed to create message");
    }

    public List<Message> getAllMessages() throws SQLException{
        try (Connection conn = ConnectionUtil.getConnection()){
            Statement stmt = conn.createStatement();
            String sql = "select * from Message";
            ResultSet rs = stmt.executeQuery(sql);

            List<Message> messages = new ArrayList<>();
            while (rs.next()) {
                messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                ));
            }
            return messages;
        }
    }

    public Message getMessageById(int messageId) throws SQLException{
        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "select * from Message where message_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, messageId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        }
        throw new SQLException("Failed to retrieve message");
    }

    public Message deleteMessageById(int messageId) throws SQLException {

        try(Connection conn = ConnectionUtil.getConnection()){
            Message messageToDelete = getMessageById(messageId);

            String sql = "delete from Message where message_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, messageId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0){
                return messageToDelete;
            }
        }
        throw new SQLException("Message was not found. Failed to delete");
    }
    
    public Message updateMessageText(int messageId, String newMessageText) throws SQLException{
        Message oldMessage = getMessageById(messageId);
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "update Message set message_text = ? where message_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newMessageText);
            stmt.setInt(2, messageId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0){
                return new Message(messageId, oldMessage.getPosted_by(), newMessageText, oldMessage.getTime_posted_epoch());
            }
        }
        throw new SQLException("Failed to update message");

    }

    public List<Message> getAllMessagesByAccount(int accountId) throws SQLException{
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "select * from Message where posted_by = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);

            ResultSet rs = stmt.executeQuery();
            List<Message> messages = new ArrayList();

            while (rs.next()){
                messages.add(new Message(rs.getInt("message_id"),
                accountId,
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")));
            }
            return messages;
        }
        
    }
}
