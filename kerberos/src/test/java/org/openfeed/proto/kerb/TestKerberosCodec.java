package org.openfeed.proto.kerb;

import org.junit.Test;

public class TestKerberosCodec {

	@Test
	public void codec() throws Exception {

		final String accountURI = "account";

		// final byte[] secretKey = KerberosUtilities.secretKey(accountURI,
		// "secret");

		// final VerifierTicket verifier1 = VerifierTicket.newBuilder()
		// .setAccountURI(accountURI)
		// .setCreationTime(System.currentTimeMillis()).build();

		// System.err.println("verifier1 = \n" + verifier1);

		// final ByteString ticket = ByteString.copyFrom(KerberosUtilities
		// .defaultEncrypt(verifier1.toByteArray(), secretKey));

		// final ClientAccreditRequestMessage source =
		// ClientAccreditRequestMessage
		// .newBuilder().setAccountURI(accountURI)
		// .setVerifierTicket(ticket).build();

		// System.err.println("source = \n" + source);

		// final KerberosMessage root = KerberosCodec.encode(source, secretKey);
		// System.err.println("root = \n" + root);

		// final ClientAccreditRequestMessage target =
		// KerberosCodec.decode(root,
		// secretKey, ClientAccreditRequestMessage.class);

		// System.err.println("target = \n" + target);

		// final byte[] ticket2 = KerberosUtilities.defaultDecrypt(target
		// .getVerifierTicket().toByteArray(), secretKey);

		// final VerifierTicket verifier2 = VerifierTicket.parseFrom(ticket2);

		// System.err.println("verfier2 = \n" + verifier2);

		// assertEquals(verifier1, verifier2);

	}
}
