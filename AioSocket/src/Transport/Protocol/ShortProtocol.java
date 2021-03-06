package Transport.Protocol;

import Interface.Protocol;
import Transport.AioChannelSession;
import java.nio.ByteBuffer;

/**
 * 
 * @ClassName:  ShortProtocol   
 * @Description:short类型的编解码协议   
 * @author: haibiscuit 
 * @date:   2019年8月28日 下午1:23:39
 * @version 1.8.0   
 * @param    
 *
 */
public class ShortProtocol implements Protocol<Short>{
    private static final byte PROTOCOL_SHORT_LENGTH = 6;    //协议长度加上数据长度
    @Override
    public Short decode(ByteBuffer readBuffer, AioChannelSession<Short> session) {
        readBuffer.getInt();      //跳过协议定长部分
        short temp = readBuffer.getShort();    //解码数据
        return temp;
    }

    @Override
    public ByteBuffer encode(Short msg, AioChannelSession<Short> session) {
        ByteBuffer buffer = ByteBuffer.allocate(PROTOCOL_SHORT_LENGTH);   //分配堆内存
        buffer.putInt(PROTOCOL_SHORT_LENGTH);
        buffer.putShort(msg);
        buffer.flip();    //数据处于读取状态
        System.err.println("已经编码的数据是:    "+msg+"消息长度:    "+buffer.remaining());
        return buffer;
    }

}
