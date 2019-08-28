package Transport;

import Interface.MessageHandler;
import Interface.Protocol;

/**
 * 
 * @ClassName:  AioServerConfig   
 * @Description：服务配置   
 * @author: haibiscuit
 * @date:   2019年7月3日 下午8:45:02
 * @version 1.8.0   
 * @param  @param <T>  
 *
 */
public class AioServerConfig<T> {
    private static final String version = "1.0.0";  //版本号
    
    private String hostString;   //远程服务器
    private int port = 6666;   //服务器端口
    
    private MessageHandler<T> messageHandler;   //消息处理
    private Protocol<T> protocol;   //编解码协议
    
    private int threadNum = Runtime.getRuntime().availableProcessors() + 1;   //CPU核数加一

    public String getHostString() {
        return hostString;
    }

    public static String getVersion() {
		return version;
	}

	public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public void setHostString(String hostString) {
        this.hostString = hostString;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public MessageHandler<T> getMessageHandler() {
        return messageHandler;
    }

    public void setMessageHandler(MessageHandler<T> messageHandler) {
        this.messageHandler = messageHandler;
    }

    public Protocol<T> getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol<T> protocol) {
        this.protocol = protocol;
    }
    /**
     * 
     * <p>Title: toString</p>   
     * <p>Description: 打印配置信息   
     * @return   
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AioServerConfig{" + "hostString=" + hostString + ", port=" + port + ", messageHandler=" + messageHandler + ", protocol=" + protocol + '}';
    }
    
    
}
