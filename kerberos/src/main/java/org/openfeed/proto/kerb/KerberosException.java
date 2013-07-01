package org.openfeed.proto.kerb;


/**
 * Violation of kerberos contract.
 */
@SuppressWarnings("serial")
public class KerberosException extends RuntimeException {

	public KerberosException(final String message) {
		super(message);
	}

	public KerberosException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
