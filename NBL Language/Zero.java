public class Zero extends Const { 
	public boolean isNumVal() {
		return true;
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

	public Type type() {
		return Type.NAT;
    }
    
    public String toString() {
    	return "Zero";
    }

    public boolean same(Term that) {
    	return that.isZero();
    }
}
