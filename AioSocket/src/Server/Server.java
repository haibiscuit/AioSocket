/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

/**
 *
 * @author Administrator
 */
public class Server {  
    private static int DEFAULT_PORT = 12345;  
    private static AsyncServerHandler serverHandle;  
    public volatile static long clientCount = 0;  
    public static void start(){  
        start(DEFAULT_PORT);  
    }  
    public static synchronized void start(int port){  
        if(serverHandle!=null)  
            return;  
        serverHandle = new AsyncServerHandler(port);  
        new Thread(serverHandle,"Server").start();  
    }  
    public static void main(String[] args){  
        Server.start();  
    }  
}  
