package com.client;


import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/*向服务器发送数据*/
public class SendDataToServer  extends  Thread{


    public Socket client;

    public SendDataToServer(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        try {
            OutputStream out=this.client.getOutputStream();
            Scanner scanner=new Scanner(System.in);//客户端输入数据
            System.out.println("请输入数据：");
            while (true){
                String data=scanner.nextLine();
                /*将客户端发出的数据发送给服务器*/
                out.write(data.getBytes());
                out.write("\n".getBytes());
                out.flush();
                if(data.equals("end")){
                    break;
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}




