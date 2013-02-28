/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package org.openfeed.proto.tool.diff;

import com.google.protobuf.Descriptors.FieldDescriptor;

public interface ProtoVisitor {

	public void visitField(FieldDescriptor descriptor, Object value);
	
}
