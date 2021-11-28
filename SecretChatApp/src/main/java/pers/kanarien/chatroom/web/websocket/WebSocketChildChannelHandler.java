package pers.kanarien.chatroom.web.websocket;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

@Component
public class WebSocketChildChannelHandler extends ChannelInitializer<SocketChannel>{

	@Resource(name = "webSocketServerHandler")
	private ChannelHandler webSocketServerHandler;
	
	@Resource(name = "httpRequestHandler")
	private ChannelHandler httpRequestHandler;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast("http-codec", new HttpServerCodec()); // HTTP codec
		ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536)); // Combine the HTTP header and HTTP body into a complete HTTP request
		ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler()); // Convenient for large file transfer, but essentially short text data
		ch.pipeline().addLast("http-handler", httpRequestHandler);
		ch.pipeline().addLast("websocket-handler",webSocketServerHandler);
	}

}
