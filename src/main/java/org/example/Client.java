package org.example;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class Client {
    public static void main(String[] args) {
        final String serverIP = "localhost";
        final int serverPort = 12345;

        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(serverIP);

            long startTime = System.currentTimeMillis();
            int requestCount = 0;
            int duration = 1000; // Tempo de contagem em milissegundos

            while (true) {
                if (System.currentTimeMillis() - startTime >= duration) {
                    System.out.println("Requisições por ms: " + requestCount);
                    requestCount = 0;
                    startTime = System.currentTimeMillis();
                }

                Pacote customObject = new Pacote("Hello, server!");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(customObject);
                oos.flush();
                byte[] sendData = baos.toByteArray();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
                clientSocket.send(sendPacket);
                requestCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


