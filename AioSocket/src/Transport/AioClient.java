package Transport;

import Interface.MessageHandler;
import Interface.Protocol;
import Interface.SessionFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadFactory;

/**
 * 
 * @ClassName:  AioClient   
 * @Description:客户端   
 * @author: 申梦杰 
 * @date:   2019年7月3日 下午8:29:40
 * @version 1.8.0   
 * @param  @param <T>  
 *
 */
public class AioClient<T> {
    protected AioServerConfig<T> config = new AioServerConfig<>();    //本地服务器配置
    
    protected AioChannelSession<T> session;    //会话
    
    
    private AsynchronousChannelGroup channelGroup;    //读写数据的线程池
    private final ReadCompletionHandler<T> readCompletionHandler;   //读回调
    private final WriteCompletionHandler<T> writeCompletionHandler;    //写回调
    
    private SocketAddress localAddress;        //本地连接地址
    private SessionFactory<AioChannelSession<T>,AsynchronousSocketChannel> sessionFactory= null;   //会话创建工厂
    
    /**
     * 
     * @Title:  AioClient   
     * @Description:    TODO(这里用一句话描述这个方法的作用)   
     * @param:  @param host   主机
     * @param:  @param port   端口
     * @param:  @param protocol   编解码协议
     * @param:  @param messageHandler   数据处理类
     * @throws
     */
    public AioClient(String host, int port, Protocol<T> protocol, MessageHandler<T> messageHandler){
        config.setHostString(host);
        config.setPort(port);
        config.setProtocol(protocol);
        config.setMessageHandler(messageHandler);
        this.readCompletionHandler = new ReadCompletionHandler<>();
        this.writeCompletionHandler = new WriteCompletionHandler<>();
    }
    /**
     * 
     * @Title: startServer   
     * @Description: 启动客户端   
     * @param: @param asynchronousChannelGroup
     * @param: @return
     * @param: @throws IOException
     * @param: @throws ExecutionException
     * @param: @throws InterruptedException      
     * @return: AioChannelSession<T>      
     * @throws
     */
    public final AioChannelSession<T> startServer(AsynchronousChannelGroup asynchronousChannelGroup) throws IOException, ExecutionException, InterruptedException{
        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open(asynchronousChannelGroup);  
//        if(localAddress==null){    
//            System.out.println("创建socketAddress!");
//            localAddress = new InetSocketAddress("127.0.0.1",8080);    //客户端默认绑定的端口号!
//        }
//        socketChannel.bind(localAddress);    //绑定当前客户端地址
        socketChannel.connect(new InetSocketAddress(config.getHostString(),config.getPort())).get();
        this.session = getSession(socketChannel);
        System.out.println("客户端服务器启动");
        return session;
    }
    /**
     * 
     * @Title: start   
     * @Description: 启动客户端,初始化启动过程   
     * @param: @return
     * @param: @throws IOException
     * @param: @throws InterruptedException
     * @param: @throws ExecutionException      
     * @return: AioChannelSession<T>      
     * @throws
     */
    public final AioChannelSession<T> start() throws IOException, InterruptedException, ExecutionException{
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
    /**
     * 
     * @Title: getClientSession   
     * @Description: 得到当前会话   
     * @param: @return      
     * @return: AioChannelSession<T>      
     * @throws
     */
    public AioChannelSession<T> getClientSession(){
        return this.session;
    }
    /**
     * 
     * @Title: setLocalAddress   
     * @Description: 配置本地地址   
     * @param: @param host
     * @param: @param port      
     * @return: void      
     * @throws
     */
    public final void setLocalAddress(String host,int port){
        if(localAddress==null){    
//            System.out.println("创建socketAddress!");
            localAddress = host == null ? new InetSocketAddress(port) : new InetSocketAddress(host, port);
        }
    }
}
