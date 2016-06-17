public class False extends Const {
	public boolean isNumVal() {
		return false;
	}

	public boolean isTrue() {
		return false;
	}
	
	public boolean isFalse() {
		return true;
	}
	
	public boolean isZero() {
		return false;
	}

	public Type type() {
		return Type.BOOL;
    }

    public String toString() {
    	return "False";
    }

    public boolean same(Term that) {
    	return that.isFalse();
    }
}
