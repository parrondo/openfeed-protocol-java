/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.proto.util.diff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;

public class ProtoDiff {

	private static final Logger logger = LoggerFactory.getLogger(ProtoDiff.class);

	private final Message expected;

	private final Message actual;

	private final List<Difference> differences;

	private final Descriptor descriptor;

	public ProtoDiff(Message expected, Message actual) {
		this.expected = expected;
		this.actual = actual;
		this.descriptor = expected.getDescriptorForType();
		this.differences = new ArrayList<Difference>();

		if (checkSameClass(expected, actual)) {
			diff();
		}

	}

	public void accept(ProtoDiffVisitor visitor) {
		for (FieldDescriptor fieldDescriptor : getAllFields()) {
			switch (fieldDescriptor.getJavaType()) {
				case BOOLEAN:
				case BYTE_STRING:
				case DOUBLE:
				case FLOAT:
				case INT:
				case LONG:
				case STRING:
				case ENUM:
					visitor.visitMessageField(fieldDescriptor, null, null);
					break;
				case MESSAGE:
					diffMessage(fieldDescriptor);
					break;
			}
		}
	}

	public List<FieldDescriptor> getAllFields() {
		return descriptor.getFields();
	}

	public boolean diffFieldpublic(FieldDescriptor fieldDescriptor) {
		if (fieldDescriptor.isRepeated()) {
			return diffRepeatedField(fieldDescriptor);
		} else {
			return diffNonRepeatedField(fieldDescriptor);
		}
	}

	private boolean diffRepeatedField(FieldDescriptor fieldDescriptor) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean diffNonRepeatedField(FieldDescriptor fieldDescriptor) {

		return false;
	}

	public void diff() {

		List<FieldDescriptor> fields = descriptor.getFields();
		for (FieldDescriptor fieldDescriptor : fields) {
			diffField(fieldDescriptor);
		}
	}

	private void diffRepeated(FieldDescriptor fieldDescriptor) {
	}

	private void diffField(FieldDescriptor fieldDescriptor) {
		switch (fieldDescriptor.getJavaType()) {
			case BOOLEAN:
			case BYTE_STRING:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case STRING:
				diffObject(fieldDescriptor);
				break;
			case MESSAGE:
				diffMessage(fieldDescriptor);
				break;
			case ENUM:
				// diffObject(fieldDescriptor);
				diffEnum(fieldDescriptor);
				break;
		}
	}

	private void diffEnum(FieldDescriptor fieldDescriptor) {
		EnumValueDescriptor enumValue1 = (EnumValueDescriptor) expected.getField(fieldDescriptor);
		EnumValueDescriptor enumValue2 = (EnumValueDescriptor) actual.getField(fieldDescriptor);
		String name1 = enumValue1.getName();
		String name2 = enumValue2.getName();
		if (!nullSafeEquals(name1, name2)) {
			differences.add(new FieldDifference(fieldDescriptor, name1, name2));
		}
	}

	@SuppressWarnings("unchecked")
	private void diffMessage(FieldDescriptor fieldDescriptor) {
		if (fieldDescriptor.isRepeated()) {
			Collection<Message> expectedMessages = (Collection<Message>) expected.getField(fieldDescriptor);
			Collection<Message> actualMessages = (Collection<Message>) actual.getField(fieldDescriptor);

		} else {
			throw new UnsupportedOperationException();
		}
	}

	// private boolean diffCollections(Iterator<Message> iter1,
	// Iterator<Message> iter2) {
	// while (iter1.hasNext() && iter2.hasNext()) {
	// Message m1 = iter1.next();
	// Message m2 = iter2.next();
	//
	// }
	// return !(e1.hasNext() || e2.hasNext());
	// }

	private void diffObject(FieldDescriptor fieldDescriptor) {
		if (fieldDescriptor.isRepeated()) {
			diffRepeatedObject(fieldDescriptor);
		} else {
			Object value1 = expected.getField(fieldDescriptor);
			Object value2 = actual.getField(fieldDescriptor);
			if (!nullSafeEquals(value1, value2)) {
				differences.add(new FieldDifference(fieldDescriptor, value1, value2));
			}
		}
	}

	private void diffRepeatedObject(FieldDescriptor fieldDescriptor) {
		ImmutableSet<Object> expectedObjects = ImmutableSet.copyOf((Collection<Object>) expected.getField(fieldDescriptor));
		ImmutableSet<Object> actualObjects = ImmutableSet.copyOf((Collection<Object>) actual.getField(fieldDescriptor));
		Set<Object> missingInExpected = Sets.difference(actualObjects, expectedObjects);
		Set<Object> missingInActual = Sets.difference(expectedObjects, actualObjects);
		if (missingInExpected.size() != missingInActual.size()) {
			Difference difference = new ListLengthDifference(fieldDescriptor, missingInExpected, missingInActual);
			differences.add(difference);
		}

	}

	private boolean checkSameClass(Message expected, Message actual) {
		if (!expected.getClass().equals(actual.getClass())) {
			differences.add(new ClassDifference(expected.getClass(), actual.getClass()));
			return false;
		} else {
			return true;
		}

	}

	private boolean nullSafeEquals(Object o1, Object o2) {
		if (o1 == null) {
			return o2 == null;
		} else {
			return o1.equals(o2);
		}
	}

	public List<Difference> getDifferences() {
		return differences;
	}

	public static class Difference {

		private final Object expected;

		private final Object actual;

		public Difference(Object expected, Object actual) {
			this.expected = expected;
			this.actual = actual;
		}

		public String toString() {
			return "Expected: " + expected + ", actual: " + actual;
		}

		public Object getExpected() {
			return expected;
		}

		public Object getActual() {
			return actual;
		}

	}

	public static class ClassDifference extends Difference {

		public ClassDifference(Object expected, Object actual) {
			super(expected, actual);
		}

		public String toString() {
			return "Class difference. - " + super.toString();
		}
	}

	public static class FieldDifference extends Difference {

		protected final FieldDescriptor fieldDescriptor;

		public FieldDifference(FieldDescriptor fieldDescriptor, Object expected, Object actual) {
			super(expected, actual);
			this.fieldDescriptor = fieldDescriptor;
		}

		public String toString() {
			return fieldDescriptor.getFullName() + " - " + super.toString();
		}
	}

	public static class ListLengthDifference extends FieldDifference {

		public ListLengthDifference(FieldDescriptor fieldDescriptor, Object expected, Object actual) {
			super(fieldDescriptor, expected, actual);
		}

	}

	public boolean hasDifferences() {
		return !differences.isEmpty();
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Difference difference : differences) {
			builder.append(difference.toString());
			builder.append("\n");
		}
		return builder.toString();
	}

	public int getDifferenceCount() {
		return differences.size();
	}

}
