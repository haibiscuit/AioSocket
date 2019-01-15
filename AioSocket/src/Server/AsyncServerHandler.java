/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import java.io.IOException;  
import java.net.InetSocketAddress;  
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;  
import java.util.concurrent.CountDownLatch;  
import java.util.concurrent.ThreadFactory;
public class AsyncServerHandler implements Runnable {  
    public CountDownLatch latch; 
    
    private AsynchronousChannelGroup asynchronousChannelGroup;
    public AsynchronousServerSocketChannel channel; 
    private int threadNum = Runtime.getRuntime().availableProcessors() + 1;
    public AsyncServerHandler(int port) {  
        try {  
             asynchronousChannelGroup = AsynchronousChannelGroup.withFixedThreadPool(threadNum, new ThreadFactory() {
                byte index = 0;

                @Override
                public Thread newThread(Runnable r) {      //这里缺少r的定义
                    return new Thread(r, "AioSocket:AIO-" + (++index));    //后面是线程名
                }
            });
            //创建服务端通道  
            channel = AsynchronousServerSocketChannel.open(asynchronousChannelGroup);  
            //绑定端口  
            channel.bind(new InetSocketAddress(port),1000);  
            
             //如果有远程地址可以这么写
            //channel.bind(new InetSocketAddress(host, port), 1000);
            System.out.println("服务器已启动，端口号：" + port);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    @Override  
    public void run() {  
        //CountDownLatch初始化  
        //它的作用：在完成一组正在执行的操作之前，允许当前的现场一直阻塞  
        //此处，让现场在此阻塞，防止服务端执行完成后退出  
        //也可以使用while(true)+sleep   
        //生成环境就不需要担心这个问题，以为服务端是不会退出的  
        latch = new CountDownLatch(1);  
        //用于接收客户端的连接  
        channel.accept(this,new AcceptHandler());  
        try {  
            latch.await();  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
    }  
}  
