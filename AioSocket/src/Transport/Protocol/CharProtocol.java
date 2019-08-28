package Transport.Protocol;

import Interface.Protocol;
import Transport.AioChannelSession;
import java.nio.ByteBuffer;
/**
 * 
 * @ClassName:  CharProtocol   
 * @Description:char类型的编解码协议   
 * @author: haibiscuit 
 * @date:   2019年8月28日 下午1:26:54
 * @version 1.8.0   
 * @param    
 *
 */
public class CharProtocol implements Protocol<Character>{
    private static final byte PROTOCOL_CHAR_LENGTH = 6;    //协议长度加上数据长度
    @Override
    public Character decode(ByteBuffer readBuffer, AioChannelSession<Character> session) {
        readBuffer.getInt();      //跳过协议定长部分
        char temp = readBuffer.getChar();    //解码数据
        return temp;
    }

    @Override
    public ByteBuffer encode(Character msg, AioChannelSession<Character> session) {
        ByteBuffer buffer = ByteBuffer.allocate(PROTOCOL_CHAR_LENGTH);   //分配堆内存
        buffer.putInt(PROTOCOL_CHAR_LENGTH);
        buffer.putChar(msg);
        buffer.flip();    //数据处于读取状态
        System.err.println("已经编码的数据是:    "+msg+"消息长度:    "+buffer.remaining());
        return buffer;
    }


}
