package Multcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
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

    /**
     * Lista estatica de recursos que possui o canal.
     *
     * FeaturesName: UserName
     *
     * UserName: Usuario que esta utilizando o recurso no momento
     */
    public static HashMap<String, String> features = new HashMap<String, String>() {
        {
            put("print", "");
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

            // convertemos mensagem para modelo padronizado no canal
            String data = new String(in.getData());
            Message message = new Message(data);

            user.screen.log(data);

        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // somente printar no log se a mensagem nao de propria autoria
//        if (!message.get("name").equals(user.name)) {
//            user.screen.log("auto message ignored");
//            System.out.println("Received in [" + user.name + "]: " + data);
//        }
    }

    /**
     * Envia uma mensagem utilizando o modelo de serializacao do usuario para o
     * grupo todo.
     */
    public void message(Message message) {
        String id = generateId();
        message.put("signed_id", new String(rsa.encrypt(id)));
        message.put("id", id);

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
        m.put("name", user.name);
        m.put("message", message);
        m.put("type", "text");
        m.put("for", "all");
        message(m);
    }

    public void hello() {
        Message m = new Message();
        m.put("name", user.name);
        m.put("type", "sys");
        m.put("message", "hello");
        m.put("for", "all");
        m.put("public_key", rsa.publicKey.toString());
        message(m);

    }

    public void by() {
        try {
            Message m = new Message();
            m.put("name", user.name);
            m.put("type", "sys");
            m.put("message", "by");
            m.put("for", "all");
            socket.leaveGroup(channel);
            message(m);
        } catch (IOException ex) {
            Logger.getLogger(Channel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void feature(String featureName) {
        Message m = new Message();
        m.put("name", user.name);
        m.put("type", "feature");
        m.put("message", featureName);
        m.put("for", "all");
        message(m);
    }

    /**
     * Gera um UUI randomico.
     */
    public static String generateId() {
        return UUID.randomUUID().toString();
    }

}
