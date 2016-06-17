public abstract class Const implements Term {
	
	public boolean isVal() {
		return true;
	}

	public abstract boolean isNumVal();
	public abstract boolean isTrue();
	public abstract boolean isFalse();
	public abstract boolean isZero();
	
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

	public Term t1() {
    	throw new RuntimeException("True/False/Zero has no subterm t1");
    }

    public Term t2() {
    	throw new RuntimeException("True/False/Zero has no subterm t2");
    }

    public Term t3() {
    	throw new RuntimeException("True/False/Zero has no subterm t3");
    }

    public int size() {
    	return 1;
    }

    public abstract Type type();

    public Term eval() {
    	return this;
    }

    public abstract String toString();

    public abstract boolean same(Term that);

}