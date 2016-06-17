import java.math.BigInteger;
import java.util.Arrays;


public class KeyExchange {
	public static final int OutputSizeBits = 2048; // IMPLEMENT THIS
	public static final int OutputSizeBytes = OutputSizeBits/8;

	private BigInteger a; //x; 
	//private byte[] k;

	public KeyExchange(PRGen rand) {
		// Prepares to do a key exchange. rand is a secure pseudorandom generator
		//    that can be used by the implementation.
		//
		// Once the KeyExchange object is created, two operations have to be performed to complete
		// the key exchange:
		// 1.  Call prepareOutMessage on this object, and send the result to the other
		//     participant.
		// 2.  Receive the result of the other participant's prepareOutMessage, and pass it in
		//     as the argument to a call on this object's processInMessage.  
		// For a given KeyExchange object, prepareOutMessage and processInMessage
		// could be called in either order, and KeyExchange should produce the same result regardless.
		//
		// The call to processInMessage should behave as follows:
		//     If passed a null value, then throw a NullPointerException.
		//     Otherwise, if passed a value that could not possibly have been generated
		//        by prepareOutMessage, then return null.
		//     Otherwise, return a "digest" value with the property described below.
		//
		// This code must provide the following security guarantee: If the two 
		//    participants end up with the same non-null digest value, then this digest value
		//    is not known to anyone else.   This must be true even if third parties
		//    can observe and modify the messages sent between the participants.
		// This code is NOT required to check whether the two participants end up with
		//    the same digest value; the code calling this must verify that property.

		// IMPLEMENT 
		// a is the user's own random number, the user wants b from the other party
		byte[] array = new byte[32];
		rand.nextBytes(array);
		a = Proj2Util.bytesToBigInteger(array); 
		//k;
	}

	public byte[] prepareOutMessage() {
//		byte out[OutputSizeBytes];
		BigInteger o = DHParams.g.modPow(this.a, DHParams.p);
		return Proj2Util.bigIntegerToBytes(o,OutputSizeBytes); // IMPLEMENT THIS
	}

	public byte[] processInMessage(byte[] inMessage) {
		if (inMessage == null)    throw new NullPointerException();
		if (inMessage.length != OutputSizeBytes) return null;
		// if (DHParams.g.equals(BigInteger.ONE)) return null; // ensure g != 1
		// if (DHParams.g.equals(DHParams.p.subtract(BigInteger.ONE))) return null; // ensure g != p - 1

		BigInteger in = Proj2Util.bytesToBigInteger(inMessage);
		BigInteger x = in.modPow(this.a, DHParams.p);
		byte[] k = new byte[OutputSizeBytes];
		byte[] x2 = new byte[OutputSizeBytes];
		x2 = Proj2Util.bigIntegerToBytes(x, OutputSizeBytes);
		k = Proj2Util.hash(x2);
		// if I want to return key, return k
		// but here I am returning x2 in h(x2) = k
		return x2; // IMPLEMENT THIS
	}

}
