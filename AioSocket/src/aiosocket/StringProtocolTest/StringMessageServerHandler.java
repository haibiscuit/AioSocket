package aiosocket.StringProtocolTest;

import Interface.MessageHandler;
import Transport.AioChannelSession;
import Transport.AioServer;
import Transport.Protocol.StringProtocol;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @ClassName:  StringMessageServerHandler   
 * @Description:String类型协议的服务端测试   
 * @author: haibiscuit 
 * @date:   2019年8月28日 下午1:30:52
 * @version 1.8.0   
 * @param    
 *
 */
public class StringMessageServerHandler implements MessageHandler<String>{
    
    public static void main(String args[]){      //服务端测试用例
        AioServer<String> server = new AioServer<>("127.0.0.1",6666,new StringProtocol(),new StringMessageServerHandler());
        try {
            server.start();   //启动服务器
        } catch (IOException ex) {
            Logger.getLogger(StringMessageServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
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
