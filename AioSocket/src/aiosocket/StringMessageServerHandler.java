package aiosocket;

import Interface.MessageHandler;
import Transport.AioChannelSession;
import Transport.AioServer;
import Transport.StringProtocol;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @ClassName:  StringMessageServerHandler   
 * @Description: 实现MessageHandler类,即实现解码后的消息处理
 * @author: 申梦杰 
 * @date:   2019年7月3日 下午8:04:57
 * @version 1.8.0   
 * @param    
 *
 */
public class StringMessageServerHandler implements MessageHandler<String>{
    
    public static void main(String args[]){      //服务端测试用例
        AioServer<String> server = new AioServer<>("127.0.0.1",6666,new StringProtocol(),new StringMessageServerHandler());//实例化服务端
        try {
            server.start();   //启动服务器
        } catch (IOException ex) {
            Logger.getLogger(StringMessageServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * 
     * <p>Title: Process</p>   
     * <p>Description: 处理客户端消息</p>   
     * @param session
     * @param message   
     * @see Interface.MessageHandler#Process(Transport.AioChannelSession, java.lang.Object)
     */
    @Override
    public void Process(AioChannelSession<String> session, String message) {  //接受到客户端消息后的处理逻辑
        System.out.println("收到客户端的信息:  "+message);
        
        try {
            session.write("服务端收到你的消息了!    "+message);
        } catch (IOException ex) {
            Logger.getLogger(StringMessageServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
