public class Pred implements Term {

	private Term val;

 	public Pred(Term t) {
 		if (t == null) {
 			throw new IllegalArgumentException("t cannot be null");
 		}
 		// Should I type check here?
 		this.val = t;
 	}

 	public boolean isVal() {
		return false;
    }

    public boolean isNumVal() {
		return false;
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
	return false;
    }

    public boolean isPred() {
	return true;
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
 			throw new TypeErrorException("Type Error within Pred");	
    }

    public Term eval() {
		// return this;
		if (!this.val.type().equals(Type.NAT))
    		throw new TypeErrorException("Value within Pred does not have type NAT");
    	
    	if (this.val.isZero()) {
    		Term rv = new Zero();
    		return rv;
    	} else if (this.val.isSucc()) {
    		return this.val.t1().eval();
    	} else {
    		Term new_val = this.val.eval();
    		Term rv = new Pred(new_val);
    		return rv.eval();
    	}
    }	

    public String toString() {
	return "Pred(" + this.val.toString() + ")";
    }

    public boolean same(Term that) {
		if (that.isPred()) 
			return this.val.same(that.t1());
		else
			return false;
    }

    public Term t1() {
		return this.val;
    }

    public Term t2() {
	throw new RuntimeException("Pred has no subterm t2");
    }

    public Term t3() {
	throw new RuntimeException("Pred has no subterm t3");
    }
}
