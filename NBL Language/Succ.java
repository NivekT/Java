 public class Succ implements Term {
 	
 	private Term val;

 	public Succ(Term t) {
 		if (t == null) {
 			throw new IllegalArgumentException("t cannot be null");
 		}
 		// Should I type check here?
 		// if (!t.isNumVal()) {
 		// 	throw new IllegalArgumentException("t cannot be null");
 		// }
 		this.val = t;
 	}

 	public boolean isVal() {
		return this.isNumVal();
    }

    public boolean isNumVal() {
		return this.val.isNumVal();
    }

    public boolean isTrue() {
	return false;
    }

    public boolean isFalse() {
	return false;
    }

    public boolean isZero() {
	return false;
    }

    public boolean isSucc() {
	return true;
    }

    public boolean isPred() {
	return false;
    }

    public boolean isIsZero() {
	return false;
    }

    public boolean isIf() {
	return false;
    }

    public int size() {
	return 1 + (this.t1()).size();
    }

    public Type type() {
    	if (this.val.type().equals(Type.NAT))
			return Type.NAT;
		else
 			throw new TypeErrorException("Type Error within Succ");	
    }

    public Term eval() {
		// return this;
    	if (!this.val.type().equals(Type.NAT))
    		throw new TypeErrorException("Value within Succ does not have type NAT");	
		
		if (this.isVal()) 
			return this;
		else {
			Term new_val = this.val.eval();
			Term rv = new Succ(new_val);
			return rv.eval();
		}
    }	

    public String toString() {
	return "Succ(" + this.val.toString() + ")";
    }

    public boolean same(Term that) {
		if (that.isSucc()) 
			return this.val.same(that.t1());
		else
			return false;
    }

    public Term t1() {
		return this.val;
    }

    public Term t2() {
	throw new RuntimeException("Succ has no subterm t2");
    }

    public Term t3() {
	throw new RuntimeException("Succ has no subterm t3");
    }
} 
