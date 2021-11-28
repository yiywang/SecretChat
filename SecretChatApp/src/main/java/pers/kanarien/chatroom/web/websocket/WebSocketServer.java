package pers.kanarien.chatroom.web.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;

/**
 * Description: Netty WebSocket server
 * Start with a separate thread
 * @author Kanarien
 * @version 1.0
 * @date May 18, 2018 11:22:51 AM
 */
public class WebSocketServer implements Runnable{

    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    
	@Autowired
	private EventLoopGroup bossGroup;
	@Autowired
	private EventLoopGroup workerGroup;
	@Autowired
	private ServerBootstrap serverBootstrap;
	
	private int port;
	private ChannelHandler childChannelHandler;
	private ChannelFuture serverChannelFuture;
	
	public WebSocketServer() {
	    
	}

	@Override
	public void run() {
        build();
	}

	/**
	 * Description: Start the Netty Websocket server
	 */
	public void build() {
		try {
		    long begin = System.currentTimeMillis();
			serverBootstrap.group(bossGroup, workerGroup) //boss assists the client's tcp connection request worker is responsible for the previous read and write operations with the client
						   .channel(NioServerSocketChannel.class) // Configure the channel type of the client
						   .option(ChannelOption.SO_BACKLOG, 1024) // Configure TCP parameters and set the length of the handshake string
						   .option(ChannelOption.TCP_NODELAY, true) // TCP_NODELAY algorithm, send large blocks of data as much as possible to reduce the flood of small blocks of data
						   .childOption(ChannelOption.SO_KEEPALIVE, true)// Turn on the heartbeat protection mechanism, that is, the client and server establish a connection in the ESTABLISHED state. If there is no communication for more than 2 hours, the mechanism will be activated
						   .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(592048))//Configure fixed-length receive buffer allocator
						   .childHandler(childChannelHandler); //Binding I/O event processing class, defined in WebSocketChildChannelHandler
			long end = System.currentTimeMillis();
			
	        serverChannelFuture = serverBootstrap.bind(port).sync();
		} catch (Exception e) {
		    logger.info(e.getMessage());
			bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            e.printStackTrace();
		}

	}

	/**
	 * Description: Close the Netty Websocket server, mainly to release the connection
	 * Connection includes: server connection serverChannel,
	 * Client TCP handles the connection bossGroup,
	 * Client I/O operations connect workerGroup
	 *
	 * If only use
	 * bossGroupFuture = bossGroup.shutdownGracefully();
	 * workerGroupFuture = workerGroup.shutdownGracefully();
	 * Will cause memory leaks.
	 */
	public void close(){
	    serverChannelFuture.channel().close();
		Future<?> bossGroupFuture = bossGroup.shutdownGracefully();
        Future<?> workerGroupFuture = workerGroup.shutdownGracefully();
        
        try {
            bossGroupFuture.await();
            workerGroupFuture.await();
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
        }
	}
	
	public ChannelHandler getChildChannelHandler() {
        return childChannelHandler;
    }

    public void setChildChannelHandler(ChannelHandler childChannelHandler) {
        this.childChannelHandler = childChannelHandler;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
