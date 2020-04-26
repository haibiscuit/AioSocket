package Transport.Protocol;

import Interface.Protocol;
import Transport.AioChannelSession;
import java.nio.ByteBuffer;
/**
 * 
 * @ClassName:  DoubleProtocol   
 * @Description:Double类型的编解码协议   
 * @author: haibiscuit 
 * @date:   2019年8月28日 下午1:26:22
 * @version 1.8.0   
 * @param    
 *
 */
public class DoubleProtocol implements Protocol<Double>{
    private static final byte PROTOCOL_DOUBLE_LENGTH = 12;    //协议长度加上数据长度
    @Override
    public Double decode(ByteBuffer readBuffer, AioChannelSession<Double> session) {
        readBuffer.getInt();      //跳过协议定长部分
        double temp = readBuffer.getDouble();    //解码数据
        return temp;
    }

    @Override
    public ByteBuffer encode(Double msg, AioChannelSession<Double> session) {
        ByteBuffer buffer = ByteBuffer.allocate(PROTOCOL_DOUBLE_LENGTH);   //分配堆内存
        buffer.putInt(PROTOCOL_DOUBLE_LENGTH);
        buffer.putDouble(msg);
        buffer.flip();    //数据处于读取状态
        System.err.println("已经编码的数据是:    "+msg+"消息长度:    "+buffer.remaining());
        return buffer;
    }
    
}
