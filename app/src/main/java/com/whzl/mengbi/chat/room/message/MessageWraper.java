package com.whzl.mengbi.chat.room.message;



import android.util.Log;

import com.google.protobuf.ByteString;
import com.whzl.mengbi.chat.ProtoStringAvg;

import java.nio.ByteBuffer;
import java.util.List;


public class MessageWraper {
	private List<ByteString> params_list;
	private short type;
	
	public MessageWraper(List<ByteString> params_list, short type) {
		this.params_list = params_list;
		this.type = type;
	}

	public byte[] getWrapedMessage() {
		//body
		ProtoStringAvg.strAvg.Builder builder = new ProtoStringAvg.strAvg.Builder();
		builder.addAllStrs(params_list);
		ProtoStringAvg.strAvg msg = builder.build();
		byte[] body_bytes = msg.toByteArray();

		//head
		short version = 0;
		int len = body_bytes.length;
		ByteBuffer byte_buff = ByteBuffer.allocate(8);
		byte_buff.putShort(version);
		byte_buff.putShort(type);
		byte_buff.putInt(len);
		byte[] head_bytes = byte_buff.array();
		
		//packet
		ByteBuffer packet_buf = ByteBuffer.allocate(body_bytes.length + head_bytes.length);
		packet_buf.put(head_bytes);
		packet_buf.put(body_bytes);
		byte[] packet_bytes = packet_buf.array();
		Log.i("pipi", "发送的消息数据 " + packet_bytes.length);
		return packet_bytes;
	}
}
