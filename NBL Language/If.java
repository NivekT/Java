 public class If implements Term {
 	
 	private Term t1;
 	private Term t2;
 	private Term t3;

 	public If(Term a,Term b,Term c) {
 		if (((a == null) || (b == null)) || (c == null)) {
 			throw new IllegalArgumentException("t cannot be null");
 		}
 		// Should I type check here?
 		// if (!t.isNumVal()) {
 		// 	throw new IllegalArgumentException("t cannot be null");
 		// }
 		this.t1 = a;
 		this.t2 = b;
 		this.t3 = c;
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
	return false;
    }

    public boolean isIsZero() {
	return false;
    }

    public boolean isIf() {
	return true;
    }

    public int size() {
	return 1 + (this.t1()).size() + (this.t2()).size() + (this.t3()).size();
    }

    public Type type() {
    	if (!this.t2.type().equals(this.t3.type()))
    		throw new TypeErrorException("Two branches of If do not have the same type");
    	return this.t2.type();
    }

    public Term eval() {
		// return this;
    	if (!this.t1.type().equals(Type.BOOL))
    		throw new TypeErrorException("Value within If does not have type BOOL");	
		
    	if (!this.t2.type().equals(this.t3.type()))
    		throw new TypeErrorException("Two branches of If do not have the same type");	

		if (this.t1.isVal()) {
			if (this.t1.isTrue())
				return this.t2.eval();
			else 
				return this.t3.eval();
		} else {
			Term new_t1 = this.t1.eval();
			Term rv = new If(new_t1, this.t2, this.t3);
			return rv.eval();
		} 
    }	

    public String toString() {
	return "If " + this.t1.toString() + " then " + this.t2.toString() + " else " + this.t3.toString();
    }

    public boolean same(Term that) {
		if (that.isIf()) 
			return ((this.t1.same(that.t1()) && this.t2.same(that.t2())) && this.t3.same(that.t3()));
		else
			return false;
    }

    public Term t1() {
		return this.t1;
    }

    public Term t2() {
		return this.t2;
    }

    public Term t3() {
		return this.t3;
    }
} 
