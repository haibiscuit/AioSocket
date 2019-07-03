package aiosocket;

import Interface.MessageHandler;
import Transport.AioChannelSession;
import Transport.AioClient;
import Transport.StringProtocol;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * 
 * @ClassName:  StringMessageClientHandler   
 * @Description:实现MessageHandler类,即实现解码后的消息处理
 * @author: 申梦杰 
 * @date:   2019年7月3日 下午7:53:45
 * @version 1.8.0   
 * @param    
 *
 */
public class StringMessageClientHandler implements MessageHandler<String>{
    
    public static void main(String args[]){
        AioClient<String> client = new AioClient<>("127.0.0.1", 6666, new StringProtocol(),new StringMessageClientHandler());//实例化客户端
        try {
            client.start();    //启动客户端服务器
            int i = 1; 
            
            
            for(;i<100;){      //循环发送100个消息
				Thread.sleep(5);        //循环测试这里先让发送线程歇会，要不然服务端扛不住（主要是半包数据还没解决）
                client.getClientSession().write("Hello World!"+i);
                System.out.println("****写完当前数据!");
                i++;
            }
        } catch (Exception ex) {
            Logger.getLogger(StringMessageClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    
      
    }
    /**
     * 
     * <p>Title: Process</p>   
     * <p>Description: 实现消息处理逻辑</p>   
     * @param session   socket会话
     * @param message   处理服务端消息(解码后得到的消息)
     * @see Interface.MessageHandler#Process(Transport.AioChannelSession, java.lang.Object)
     */
    @Override
    public void Process(AioChannelSession<String> session, String message) {    //收到服务器的消息后处理逻辑
           System.out.println("收到服务端消息：" + message);
    }
    
}
