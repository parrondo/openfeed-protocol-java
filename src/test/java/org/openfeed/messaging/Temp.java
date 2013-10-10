package org.openfeed.messaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class Temp {

	@Test
	public void test() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		MessageWriter writer = new MessageWriter();
		
		writer.register(new UTF8StringCodec());
		
		writer.write("A", baos);
		byte[] byteArray = baos.toByteArray();
		System.out.println(Arrays.toString(byteArray));

		baos.reset();
		
		writer.write("AA", baos);
		byte[] byteArray2 = baos.toByteArray();
		System.out.println(Arrays.toString(byteArray2));
		
	}
	
}
