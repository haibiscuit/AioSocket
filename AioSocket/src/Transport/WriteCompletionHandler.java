package Transport;

import java.nio.channels.CompletionHandler;
/**
 * 
 * @ClassName:  WriteCompletionHandler   
 * @Description:   写回调  
 * @author: 申梦杰 
 * @date:   2019年7月3日 下午8:46:28
 * @version 1.8.0   
 * @param  @param <T>  消息类型  
 *
 */
public class WriteCompletionHandler<T> implements CompletionHandler<Integer , AioChannelSession<T>>{

    @Override
    public void completed(Integer result, AioChannelSession<T> session) {
        System.out.println("当前的写完线程:  "+Thread.currentThread().getName());
          
        session.getWriteSemphore().release();    //写完将写锁释放掉
        
    }

    @Override
    public void failed(Throwable exc, AioChannelSession<T> session) {
        session.getWriteSemphore().release();    //写完将写锁释放掉
    }
}
