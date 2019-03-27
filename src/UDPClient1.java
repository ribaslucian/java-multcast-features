
import java.net.*;
import java.io.*;

public class UDPClient1 {

    public static void main(String args[]) {
        String args1 = "228.5.6.7";
        // String args1 = "localhost";
        String args0 = "UDPCliente1: Hello";
        
        // args give message contents and destination hostname
        DatagramSocket aSocket = null;
        
        try {
            aSocket = new DatagramSocket();
            byte[] m = args0.getBytes();
            InetAddress aHost = InetAddress.getByName(args1);
            int serverPort = 6789;
            DatagramPacket request = new DatagramPacket(m, args0.length(), aHost, serverPort);
            aSocket.send(request);
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            System.out.println("Reply: " + new String(reply.getData()));
            aSocket.close();
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null) {
                aSocket.close();
            }
        }
    }
}
