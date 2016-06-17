
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;


public class SecureChannel extends InsecureChannel {
	// This is just like an InsecureChannel, except that it provides 
	//    authenticated encryption for the messages that pass
	//    over the channel.   It also guarantees that messages are delivered 
	//    on the receiving end in the same order they were sent (returning
	//    null otherwise).  Also, when the channel is first set up,
	//    the client authenticates the server's identity, and the necessary
	//    steps are taken to detect any man-in-the-middle (and to close the
	//    connection if a MITM is detected).
	//
	// The code provided here is not secure --- all it does is pass through
	//    calls to the underlying InsecureChannel.

	private KeyExchange dh;
	private AuthEncryptor encryptor;
	private AuthDecryptor decryptor;
	private PRGen prg;

	// message counter for replay defense
	private Long nmr = new Long(0); // number of message received
	private Long nms = new Long(0); // number of message sent

	public SecureChannel(InputStream inStr, OutputStream outStr, 
			PRGen rand, boolean iAmServer,
			RSAKey serverKey) throws IOException {
		// if iAmServer==false, then serverKey is the server's *public* key
		// if iAmServer==true, then serverKey is the server's *private* key
		super(inStr, outStr);
		// IMPLEMENT THIS
		dh = new KeyExchange(rand);
		prg = rand;

		if (iAmServer) { // Server side implementation
			byte[] c = dh.prepareOutMessage(); // g^a mod p
			byte[] sign = serverKey.sign(c,rand); // sign c with private key
			super.sendMessage(c); // send (g^a mod p)
			super.sendMessage(sign);  // send sign

			byte[] in = super.receiveMessage(); // g^b mod p

			// this secion checks if hash of past messages (and untempered) equal on both ends
			// if true, their keys will the same
			ByteArrayOutputStream m_so_far = new ByteArrayOutputStream();
			m_so_far.write(101); m_so_far.write(c); m_so_far.write(sign); m_so_far.write(in);
			byte[] all_m = m_so_far.toByteArray();
			super.sendMessage(Proj2Util.hash(all_m));
			byte[] check = super.receiveMessage();

			if (!(Arrays.equals(Proj2Util.hash(all_m), check))) close(); // the hash of messages sent so far do not match

			encryptor = new AuthEncryptor(Proj2Util.hash(dh.processInMessage(in)));
			decryptor = new AuthDecryptor(Proj2Util.hash(dh.processInMessage(in)));
		}

		if (!iAmServer) { // Client side implementation
			byte[] c = super.receiveMessage(); // will only return the first message
			byte[] sign = super.receiveMessage(); // second message is the signature

			if (!(serverKey.verifySignature(c,sign))) close(); // signature is incorrect

			byte[] a = dh.prepareOutMessage(); // generate (g^b mod p)
			super.sendMessage(a); // send g^b mod p

			// this secion checks if hash of past messages (and untempered) equal on both ends
			// if true, their keys will the same
			ByteArrayOutputStream m_so_far = new ByteArrayOutputStream();
			m_so_far.write(101); m_so_far.write(c); m_so_far.write(sign); m_so_far.write(a);
			byte[] all_m = m_so_far.toByteArray();
			super.sendMessage(Proj2Util.hash(all_m));
			byte[] check = super.receiveMessage();
			if (!(Arrays.equals(Proj2Util.hash(all_m), check))) close(); // the hash of messages sent so far do not match

			encryptor = new AuthEncryptor(Proj2Util.hash(dh.processInMessage(c)));
			decryptor = new AuthDecryptor(Proj2Util.hash(dh.processInMessage(c)));
		}
	}

	public void sendMessage(byte[] message) throws IOException {
		//encryptor.encrypt = ;
			this.nms++;  // message sent counter, first message is 1

			byte[] nonce = new byte[encryptor.NonceSizeBytes]; // generate nonce
			prg.nextBytes(nonce);
			
			byte[] nms_b = new byte[8]; // 8 bytes for long message sent counter
			for (int i = 0; i < 8; i++) { // convert counter into byte array
			  nms_b[i] = (byte) (nms >> ((8 - i - 1) * 8));
			}

			byte[] intermediate = new byte[message.length + 8];
			System.arraycopy(message,0,intermediate,0,message.length);
			System.arraycopy(nms_b,0,intermediate,message.length,8); // append counter to message

			//byte[] c = new byte[message.length + 8 + encryptor.NonceSizeBytes]; 
			byte[] c = encryptor.encrypt(intermediate, nonce, true);
			super.sendMessage(c);    // IMPLEMENT THIS
	}

	public byte[] receiveMessage() throws IOException {
		byte[] c = super.receiveMessage();		
		this.nmr++; // message receive counter, first message is 1
		// byte[] nonce = new byte[decryptor.NonceSizeBytes];
		// prg.nextBytes(nonce);
		byte[] intermediate = decryptor.decrypt(c, null, true);

		byte[] nmr_b = new byte[8];
		System.arraycopy(intermediate,(intermediate.length - 8),nmr_b,0,8); // extract counter array
		Long nmr_check = new Long(0); // steps before checking if counter is correct;
		for (int i = 0; i < 8; i++) { // convert counter array to Long
			nmr_check = (nmr_check << 8) + (nmr_b[i] & 0xff);
		}
		if (!(nmr.equals(nmr_check))) close(); // the counter values do not match, order disrupted

		byte[] ptext = new byte [intermediate.length - 8];
		System.arraycopy(intermediate,0,ptext,0,(intermediate.length - 8));

		if(ptext == null) close();
		return ptext;   // IMPLEMENT THIS
	}
}