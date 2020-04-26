package Transport.Protocol;

import Interface.Protocol;
import Transport.AioChannelSession;
import java.nio.ByteBuffer;
/**
 * 
 * @ClassName:  LongProtocol   
 * @Description:long类型的编解码协议   
 * @author: haibiscuit 
 * @date:   2019年8月28日 下午1:24:24
 * @version 1.8.0   
 * @param    
 *
 */
public class LongProtocol implements Protocol<Long>{
    private static final byte PROTOCOL_LONG_LENGTH = 12;    //协议长度加上数据长度
    @Override
    public Long decode(ByteBuffer readBuffer, AioChannelSession<Long> session) {
        readBuffer.getInt();      //跳过协议定长部分
        Long temp = readBuffer.getLong();    //解码数据
        return temp;
    }

    @Override
    public ByteBuffer encode(Long msg, AioChannelSession<Long> session) {
        ByteBuffer buffer = ByteBuffer.allocate(PROTOCOL_LONG_LENGTH);   //分配堆内存
        buffer.putInt(PROTOCOL_LONG_LENGTH);
        buffer.putLong(msg);
        buffer.flip();    //数据处于读取状态
        System.err.println("已经编码的数据是:    "+msg+"消息长度:    "+buffer.remaining());
        return buffer;
    }
    
}
