package Controller;

import Model.Message;
import Service.SocialmediaService;
import Model.Account;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    SocialmediaService socialmediaService;
    
    public SocialMediaController(){
        socialmediaService = new SocialmediaService();
    }
    
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    


    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/messages", this::get_retrieveAllMessagesHandler);
        app.get("/messages/{message_id}", this::get_retrieveMessagesbyMessageIdHandler);
        app.get("/accounts/{account_id}/messages", this::get_retrieveAllMessagesforUserHandler);

        app.post("/register",this::post_UserRegistration);
        app.post("/messages",this::post_createMessage);
        app.post("/login",this::post_Userlogin);

        app.delete("messages/{message_id}",this::delete_deleteMessagebyMessageId);
        app.patch("messages/{message_id}", this::patch_updateMessageText);          

        return app;
    }
    

    //do this 
    public void post_UserRegistration(Context ctx) throws JsonProcessingException, IOException, InterruptedException{
         
        ObjectMapper mapper = new ObjectMapper(); 
        Account account = mapper.readValue(ctx.body(),Account.class);
        Account addedAccount = socialmediaService.service_userRegistration(account);

         
        if (addedAccount == null ){
            ctx.status(400); 
        } 
        else {
           
            ctx.json(mapper.writeValueAsString(addedAccount));  
      
        }
        
    
    }


    public void post_createMessage(Context ctx) throws IOException, InterruptedException{
        ObjectMapper mapper = new ObjectMapper(); 
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = socialmediaService.service_createMessage(message);


        if (addedMessage == null) {
            ctx.status(400);
            
        } else {
            ctx.json(mapper.writeValueAsString(addedMessage));
         
        }


    }

    public void get_retrieveAllMessagesHandler(Context ctx) throws IOException, InterruptedException{
        List<Message> messages = socialmediaService.service_retrieveAllMessages();
     
        if (messages == null) {
            ctx.status(200);   
                        
        } 
       else{
            ctx.json(messages);   
        }
    }

    public void get_retrieveMessagesbyMessageIdHandler(Context ctx) throws IOException, InterruptedException{
        
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        socialmediaService.service_retrieveMessagesbyMessageId(id);
        Message retrievemessagebyId = socialmediaService.service_retrieveMessagesbyMessageId(id);
        

        if (retrievemessagebyId == null) {
            ctx.status(200);                 
        } 
        else{
     
            ctx.json(retrievemessagebyId);    
        }
    
    }    

    public void get_retrieveAllMessagesforUserHandler(Context ctx) throws IOException, InterruptedException{
        int id = Integer.parseInt(ctx.pathParam("account_id"));
        
        List<Message> messages = socialmediaService.service_retrieveAllMessagesforUser(id);
 
        if (messages == null) {
            ctx.status(200);                 
        } 
        else{
     
            ctx.json(messages);    
        }
 
    }   


    
    public void delete_deleteMessagebyMessageId(Context ctx) throws JsonProcessingException, IOException, InterruptedException{
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message adddeleteMsgbyId = socialmediaService.service_deleteMessagebyMessageId(id);
        
        if (id == 1) {         
            ctx.json(socialmediaService.service_deleteMessagebyMessageId(id));
            System.out.println("debug:" + adddeleteMsgbyId);
        } 
        else{

            ctx.status(200);                 
           
        }
        
    }
    
        
    public void patch_updateMessageText(Context ctx) throws JsonProcessingException, IOException, InterruptedException{
      
                
        ObjectMapper mapper = new ObjectMapper(); 
        Message MsgbyId = mapper.readValue(ctx.body(), Message.class);
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updateMsgbyId = socialmediaService.service_updateMesssageText(id, MsgbyId);
               
        System.out.println(updateMsgbyId);
        if (MsgbyId.message_text.length()<255 && MsgbyId.message_text.length() > 0 && id ==1) {
            ctx.json(mapper.writeValueAsString(updateMsgbyId));  
            ctx.status(200);                   
          
        } 
        else{
            ctx.status(400);                   
            
        }
        
    } 


    public void post_Userlogin(Context ctx) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper(); 
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedaccount = socialmediaService.service_UserLogin(account);


        if (addedaccount == null) {
            ctx.status(401);
            
        } else {
            ctx.json(mapper.writeValueAsString(addedaccount));
         
        }

    }
}