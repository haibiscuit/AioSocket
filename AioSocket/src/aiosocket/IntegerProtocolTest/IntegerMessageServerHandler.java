package aiosocket.IntegerProtocolTest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import Interface.MessageHandler;
import Transport.AioChannelSession;
import Transport.AioServer;
import Transport.Protocol.IntegerProtocol;

/**
 * 
 * @ClassName:  IntegerMessageServerHandler   
 * @Description:Integer类型协议的服务端测试   
 * @author: haibiscuit 
 * @date:   2019年8月28日 下午1:32:00
 * @version 1.8.0   
 * @param    
 *
 */
public class IntegerMessageServerHandler implements MessageHandler<Integer>{
    public static void main(String args[]){
        AioServer<Integer> server = new AioServer<>("127.0.0.1",6666,new IntegerProtocol(),new IntegerMessageServerHandler());
        try {
            server.start();
        } catch (IOException ex) {
            Logger.getLogger(IntegerMessageServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void Process(AioChannelSession<Integer> session, Integer message) {
        System.out.println("收到客户端的信息:  "+message);
        try {
            session.write(message);
        } catch (IOException ex) {
            Logger.getLogger(IntegerMessageServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
