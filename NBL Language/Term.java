public interface Term {

    public boolean isVal();

    public boolean isNumVal();

    /* return true only if the term is a True object */
    public boolean isTrue();

    /* return true only if the term is a False object */
    public boolean isFalse();

    /* return true only if the term is a Zero object */
    public boolean isZero();

    /* return true only if the term is a Succ object */
    public boolean isSucc();

    /* return true only if the term is a Pred object */
    public boolean isPred();

    /* return true only if the term is an IsZero object */
    public boolean isIsZero();

    /* return true only if the term is an If object */
    public boolean isIf();

    /* return subterm t1, t2, or t3 is there is one, else throw a RuntimeException */
    public Term t1();
    public Term t2();
    public Term t3();

    /* size as defined by 3.3.2 */
    public int size();

    /* for well-typed terms, return the type; otherwise throw a TypeErrorException */
    public Type type();

    public Term eval();

    public String toString();

    public boolean same(Term that);

}