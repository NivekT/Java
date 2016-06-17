
public class AuthDecryptor {
	// This class is used to decrypt and authenticate a sequence of values that were encrypted 
	//     by an AuthEncryptor.

	
	public static final int KeySizeBits = AuthEncryptor.KeySizeBits;
	public static final int KeySizeBytes = AuthEncryptor.KeySizeBytes;

	public static final int NonceSizeBytes = AuthEncryptor.NonceSizeBytes;


	private StreamCipher sc;
	private PRF prf;

	private byte[] in_wo_nonce; // ciphertext without nonce
	private byte[] out; // message
	private byte[] mac;

	private byte[] k1;
	private byte[] k2;

	public AuthDecryptor(byte[] key) {
		assert key.length == KeySizeBytes;

		// IMPLEMENT THIS
		// split into two keys
		k1 = new byte[24];
		k2 = new byte[32];

		//k1 = k >>> 192;
		//k2 = (k <<< 192) >>> 192;

		System.arraycopy(key,0,k1,0,24);
		System.arraycopy(key,24,k2,0,32);

		sc = new StreamCipher(k1);
	}

	public byte[] decrypt(byte[] in, byte[] nonce, boolean nonceIncluded) {
		// Decrypt and authenticate the contents of <in>.  The value passed in will normally
		//    have been created by calling encrypt() with the same nonce in an AuthEncryptor 
		//    that was initialized with the same key as this AuthDecryptor.
		// If <nonceIncluded> is true, then the nonce has been included in <in>, and
		//    the value passed in as <nonce> will be disregarded.
		// If <nonceIncluded> is false, then the value of <nonce> will be used.
		// If the integrity of <in> cannot be verified, then this method returns null.   Otherwise it returns 
		//    a newly allocated byte-array containing the plaintext value that was originally 
		//    passed to encrypt().

		// extract mac from in 
		//System.out.println(in.length);
		mac = new byte[32]; // for the extracted mac
		System.arraycopy(in, in.length - 32, mac, 0, 32); // extracts mac
		//System.out.println("Mac in decrypt is");
		//System.out.println(new String (mac));

		byte[] intermediate;
		intermediate = new byte[in.length - 32]; // ciphertext, may have nonce
		System.arraycopy(in, 0, intermediate, 0, in.length - 32); // extracts the rest to intermediate
		//System.out.println(intermediate.length);
		// System.out.println(new String(intermediate));
		
		//System.out.println("The in in Decrypt is" + new String(intermediate) +"\n");
		// System.out.println(intermediate);
		// computes mac from k2
		prf = new PRF(k2);
		byte[] mac2 = new byte[32]; // for the calculated mac
		if (nonceIncluded) {
			prf.update(nonce);
		}
		mac2 = prf.eval(intermediate);
		//System.out.println("Mac 2 is");
		// System.out.println(new String(mac2));

		if (!((new String(mac)).equals(new String(mac2)))) { // check if MAC hash is valid
			//System.out.println("Macs are unequal");
			return null; 
		}

		sc.setNonce(nonce);

		// MAC is valid
		if (nonceIncluded) {
			
			// delete nonce from in
			in_wo_nonce = new byte[intermediate.length - NonceSizeBytes]; // ciphertext without nonce
			System.arraycopy(in, 0, in_wo_nonce, 0, intermediate.length - NonceSizeBytes);
			out = new byte[intermediate.length - NonceSizeBytes]; // plaintext
			sc.cryptBytes(in_wo_nonce, 0, out, 0, in_wo_nonce.length);
		} else {

			out = new byte[intermediate.length]; // plaintext
			sc.cryptBytes(intermediate, 0, out, 0, intermediate.length);
		}

		return out;
		//return null;   // IMPLEMENT THIS
	}
}