package org.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.example.protocol.ProtocolHandler;  // Nhập khẩu ProtocolHandler
import org.example.server.MeetingHandler;

public class MeetingServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server is listening on port 8080...");


            while (true) {
                Socket clientSocket = serverSocket.accept();
                // Tạo MeetingHandler với cả Socket và ProtocolHandler
                new MeetingHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
