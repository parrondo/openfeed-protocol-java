/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.proto.util.diff;

import java.util.Map;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;

public class ProtoInspect {

	private Message message;

	public ProtoInspect(Message message) {
		this.message = message;
	}
	
	public void accept(ProtoVisitor visitor) {
		Map<FieldDescriptor, Object> allFields = message.getAllFields();
		for (Map.Entry<FieldDescriptor, Object> entry : allFields.entrySet()) {
			
		}
	}
	
}
