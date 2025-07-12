package Controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService = new AccountService();
    MessageService messageService = new MessageService();
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::register);
        app.post("/login", this::login);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessageText);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccount);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void register(Context context) {
        try {
            Account request = context.bodyAsClass(Account.class);

            Account account = accountService.register(request.getUsername(), request.getPassword());
            context.status(200).json(account);
        }
        catch (Exception e){
            context.status(400);
        }
    }

    private void login(Context context) {
        try {
            Account request = context.bodyAsClass(Account.class);
            Account account = accountService.login(request.getUsername(), request.getPassword());
            context.status(200).json(account);
        }
        catch (SQLException e){
            context.status(401);
        }
    }

    private void createMessage(Context context){
        try {
            
            Message request = context.bodyAsClass(Message.class);
            Message message = messageService.createMessage(request);
            context.status(200).json(message);
        }
        catch (Exception e){
            context.status(400);
        }
    }
    private void getAllMessages(Context context){
        try {
            List<Message> messages = messageService.getAllMessages();
            context.status(200).json(messages); 
        }
        catch(SQLException e){
            context.status(400);
        }
    }

    private void getMessageById(Context context){
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);
            context.status(200).json(message);
        }
        catch(SQLException e){
            context.status(200);
        }
    
    }
    private void deleteMessageById(Context context){
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        
        try{
            Message message = messageService.deleteMessageById(messageId);
            context.status(200).json(message);
        }
        catch(SQLException e){
            context.status(200);
        }
    }
    private void updateMessageText(Context context){
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message request = context.bodyAsClass(Message.class);
        String newMessageText = request.getMessage_text();
        try {
            Message newMessage = messageService.updateMessageText(messageId, newMessageText);
            context.status(200).json(newMessage);
        }
        catch(Exception e){
            context.status(400);
        }
    }
    private void getAllMessagesByAccount(Context context){
        try {
            int accountId = Integer.parseInt(context.pathParam("account_id"));
            List<Message> messages = messageService.getAllMessagesByAccount(accountId);
            context.status(200).json(messages);
        }
        catch(SQLException e){
            context.status(200);
        }
    }
}