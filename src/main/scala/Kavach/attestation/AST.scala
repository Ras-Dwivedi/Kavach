package kavach.attestation

import scala.io.Source


final case class InvalidDerivationException(
    private val message: String = "Invalid derivation",
    private val cause: Throwable = None.orNull)
  extends java.lang.RuntimeException(message, cause)

abstract class Expression

case object True extends Expression
case class Principal (v : String)
case class Proposition (v: String) extends Expression

case class Says (P: Principal, s: Expression) extends Expression 
case class And(a: Expression, b: Expression) extends Expression 
case class Or(a: Expression, b: Expression) extends Expression 
case class Implies(a: Expression, b: Expression) extends Expression


//each case class of this class represent a one step Derivation 
abstract class Derivation { 
    val ctx: Set[Expression]
    val st: Expression
}

case class Var (ctx1: Set[Expression], s:Expression) extends Derivation {
    override val ctx = ctx1 + s
    override val st = s
}

case class Unit_true (ctx: Set[Expression]) extends Derivation{
    override val st = True
}

case class Lam(d: Derivation, s1: Expression, s2: Expression) extends Derivation{
    if(!d.ctx.contains(s1) || d.st != s2) {
        throw InvalidDerivationException()
    }
    override val ctx = d.ctx - s1
    override val st = Implies(s1,s2)
}

case class App(d: Derivation, s1:Implies) extends Derivation {
    if(!(d.st == s1 && d.ctx.contains(s1.a))) {
        throw InvalidDerivationException()
    }
    override val ctx = d.ctx
    override val st = s1.b
}



case class Pair(d1: Derivation, d2: Derivation) extends Derivation{
    if (!(d1.ctx==d2.ctx)){
        throw InvalidDerivationException()
        }

        override val ctx = d1.ctx    
        override val st = And(d1.st, d2.st)
}


case class Proj1(d: Derivation, s: And) extends Derivation{
    if (!(d.st==s)){
        throw InvalidDerivationException()
    }
       override val ctx= d.ctx
        override val st=s.a
}

case class Proj2(d: Derivation, s: And) extends Derivation{
    if (!(d.st==s)){
        throw InvalidDerivationException()
    }
       override val ctx= d.ctx
        override val st=s.b
}


case class Inj1(d: Derivation, s: Expression) extends Derivation{
        override val ctx= d.ctx
        override val st=Or(d.st, s)
}

case class Inj2(d: Derivation, s: Expression) extends Derivation{
        override val ctx= d.ctx
        override val st=Or(s, d.st)
}


case class UnitM(d: Derivation, P: Principal) extends Derivation{
        override val ctx= d.ctx
        override val st=Says(P, d.st)
}


case class Case (d1: Derivation, d2: Derivation, d3: Derivation, s: Expression){
    d1.st match {
        case Or(a,b) => {assert (d1.ctx == d2.ctx - a);
                                 assert (d1.ctx == d3.ctx - b);
                                assert(d2.st == d3.st);
                     }
        case _ =>  throw InvalidDerivationException()                 
    }
    override val ctx = d1.ctx
    override val st = d3.st
}


case class Bindm(d1: Derivation, d2: Derivation) extends Derivation{
    d1.st match{
        case Says(p1,s1) => {d2.st match {
                                case Says(p2,s2) => {assert(p1==p2);
                                                    assert(d1.ctx == d2.ctx - s1)
                                                   }
                                case _ => throw InvalidDerivationException()
                                       }
                           }
        case _ => throw InvalidDerivationException() 
    }

    override val ctx = d1.ctx
    override val st= d2.st
}

// Todays work
case class ForAll(x: Proposition, s: Expression) extends Expression

// you must be able to add ForAll int the context without invoking these rules. This could be done via Var then you must be able check which variables are not free  in the context, (do it for a Expression and the iterate over the context) and only for those variables you can do this quantification

// This function checks whether x is free in s
def isFree(s: Expression, x):Boolean = {
    val v = s match {
        case Says(P,s1)      => isFree(s1,x)
        case And(s1,s2)      => isFree(s1,x) && isFree(s2,x)  
        case Or(s1,s2)       =>  isFree(s1,x) && isFree(s2,x)
        case Implies(s1,s2)  => isFree(s1,x) && isFree(s2,x)   
        case ForAll(s1,y)    => if ( y== x) false else isFree(s1)
        case Proposition(s1) => True
        case _ => true    
    }

    retrun v
}

// This functions checks whether x is free in the context or not. Idea is that if for any one expression x is not free, then it is not free in the context
def isFree(ctx: Set[Expression], x) = {
    var flag =true
    for (s <- ctx) {
        flag = flag && isFree(s,x)
    }
    retrun flag
}

// the two overloaded function finds out the variables in a context, so that one can generate the list of not free variabls in the context
def variablesIn (s:Expression): Set[Proposition] ={
    var v = Set[Proposition]() //declared empty set
    v = s match {
        case Says(P,s1)      => variablesIn(s1)
        case And(s1,s2)      => variablesIn(s1).++variablesIn(s2)  
        case Or(s1,s2)       =>  variablesIn(s1).++variablesIn(s2)
        case Implies(s1,s2)  => variablesIn(s1).++variablesIn(s2)   
        case ForAll(s1,y)    => variablesIn(s1) + y
        case Proposition(s1) => Set[Proposition](s1)
        }
}

def variablesIn(ctx: Set[Expression]):Set[Proposition] = {
    var v = Set[Expression]()
    for (s<- ctx){
        v = v + variablesIn(s)
    }
    retrun v
}
// This function retruns the variables not free in context ctx
def not_Free (ctx: Set[Expression]): Set[Proposition]= {
    var v = variablesIn(ctx)
    var notFree = Set[Proposition]()
    for (x <- v) {
        var flag = isFree(ctx,x)
        if(!flag) (notFree = notFree + x)
    }
    
    retrun notFree

}



case class TLam(d1: Derivation, x: Proposition) extends Derivation{
    override val ctx = d1.ctx
    override val st = ForAll(x, d1.st)
}

case class TLam(d1: TLam, t: Proposition) extends Derivation{
    override val ctx= d1.ctx
    override st = substitute (d1, t, x)
}

// case class Says (P: Principal, s: Expression) extends Expression 
// case class And(a: Expression, b: Expression) extends Expression 
// case class Or(a: Expression, b: Expression) extends Expression 
// case class Implies(a: Expression, b: Expression) extends Expression



def substitute(d: Expression, t: Proposition, x: Proposition)={
    // For now assume that we can replace x by t. Checking this validity is different matter :-)
    // Do we need to do substitution in a derivation or in a Expression? I think it is Expression
    val v1 = d1 match {
        case Says(P, s)       => Says(P, substitute (s,t,x))
        case And(s1,s2)       => And(substitute(s1,t,x), substitute(s2,t,x))
        case Or(s1,s2)        => Or(substitute(s1,t,x), substitute(s2,t,x))
        case Implies(s1,s2)   => Implies(substitute(s1,t,x), substitute(s2,t,x)) 
       // case ForAll ??  
        1. when do we need the substitution. Do we need to obtain certain formula by replacing an Expression by another expression, or do we need to remove the quantifier to get the instance of a formula
    }
}

def substitute(d: Proposition, t: Proposition, x: Proposition)={
    // For now assume that we can replace x by t. Checking this validity is different matter :-)
    // Do we need to do substitution in a derivation or in a Expression? I think it is Expression

    // so a term t is free for x if either there is not free  occurances of x, or in every free occurances of x, there is no quanfier containing same terms as in t
    val v1 = d match {
        case d if (d==x) => t
        case _ => d    
    }
}

// this does the reading part
// object AST {
//     def main(args: Array[String]) {
//         val a = Proposition("a")
//         val b = Proposition("b")
//         val impl = Implies(a, b)
//         val ctx : Set[Expression] = Set(a)

//         val deriv = Var(ctx, impl)
//         val proof = App(deriv, impl)

//         println(proof.toString())
//         println("hello, world!")
//     }
// }

