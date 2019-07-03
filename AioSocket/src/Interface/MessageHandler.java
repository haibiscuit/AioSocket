/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Interface;

import Transport.AioChannelSession;

/**
 * 
 * @ClassName:  MessageHandler   
 * @Description:接收到消息的处理   
 * @author: 申梦杰 
 * @date:   2019年7月3日 下午8:07:35
 * @version 1.8.0   
 * @param  @param <T>  发送的消息类型
 *
 */
public interface MessageHandler<T> {
	/**
	 * 
	 * @Title: Process   
	 * @Description: 处理接受到的消息   
	 * @param: @param session   socket会话
	 * @param: @param message   接受到的消息
	 * @return: void      
	 * @throws
	 */
    void Process(AioChannelSession<T> session,T message);
}
