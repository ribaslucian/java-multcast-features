package Multcast;

import Multcast.Message;
import Multcast.RSA;
import Multcast.User;
import com.sun.xml.internal.ws.util.StringUtils;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {

    

    public static void main(String args[]) {
        
//        String[] s1 = {"onee", "twoo"};
//        String[] s2 = Utils.remove(s1, "twoo");
//        
//        System.out.println(s2[0]);
//        
//        System.out.println(Utils.implode(s2));
        
        
        User u1 = new User("Usuário 01");
        User u2 = new User("Usuário 02");
//        User u3 = new User("Usuário 03");
//        User u1 = new User("Usuário 03");

////        Uso da mensagem
//        Message message = new Message();
//        message.put("name", "Usuario 01");
//        message.put("public_key", "kd83hd8yfofij123");
//        message.put("message", "Hello!");
//        
//        message.unserialize(message.serialize());
//        System.out.println(message);
//        
//
//
////        Uso do canal
//        User u1 = new User("Usuário 01");
//        User u2 = new User("Usuário 02");
//        
//        u1.channel.message("Olá!");
//        u1.channel.message("Como vcs estão ?");
//        
//        u2.channel.message("Olá Usuário 01!");
//        u2.channel.message("Eu estou ótimo e você ?");
//
//
//
////        Uso do RSA
//        KeyPair pair = RSA.getPair();
//        PublicKey publicKey = pair.getPublic();
//        PrivateKey privateKey = pair.getPrivate();
//
//        System.out.println("PUBLIC_KEY");
//        System.out.println(publicKey);
//        
//        System.out.println("---");
//        System.out.println("PRIVATE_KEY");
//        System.out.println(privateKey);
//
//        System.out.println("\n---\n");
//
//        System.out.println("ENCRYPTED_MESSAGE");
//        byte[] encrypted = RSA.encrypt(privateKey, "Olá a todos!!!");
//        System.out.println(new String(encrypted));
//        
//        System.out.println("---");
//        System.out.println("DECRYPTED_MESSAGE");
//        byte[] decrypted = RSA.decrypt(publicKey, encrypted);
//        System.out.println(new String(decrypted));
    }

}
