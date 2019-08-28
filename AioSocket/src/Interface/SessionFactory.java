package Interface;

/**
 * 
 * @ClassName:  SessionFactory   
 * @Description: socket会话的创建型工厂   
 * @author: haibiscuit
 * @date:   2019年7月3日 下午8:13:17
 * @version 1.8.0   
 * @param  @param <T>  专为AioChannelSession类设计
 * @param  @param <F>  专为AsynchronousSocketChannel类设计
 *
 */
public interface SessionFactory<T,F> {
	/**
	 * 
	 * @Title: createSession   
	 * @Description: 创建会话Session   
	 * @param: @param channel 客户端channel
	 * @param: @return      
	 * @return: T  返回AioChannelSession
	 * @throws
	 */
     public T createSession(F channel);
}
