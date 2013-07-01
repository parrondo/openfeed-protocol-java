package org.openfeed.proto.kerb;

import com.google.protobuf.ByteString;

/**
 * Magic ID extractor.
 */
public class KerberosMagicID implements KerberosObserver {

	private volatile ByteString magicID;

	public ByteString magicID() {
		return magicID;
	}

	@Override
	public void on(final KerberosMessage kerberos,
			final ClientAccreditRequest extension) {
		if (extension.hasMagicID()) {
			magicID = extension.getMagicID();
		} else {
			throw new IllegalStateException("Missing magic id.");
		}
	}

	@Override
	public void on(final KerberosMessage kerberos,
			final ClientAuthorizeRequest extension) {
		if (extension.hasMagicID()) {
			magicID = extension.getMagicID();
		} else {
			throw new IllegalStateException("Missing magic id.");
		}
	}

	@Override
	public void on(final KerberosMessage kerberos,
			final ClientServiceRequest extension) {
		if (extension.hasMagicID()) {
			magicID = extension.getMagicID();
		} else {
			throw new IllegalStateException("Missing magic id.");
		}
	}

	@Override
	public void on(final KerberosMessage kerberos,
			final DomainAccreditResponse extension) {
		if (extension.hasMagicID()) {
			magicID = extension.getMagicID();
		} else {
			throw new IllegalStateException("Missing magic id.");
		}
	}

	@Override
	public void on(final KerberosMessage kerberos,
			final DomainAuthorizeResponse extension) {
		if (extension.hasMagicID()) {
			magicID = extension.getMagicID();
		} else {
			throw new IllegalStateException("Missing magic id.");
		}
	}

	@Override
	public void on(final KerberosMessage kerberos,
			final ServerServiceResponse extension) {
		if (extension.hasMagicID()) {
			magicID = extension.getMagicID();
		} else {
			throw new IllegalStateException("Missing magic id.");
		}
	}

}
