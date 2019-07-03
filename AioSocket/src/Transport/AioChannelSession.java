package Transport;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * 
 * @ClassName:  AioChannelSession，封装AsynchronousSocketChannel类
 * @Description:   socket会话   
 * @author: 申梦杰 
 * @date:   2019年7月3日 下午8:18:26
 * @version 1.8.0   
 * @param  @param <T>  
 *
 */
public class AioChannelSession<T> {
 
    protected AsynchronousSocketChannel channel;   //对接收到的channel封装
    
    private final ReadCompletionHandler<T> readCompletionHandler;  //读回调
    private final WriteCompletionHandler<T> writeCompletionHandler;  //写回调
    private final AioServerConfig<T> ioServerConfig;    //channel的配置
    
    
    private final Semaphore readSemaphore = new Semaphore(1);   //写并发控制
    private final Semaphore writeSemphore = new Semaphore(1);   //读并发控制

    private final ByteBuffer readByteBuffer = ByteBuffer.allocateDirect(512);  //这里直接使用直接内存
    /**
     * 
     * @Title:  AioChannelSession   
     * @Description:    TODO(这里用一句话描述这个方法的作用)   
     * @param:  @param channel     待封装的AsynchronousSocketChannel对象
     * @param:  @param config      本地sockett服务器配置
     * @param:  @param readCompletionHandler     读回调
     * @param:  @param writeCompletionHandler    写回调
     * @throws
     */
    public AioChannelSession(AsynchronousSocketChannel channel, AioServerConfig<T> config, ReadCompletionHandler<T> readCompletionHandler, WriteCompletionHandler<T> writeCompletionHandler){
        this.channel = channel;
        this.readCompletionHandler = readCompletionHandler;
        this.writeCompletionHandler = writeCompletionHandler;
        this.ioServerConfig = config;
        initSession();
    }
    /**
     * 
     * @Title: initSession   
     * @Description: 初始化,即开始监听数据读请求   
     * @param:       
     * @return: void      
     * @throws
     */
    private void initSession(){ 
         readFromChannel(readByteBuffer);
    }
    /**
     * 
     * @Title: readFromChannel   
     * @Description: channel中读数据   
     * @param: @param byteBuffer  读到的数据写到byteBuffer中    
     * @return: void      
     * @throws
     */
    protected final void readFromChannel(ByteBuffer byteBuffer){
//        System.out.println("读数据当前线程:  "+Thread.currentThread().getName());
        try {
            readSemaphore.acquire();   //这里会阻塞
//            System.out.println("读数据获取锁当前线程:  "+Thread.currentThread().getName());
        } catch (InterruptedException ex) {
            Logger.getLogger(AioChannelSession.class.getName()).log(Level.SEVERE, null, ex);
        }
        channel.read(byteBuffer, this, readCompletionHandler);
    }
    protected final void continueRead(){
        readFromChannel(readByteBuffer);
    }
    /**
     * 
     * @Title: writeToChannel   
     * @Description: 向channel写数据并发送   
     * @param: @param byteBuffer      
     * @return: void      
     * @throws
     */
    protected final void writeToChannel(ByteBuffer byteBuffer){
        try {
            writeSemphore.acquire();     //这里会阻塞
//            System.out.println("写数据获取锁当前线程:  "+Thread.currentThread().getName());
        } catch (InterruptedException ex) {
            Logger.getLogger(AioChannelSession.class.getName()).log(Level.SEVERE, null, ex);
        }
        channel.write(byteBuffer, this, writeCompletionHandler);
        
    }
    /**
     * 
     * @Title: getReadSemaphore   
     * @Description: 读并发控制   
     * @param: @return      
     * @return: Semaphore      
     * @throws
     */
    protected Semaphore getReadSemaphore() {
        return readSemaphore;
    }
    /**
     * 
     * @Title: getWriteSemphore   
     * @Description: 写并发控制   
     * @param: @return      
     * @return: Semaphore      
     * @throws
     */
    protected Semaphore getWriteSemphore() {
        return writeSemphore;
    }
    /**
     * 
     * @Title: readDataProcess   
     * @Description: 读到数据的处理逻辑   
     * @param: @param readResult      
     * @return: void      
     * @throws
     */
    public void readDataProcess(boolean readResult){
        T msg = ioServerConfig.getProtocol().decode(readByteBuffer, this);  //此时数据处于可读状态
        ioServerConfig.getMessageHandler().Process(this, msg);   //读完处理消息
        readByteBuffer.compact();    //将数据清空,变为可写状态
        readSemaphore.release();      //将读信号量释放
        readFromChannel(readByteBuffer);    //数据清空之后再监听读操作   
    }
    /**
     * 
     * @Title: readStickDataProcess   
     * @Description: 粘包数据的处理逻辑   
     * @param: @param readResult      
     * @return: void      
     * @throws
     */
    public void readStickDataProcess(boolean readResult){  //粘包数据的处理
        while(readByteBuffer.hasRemaining()&&(readByteBuffer.remaining()>=readByteBuffer.getInt(0))){  //粘包的判断
            T msg = ioServerConfig.getProtocol().decode(readByteBuffer, this);  //此时数据处于可读状态
            ioServerConfig.getMessageHandler().Process(this, msg);   //读完处理消息   
        }
        readByteBuffer.compact();    //将数据清空,变为可写状态
        readSemaphore.release();      //将读信号量释放
        readFromChannel(readByteBuffer);    //数据清空之后再监听读操作 
    }
    /**
     * 
     * @Title: write   
     * @Description: 向channel写数据   
     * @param: @param msg
     * @param: @throws IOException      
     * @return: void      
     * @throws
     */
    public void write(T msg) throws IOException{    
        ByteBuffer byteBuffer = ioServerConfig.getProtocol().encode(msg, this);   //将数据编码
        writeToChannel(byteBuffer);
    }
    /**
     * 
     * @Title: getReadByteBuffer   
     * @Description: 获取读缓冲区   
     * @param: @return      
     * @return: ByteBuffer      
     * @throws
     */
    protected ByteBuffer getReadByteBuffer(){
        return this.readByteBuffer;
    }
}
