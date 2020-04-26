package aiosocket.IntegerProtocolTest;

import Interface.MessageHandler;
import Transport.AioChannelSession;
import Transport.AioClient;
import Transport.Protocol.IntegerProtocol;
import aiosocket.StringProtocolTest.StringMessageClientHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * 
 * @ClassName:  IntegerMessageClientHandler   
 * @Description:Integer类型协议的客户端面试   
 * @author: haibiscuit 
 * @date:   2019年8月28日 下午1:33:07
 * @version 1.8.0   
 * @param    
 *
 */
public class IntegerMessageClientHandler implements MessageHandler<Integer>{
    public static void main(String args[]){
        AioClient<Integer> client = new AioClient<>("127.0.0.1", 6666, new IntegerProtocol(),new IntegerMessageClientHandler());
        try {
            client.start();
            int i = 0; 
            for(;i<1000;){
                client.getClientSession().write(i);
                System.out.println("****写完当前数据!");
                i++;
            }
        } catch (Exception ex) {
            Logger.getLogger(StringMessageClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(StringMessageClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void Process(AioChannelSession<Integer> session, Integer message) {
        System.out.println("客户端收到服务端消息:    "+message);
    }
}
