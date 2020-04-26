package Transport.Protocol;

import Interface.Protocol;
import Transport.AioChannelSession;
import java.nio.ByteBuffer;
/**
 * 
 * @ClassName:  FloatProtocol   
 * @Description:Float类型的编解码协议   
 * @author: haibiscuit
 * @date:   2019年8月28日 下午1:25:40
 * @version 1.8.0   
 * @param    
 *
 */
public class FloatProtocol implements Protocol<Float>{
    private static final byte PROTOCOL_FLOAT_LENGTH = 8;    //协议长度加上数据长度
    @Override
    public Float decode(ByteBuffer readBuffer, AioChannelSession<Float> session) {
        readBuffer.getInt();      //跳过协议定长部分
        float temp = readBuffer.getFloat();    //解码数据
        return temp;
    }

    @Override
    public ByteBuffer encode(Float msg, AioChannelSession<Float> session) {
        ByteBuffer buffer = ByteBuffer.allocate(PROTOCOL_FLOAT_LENGTH);   //分配堆内存
        buffer.putInt(PROTOCOL_FLOAT_LENGTH);
        buffer.putFloat(msg);
        buffer.flip();    //数据处于读取状态
        System.err.println("已经编码的数据是:    "+msg+"消息长度:    "+buffer.remaining());
        return buffer;
    }
    

}
