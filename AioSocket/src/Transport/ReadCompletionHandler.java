package Transport;

import java.nio.channels.CompletionHandler;

/**
 * 
 * @ClassName:  ReadCompletionHandler   
 * @Description:读回调   
 * @author: 申梦杰 
 * @date:   2019年7月3日 下午8:45:33
 * @version 1.8.0   
 * @param  @param <T>  消息类型  
 *
 */
public class ReadCompletionHandler<T> implements CompletionHandler<Integer , AioChannelSession<T>>{
    @Override
    public void completed(Integer result, AioChannelSession<T> session) {
//        System.out.println("读完当前线程:  "+Thread.currentThread().getName());
        int a = 0; 
        try{
                session.getReadByteBuffer().flip();     //将数据改成可读状态
                a = session.getReadByteBuffer().getInt(0);      //得到编码的数据
//                System.out.println("a:  "+a+"     剩余数据:  "+session.getReadByteBuffer().remaining());
            if(session.getReadByteBuffer().remaining()==a){   //说明没有半包和粘包情况
                session.readDataProcess(result==-1);   //处理数据，数据处于可读状态
                System.out.println("---整包数据---");
            }else if((session.getReadByteBuffer().remaining()<a)||(session.getReadByteBuffer().remaining() < 4)){   //说明出现半包情况,半包处理并不是很好!
                
                
                session.getReadByteBuffer().compact();   //变为可写状态,继续读取数据
                session.getReadSemaphore().release();   //将读锁释放，重新读
                session.continueRead();   //继续从网络中读数据
                System.out.println("---半包数据---");
            }else{    //说明出现粘包情况
                
                System.out.println("---粘包数据---");
                session.readStickDataProcess(result==-1);   //处理粘包数据，数据处于可读状态
            }
        }catch(IndexOutOfBoundsException exception){   //说明没有任何数据
            System.out.println("没有数据");
            session.getReadByteBuffer().compact();   //变为可写状态,继续读取数据
            session.getReadSemaphore().release();   //将读锁释放，重新读
            session.continueRead();   //继续读数据
        }
    }

    @Override
    public void failed(Throwable exc, AioChannelSession<T> session) {
        session.getReadSemaphore().release();     //出现异常将读锁释放掉
    }

}
