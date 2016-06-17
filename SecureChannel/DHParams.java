
import java.math.BigInteger;


public class DHParams {
	
	// CMSC 23200 Diffie-Hellman parameters (2048 bits)
	// Generated with: openssl dhparam -text 2048
	
	private static final String gStr = "2";
	private static final String pStr = "00d415d978a0ae2a13872b17193845474b44b0b733906b53a7eb63d9829f62c38785f69c5bd1c21ce5548e486b9f46950e5f9f43979046fcca9a85aa31ff3affcea16168bb69c1cd64ccfd479e67e54caeb7350a0b373725cfc2f895e0d3ac64b845799a18cc60cf00a521b33a466a341053bf24ba7b696ab7002a119e0bf7fdfd4794cafbd9df6eb26c41eea5f7e7e355ab2e682ba9a968def23314320a4fd77db15e023e66f948eeb2eb289adda1d3374537fd9838b8862203489968614d0ff799c2d932432925719317edbe61ce0456bbbb12a2345f4bd90471cb669fd4024dbe66e2ab5e4b25c5e524b50890cea5371f7a05ae7eca2449524bd875d070ee53";

	public static final BigInteger g = new BigInteger(gStr, 16);
	public static final BigInteger p = new BigInteger(pStr, 16);

	private DHParams() {}  // this class isn't meant to be instantiated
}