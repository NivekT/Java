public class Tests {

    static void evalTerm(Term t) {
	System.out.println("================");
	System.out.println("term: " + t);
	System.out.println("size: " + t.size());
	System.out.println("type: " + t.type());
	System.out.println("eval: " + t.eval());
    }

    static void typeError(Term t) {
	System.out.println("================");
	System.out.println("term: " + t);
	System.out.println("size: " + t.size());
	try {
	    /* we're expecting t.type() to raise */
	    System.out.println("type: " + t.type());
	    /* eval should never happen */
	    System.out.println("eval: " + t.eval());
	} catch (TypeErrorException e) {
	    System.out.println(e);
	}
    }    

    public static void main(String[] args) {

	
	evalTerm(new True());
	evalTerm(new False());
	evalTerm(new Zero());

	evalTerm(new If(new True(),
			new Zero(),
			new Succ(new Zero())));

	evalTerm(new If(new IsZero(new Succ(new Zero())),
			new Pred(new Pred(new Zero())),
			new Succ(new Zero())));

	evalTerm(new Pred(new Pred(new Pred(new Zero()))));

	evalTerm(new Pred(new Succ(new Pred(new Succ(new Zero())))));

	typeError(new IsZero(new True()));
	typeError(new Succ(new True()));
	typeError(new Pred(new True()));

	typeError(new If(new Zero(), new True(), new False()));

	typeError(new If(new True(), new True(), new Zero()));
	

    }

}