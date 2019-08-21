package com.client;

import java.io.IOException;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {

        String host="127.0.0.1";
        int port=6666;

        try {
            Socket client=new Socket(host,port);

          /*客户端向服务器端发送数据*/
            new SendDataToServer(client).start();
            /*客户端从服务器端读取数据*/
            new ReadDataFromServer(client).start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
