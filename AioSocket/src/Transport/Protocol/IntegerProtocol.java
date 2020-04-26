package Transport.Protocol;

import Interface.Protocol;
import Transport.AioChannelSession;
import java.nio.ByteBuffer;
/**
 * 
 * @ClassName:  IntegerProtocol   
 * @Description: Integer类型的编解码协议   
 * @author: haibiscuit
 * @date:   2019年8月28日 下午1:24:58
 * @version 1.8.0   
 * @param    
 *
 */
public class IntegerProtocol implements Protocol<Integer>{
    private static final byte PROTOCOL_INT_LENGTH = 8;    //协议长度加上数据长度
    @Override
    public Integer decode(ByteBuffer readBuffer, AioChannelSession<Integer> session) {
        readBuffer.getInt();      //跳过协议定长部分
        int temp = readBuffer.getInt();    //解码数据
        return temp;
    }

    @Override
    public ByteBuffer encode(Integer msg, AioChannelSession<Integer> session) {

        ByteBuffer buffer = ByteBuffer.allocate(PROTOCOL_INT_LENGTH);   //分配堆内存
        buffer.putInt(PROTOCOL_INT_LENGTH);
        buffer.putInt(msg);
        buffer.flip();    //数据处于读取状态
        System.err.println("已经编码的数据是:    "+msg+"消息长度:    "+buffer.remaining());
        return buffer;
    }
    
}
