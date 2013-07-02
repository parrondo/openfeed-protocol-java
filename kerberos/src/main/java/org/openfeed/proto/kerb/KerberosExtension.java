package org.openfeed.proto.kerb;

import com.google.protobuf.GeneratedMessage;

/**
 * Extension extractor.
 */
public class KerberosExtension<T extends GeneratedMessage> implements
		KerberosObserver<T> {

	public static <T extends GeneratedMessage> KerberosExtension<T> make() {
		return new KerberosExtension<T>();
	}

	private volatile GeneratedMessage extension;

	private KerberosExtension() {

	}

	@Override
	public void on(final KerberosMessage kerberos,
			final ClientAccreditRequest extension) {
		this.extension = extension;
	}

	@Override
	public void on(final KerberosMessage kerberos,
			final ClientAuthorizeRequest extension) {
		this.extension = extension;
	}

	@Override
	public void on(final KerberosMessage kerberos,
			final ClientServiceRequest extension) {
		this.extension = extension;
	}

	@Override
	public void on(final KerberosMessage kerberos,
			final DomainAccreditResponse extension) {
		this.extension = extension;
	}

	@Override
	public void on(final KerberosMessage kerberos,
			final DomainAuthorizeResponse extension) {
		this.extension = extension;
	}

	@Override
	public void on(final KerberosMessage kerberos,
			final ServerServiceResponse extension) {
		this.extension = extension;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T result() {
		return (T) extension;
	}

}
