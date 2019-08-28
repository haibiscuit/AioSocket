package Transport.Protocol;

import Interface.Protocol;
import Transport.AioChannelSession;
import java.nio.ByteBuffer;

/**
 * 
 * @ClassName:  StringProtocol   
 * @Description: 字符串的编解码协议   
 * @author: haibiscuit 
 * @date:   2019年8月28日 下午1:21:37
 * @version 1.8.0   
 * @param    
 *
 */
public class StringProtocol implements Protocol<String>{
    private static final byte INT_LENGTH = 4;   //消息体的协议长度

    @Override
    public String decode(ByteBuffer readBuffer, AioChannelSession<String> session){
//        //识别消息长度
//        if (readBuffer.remaining() < INT_LENGTH) {
//            return null;
//        }
        //判断是否存在半包情况
        int len = readBuffer.getInt(readBuffer.position());
        readBuffer.getInt();//跳过length字段
        byte[] bytes = new byte[len - INT_LENGTH];
        readBuffer.get(bytes);
        String temp = new String(bytes);
        System.out.println("@@@@@解码后得到的数据是@@@@@   "+temp+"    数据长度："+len);
        return temp;
    }

    @Override
    public ByteBuffer encode(String msg, AioChannelSession<String> session) {
        byte[] bytes = msg.getBytes();
        System.err.println("待编码的数据是:    "+msg+"消息长度:    "+bytes.length);
        ByteBuffer buffer = ByteBuffer.allocate(INT_LENGTH + bytes.length);   //分配堆内存
        buffer.putInt(INT_LENGTH + bytes.length);
        buffer.put(bytes);
        buffer.flip();    //数据处于读取状态
        System.err.println("已经编码的数据是:    "+msg+"消息长度:    "+buffer.remaining());
        return buffer;
    }

}
