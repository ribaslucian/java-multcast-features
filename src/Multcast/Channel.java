package Multcast;

import java.awt.List;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Channel {

    /**
     * Propriedades de configuracao de comunicacao
     */
    public String ip = "228.5.6.10";
    public int port = 6789;

    /**
     * Propriedade para estruturamento logico do canal.
     */
    public InetAddress channel = null;
    public MulticastSocket socket = null;
    public User user;
    public RSA rsa = new RSA();

    public String listenResponseId;

    /**
     * Lista estatica de recursos que possui o canal.
     *
     * FeaturesName: UserName
     *
     * UserName: Usuario que esta utilizando o recurso no momento
     */
    public static HashMap<String, String> features = new HashMap<String, String>() {
        {
            put("print", "#");
//            put("fax", "");
        }
    };

    /**
     * Toda vez que um usuario entrar no grupo a sessao do mesmo sera armazenado
     * nesse atributo estatico.
     * <UserID: UserInstance>
     */
//    public HashMap<String, User> users = new HashMap<String, User>();
    /**
     * Ao iniciar a classe, o usuario entrara no canal de comunicacao
     */
    public Channel(User u) {
        user = u;
        enter();
    }

    /**
     * Entra no canal de comunicacao MultCast
     */
    public void enter() {
        try {

            // inicia a conexao com o canal de comunicacao, baseado no IP
            channel = InetAddress.getByName(ip);

            // inicia os recursos de sockets para troca de mensagens, baseado na porta
            socket = new MulticastSocket(port);

            // entramos no grupo de comunicao
            socket.joinGroup(channel);

            // dar boas vindas ao canal
            hello();

            // colocamos uma threat para ouvir o canal e nao bloquear a execucao
            new Thread() {

                @Override
                public void run() {

                    user.screen.logClean();

                    // receber no max 3 mensagens e sair
                    for (int i = 0; i < 40000; i++) {
                        listen();
                    }
                }
            }.start();

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());

            // em caso de exceptions, despedimos o 
            // usuario e fecharemo o canal de comunicao
            if (socket != null) {
                by();
                socket.close();
            }
        }
    }

    /**
     * Ouve o canal e recebe as mensagens enviadas pelo meio.
     */
    public void listen() {
        byte[] buffer = new byte[5000];
        DatagramPacket in = new DatagramPacket(buffer, buffer.length);

        try {
            socket.receive(in);
            
            new Thread() {

                @Override
                public void run() {

                    
                    // convertemos mensagem para modelo padronizado no canal
                    String data = new String(in.getData());
                    Message message = new Message(data);



                    // alguem solicitando recurso
                    if (message.get("type").trim().equals("feature")) {

                        Message m = new Message();
                        m.put("type", "feature_response");
                        m.put("message", user.screen.getFeatureOwners());
                        m.put("request_id", message.get("id"));
                        message(m);
                    }




                    // alguem me respondeu a uma solicitacao minha de recurso
                    if (message.containsKey("request_id") && message.get("request_id").trim().equals(listenResponseId)) {
                        String ownersString = user.name;

                        if (!message.get("message").equals("#")) {
                            boolean isWaiting = false;
                            ownersString = message.get("message").trim();
                            String[] owners = ownersString.split(",");

                            for (String owner: owners) {
                                if (owner.equals(user.name))
                                    isWaiting = true;
                            }

                            if (!isWaiting)
                                ownersString += "," + user.name;
                        }


                        user.screen.setFeatureOwners(ownersString);

                        Message m = new Message();
                        m.put("type", "feature_refresh");
                        m.put("message", ownersString);
                        message(m);
                    }


                    // atualizar status do recurso
                    if (message.get("type").equals("feature_refresh")) {
                        user.screen.setFeatureOwners(message.get("message").trim());
                    }


                    // ignorar o resto do processamento se a mensagem for minha
                    if (message.get("name").equals(user.name)) {
                        user.screen.logRaw("self message ignored");
                        return;
                    }


                    // se mensagem de hello, alguem entrou no sistema: mensagem "hello"
                    if (message.get("message").trim().equals("hello")) {
                        // processar
                        // atraves desse escopo consigo saber quantos usuarios estao online
                    }


                    user.screen.log(data);
                    
                    
                }
            }.start();

            

        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Envia uma mensagem utilizando o modelo de serializacao do usuario para o
     * grupo todo.
     */
    public void message(Message message) {
        String id = generateId();

        message.put("name", user.name);
        message.put("signed_id", new String(rsa.encrypt(id)));
        message.put("id", id);

        // aguardaremos responde para mensagem de solicitacao de recurso
        if (message.get("type").equals("feature"))
            listenResponseId = id;

        byte[] bytes = message.serialize().getBytes();
        DatagramPacket out = new DatagramPacket(bytes, bytes.length, channel, port);

        try {
            socket.send(out);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
    
    public void message(Message message, String id) {
//        String id = generateId();

        message.put("name", user.name);
        message.put("signed_id", new String(rsa.encrypt(id)));
        message.put("id", id);

        // aguardaremos responde para mensagem de solicitacao de recurso
        if (message.get("type").equals("feature"))
            listenResponseId = id;

        byte[] bytes = message.serialize().getBytes();
        DatagramPacket out = new DatagramPacket(bytes, bytes.length, channel, port);

        try {
            socket.send(out);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Envia uma mensagem textual do usuario para o grupo todo.
     */
    public void message(String message) {
        Message m = new Message();
        m.put("message", message);
        m.put("type", "text");
        message(m);
    }

    public void hello() {
        Message m = new Message();
        m.put("type", "sys");
        m.put("message", "hello");
        m.put("public_key", rsa.publicKey.toString());
        message(m);

    }

    public void by() {
        try {
//            Message m = new Message();
//            m.put("type", "sys");
//            m.put("message", "by");
            featureUnallocate();

            socket.leaveGroup(channel);
        } catch (IOException ex) {
            Logger.getLogger(Channel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void feature(String featureName) {
        Message m = new Message();
        m.put("type", "feature");
        m.put("message", featureName);
        message(m);
    }

    public void featureUnallocate() {
        boolean isWaiting = false;
        String ownersString = user.screen.getFeatureOwners();
        String[] owners = ownersString.split(",");

        for (String owner: owners) {
            if (owner.equals(user.name)) {
                owners = Utils.remove(owners, user.name);
                ownersString = Utils.implode(owners);
                
                if (ownersString.equals(""))
                    ownersString = "#";
                
                Message m = new Message();
                m.put("type", "feature_refresh");
                m.put("message", ownersString);
                message(m);
                return;
            }
        }
    }
    
    Thread timeoutThreat = new Thread() {

        @Override
        public void run() {
            try {
                sleep(3000);
                user.screen.logRaw("timeout! no response for message " + timeoutMessageId);
            } catch (Exception ex) {
            }
        }
    };
    
    String timeoutMessageId;
    
    public void simulateTimeout() {
        timeoutMessageId = generateId();
        timeoutThreat.start();
//        timeoutThreat.out
    }
    
    public void simulateReceive() {
        timeoutThreat.interrupt();
        user.screen.logRaw("success! response for timeout message " + timeoutMessageId);
    }

    /**
     * Gera um UUI randomico.
     */
    public static String generateId() {
        return UUID.randomUUID().toString();
    }

}
