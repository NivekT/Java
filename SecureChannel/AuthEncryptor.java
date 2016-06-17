
public class AuthEncryptor {
	// This class is used to compute the authenticated encryption of values.  
	//     Authenticated encryption protects the confidentiality of a value, so that the only 
	//     way to recover the initial value is to do authenticated decryption of the value using the 
	//     same key and nonce that were used to encrypt it.   At the same time, authenticated encryption
	//     protects the integrity of a value, so that a party decrypting the value using
	//     the same key and nonce (that were used to decrypt it) can verify that nobody has tampered with the
	//     value since it was encrypted.

	  
	public static final int KeySizeBits = 448;	// want to split into one 192-bit key for streamcipher and one 256-bit for mac
	public static final int KeySizeBytes = KeySizeBits/8; 

	public static final int NonceSizeBytes = StreamCipher.NonceSizeBytes;
	

	// private byte [] k;
	private StreamCipher sc;
	private PRF prf; 
	private byte[] mac;
	private byte[] out;

	private byte[] k1;
	private byte[] k2;
	// private byte[] mac;

	public AuthEncryptor(byte[] key) {
		assert key.length == KeySizeBytes;
		// make k1, k2

		// send k1 to SC
		// send k2 to PRF
		// IMPLEMENT THIS
		k1 = new byte[24];
		k2 = new byte[32];

		System.arraycopy(key,0,k1,0,24);
		System.arraycopy(key,24,k2,0,32);
		// System.out.println(k2.length);
		//k1 = k >>> 192;
		//k2 = (k <<< 192) >>> 192;
		sc = new StreamCipher(k1);

		//System.out.println("AE Success");
	}

	public byte[] encrypt(byte[] in, byte[] nonce, boolean includeNonce) {
		// Encrypts the contents of <in> so that its confidentiality and 
		//    integrity are protected against would-be attackers who do 
		//    not know the key that was used to initialize this AuthEncryptor.
		// Callers are forbidden to pass in the same nonce more than once;
		//    but this code will not check for violations of this rule.
		// The nonce will be included as part of the output iff <includeNonce>
		//    is true.  The nonce should be in plaintext if it is included.
		//
		// This returns a newly allocated byte[] containing the authenticated
		//    encryption of the input.
		sc.setNonce(nonce); // use nonce to update key and PRG
		//System.out.println("Nonce has been set");

		byte[] intermediate; //ciphertext with or without nonce

		if (includeNonce) {
			intermediate = new byte[in.length + NonceSizeBytes];
		} else {
			intermediate = new byte[in.length];
		}

		if (includeNonce) {
			out = new byte[in.length + NonceSizeBytes + 32];
		} else {
			out = new byte[in.length + 32];
		}

		sc.cryptBytes(in, 0, intermediate, 0, in.length); // encrypt in to ciphertext inter
												// is in.length the correct argument?
		// System.out.println("cryptBytes is done"); 
		// System.out.println(new Char(k2.length));
		//System.out.println(k2.length);
		prf = new PRF(k2);   // this is our mac
		// byte[] mac = new byte[32];
		
		// System.out.println("PRF k2 is set");

		if (includeNonce) { // include plaintext of Nonce in output "in"
			//System.out.println("In true branch:");

			prf.update(nonce);
			
			System.arraycopy(nonce, 0, intermediate, in.length, NonceSizeBytes); // ciphertext with nonce
			System.arraycopy(intermediate, 0, out, 0, intermediate.length); // save int to out

			mac = prf.eval(intermediate); // creates hash
			System.arraycopy(mac, 0, out, intermediate.length, 32); // append hash
			
		} else { // does not include Nonce in return

			//System.out.println("In else branch");

			System.arraycopy(intermediate, 0, out, 0, intermediate.length); // save int to out
			mac = prf.eval(intermediate); // creates hash
			//System.out.println(intermediate.length);
			System.arraycopy(mac, 0, out, intermediate.length, 32); // append hash
			//System.out.println("Mac in encrypt is ");
			//System.out.println(new String(mac));
		}
		//System.out.println(out.length);
		return out;
		//return null;   // IMPLEMENT THIS
	}
}