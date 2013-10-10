/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package util;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class EnumUtil {

	/** enforce unique name-number mapping */
	public static <E extends Enum<E>> void testUnique(final Class<E> klaz)
			throws Exception {

		final Set<String> nameSet = new HashSet<String>();

		final Set<Integer> ordinalSet = new HashSet<Integer>();

		int count = 0;

		final Field[] fieldArray = klaz.getDeclaredFields();

		for (final Field field : fieldArray) {

			final Class<?> type = field.getType();

			if (type == klaz) {

				@SuppressWarnings("unchecked")
				final E value = (E) field.get(null);

				final String name = value.name();

				final int ordinal = value.ordinal();

				nameSet.add(name);

				ordinalSet.add(ordinal);

				count++;

			}

		}

		assertEquals(nameSet.size(), count);

		assertEquals(ordinalSet.size(), count);

		assertEquals(ordinalSet.size(), ordinalSet.size());

	}

}
