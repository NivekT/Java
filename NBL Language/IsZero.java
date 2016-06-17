 public class IsZero implements Term { 
 	private Term val;

 	public IsZero(Term t) {
 		if (t == null) {
 			throw new IllegalArgumentException("t cannot be null");
 		}
 		// Should I type check t here?
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
	return true;
    }

    public boolean isSucc() {
	return false;
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
			return Type.BOOL;
		else
 			throw new TypeErrorException("Type Error within IsZero");	
    }

    public Term eval() {
    	if (this.val.type().equals(Type.NAT)) {
    		if (this.val.isZero()) {
				Term rv = new True();
    			return rv;
    		} else if (this.val.isSucc()) {
    			Term rv = new False();
    			return rv;
    		} else {
    			Term new_val = this.val.eval();
    			Term rv = new IsZero(new_val);
    			return rv.eval();
    		} 
    	} else 
    		throw new TypeErrorException("Value within IsZero does not have type NAT");	
    }	

    public String toString() {
	return "IsZero(" + this.val.toString() + ")";
    }

    public boolean same(Term that) {
		if (that.isZero()) 
			return this.val.same(that.t1());
		else
			return false;
    }

    public Term t1() {
		return this.val;
    }

    public Term t2() {
	throw new RuntimeException("IsZero has no subterm t2");
    }

    public Term t3() {
	throw new RuntimeException("IsZero has no subterm t3");
    }
}

