/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.proto.util.diff;

import java.util.Collection;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;

public interface ProtoDiffVisitor {

	public void visitValueField(FieldDescriptor fieldDescriptor, Object...objects);
	
	public void visitRepeatedValues(FieldDescriptor fieldDescriptor, Collection<Object>...value);
	
	public void visitMessageField(FieldDescriptor fieldDescriptor, Message...messages);
}
