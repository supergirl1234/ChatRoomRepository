package com.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


public class HandleClient implements Runnable {


    /*存放连接到服务器端的所有客户端*/
    public static Map<String,Socket> ClientMap=new HashMap<>();

    public Socket client;

    public HandleClient(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        try {
            /*获取客户端输入的数据*/
            InputStream clientIn=this.client.getInputStream();
            Scanner scanner=new Scanner(clientIn);

            /*根据客户端输入的信息进行处理,怎么将客户端发送过来的信息再转发出去*/

             /*注册：register:username
             * 群聊:groupChat:chatMessage
             * 私聊:privateChat:username:chatMessage
             * 退出系统：end
             *
             * */
             while(true){
                 String data=scanner.nextLine();
                 /*针对输入的信息进行处理信息*/

                 /*注册信息*/
                 if(data.startsWith("register")){

                     /*注册的用户名*/
                     String username=data.split(":")[1];
                     /*注册*/
                     Register(username);
                     continue;

                 }
                 /*群聊*/
                 if(data.startsWith("groupChat")){

                     /*注册的用户名*/
                     String message=data.split(":")[1];
                     /*群聊*/
                     groupChat(message);
                     continue;

                 }

                 /*私聊*/
                 if(data.startsWith("privateChat")){
                     /*私聊对象*/
                     String ChatObject=data.split(":")[1];
                     /*私聊的信息*/
                     String message=data.split(":")[2];


                     /*私聊*/
                     privateChat(ChatObject,message);
                     continue;
                 }
                 /*结束，退出聊天系统*/
                 if(data.startsWith("end")){
                     end();
                     continue;
                 }
             }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*获取当前客户端的名字username*/
    public String getClientUserName(){
        Set<Map.Entry<String,Socket>>   clientSet =ClientMap.entrySet();
        for(Map.Entry<String,Socket> item:clientSet){
            if(item.getValue()==this.client){//在ClientMap中查找当前客户端的名字username；
                return  item.getKey();
            }
        }
        return  null;
    }

    /*用户注册*/
    private void Register(String username) {
        /*将用户添加到ClientMap中*/
        ClientMap.put(username,this.client);
        sendMessage(this.client,username+"注册成功",true);
        PrintClientNum();
    }

    /*群聊*/
    private void groupChat(String message) {

        /*服务器端除了不向 发该信息的那个人转发该信息了，要转发该信息给其他客户端*/
        Set<Map.Entry<String,Socket>>   clientSet =ClientMap.entrySet();
        for(Map.Entry<String,Socket> item:clientSet){
            if(item.getValue()!=this.client){
              sendMessage(item.getValue(),message,false);
            }
        }
    }


    /*私聊*/
    private void privateChat(String chatObject, String message) {

        if(!ClientMap.containsKey(chatObject)) {
            /*如果私聊对象不存在，服务器端告知该客户端：无此人*/
            sendMessage(this.client, "无此人", true);
        }else {
            Set<Map.Entry<String, Socket>> clientSet = ClientMap.entrySet();

            for (Map.Entry<String, Socket> item : clientSet) {

                if (item.getKey().equals(chatObject)) {//找到私聊对象
                    sendMessage(item.getValue(), message, false);
                }


            }
        }

    }

    /*结束，退出聊天*/
    private void end() {
        /*将该用户从ClientMap中移除*/

        Set<Map.Entry<String,Socket>> clientSet=ClientMap.entrySet();
        for(Map.Entry<String,Socket> item:clientSet) {
            if(item.getValue()!=this.client) {
                /*给每一个客户端通知  谁退出了聊天*/
                sendMessage(item.getValue(), getClientUserName() + "退出聊天", true);
            }
        }

        for(Map.Entry<String,Socket> item:clientSet) {
            if(item.getValue()==this.client) {
                ClientMap.remove(item.getKey());
            }
        }
        /*并打印一下当前在线人*/
        PrintClientNum();

    }


    /*该客户端给某个客户端发送信息*/

    /*
    * Socket ChatObject:接收信息的客户端
    * String message：发送的信息
    * boolean isServer：是客户端发送的信息，还是服务器发送的提示信息
    * */
    public void sendMessage(Socket ChatObject,String message,boolean isServer){

        try {
            OutputStream outputStream=ChatObject.getOutputStream();
            if(!isServer) {
                /*客户端发出的聊天信息*/
                outputStream.write(getClientUserName().getBytes());
                outputStream.write("说:".getBytes());
                outputStream.write(message.getBytes());
                outputStream.write("\n".getBytes());
            }else {
                /*服务器端发出的提示信息*/
                outputStream.write(message.getBytes());
                outputStream.write("\n".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*打印当前聊天室有多少人*/
    public void PrintClientNum(){



        Set<Map.Entry<String,Socket>> clientSet=ClientMap.entrySet();
        for(Map.Entry<String,Socket> item:clientSet) {
            sendMessage(this.client,"当前聊天室共"+ClientMap.size()+"个人,在线人为：",true);

        }

        for(Map.Entry<String,Socket> item:clientSet) {
            sendMessage(this.client, item.getKey(), true);
        }
    }

}
