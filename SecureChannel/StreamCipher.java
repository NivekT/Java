
public class StreamCipher {
	// This class encrypts or decrypts a stream of bytes, using a stream cipher.
	  
	public static final int KeySizeBits = 192;	// want x + 64 = 256 bit
	// want KeySizeBits + NonceSizeBits = 256
	public static final int KeySizeBytes = KeySizeBits/8;
	
	public static final int NonceSizeBits = 64;
	public static final int NonceSizeBytes = NonceSizeBits/8;

	private PRGen prg;
	private byte[] k;

	public StreamCipher(byte[] key) {
		// <key> is the key, which must be KeySizeBytes bytes in length.

		assert key.length == KeySizeBytes;

		// IMPLEMENT THIS
		k = key;
	}

	public void setNonce(byte[] arr, int offset){ // array of nonce, and offset
		assert arr.length == NonceSizeBytes + offset; // ensure the Nonce has the right size
		// Reset to initial state, and set a new nonce.
		// The nonce is in arr[offset] thru arr[offset+NonceSizeBytes-1].
		// It is an error to call setNonce with the same nonce
		//    more than once on a single StreamCipher object.
		// StreamCipher does not check for nonce uniqueness;
		//    that is the responsibility of the caller.

		// IMPLEMENT THIS
		// use whole thing (arr) starting from offset
		// move to the end of key array
		// combine key with Nonce
		byte[] k2 = new byte[KeySizeBytes + NonceSizeBytes];
		// System.arraycopy(arr, offset, k, KeySizeBytes, NonceSizeBytes);
		// in case k runs out of space in direct copy
		System.arraycopy(k, 0, k2, 0, KeySizeBytes);
		System.arraycopy(arr, offset, k2, KeySizeBytes, NonceSizeBytes); 
		// should I left shift to compress Key back to original size?

		prg = new PRGen(k2); // initialize PRG
	}

	public void setNonce(byte[] nonce) {
		// Reset to initial state, and set a new nonce
		// It is an error to call setNonce with the same nonce
		//    more than once on a single StreamCipher object.
		// StreamCipher does not check for nonce uniqueness;
		//    that is the responsibility of the caller.

		assert nonce.length == NonceSizeBytes;
		setNonce(nonce, 0);
	}

	public byte cryptByte(byte in) { // encrypting a single byte 
		// Encrypt/decrypt the next byte in the stream
		byte[] c = new byte[32]; // need a byte constant
		//System.out.println("byte[0] is" + (char) c[0]);
		prg.nextBytes(c); // nextBytes is a method on random that uses .next on PRG to update the prg
		//System.out.println("byte[0] is" + (char) c[0]);
		// System.out.println("\"encrypted\" byte is " + new String ((char) c[0]));
		return (byte) (c[0] ^ in); // encrypt the byte
//		return in;   // IMPLEMENT THIS
	}

	public void cryptBytes(byte[] inBuf, int inOffset, 
			byte[] outBuf, int outOffset, 
			int numBytes) {
		// Encrypt/decrypt the next <numBytes> bytes in the stream
		// Take input bytes from inBuf[inOffset] thru inBuf[inOffset+numBytes-1]
		// Put output bytes at outBuf[outOffset] thru outBuf[outOffset+numBytes-1];

		// IMPLEMENT THIS
		for (int i = 0; i < numBytes; i++) {
			outBuf[outOffset + i] = this.cryptByte(inBuf[inOffset + i]); // loop over the entire text
		}
		// System.out.println("The outBuf is " + new String(outBuf));
	}

}
