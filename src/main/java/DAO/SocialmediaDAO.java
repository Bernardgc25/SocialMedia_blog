package DAO;

import Model.Account;
import Model.Message;

import Util.ConnectionUtil;

import static org.mockito.Mockito.never;

import java.security.KeyStore.PrivateKeyEntry;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SocialmediaDAO {


        //insert new account on account table
        public Account insertAccount(Account account){
         
            Connection connection = ConnectionUtil.getConnection(); 

            try {
                String sql = "INSERT into account (username, password) values (?,?)";
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        
                ps.setString(1, account.getUsername());
                ps.setString(2, account.getPassword());

                ps.executeUpdate();

                 ResultSet rs = ps.getGeneratedKeys();
            
                if (rs.next() ){
                    int generateacctId = (int) rs.getLong(1);
                    //username not empty and password over 4 chars
                    if ( !(account.getUsername().isEmpty()) && (account.getPassword().length() > 4) ){
                    return new Account(generateacctId, account.getUsername(), account.getPassword());
                }
            }
               
                        
        } catch (SQLException e) {
     
            System.out.println(e.getMessage());
        }

        return null; 

}
    
        //retrieve username from accounts table
        public Account getUsername(String username){
            Connection connection = ConnectionUtil.getConnection();
         
            try {
                String sql = "SELECT * from account where username = ?";

                PreparedStatement ps = connection.prepareStatement(sql);

                ps.setString(1, username);
                ResultSet rs = ps.executeQuery(); 
                
         
                while( rs.next() ){
                    Account account = new Account(rs.getInt("account_id"), 
                                                  rs.getString("username"), 
                                                  rs.getString("password "));      

                    return account; 
                }
            
                
                
            }catch (SQLException e) {
               System.out.println(e.getMessage());
            }
            return null;
        }

        // create a message
        public Message createMessage(Message message){
            Connection connection = ConnectionUtil.getConnection(); 
            try {
                String sql = "INSERT into message (posted_by, message_text, time_posted_epoch) values (?,?,?);";
                
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                
                ps.setInt(1, message.posted_by);
                ps.setString(2, message.message_text);
                ps.setLong(3, message.time_posted_epoch);
                
                ps.executeUpdate();

                 
                ResultSet rs = ps.getGeneratedKeys();
                
                if (rs.next()){
                    
                int generateId = (int) rs.getLong(1);    
                    //message not empty and over 255 chars
                    if(!message.getMessage_text().isEmpty()  && message.getMessage_text().length() < 255){   
                        return new Message(generateId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                    }
                                
                }
   

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            
            return null; 
        
    }
        //get all messages
        public List<Message> retrieveAllMessages(){
            Connection connection = ConnectionUtil.getConnection(); 
            List<Message> messages = new ArrayList<>(); 

            try {
                String sql = "SELECT * from message";
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery(); 

                while( rs.next() ){
                    Message message = new Message
                                       (rs.getInt("message_id"), 
                                       rs.getInt("posted_by"), 
                                       rs.getString("message_text"), 
                                       rs.getLong("time_posted_epoch"));
                                    
                              
                    
                        messages.add(message);
                    }
                
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            return messages; 
        }

        //get message by Id
        public Message retrieveMessagesbyMessageId(int id){
            Connection connection = ConnectionUtil.getConnection(); 
          
            
            try {
                String sql = "SELECT * from message WHERE message_id = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery(); 

                while (rs.next()) {
                            Message message = new Message
                            (rs.getInt("message_id"), 
                            rs.getInt("posted_by"), 
                            rs.getString("message_text"), 
                            rs.getLong("time_posted_epoch"));
                  
                    return message;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            return null; 
        }

        //get all messages for user
        public List<Message> retrieveAllMessagesforUser(int id){
          
            Connection connection = ConnectionUtil.getConnection(); 
            List<Message> messagesbyaccount = new ArrayList<>(); 
            
            try {
                String sql = "SELECT * from message WHERE posted_by = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery(); 

                while (rs.next()) {
                    Message message = new Message(rs.getInt("message_id"), 
                                                  rs.getInt("posted_by"),
                                                  rs.getString("message_text"), 
                                                  rs.getInt("time_posted_epoch"));    
                
                    messagesbyaccount.add(message);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            return messagesbyaccount;
        
        }

        //delete message by messageId
        public Message deleteMessagebyMessageId(int id){
           
            Connection connection = ConnectionUtil.getConnection(); 
               
            try {
                
                String sql = "DELETE * from message WHERE message_id = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                
                ps.setInt(1,  id);
                ps.executeUpdate();
                              
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            return null; 
            
    }

    //update message by message Id
    public Message updateMessageText(int id, Message message){
        Connection connection = ConnectionUtil.getConnection(); 
           
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, id);
                     
            ps.executeUpdate();
               

            ResultSet rs = ps.getGeneratedKeys();
            
            if (rs.next()){
                     
                         
                        return new Message(message.getMessage_id(), 
                                           message.getPosted_by(), 
                                           message.getMessage_text(), 
                                           message.getTime_posted_epoch());
                        
            }
               
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; 
     
    }

    //User login
    public Account userLogin(Account account){
        Connection connection = ConnectionUtil.getConnection(); 
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, account.username);
            ps.setString(2, account.password);
       
            ResultSet rs = ps.executeQuery(); 
            
            while( rs.next() ){
                            return new Account(rs.getInt("account_id"), 
                                              rs.getString("username"), 
                                              rs.getString("password"));                   
           
            }
        }catch (SQLException e) {
           System.out.println(e.getMessage());
        }
        return null;

    }
}
