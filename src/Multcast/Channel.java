package Multcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;

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
//    public static HashMap<String, String> features = new HashMap<String, String>() {{
//            put("print", "");
//            put("fax", "");
//        }};
    
    /**
     * Toda vez que um usuario entrar no grupo a sessao do 
     * mesmo sera armazenado nesse atributo estatico.
     * <UserID: UserInstance>
     */
    public static HashMap<String, User> users = new HashMap<String, User>();

    /**
     * Ao iniciar a classe entrara no canal de comunicacao
     */
    public Channel(User u) {
        user = u;
        enter();
    }

    /**
     * Entra em um canal de comunicacao
     */
    public void enter() {
        try {

            // inicia a conexao com o canal de comunicacao, baseado no IP
            channel = InetAddress.getByName(ip);

            // inicia os recursos de sockets para troca de mensagens, baseado na porta
            socket = new MulticastSocket(port);

            // entramos no grupo de comunicao
            socket.joinGroup(channel);

            // dar boas vindas
            message("#" + user.name + " entrou no grupo");

            // colocamos uma threat para ouvir o canal e nao bloquear a execucao
            new Thread() {

                @Override
                public void run() {
                    
                    user.screen.logClean();
                    
                    // receber no max 3 mensagens e sair
                    for (int i = 0; i < 4; i++)
                        listen();
                }
            }.start();

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());

            // em caso de exceptions fecharemos o canal de comunicao do Usuario
            if (socket != null)
                socket.close();
        }
    }

    /**
     * Envia uma mensagem do usuario para o grupo todo.
     *
     * @param message
     */
    public void message(String message) {
        
        // contruimos a mensagem
        Message m = new Message();
        m.put("name", user.name);
        m.put("message", message);
        m.put("for", "all");
        m.put("public_key", rsa.publicKey.toString());
//        m.put("feature_1", "");
//        m.put("feature_2", "");
        
        byte[] bytes = m.serialize().getBytes();
        DatagramPacket out = new DatagramPacket(bytes, bytes.length, channel, port);

        try {
            socket.send(out);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
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
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
        
        // convertemos mensagem para modelo padronizado no canal
        String data = new String(in.getData());
        Message message = new Message(data);
        
        user.screen.log(data);
        
        // somente printar no log se a mensagem nao de propria autoria
//        if (!message.get("name").equals(user.name)) {
            System.out.println("Received in [" + user.name + "]: " + data);
//        }
    }

    /**
     * Retira o usuario do grupo de comunicacao.
     *
     * @throws IOException
     */
    public void by() throws IOException {
        socket.leaveGroup(channel);
    }
    

}
