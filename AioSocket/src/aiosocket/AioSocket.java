/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aiosocket;

import Client.Client;
import Server.Server;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class AioSocket {
    public static void main(String[] args) throws Exception {
          //运行服务器  
        Server.start();  
        try {
            //避免客户端先于服务器启动前执行代码
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(AioSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        //运行客户端   
        Client.start();  
        System.out.println("请输入请求消息：");  
        Scanner scanner = new Scanner(System.in);  
        while(Client.sendMsg(scanner.nextLine()));  
    }
    
}
