package com.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket=new ServerSocket(6666);
            System.out.println("服务器端已启动，等待客户端连接....");

            /*创建线程池*/
            ExecutorService executorService=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);

            while(true) {
                /*连接客户端*/
                Socket socket = serverSocket.accept();
                System.out.println("客户端" + socket.getLocalAddress() + "连接到服务器端");

                /*在线程池中执行每一个客户端*/
                executorService.execute(new HandleClient(socket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
