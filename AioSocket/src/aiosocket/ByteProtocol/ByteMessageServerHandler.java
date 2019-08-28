package aiosocket.ByteProtocol;

import Interface.MessageHandler;
import Transport.AioChannelSession;
import Transport.AioServer;
import Transport.Protocol.ByteProtocol;
import aiosocket.IntegerProtocolTest.IntegerMessageServerHandler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * 
 * @ClassName:  ByteMessageServerHandler   
 * @Description:Byte类型协议的Server端测试   
 * @author: haibiscuit 
 * @date:   2019年8月28日 下午1:33:57
 * @version 1.8.0   
 * @param    
 *
 */
public class ByteMessageServerHandler implements MessageHandler<Byte>{

    public static void main(String args[]){
        AioServer<Byte> server = new AioServer<>("127.0.0.1",6666,new ByteProtocol(),new ByteMessageServerHandler());
        try {
            server.start();
        } catch (IOException ex) {
            Logger.getLogger(IntegerMessageServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void Process(AioChannelSession<Byte> session, Byte message) {
        System.out.println("收到客户端的信息:  "+message);
        try {
            session.write(message);
        } catch (IOException ex) {
            Logger.getLogger(IntegerMessageServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

}
