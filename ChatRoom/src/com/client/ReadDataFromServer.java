package com.client;



import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/*从服务器端读取数据*/
public class ReadDataFromServer extends  Thread {


    /*客户端*/
    public final Socket client;

    public ReadDataFromServer(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        try {
            /*获取客户端输入流*/
            InputStream in= this.client.getInputStream();
            Scanner scanner=new Scanner(in);
            while(true){
                String data=scanner.nextLine();
                System.out.println(data);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
