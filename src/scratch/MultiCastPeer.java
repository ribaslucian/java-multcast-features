package scratch;


import java.net.*;
import java.io.*;

public class MultiCastPeer {

    public static void main(String args[]) {
        String args1 = "228.5.6.7";
        String args0 = "MultCastPeer: Hello!!";

        // args give message contents and destination multicast group (e.g. "228.5.6.7")
        MulticastSocket s = null;

        try {
            InetAddress group = InetAddress.getByName(args1);
            s = new MulticastSocket(6789);
            s.joinGroup(group);

            byte[] m = args0.getBytes();
            DatagramPacket messageOut = new DatagramPacket(m, m.length, group, 6789);
            s.send(messageOut);

            for (int i = 0; i < 3; i++) {		// get messages from others in group
                byte[] buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                s.receive(messageIn);
                // s.send(messageIn);
                System.out.println("Received: " + new String(messageIn.getData()));
            }
            s.leaveGroup(group);

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

}
