package org.openfeed.proto.kerb;

import com.google.protobuf.ByteString;

/**
 * Magic ID extractor.
 */
public class KerberosMagicID implements KerberosObserver<ByteString> {

	public static KerberosMagicID make() {
		return new KerberosMagicID();
	}

	private volatile ByteString magicID;

	private KerberosMagicID() {

	}

	private void exception() {
		throw new IllegalStateException("Missing magic id.");
	}

	@Override
	public void on(final KerberosMessage kerberos,
			final ClientAccreditRequest extension) {
		if (extension.hasMagicID()) {
			result(extension.getMagicID());
		} else {
			exception();
		}
	}

	@Override
	public void on(final KerberosMessage kerberos,
			final ClientAuthorizeRequest extension) {
		if (extension.hasMagicID()) {
			result(extension.getMagicID());
		} else {
			exception();
		}
	}

	@Override
	public void on(final KerberosMessage kerberos,
			final ClientServiceRequest extension) {
		if (extension.hasMagicID()) {
			result(extension.getMagicID());
		} else {
			exception();
		}
	}

	@Override
	public void on(final KerberosMessage kerberos,
			final DomainAccreditResponse extension) {
		if (extension.hasMagicID()) {
			result(extension.getMagicID());
		} else {
			exception();
		}
	}

	@Override
	public void on(final KerberosMessage kerberos,
			final DomainAuthorizeResponse extension) {
		if (extension.hasMagicID()) {
			result(extension.getMagicID());
		} else {
			exception();
		}
	}

	@Override
	public void on(final KerberosMessage kerberos,
			final ServerServiceResponse extension) {
		if (extension.hasMagicID()) {
			result(extension.getMagicID());
		} else {
			exception();
		}
	}

	@Override
	public ByteString result() {
		return magicID;
	}

	private void result(final ByteString magicID) {
		this.magicID = magicID;
	}

}
