public class True implements Term {

    public boolean isVal() {
	return true;
    }

    public boolean isNumVal() {
	return false;
    }

    public boolean isTrue() {
	return true;
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
	return false;
    }

    public int size() {
	return 1;
    }

    public Type type() {
	return Type.BOOL;
    }

    public Term eval() {
	return this;
    }	

    public String toString() {
	return "True";
    }

    public boolean same(Term that) {
	return that.isTrue();
    }

    public Term t1() {
	throw new RuntimeException("True has no subterm t1");
    }

    public Term t2() {
	throw new RuntimeException("True has no subterm t2");
    }

    public Term t3() {
	throw new RuntimeException("True has no subterm t3");
    }
    
}
