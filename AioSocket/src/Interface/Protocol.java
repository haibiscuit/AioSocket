package Interface;

import Transport.AioChannelSession;
import java.nio.ByteBuffer;
/**
 * 
 * @ClassName:  Protocol   
 * @Description:编解码协议   
 * @author: haibiscuit 
 * @date:   2019年7月3日 下午8:09:34
 * @version 1.8.0   
 * @param  @param <T>  传送消息的类型
 *
 */
public interface Protocol<T> {
	/**
	 * 
	 * @Title: decode   
	 * @Description: 对接收到的消息实现解码   
	 * @param: @param readBuffer   缓冲字节流
	 * @param: @param session    socket会话
	 * @param: @return      
	 * @return: T      返回解码后的消息类型数据
	 * @throws
	 */
    T decode(final ByteBuffer readBuffer, AioChannelSession<T> session);  
    /**
     * 
     * @Title: encode   
     * @Description: 对发送的消息实现编码   
     * @param: @param msg        待编码的消息
     * @param: @param session    socket会话
     * @param: @return      
     * @return: ByteBuffer 返回缓冲字节流     
     * @throws
     */
    ByteBuffer encode(T msg, AioChannelSession<T> session);
}
