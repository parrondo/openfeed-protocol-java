package org.openfeed.proto.kerb;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistry.ExtensionInfo;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.GeneratedMessage.GeneratedExtension;
import com.google.protobuf.ProtocolMessageEnum;

public class ProtobufUtil {

	/**
	 * Extract registered extensions.
	 */
	public static Collection<ExtensionInfo> extensionList(
			final ExtensionRegistry registry) {
		try {

			final Field field = registry.getClass().getDeclaredField(
					"extensionsByName");

			field.setAccessible(true);

			@SuppressWarnings("unchecked")
			final Map<String, ExtensionInfo> extensionsByName = //
			(Map<String, ExtensionInfo>) field.get(registry);

			return extensionsByName.values();

		} catch (final Throwable e) {
			throw new IllegalStateException("Extract failure.", e);
		}
	}

	public static class Meta<T extends GeneratedMessage> {

		/**
		 * Message extension descriptor.
		 */
		final GeneratedExtension<KerberosMessage, T> extention;

		/**
		 * Message classification type.
		 */
		final ProtocolMessageEnum type;

		Meta(final GeneratedExtension<KerberosMessage, T> extension,
				final ProtocolMessageEnum type) {
			this.extention = extension;
			this.type = type;
		}
	}

	public static void a(final FileDescriptor fileDesc) {

		final List<FieldDescriptor> list = fileDesc.getExtensions();

		for (final FieldDescriptor field : list) {

			field.getNumber();

		}

	}

	public static <T extends GeneratedMessage> void register(final Class<T> klaz) {
		try {

			final Field extensionNameField = klaz.getDeclaredField("extension");
			final Field extensionNumberField = klaz
					.getDeclaredField("EXTENSION_FIELD_NUMBER");

			@SuppressWarnings("unchecked")
			final GeneratedExtension<KerberosMessage, T> extension = //
			(GeneratedExtension<KerberosMessage, T>) extensionNameField
					.get(null);

			// final Type type = typeFrom(extensionNumberField.getInt(null));
			//
			// final Meta<T> meta = new Meta<T>(extension, type);
			//
			// metaMap.put(klaz, meta);

		} catch (final Throwable e) {
			throw new IllegalStateException("Register failure.", e);
		}
	}

}
