package org.openfeed.proto.kerb;

/**
 * Kerberos message reactor.
 */
public interface KerberosObserver {

	class Adapter implements KerberosObserver {

		@Override
		public void on(final KerberosMessage kerberos,
				final ClientAccreditRequest extension) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void on(final KerberosMessage kerberos,
				final ClientAuthorizeRequest extension) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void on(final KerberosMessage kerberos,
				final ClientServiceRequest extension) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void on(final KerberosMessage kerberos,
				final DomainAccreditResponse extension) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void on(final KerberosMessage kerberos,
				final DomainAuthorizeResponse extension) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void on(final KerberosMessage kerberos,
				final ServerServiceResponse extension) {
			// TODO Auto-generated method stub

		}

	}

	void on(KerberosMessage kerberos, ClientAccreditRequest extension);

	void on(KerberosMessage kerberos, ClientAuthorizeRequest extension);

	void on(KerberosMessage kerberos, ClientServiceRequest extension);

	//

	void on(KerberosMessage kerberos, DomainAccreditResponse extension);

	void on(KerberosMessage kerberos, DomainAuthorizeResponse extension);

	void on(KerberosMessage kerberos, ServerServiceResponse extension);

}
