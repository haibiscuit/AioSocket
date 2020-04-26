package aiosocket.ByteProtocol;

import Interface.MessageHandler;
import Transport.AioChannelSession;
import Transport.AioClient;
import Transport.Protocol.ByteProtocol;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * 
 * @ClassName:  ByteMessageClientHandler   
 * @Description: Byte类型协议的客户端测试   
 * @author: haibiscuit 
 * @date:   2019年8月28日 下午1:34:49
 * @version 1.8.0   
 * @param    
 *
 */
public class ByteMessageClientHandler implements MessageHandler<Byte>{
    public static void main(String args[]){
        AioClient<Byte> client = new AioClient<>("127.0.0.1", 6666, new ByteProtocol(),new ByteMessageClientHandler());
        try {
            client.start();
            Byte i = -128; 
            for(;i<127;){      //既然是byte类型,整数范围-128~127,这里没有发送127的原因是服务端会加1发送，超出127的编码范围
                client.getClientSession().write(i);
                System.out.println("****写完当前数据!");
                i++;
            }
        } catch (Exception ex) {
            Logger.getLogger(ByteMessageClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ByteMessageClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @Override
    public void Process(AioChannelSession<Byte> session, Byte message) {
        System.out.println("收到服务端消息：" + message);
    }
    

}
