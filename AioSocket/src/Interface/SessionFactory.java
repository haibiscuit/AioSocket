package Interface;
/**
 * 
 * @ClassName:  SessionFactory   
 * @Description: socket会话的创建型工厂   
 * @author: 申梦杰 
 * @date:   2019年7月3日 下午8:13:17
 * @version 1.8.0   
 * @param  @param <T>  专为AioChannelSession类设计
 * @param  @param <F>  专为AsynchronousSocketChannel类设计
 *
 */
public interface SessionFactory<T,F> {
     public T createSession(F channel);
}
