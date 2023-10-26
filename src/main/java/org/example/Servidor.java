package org.example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private static List<InetAddress> clientAddresses = new ArrayList<>();
    private static List<Integer> clientPorts = new ArrayList<>();

    public static void main(String[] args) {
        final int serverPort = 12345;

        try {
            DatagramSocket serverSocket = new DatagramSocket(serverPort);
            byte[] receiveData = new byte[1024];

            System.out.println("Servidor UDP aguardando conexões na porta " + serverPort);

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                if (!clientAddresses.contains(clientAddress) || !clientPorts.contains(clientPort)) {
                    clientAddresses.add(clientAddress);
                    clientPorts.add(clientPort);
                }

                byte[] receivedData = receivePacket.getData();

                ByteArrayInputStream bais = new ByteArrayInputStream(receivedData);
                ObjectInputStream ois = new ObjectInputStream(bais);
                Pacote customObject = (Pacote) ois.readObject();

                System.out.println("Recebido de " + clientAddress + ":" + clientPort + ": " + customObject.getData());

                ois.close();
                bais.close();

                sendToAll(serverSocket, customObject); // Envia para todos os clientes
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendToAll(DatagramSocket serverSocket, Pacote customObject) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(customObject);
            oos.flush();
            byte[] sendData = baos.toByteArray();

            for (int i = 0; i < clientAddresses.size(); i++) {
                InetAddress clientAddress = clientAddresses.get(i);
                int clientPort = clientPorts.get(i);
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }

            oos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
