package org.openfeed.proto.kerb;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.openfeed.proto.kerb.KerberosMessage.Builder;
import org.openfeed.proto.kerb.KerberosMessage.Type;

import com.google.protobuf.ByteString;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.GeneratedMessage.GeneratedExtension;
import com.google.protobuf.ProtocolMessageEnum;

/**
 * Kerberos message codec.
 */
public class KerberosCodec {

	/**
	 * Meta bean.
	 */
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

	private static final Map<Class<?>, Meta<?>> klazMap = new HashMap<Class<?>, Meta<?>>();

	private static final ExtensionRegistry registry = ExtensionRegistry
			.newInstance();
	private static final Map<ProtocolMessageEnum, Meta<?>> typeMap = new HashMap<ProtocolMessageEnum, Meta<?>>();

	/**
	 * Populate meta info map.
	 */
	static {

		KerberosMessageSpec.registerAllExtensions(registry);

		register(ClientAccreditRequest.class);
		register(ClientAuthorizeRequest.class);
		register(ClientServiceRequest.class);

		register(DomainAccreditResponse.class);
		register(DomainAuthorizeResponse.class);
		register(ServerServiceResponse.class);

	}

	/**
	 * Extract extension message and verify signature.
	 */
	public static <T extends GeneratedMessage> T decode(
			final KerberosMessage kerberos, final byte[] secretKey,
			final Class<T> extensionType) {

		final T message = decode(kerberos, extensionType);

		final boolean isValid = KerberosUtilities.defaultSignatureVerify(
				message.toByteArray(), secretKey, kerberos.getSignature()
						.toByteArray());

		if (isValid) {
			return message;
		}

		throw new IllegalStateException("Signature mismatch.");

	}

	/**
	 * Extract extension message.
	 */
	static <T extends GeneratedMessage> T decode(
			final KerberosMessage kerberos, final Class<T> extensionType) {

		@SuppressWarnings("unchecked")
		final Meta<T> meta = (Meta<T>) klazMap.get(extensionType);

		if (meta == null) {
			throw new IllegalStateException("Missing metadata.");
		}
		if (meta.type != kerberos.getType()) {
			throw new IllegalStateException("Message type mismatch.");
		}

		final T message = kerberos.getExtension(meta.extention);

		return message;

	}

	public static <T> T decode(final KerberosMessage kerberos,
			final KerberosObserver<T> observer) {

		switch (kerberos.getType()) {

		case CLIENT_ACCREDIT_REQUEST: {
			final ClientAccreditRequest extension = kerberos
					.getExtension(ClientAccreditRequest.extension);
			observer.on(kerberos, extension);
			return observer.result();
		}
		case DOMAIN_ACCREDIT_RESPONSE: {
			final DomainAccreditResponse extension = kerberos
					.getExtension(DomainAccreditResponse.extension);
			observer.on(kerberos, extension);
			return observer.result();
		}

		case CLIENT_AUTHORIZE_REQUEST: {
			final ClientAuthorizeRequest extension = kerberos
					.getExtension(ClientAuthorizeRequest.extension);
			observer.on(kerberos, extension);
			return observer.result();
		}
		case DOMAIN_AUTHORIZE_RESPONSE: {
			final DomainAuthorizeResponse extension = kerberos
					.getExtension(DomainAuthorizeResponse.extension);
			observer.on(kerberos, extension);
			return observer.result();
		}

		case CLIENT_SERVICE_REQUEST: {
			final ClientServiceRequest extension = kerberos
					.getExtension(ClientServiceRequest.extension);
			observer.on(kerberos, extension);
			return observer.result();
		}
		case SERVER_SERVICE_RESPONSE: {
			final ServerServiceResponse extension = kerberos
					.getExtension(ServerServiceResponse.extension);
			observer.on(kerberos, extension);
			return observer.result();
		}

		default: {
			throw new IllegalStateException("Invalid type = "
					+ kerberos.getType());
		}

		}

	}

	public static KerberosMessage decodeKerberos(final ByteString byteString) {
		try {
			return KerberosMessage.PARSER.parseFrom(byteString, registry);
		} catch (final Throwable e) {
			throw new KerberosException("Decode failure.", e);
		}
	}

	public static void encode(final KerberosMessage.Builder kerberos)
			throws Exception {

	}

	/**
	 * Embed extension message and generate signature.
	 */
	public static <T extends GeneratedMessage> KerberosMessage encode(
			final T extension, final byte[] secretKey) {

		@SuppressWarnings("unchecked")
		final Meta<T> meta = (Meta<T>) klazMap.get(extension.getClass());

		if (meta == null) {
			throw new IllegalStateException("Missing registration.");
		}

		final Builder kerberos = KerberosMessage.newBuilder();
		kerberos.setType((Type) meta.type);
		kerberos.setExtension(meta.extention, extension);
		kerberos.setSignature(signatureCreate(extension, secretKey));
		return kerberos.build();

	}

	public static ByteString magicID(final KerberosMessage kerberos) {
		return decode(kerberos, KerberosMagicID.make());
	}

	public static ByteString randomGUID() {
		return ByteString.copyFrom(KerberosUtilities.defaultRandomUUID());
	}

	public static <T extends GeneratedMessage> void register(final Class<T> klaz) {
		try {

			final Field entryField = klaz.getDeclaredField("extension");
			final Field numberField = klaz
					.getDeclaredField("EXTENSION_FIELD_NUMBER");

			@SuppressWarnings("unchecked")
			final GeneratedExtension<KerberosMessage, T> extension = //
			(GeneratedExtension<KerberosMessage, T>) entryField.get(null);

			final ProtocolMessageEnum type = //
			typeFrom(numberField.getInt(null));

			final Meta<T> meta = new Meta<T>(extension, type);

			klazMap.put(klaz, meta);
			typeMap.put(type, meta);

		} catch (final Throwable e) {
			throw new IllegalStateException("Register failure.", e);
		}
	}

	public static void signatureAssert(final KerberosMessage kerberos,
			final byte[] secretKey) {
		if (!signatureVerify(kerberos, secretKey)) {
			throw new IllegalStateException("Signature mismatch.");
		}
	}

	public static ByteString signatureCreate(final GeneratedMessage extension,
			final byte[] secretKey) {
		return ByteString.copyFrom(KerberosUtilities.defaultSignatureCreate(
				extension.toByteArray(), secretKey));
	}

	public static boolean signatureVerify(final KerberosMessage kerberos,
			final byte[] secretKey) {

		final GeneratedMessage extension = decode(kerberos,
				KerberosExtension.make());

		return signatureVerify(kerberos, extension, secretKey);

	}

	public static boolean signatureVerify(final KerberosMessage kerberos,
			final GeneratedMessage extension, final byte[] secretKey) {
		return KerberosUtilities.defaultSignatureVerify(
				extension.toByteArray(), secretKey, kerberos.getSignature()
						.toByteArray());
	}

	public static String stringDecrypt(final ByteString stringCifer,
			final byte[] secretKey) {
		return KerberosUtilities.defaultStringDecrypt(
				stringCifer.toByteArray(), secretKey);
	}

	public static ByteString stringEcrypt(final String text,
			final byte[] secretKey) {
		return ByteString.copyFrom(KerberosUtilities.defaultStringEncrypt(text,
				secretKey));
	}

	public static String stringEncrypt(final ByteString text,
			final byte[] secretKey) {
		return KerberosUtilities.defaultStringDecrypt(text.toByteArray(),
				secretKey);
	}

	public static BaseTicket ticketDecrypt(final byte[] ticketCifer,
			final byte[] secretKey) {
		final byte[] plainText = KerberosUtilities.defaultDecrypt(ticketCifer,
				secretKey);
		try {
			return BaseTicket.PARSER.parseFrom(plainText);
		} catch (final Throwable e) {
			throw new IllegalStateException(e);
		}
	}

	public static BaseTicket ticketDecrypt(final ByteString ticketCifer,
			final byte[] secretKey) {
		return ticketDecrypt(ticketCifer.toByteArray(), secretKey);
	}

	public static ByteString ticketEncrypt(final BaseTicket ticket,
			final byte[] secretKey) {
		final byte[] plainText = ticket.toByteArray();
		final byte[] ciferText = KerberosUtilities.defaultEncrypt(plainText,
				secretKey);
		return ByteString.copyFrom(ciferText);
	}

	private static KerberosMessage.Type typeFrom(final int number) {
		for (final KerberosMessage.Type type : KerberosMessage.Type.values()) {
			if (type.getNumber() == number) {
				return type;
			}
		}
		return KerberosMessage.Type.UNKNOWN;
	}

	private KerberosCodec() {

	}

}
