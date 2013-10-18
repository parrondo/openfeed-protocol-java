package org.openfeed.proto.kerb;


/**
 * Violation of kerberos contract.
 */
@SuppressWarnings("serial")
public class KerberosError extends RuntimeException {

	public KerberosError(final String message) {
		super(message);
	}

	public KerberosError(final String message, final Throwable cause) {
		super(message, cause);
	}

}
