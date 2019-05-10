/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

import java.io.IOException;  
import java.net.InetSocketAddress;  
import java.nio.ByteBuffer;  
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;  
import java.nio.channels.CompletionHandler;  
import java.util.concurrent.CountDownLatch;  
import java.util.concurrent.ThreadFactory;
public class AsyncClientHandler implements CompletionHandler<Void, AsyncClientHandler>, Runnable {  
    private AsynchronousChannelGroup asynchronousChannelGroup;
    private AsynchronousSocketChannel clientChannel;  
    private String host;  
    private int port;  
    private CountDownLatch latch;  
    protected static Semaphore semphore = new Semaphore(1);      //防止异步写报错
    public AsyncClientHandler(String host, int port) {  
        this.host = host;  
        this.port = port;  
        try {  
            asynchronousChannelGroup = AsynchronousChannelGroup.withFixedThreadPool(2, new ThreadFactory() {
                byte index = 0;

                @Override
                public Thread newThread(Runnable r) {      //这里缺少r的定义
                    return new Thread(r, "AioSocket:AIO-" + (++index));    //后面是线程名
                }
            });    
            //创建异步的客户端通道  
            clientChannel = AsynchronousSocketChannel.open(asynchronousChannelGroup);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    @Override  
    public void run() {  
        //创建CountDownLatch等待  
        latch = new CountDownLatch(1);  
        //发起异步连接操作，回调参数就是这个类本身，如果连接成功会回调completed方法  
        clientChannel.connect(new InetSocketAddress(host, port), this, this);  
        try {  
            latch.await();  
        } catch (InterruptedException e1) {  
            e1.printStackTrace();  
        }  
        try {  
            clientChannel.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    //连接服务器成功  
    //意味着TCP三次握手完成  
    @Override  
    public void completed(Void result, AsyncClientHandler attachment) {  
        System.out.println("客户端成功连接到服务器...");  
    }  
    //连接服务器失败  
    @Override  
    public void failed(Throwable exc, AsyncClientHandler attachment) {  
        System.err.println("连接服务器失败...");  
        exc.printStackTrace();  
        try {  
            clientChannel.close();  
            latch.countDown();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    //向服务器发送消息  
    public void sendMsg(String msg){  
        byte[] req = msg.getBytes();  
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);  
        writeBuffer.put(req);  
        writeBuffer.flip();  
        
        
        
         try {
            semphore.acquire();       //这里控制异步写
        } catch (InterruptedException ex) {
            Logger.getLogger(AsyncClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        //异步写  
        clientChannel.write(writeBuffer, writeBuffer,new WriteCompletionHandler(clientChannel, latch));  
    }  
}  
