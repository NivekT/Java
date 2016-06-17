
import java.util.Random;


public class PRGen extends Random {
	// This implements a pseudorandom generator.  It extends java.util.Random, which provides
	//     a useful set of utility methods that all build on next(.).  See the documentation for
	//     java.util.Random for an explanation of what next(.) is supposed to do.
	// If you're calling a PRGen, you probably want to call methods of the Random superclass.
	//
	// There are two requirements on a pseudorandom generator.  First, it must be pseudorandom,
	//     meaning that there is no (known) way to distinguish its output from that of a
	//     truly random generator, unless you know the key.  Second, it must be deterministic, 
	//     which means that if two programs create generators with the same seed, and then
	//     the two programs make the same sequence of calls to their generators, they should
	//     receive the same return values from all of those calls.
	// Your generator must have an additional property: backtracking resistance.  This means that if an
	//     adversary is able to observe the full state of the generator at some point in time, that
	//     adversary cannot reconstruct any of the output that was produced by previous calls to the
	//     generator.
	
	
	public static final int SeedSizeBits = 256;
	public static final int SeedSizeBytes = SeedSizeBits/8;
	private PRF prf;
	private byte[] state;

	public PRGen(byte[] seed) {
		super();
		assert seed.length == SeedSizeBytes;

		// IMPLEMENT THIS
		// state = seed; // Should not save state into memory
		prf = new PRF(state);
	}


	protected int next(int bits) { 
		// For description of what this is supposed to do, see the documentation for 
		//      java.util.Random, which we are subclassing.
		byte [] c = {42, 7, 13, 44}; // initial constants
		byte [] rv = prf.eval(c); // returns 4 random values each of 8 bits
		int r = (rv[0] << 24 | rv[1] << 16 | rv[2] << 8 | rv[3]); // compress rv into 32 bits
		r = r >>> (32 - bits); // unsigned right shift based on input
		byte [] c2 = {24, 40, 21, 8}; // 2nd constant
		
		state = prf.eval(state);
		prf = new PRF(prf.eval(c2));	// update state and prf

		return r;   // IMPLEMENT THIS
	}
}