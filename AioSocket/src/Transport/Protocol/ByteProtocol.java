package Transport.Protocol;

import Interface.Protocol;
import Transport.AioChannelSession;
import java.nio.ByteBuffer;
/**
 * 
 * @ClassName:  ByteProtocol   
 * @Description:Byte类型编解码协议   
 * @author: haibiscuit 
 * @date:   2019年8月28日 下午1:27:27
 * @version 1.8.0   
 * @param    
 *
 */
public class ByteProtocol implements Protocol<Byte>{
    private static final byte PROTOCOL_BYTE_LENGTH = 5;    //协议长度加上数据长度
    @Override
    public Byte decode(ByteBuffer readBuffer, AioChannelSession<Byte> session) {
        readBuffer.getInt();      //跳过协议定长部分
        Byte temp = readBuffer.get();    //解码数据
        return temp;
    }

    @Override
    public ByteBuffer encode(Byte msg, AioChannelSession<Byte> session) {
        ByteBuffer buffer = ByteBuffer.allocate(PROTOCOL_BYTE_LENGTH);   //分配堆内存
        buffer.putInt(PROTOCOL_BYTE_LENGTH);
        buffer.put(msg);
        buffer.flip();    //数据处于读取状态
        System.err.println("已经编码的数据是:    "+msg+"消息长度:    "+buffer.remaining());
        return buffer;
    }
}
