package com.mauvesu.mixture.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioSocketClient {
	
	private SocketChannel channel;
	
	public NioSocketClient(String server, int port) throws IOException {
		channel = SocketChannel.open();
		channel.configureBlocking(false);
		channel.connect(new InetSocketAddress(server, port));
		while (!channel.finishConnect()){
		}
	}
	
	public String send(String content) throws IOException {
		ByteBuffer writeBuffer = ByteBuffer.wrap(content.getBytes());
		ByteBuffer readBuffer = ByteBuffer.allocate(writeBuffer.capacity());
		this.channel.write(writeBuffer);
		while (readBuffer.position() < readBuffer.capacity()) {
			if (writeBuffer.hasRemaining())
				channel.write(writeBuffer);
			if (channel.read(readBuffer) == -1)
				break;
		}
		return new String(readBuffer.array());
	}
	
	public void close() throws IOException {
		this.channel.close();
	}
	
	public static void main(String[] args) throws IOException {
		NioSocketClient client = new NioSocketClient("127.0.0.1", 30010);
		System.out.println(client.send("test").equals("test"));
		client.close();
	}
}
