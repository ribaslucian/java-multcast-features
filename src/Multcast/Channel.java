package Multcast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
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
        byte[] buffer1 = new byte[100];
        DatagramPacket in = new DatagramPacket(buffer1, buffer1.length);
        
        try {
            socket.receive(in);
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
        
        // convertemos mensagem para modelo padronizado no canal
        String data = new String(in.getData());
        Message message = new Message(data);
        
        // somente printar no log se a mensagem nao for minha
        if (!message.get("name").equals(user.name)) {
            System.out.println("Received in [" + user.name + "]: " + data);
        }
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
