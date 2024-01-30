package org.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.JsonObject;

public class MeetingClient {
    public static void main(String[] args) {
        try {
            // Kết nối tới server đang lắng nghe ở cổng 8080
            Socket socket = new Socket("localhost", 8080);

            // Tạo đối tượng để đọc dữ liệu từ console
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            // Tạo đối tượng để gửi dữ liệu tới server
            PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);

            // Đọc dữ liệu từ console và gửi tới server dưới dạng JSON
            JsonObject jsonMessage = new JsonObject();
            jsonMessage.addProperty("action", "VIEW_AVAILABLE_TIME_SLOTS");
            jsonMessage.addProperty("search", "teacher_name");
            jsonMessage.addProperty("page", "1");
            jsonMessage.addProperty("size", "2");
            jsonMessage.addProperty("sort", "ASC");

            // Gửi dữ liệu tới server
            outToServer.println(jsonMessage.toString());

            // Đóng kết nối
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
