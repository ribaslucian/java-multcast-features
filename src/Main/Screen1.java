package Main;

import Multcast.User;

public class Screen1 {

    public static void main(String args[]) {

        User u1 = new User("Usuário 01");

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
