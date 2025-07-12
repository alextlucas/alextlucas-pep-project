package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    AccountDAO accountDAO = new AccountDAO();
    MessageDAO messageDAO = new MessageDAO();
    public Message createMessage(Message message) throws Exception{
        String messageText = message.getMessage_text();
        int postedBy = message.getPosted_by();
        if (messageText == null || messageText.isEmpty() || messageText.length() > 255 || !accountDAO.userExists(postedBy)){
            throw new Exception("Invalid message");
        }
        else {
            return messageDAO.createMessage(message);
        }
    }
    public List<Message> getAllMessages() throws SQLException{
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) throws SQLException{
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessageById(int messageId) throws SQLException{
        return messageDAO.deleteMessageById(messageId);
    }

    public Message updateMessageText(int messageId, String newMessageText) throws Exception{
        if (newMessageText == null || newMessageText.isEmpty() || newMessageText.length() > 255){
            throw new Exception("Invalid message");
        }
        return messageDAO.updateMessageText(messageId, newMessageText);
    }

    public List<Message> getAllMessagesByAccount(int accountId) throws SQLException{
        return messageDAO.getAllMessagesByAccount(accountId);
    }
}
