package Service;

import Model.Account;
import Model.Message;
import DAO.SocialmediaDAO;

import java.sql.*;
import java.util.List;

public class SocialmediaService {

    SocialmediaDAO socialmediaDAO; 

    //constructors
    public SocialmediaService(){
        socialmediaDAO = new SocialmediaDAO();

    }

    public SocialmediaService(SocialmediaDAO socialmediaDAO){
        this.socialmediaDAO = socialmediaDAO; 
    }
    
    /* 
    add new account
    validate username based on conditions: 
    1. doesn't exist in database
    2. username length is atleast 4 characters
    3. username is not empty 
    */

    public Account service_userRegistration(Account account){
  
      
        Account Username = socialmediaDAO.getUsername(account.getUsername());
     
        if (Username == null)  {
            Account newaccount = socialmediaDAO.insertAccount(account);
            return newaccount;
  
        }
        else {
            return null; 
        }             
    
    }

    public Message service_createMessage(Message message){
  
        return socialmediaDAO.createMessage(message);
    }

    public List<Message> service_retrieveAllMessages(){
        return socialmediaDAO.retrieveAllMessages();

    }

    public Message service_retrieveMessagesbyMessageId(int message_id){ 
        
        Message retrieve_msg; 
        retrieve_msg = socialmediaDAO.retrieveMessagesbyMessageId(message_id);
        
        return retrieve_msg; 
        
      
    }

    public List<Message> service_retrieveAllMessagesforUser(int accountid){ 
     
        return socialmediaDAO.retrieveAllMessagesforUser(accountid);
               
    }

    public Message service_deleteMessagebyMessageId(int id){ 
        
       Message deletbyId;
       deletbyId = socialmediaDAO.retrieveMessagesbyMessageId(id); 

       return deletbyId; 
    }

    public Message service_updateMesssageText(int id, Message message){ 
   
         
        message = socialmediaDAO.retrieveMessagesbyMessageId(id);
     
        return message;
     
        //return socialmediaDAO.updateMessageText(id, message);
    }


    public Account service_UserLogin(Account account){
        return socialmediaDAO.userLogin(account);  
    }

}