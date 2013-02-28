/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.proto.buf.assm;

import java.util.List;

import com.barchart.proto.buf.data.MarketMessage;
import com.barchart.proto.buf.inst.InstrumentDefinition;

public interface PacketVisitor<T> {

	void apply(List<MarketMessage> messageList, T target);

	void apply(InstrumentDefinition message, T target);

}
