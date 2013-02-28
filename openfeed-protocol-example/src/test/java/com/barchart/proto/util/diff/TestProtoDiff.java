/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.proto.util.diff;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.barchart.proto.util.diff.ProtoDiff.Difference;
import com.barchart.proto.util.diff.ProtoDiffMessage.Builder;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.FieldDescriptor;

public class TestProtoDiff {

	
	private static final double DELTA = .000001;
	private ProtoDiffMessage.Builder expectedBuilder;
	private ProtoDiffMessage.Builder actualBuilder;
	
	@Before()
	public void setup() {
		this.expectedBuilder = createTestMessageBuilder();
		this.actualBuilder = createTestMessageBuilder();
		
		expectedBuilder.addMessages(createTestMessageBuilder());
		expectedBuilder.addMessages(createTestMessageBuilder());
		
		actualBuilder.addMessages(createTestMessageBuilder());
		actualBuilder.addMessages(createTestMessageBuilder());
	}
	
	
	
	private Builder createTestMessageBuilder() {
		ProtoDiffMessage.Builder builder = ProtoDiffMessage.newBuilder();
		builder.setDoubleValue(1234.00);
		builder.setInt32Value(1111);
		builder.setInt64Value(234523452435234L);
		builder.setUint32Value(65431);
		builder.setUint64Value(9191919191991L);
		builder.setSint32Value(-1234);
		builder.setSint64Value(-99999999);
		builder.setFixed32Value(12341234);
		builder.setFixed64Value(1234123499999L);
		builder.setBoolValue(false);
		builder.setStringValue("abcdefghijklmnopqrstuvwxyz");
		builder.setBytesValue(ByteString.copyFromUtf8("1234567890")); 
		builder.setEnumValue(Type.A);
		
		builder.addRepeatedIntValues(1);
		builder.addRepeatedIntValues(2);
		builder.addRepeatedIntValues(3);
		return builder;
	}


	@Test
	public void noDifferences() {
		ProtoDiffMessage expected = expectedBuilder.build();
		ProtoDiffMessage actual = actualBuilder.build();
		ProtoDiff diff = new ProtoDiff(expected, actual);
		assertFalse(diff.hasDifferences());
		assertEquals(0, diff.getDifferenceCount());
	}
	

	@Test
	public void doubleDiff() {
		actualBuilder.setDoubleValue(123.00);
		performSingleDiff(expectedBuilder.getDoubleValue(), actualBuilder.getDoubleValue());
	}

	@Test
	public void floatDiff() {
		actualBuilder.setFloatValue(987.0f);
		performSingleDiff(expectedBuilder.getFloatValue(), actualBuilder.getFloatValue());
	}
	
	@Test
	public void int32Diff() {
		actualBuilder.setUint32Value(1234123);
		performSingleDiff(expectedBuilder.getUint32Value(), actualBuilder.getUint32Value());
	}
	
	@Test
	public void int64Diff() {
		actualBuilder.setInt64Value(99999999999L);
		performSingleDiff(expectedBuilder.getInt64Value(), actualBuilder.getInt64Value());
	}

	@Test
	public void uint32Diff() {
		actualBuilder.setUint32Value(12341634);
		performSingleDiff(expectedBuilder.getUint32Value(), actualBuilder.getUint32Value());
	}
	
	@Test
	public void uint64Diff() {
		actualBuilder.setUint64Value(1234123412341224L);
		performSingleDiff(expectedBuilder.getUint64Value(), actualBuilder.getUint64Value());
	}
	
	
	@Test
	public void sint32ValueDiff() {
		actualBuilder.setSint32Value(234);
		performSingleDiff(expectedBuilder.getSint32Value(), actualBuilder.getSint32Value());
	}
	
	@Test
	public void sint64ValueDiff() {
		actualBuilder.setSint64Value(1234123412L);
		performSingleDiff(expectedBuilder.getSint64Value(), actualBuilder.getSint64Value());
	}
	
	
	@Test
	public void fixed32ValueDiff() {
		actualBuilder.setFixed32Value(98123);
		performSingleDiff(expectedBuilder.getFixed32Value(), actualBuilder.getFixed32Value());
	}
	
	@Test
	public void fixed64ValueDiff() {
		actualBuilder.setFixed64Value(12121212122121L);
		performSingleDiff(expectedBuilder.getFixed64Value(), actualBuilder.getFixed64Value());
	}
	
	@Test
	public void sfixed32ValueDiff() {
		actualBuilder.setFixed32Value(888888);
		performSingleDiff(expectedBuilder.getFixed32Value(), actualBuilder.getFixed32Value());
	}
	
	@Test
	public void sfixed64ValueDiff() {
		actualBuilder.setSfixed64Value(1234123412341L);
		performSingleDiff(expectedBuilder.getSfixed64Value(), actualBuilder.getSfixed64Value());
	}
	
	@Test
	public void boolValueDiff() {
		actualBuilder.setBoolValue(true);
		performSingleDiff(expectedBuilder.getBoolValue(), actualBuilder.getBoolValue());
	}
	
	@Test
	public void stringValueDiff() {
		actualBuilder.setStringValue("zxc");
		performSingleDiff(expectedBuilder.getStringValue(), actualBuilder.getStringValue());
	}
	
	@Test
	public void bytesValueDiff() {
		actualBuilder.setBytesValue(ByteString.copyFromUtf8("qqq"));
		performSingleDiff(expectedBuilder.getBytesValue(), actualBuilder.getBytesValue());
	}
	
	@Test
	public void enumValueDiff() {
		actualBuilder.setEnumValue(Type.C);
		performSingleDiff(expectedBuilder.getEnumValue().toString(), actualBuilder.getEnumValue().toString());
	}
	
	@Ignore
	@Test
	public void testRepeatedInts() {
		actualBuilder.addRepeatedIntValues(4);
		ProtoDiff diff = performDiff();
		assertEquals(1, diff.getDifferenceCount());
		Difference difference = diff.getDifferences().get(0);
		assertEquals(null, difference.getExpected());
		assertEquals(4, difference.getActual());
	}
	
	
	@Test
	public void experiment() {
		ProtoDiffMessage expected = expectedBuilder.addMessages(createTestMessageBuilder()).build();
		ProtoDiffMessage actual = actualBuilder.addMessages(createTestMessageBuilder()).build();
		Map<FieldDescriptor, Object> allFields = expected.getAllFields();
		for (Map.Entry<FieldDescriptor, Object> entry : allFields.entrySet()) {
			Object field = actual.getField(entry.getKey());
			assertEquals(entry.getValue(), field);
		}
	}
	
	private ProtoDiff performDiff() {
		ProtoDiffMessage expected = expectedBuilder.build();
		ProtoDiffMessage actual = actualBuilder.build();
		ProtoDiff diff = new ProtoDiff(expected, actual);
		return diff;
	}
	
	
	private void performSingleDiff(Object expectedValue, Object actualValue) {
		ProtoDiff diff = performDiff();
		expectSingleDifference(expectedValue, actualValue, diff);
	}

	private void expectSingleDifference(Object expectExpected, Object expectActual, ProtoDiff diff) {
		assertTrue(diff.hasDifferences());
		assertEquals(1, diff.getDifferenceCount());
		List<Difference> differences = diff.getDifferences();
		Difference difference = differences.get(0);
		expectDifference(expectExpected, expectActual, difference);
	}

	
	private void expectDifference(Object expectExpected, Object expectActual, Difference difference ) {
		assertEquals(expectExpected, difference.getExpected());
		assertEquals(expectActual, difference.getActual());
	}
	
}

