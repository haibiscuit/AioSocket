package Transport;

import Interface.MessageHandler;
import Interface.Protocol;
import Interface.SessionFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ThreadFactory;
/**
 * 
 * @ClassName:  AioServer   
 * @Description:服务端  
 * @author: 申梦杰 
 * @date:   2019年7月3日 下午8:40:34
 * @version 1.8.0   
 * @param  @param <T>  
 *
 */
public class AioServer<T>{
    
    
    protected AioServerConfig<T> config = new AioServerConfig<>();    //服务端配置
    
    protected  ReadCompletionHandler<T> readCompletionHandler = null;   //读回调
    protected WriteCompletionHandler<T>  writeCompletionHandler = null;  //写回调
    
    private AsynchronousChannelGroup channelGroup = null;   //处理线程
    private AsynchronousServerSocketChannel serverSocketChannel = null;   //服务端channel
    
    private SocketAddress socketAddress = null;   //服务器地址
    private SessionFactory<AioChannelSession<T>,AsynchronousSocketChannel> sessionFactory= null;

    /**
     * 
     * @Title:  AioServer   
     * @Description:    构造函数   
     * @param:  @param port    端口
     * @param:  @param protocol    协议
     * @param:  @param messageHandler 消息处理 
     * @throws
     */
    public AioServer(int port,Protocol<T> protocol,MessageHandler<T> messageHandler){
        config.setPort(port);
        config.setProtocol(protocol);
        config.setMessageHandler(messageHandler);
        this.readCompletionHandler = new ReadCompletionHandler<>();
        this.writeCompletionHandler = new WriteCompletionHandler<>();
    }
    /**
     * 
     * @Title:  AioServer   
     * @Description:    重载构造函数   
     * @param:  @param host   主机
     * @param:  @param port   端口
     * @param:  @param protocol   协议
     * @param:  @param messageHandler   消息处理  
     * @throws
     */
    public AioServer(String host,int port,Protocol<T> protocol,MessageHandler<T> messageHandler){
        this(port, protocol, messageHandler);
        config.setHostString(host);
    }
    
    /**
     * 
     * @Title: startServer   
     * @Description: 启动服务端   
     * @param: @param asynchronousChannelGroup
     * @param: @return
     * @param: @throws IOException      
     * @return: AioChannelSession<T>      
     * @throws
     */
    public final AioChannelSession<T> startServer(AsynchronousChannelGroup asynchronousChannelGroup) throws IOException{
        this.serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);  
        AioChannelSession<T> session = null;
        if(socketAddress==null){    
            socketAddress = new InetSocketAddress(config.getHostString(),config.getPort());
        }
        serverSocketChannel.bind(socketAddress);  //绑定端口和服务器
        
        this.serverSocketChannel.accept(serverSocketChannel,new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>(){

            @Override
            public void completed(final AsynchronousSocketChannel channel, AsynchronousServerSocketChannel serverSocketChannel) {
                serverSocketChannel.accept(serverSocketChannel, this);  //服务器继续监听端口
                getSession(channel);        //创建Session
            }

            @Override
            public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
                System.out.println("服务器连接失败!");
            }
        
        });
        System.out.println("服务器已经启动!");
        return session;    //这里先放着,暂时用不到，返回的是null值
    }
    /**
     * 
     * @Title: start   
     * @Description: 启动服务端,进行服务端实例的初始化   
     * @param: @return
     * @param: @throws IOException      
     * @return: AioChannelSession<T>      
     * @throws
     */
    public final AioChannelSession<T> start() throws IOException{
      System.out.println("服务器正在启动，启动后再打开客户端!");
      this.channelGroup = AsynchronousChannelGroup.withFixedThreadPool(config.getThreadNum(), new ThreadFactory() {
         byte i = 0;
          @Override
          public Thread newThread(Runnable r) {
              return new Thread(r,"AioSocket:session-" + (++i));
          }
      });
      this.sessionFactory = new SessionFactory<AioChannelSession<T>,AsynchronousSocketChannel>(){     
          @Override
          public AioChannelSession<T> createSession(AsynchronousSocketChannel channel) {
               return new AioChannelSession<>(channel,config,readCompletionHandler,writeCompletionHandler);
          }
      
      };      
      return startServer(channelGroup);
    }
    /**
     * 
     * @Title: getSession   
     * @Description: 创建会话   
     * @param: @param channel
     * @param: @return      
     * @return: AioChannelSession<T>      
     * @throws
     */
    private AioChannelSession<T> getSession(AsynchronousSocketChannel channel){
        return sessionFactory.createSession(channel);
    }
}
