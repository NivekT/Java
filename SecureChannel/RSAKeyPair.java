import java.math.BigInteger;

public class RSAKeyPair {
	private RSAKey publicKey;
	private RSAKey privateKey;

	private BigInteger p;
	private BigInteger q;

	private BigInteger d;
	private BigInteger e;
	private BigInteger n;

	public RSAKeyPair(PRGen rand, int numBits) {
		// Create an RSA key pair.  rand is a PRGen that this code can use to get pseudorandom
		//     bits.  numBits is the size in bits of each of the primes that will be used.

		// IMPLEMENT THIS

		p = Proj2Util.generatePrime(rand, numBits);
		q = Proj2Util.generatePrime(rand, numBits);
		BigInteger t;
		BigInteger p2 = BigInteger.ONE;
		BigInteger q2 = BigInteger.ONE;
		// byte[] eint = new byte[4];
		int eint = 65537;
		n = p.multiply(q);
		// t = (p - 1) * (q - 1); BigInteger.ONE
		t = (p.subtract(p2)).multiply(q.subtract(q2));
		e = BigInteger.valueOf(eint);
		d = e.modInverse(t);
		publicKey = new RSAKey(e, n);
		privateKey = new RSAKey(d, n);
	}

	public RSAKey getPublicKey() {
		return publicKey;
	}

	public RSAKey getPrivateKey() {
		return privateKey;
	}

	public BigInteger[] getPrimes() {
		// Returns an array containing the two primes that were used in key generation.
		//   In real life we don't always keep the primes around.
		//   But including this helps us grade the assignment.
		BigInteger[] ret = new BigInteger[2];
		ret[0] = p; // IMPLEMENT THIS
		ret[1] = q;
		return ret;
	}

}
