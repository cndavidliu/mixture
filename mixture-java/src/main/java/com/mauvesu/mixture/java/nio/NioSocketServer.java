package com.mauvesu.mixture.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioSocketServer {
	
	private Selector selector;
	private SocketHandler handler = new SocketHandler();
	private int bufferSize = 50;
	
	public NioSocketServer(int port) throws IOException {
		selector = Selector.open();
		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.socket().bind(new InetSocketAddress(port));
		channel.configureBlocking(false);
		channel.register(this.selector, SelectionKey.OP_ACCEPT);
	}
	
	public void start() throws IOException {
		while (true) {
			selector.select();
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				if (key.isValid()) {
					if (key.isAcceptable()) {
						this.handler.handleAccept(key);
					}
					if (key.isWritable()) {
						this.handler.handleWrite(key);
					}
					if (key.isReadable()) {
						this.handler.handleRead(key);
					}
				}
				iterator.remove();
			}
		}
	}
	
	private class SocketHandler {
		
		public void handleAccept(SelectionKey key) throws IOException {
			SocketChannel channel = ((ServerSocketChannel)key.channel()).accept();
			channel.configureBlocking(false);
			channel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
		}
		
		public void handleRead(SelectionKey key) throws IOException {
			SocketChannel channel = (SocketChannel)key.channel();
			ByteBuffer buffer = (ByteBuffer)key.attachment();
			int len = channel.read(buffer);
			if (len == -1)
				channel.close();
			else 
				key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		}
		
		public void handleWrite(SelectionKey key) throws IOException {
			SocketChannel channel = (SocketChannel)key.channel();
			if (!channel.isOpen())
				return;
			ByteBuffer buffer = (ByteBuffer)key.attachment();
			buffer.flip();
			channel.write(buffer);
			if (!buffer.hasRemaining()) {
				key.interestOps(SelectionKey.OP_READ);
			}
			buffer.compact();
		}
	}
	
	public static void main(String[] args) throws IOException {
		NioSocketServer server = new NioSocketServer(30010);
		server.start();
	}
}
