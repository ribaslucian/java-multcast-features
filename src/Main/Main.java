/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Multcast.Message;
import java.util.HashMap;

/**
 *
 * @author Lucian
 */
public class Main {
    
    public static void main(String args[]) {
        
//        User u1 = new User("Usuário 01");
        
        
        
        
        
//        Uso da mensagem
        Message message = new Message();
        message.put("name", "Usuario 01");
        message.put("public_key", "kd83hd8yfofij123");
        message.put("message", "Hello!");
        
        message.unserialize(message.serialize());
        System.out.println(message);
        

//        Uso do canal
//        User u1 = new User("Usuário 01");
//        User u2 = new User("Usuário 02");
//        
//        u1.channel.message("Olá!");
//        u1.channel.message("Como vcs estão ?");
//        
//        u2.channel.message("Olá Usuário 01!");
//        u2.channel.message("Eu estou ótimo e você ?");
    }
    
}
