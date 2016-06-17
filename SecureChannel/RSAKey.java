import java.math.BigInteger;
import java.util.Arrays;

public class RSAKey {
    private BigInteger exponent;
    private BigInteger modulus;
    
    private static final int oaepK0SizeBytes = 32;
	private static final int oaepK1SizeBytes = 32;

    public RSAKey(BigInteger theExponent, BigInteger theModulus) {
        exponent = theExponent;
        modulus = theModulus;
    }

    public BigInteger getExponent() {
        return exponent;
    }

    public BigInteger getModulus() {
        return modulus;
    }

    public byte[] encrypt(byte[] plaintext, PRGen prgen) {
        if (plaintext == null)    throw new NullPointerException();
        int rv_len = (maxPlaintextLength()) + oaepK0SizeBytes + oaepK1SizeBytes;
        byte[] xy = new byte[rv_len];
        xy = this.encodeOaep(plaintext, prgen);
        // System.out.println("xy: " + Arrays.toString(xy)); 
        // System.out.println("xy len: " + xy.length); 
        // System.out.println("rv len: " + rv_len); 
        // return xy;
        BigInteger m, c;
        m = Proj2Util.bytesToBigInteger(xy);
        c = m.modPow(exponent,modulus);
        // System.out.println(c);
        // System.out.println("c in e:" + Arrays.toString(Proj2Util.bigIntegerToBytes(c, rv_len)));
        return Proj2Util.bigIntegerToBytes(c, rv_len+1); // IMPLEMENT THIS
    }

    public byte[] decrypt(byte[] ciphertext) {
        if (ciphertext == null)    throw new NullPointerException();
        //return decodeOaep(ciphertext);
        BigInteger c, m; 
        // System.out.println("c in d:" + Arrays.toString(ciphertext));
        c = Proj2Util.bytesToBigInteger(ciphertext);
        //System.out.println(c);
        m = c.modPow(exponent, modulus);
        //int msize = m.bitLength() >>> 3;
        int rv_len = (maxPlaintextLength()) + oaepK0SizeBytes + oaepK1SizeBytes;
        byte[] m2 = Proj2Util.bigIntegerToBytes(m, rv_len);
        // System.out.println("m2:" + Arrays.toString(m2));
        return decodeOaep(m2); // IMPLEMENT THIS
    }

    public byte[] sign(byte[] message, PRGen prgen) {
        // Create a digital signature on <message>. The signature need
        //     not contain the contents of <message>--we will assume
        //     that a party who wants to verify the signature will already
        //     know which message this is (supposed to be) a signature on.
    	//
    	//     Note: The signature algorithm that we discussed in class is 
    	//     deterministic, and so if you implement it, you do not need 
    	//     to use the PRGen parameter. There is, however, a signature 
    	//     algorithm that is superior to the one that we discussed that 
    	//     does use pseudorandomness. Implement it for extra credit. See
    	//     the assignment description for details.
        if (message == null)    throw new NullPointerException();
        // hash message to h
        // encrypt h using RSA
        // that returns a BigInteger, we converts that byte[]
        byte[] h = new byte[oaepK0SizeBytes];
        // byte[] s = new byte[oaepK0SizeBytes];
        h = Proj2Util.hash(message);
        byte[] h_pad = new byte[maxPlaintextLength()];
        h_pad = addPadding(h);
        BigInteger h2, h2r;
        // System.out.println("hash of m is" + Arrays.toString(h));
        h2 = Proj2Util.bytesToBigInteger(h_pad);
        // System.out.println("h2 is" + new String (h2.toByteArray()));
        h2r = h2.modPow(exponent, modulus);
        //System.out.println(Arrays.toString(h2r.abs().toByteArray()));
        return h2r.abs().toByteArray(); 
        //Proj2Util.bigIntegerToBytes(h2r, oaepK0SizeBytes); // IMPLEMENT THIS
    }

    public boolean verifySignature(byte[] message, byte[] signature) {
        // Verify a digital signature. Returns true if  <signature> is
        //     a valid signature on <message>; returns false otherwise.
        //     A "valid" signature is one that was created by calling
        //     <sign> with the same message, using the other RSAKey that
        //     belongs to the same RSAKeyPair as this object.
        if ((message == null) || (signature == null))    throw new NullPointerException();
        // RSA decrytion
        // make sure the calculated value match the input signature
        // requires the user to call the method with the correct Key with proper exponent 
        byte[] h = new byte[oaepK0SizeBytes];
        byte[] h_pad = new byte[maxPlaintextLength()];
        h_pad = addPadding(h);
        //byte[] s = new byte[oaepK0SizeBytes];
        h = Proj2Util.hash(message);
        // System.out.println("hash of m is" + Arrays.toString(h));
        BigInteger h2, h2r, s, s2r;
        h2 = Proj2Util.bytesToBigInteger(h_pad);
        // System.out.println("h2 is" + new String (h2.toByteArray()));
        // h2r = h2.modPow(exponent, modulus);
        s = Proj2Util.bytesToBigInteger(signature);
        s2r = s.modPow(exponent, modulus);
        // System.out.println("s2r is" + new String (s2r.toByteArray()));
        // s = Proj2Util.bigIntegerToBytes(h2r, oaepK0SizeBytes); 
        //System.out.println("Sig is" + Arrays.toString(signature));
        // System.out.println("hash is" + Arrays.toString(h2r.abs().toByteArray()));
        //return (Arrays.equals((h2.abs().toByteArray()), signature)); // IMPLEMENT THIS
        return (s2r.equals(h2));
        //return false;
    } 

    public int maxPlaintextLength() {
        // Return the largest x such that any plaintext of size x bytes
        //      can be encrypted with this key
        
        return (modulus.bitLength() / 8 - 1) - oaepK0SizeBytes - oaepK1SizeBytes;
    }
       
    // The next four methods are public to help us grade the assignment. In real life, these would
    // be private methods as there's no need to expose these methods as part of the public API
    
    public byte[] encodeOaep(byte[] input, PRGen prgen) {
        int max_len = this.maxPlaintextLength();
        byte[] rv = new byte[max_len + oaepK0SizeBytes + oaepK1SizeBytes];
        byte[] x = new byte[max_len + oaepK1SizeBytes];
        byte[] y = new byte[oaepK0SizeBytes];
        byte[] k0 = new byte[oaepK0SizeBytes]; // 32 bytes
        byte[] k1 = new byte[oaepK1SizeBytes];
        byte[] k0_g = new byte[max_len + oaepK1SizeBytes];
        byte[] h = new byte[oaepK0SizeBytes];

        Arrays.fill(k1, (byte) 0); // k1 is all 0s
        // if (input.length == max_len) {
        //     k1[0] = (byte) 1; 
        // }

        //pad m, x = m_pad
        //System.out.println("Input is " + Arrays.toString(input));
        x = this.addPadding(input);
        //System.out.println("x is " + Arrays.toString(x));
        //System.arraycopy(input, 0, x, 0, maxPlaintextLength);
        byte[] x2 = new byte[max_len + oaepK1SizeBytes];
        // pad m_pad by k1 
        // System.out.println("bug here");
        // System.out.println(max_len);
        // System.out.println(x.length);
        System.arraycopy(x, 0, x2, 0, max_len);
        System.arraycopy(k1, 0, x2, max_len, oaepK1SizeBytes);

        //System.out.println("x2 " + Arrays.toString(x2));
        
        // k0 = r
        prgen.nextBytes(k0); // making r

        //System.out.println("k0 in e is: " + Arrays.toString(k0));
        // k0_g = G(r)
        PRGen g = new PRGen(k0);
        g.nextBytes(k0_g);
        //k0_g = Proj2Util.hash(k0); 
        
        // xor k0_g with m_pad_k1, resulting in x
        for (int i = 0; i < (max_len + oaepK1SizeBytes); i++) {
            x2[i] = (byte) (x2[i] ^ k0_g[i]);
        }

        // hash X to 32-byte 
        h = Proj2Util.hash(x2);
        // xor the hash of x with r (k0)
        for (int j = 0; j < oaepK0SizeBytes; j++) {
            y[j] = (byte) (h[j] ^ k0[j]);
        }
        //System.out.println("y in e is " + Arrays.toString(y));
        // System.out.println("x in e:" + Arrays.toString(x2));
        // System.out.println("x len" + x2.length);
        // System.out.println("y in e:" + Arrays.toString(y));
        // System.out.println("y len" + y.length);
        // rv = x + y
        System.arraycopy(x2, 0, rv, 0, max_len + oaepK1SizeBytes);
        System.arraycopy(y, 0, rv, max_len + oaepK1SizeBytes, oaepK0SizeBytes);
        //System.out.println("rv: " + Arrays.toString(rv));
        return rv; // IMPLEMENT THIS
    }
    
    public byte[] decodeOaep(byte[] input) {
        int len = input.length;
        //System.out.println("input " + Arrays.toString(input));
        byte[] x = new byte[len - oaepK0SizeBytes];
        byte[] y = new byte[oaepK0SizeBytes];
        byte[] h = new byte[oaepK0SizeBytes];
        byte[] r = new byte[oaepK0SizeBytes];
        byte[] m_pad = new byte[len - oaepK0SizeBytes];
        byte[] k0_g = new byte[maxPlaintextLength() + oaepK1SizeBytes];

        System.arraycopy(input, 0, x, 0, len - oaepK0SizeBytes);
        System.arraycopy(input, len - oaepK0SizeBytes, y, 0, oaepK0SizeBytes);

        h = Proj2Util.hash(x);

        // System.out.println("h in d is: " + Arrays.toString(h));
        // System.out.println("y in d is: " + Arrays.toString(y));

        // System.out.println("x " + Arrays.toString(x));
        // System.out.println("x len" + x.length);
        // System.out.println("h " + Arrays.toString(h));
        // System.out.println("y " + Arrays.toString(y));
        // System.out.println("y len" + y.length);

        // if (!(Arrays.equals(h,y))) { // check if h matches y
        //     System.out.println("h does not match y");
        //     return null;
        // }

        for (int i = 0; i < oaepK0SizeBytes; i++) {
            r[i] = (byte) (h[i] ^ y[i]);
        }

        // System.out.println("r in d is: " + Arrays.toString(r));

        PRGen g = new PRGen(r);
        g.nextBytes(k0_g);
        // len - oaepK0SizeBytes
        for (int j = 0; j < (maxPlaintextLength() + oaepK1SizeBytes); j++) {
            m_pad[j] = (byte) (k0_g[j] ^ x[j]);
        }
        // returns m without k1 padding
        return this.removePadding(m_pad); // IMPLEMENT THIS
    }
    
    public byte[] addPadding(byte[] input) {
        int len = input.length;
        int max = this.maxPlaintextLength();
        int diff = max - len;

        if (diff == 0) {
            return input; // no need to pad if input.len == max len
        }

        byte[] pad = new byte[diff];
        Arrays.fill(pad, (byte) 0); // k1 is all 0s, unless input.len == max len
        pad[0] = (byte) 1;


        byte[] rv = new byte[max]; 
        System.arraycopy(input, 0, rv, 0, len);
        System.arraycopy(pad, 0, rv, len, diff);

        // byte[] zero = new byte[1];
        // byte[] one = new byte[1]
        // zero[0] = (byte) 0;
        // one[0] = (byte) 1;

        // if (len < max) {
        //     System.arraycopy(one, 0, rv, len, 1);
        //     len++;
        // }

        // while (len < max) {
        //     System.arraycopy(zero, 0, rv, len, 1);
        //     len++;
        // }
        // System.out.println("padding");
        // System.out.println(input.length);
        // System.out.println(max);
        // System.out.println(rv.length);

        return rv; // IMPLEMENT THIS
    }
    
    // removes k1 padding as well
    public byte[] removePadding(byte[] input) {
        int len = input.length;

        // System.out.println("input of padding is" + Arrays.toString(input));
        if (input[len - 1] == 1) 
            return input;     
        while (input[len - 1] != 1) { 
            len--;
        }
        byte[] rv = new byte[len - 1];
        System.arraycopy(input, 0, rv, 0, len - 1);
        return rv; // IMPLEMENT THIS
    }
}
