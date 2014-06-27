
package com.hk.remote.amqp;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

import elf.api.commons.compress.ByteCompress;
import elf.api.commons.compress.ByteGZipCompress;

/**
 * @description
 * @author jiangch
 * @create 2014-5-8 ÏÂÎç3:32:57
 */
public class ElfMessageConverter extends SimpleMessageConverter {

	private ByteCompress byteCompress = new ByteGZipCompress();

	@Override
	protected Message createMessage(Object arg0, MessageProperties arg1) throws MessageConversionException {
		Message message = super.createMessage(arg0, arg1);
		byte[] compressBody = byteCompress.compress(message.getBody());
		Message compressMessage = new Message(compressBody, message.getMessageProperties());
		return compressMessage;
	}

	@Override
	public Object fromMessage(Message compressMessage) throws MessageConversionException {
		byte[] plainBody = byteCompress.decompress(compressMessage.getBody());
		Message plainMessage = new Message(plainBody, compressMessage.getMessageProperties());
		Object object = super.fromMessage(plainMessage);
		return object;
	}

}
