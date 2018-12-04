package kavach.attestation
import kavach.attestation.definitions._

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
case class ForAll(x: Proposition, s: Expression) extends Expression


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


case class Case (d1: Derivation, d2: Derivation, d3: Derivation, s: Expression) extends Derivation{
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

case class TLam(d: Derivation, x: Proposition) extends Derivation{
    if(isFree(d.ctx,x)) throw InvalidDerivationException()
    override val ctx = d.ctx
    override val st = ForAll(x, d.st)
}

case class Tapp(d: TLam, t: Proposition) extends Derivation{
    override val ctx= d.ctx
    override val st = substitute (d.st, t, d.x)
}

object AST {
// this does the reading part

    def main(args: Array[String]) {
        val a = Proposition("a")
        val b = Proposition("b")
        val impl = Implies(a, b)
        val ctx : Set[Expression] = Set(a)

        val deriv = Var(ctx, impl)
        val proof = App(deriv, impl)

        println(proof.toString())
        println("hello, world!")
    }
}

