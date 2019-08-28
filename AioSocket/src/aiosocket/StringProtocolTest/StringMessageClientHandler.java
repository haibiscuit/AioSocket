package aiosocket.StringProtocolTest;

import Interface.MessageHandler;
import Transport.AioChannelSession;
import Transport.AioClient;
import Transport.Protocol.StringProtocol;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @ClassName:  StringMessageClientHandler   
 * @Description:String类型协议的客户端测试   
 * @author: haibiscuit
 * @date:   2019年8月28日 下午1:29:48
 * @version 1.8.0   
 * @param    
 *
 */
public class StringMessageClientHandler implements MessageHandler<String>{
    
    public static void main(String args[]){    //服务端测试代码
        AioClient<String> client = new AioClient<>("127.0.0.1", 6666, new StringProtocol(),new StringMessageClientHandler());
        try {
            client.start();
            int i = 0; 
            for(;i<10000;){

                client.getClientSession().write("Hello World!"+i);
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
    public void Process(AioChannelSession<String> session, String message) {    //收到服务器的消息后处理逻辑
           System.out.println("收到服务端消息：" + message);
    }
    
}
