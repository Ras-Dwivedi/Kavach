import org.scalatest._

import kavach.attestation._
import java.io._
import kavach.attestation.RSA._
// @Ignore
class LogicalAttestationSpec extends FlatSpec {
  "Proposition" should "execute" in {
    val a= Proposition ("a")
    val b = Proposition("b")
    val c = Proposition("c")
    val prop_or = Or(a,b) // a||b
    val prop_and = And(a,b) // a && b
    val prop_implies = Implies(c,b) // c=> b
    val P = Principal ("P", "P_Keys")
    val P_says= Says(P,c,P.sign(c))
    println("\n\n\n\n\n\n\n\n\nall Proposition checked, all proposition formulated successfully")
  }  

  "Says_error" should  "throw InvaliVerificationError" in {
    val P = Principal ("P", "P_Keys")
    val c = Proposition("c") ;// defining the Proposition "c"
    val Q = Principal("Q", "Q_keys")
    // [InvalidVerificationException] should be thrownBy {
        assertThrows [InvalidVerificationException] {
        // val P_says= Says(P,c,P.sign(c))
        // val P_says= Says(P,c,"c".getBytes()) java.security.SignatureException
        val P_says= Says(P,c,Q.sign(c))
    }
  }
  it should  "java.security.SignatureException" in {
    val P = Principal ("P", "P_Keys")
    val c = Proposition("c") ;// defining the Proposition "c"
    val Q = Principal("Q", "Q_keys")
    // [InvalidVerificationException] should be thrownBy {
        assertThrows [java.security.SignatureException] {
        // val P_says= Says(P,c,P.sign(c))
        val P_says= Says(P,c,"c".getBytes()) //java.security.SignatureException
        // val P_says= Says(P,c,Q.sign(c))
    }

}

"var" should "execute " in {
    val a= Proposition ("a")
    val deriv = Var(Set[Expression](), a)
   deriv.print
}

"Unit_true" should "execute " in {
    val a= Proposition ("a")
    val deriv = Unit_true(Set[Expression](a))
    deriv.print
} 
"Unit_true no_prams" should "execute " in {
    val a= Proposition ("a")
    val deriv = new Unit_true()
    deriv.print
}
"Unit_true_null" should "execute " in {
    val a= Proposition ("a")
    val deriv = Unit_true(Set[Expression]())
    deriv.print
}
"Lam " should "execute" in {
    val a = Proposition("a")
    val b = Proposition("b")
    val ctx = Set[Expression](a,b)
    val deriv = Var(ctx,b) // d.st = b
    val deriv2 = Lam(deriv,a,b)
    deriv2.print
}
"Lam1 " should "execute" in {
    val a = Proposition("a")
    val b = Proposition("b")
    val ctx = Set[Expression](a,b)
    val deriv = Var(ctx,b) // d.st = b
    val deriv2 = Lam(deriv,b,b)
    deriv2.print

}

"Lam1 " should "Thow InvalidDerivationException" in {
    val a = Proposition("a")
    val b = Proposition("b")
    val ctx = Set[Expression](a,b)
    val deriv = Var(ctx,a) // d.st = b
    assertThrows [InvalidDerivationException]{
    val deriv2 = Lam(deriv,b,b)
    deriv2.print
    }
}
"Lam" should "Thow InvalidDerivationException" in {
    val a = Proposition("a")
    val b = Proposition("b")
    val ctx = Set[Expression](a)
    val deriv = Var(ctx,a) // d.st = b
    assertThrows [InvalidDerivationException]{
    val deriv2 = Lam(deriv,b,a)
    deriv2.print
    }
}
"App" should "execute" in {
    val a = Proposition("a")
    val b = Proposition("b")
    val c = Implies(a,b)
    val ctx = Set[Expression](a)
    val deriv = Var(ctx,c) // d.st = b
    val deriv2 = App(deriv,Implies(a,b))
    deriv2.print
}
"App_context" should "Thow InvalidDerivationException" in {
    val a = Proposition("a")
    val b = Proposition("b")
    val c = Implies(a,b)
    val ctx = Set[Expression]()
    val deriv = Var(ctx,b)
    assertThrows [InvalidDerivationException]{
    val deriv2 = App(deriv,Implies(a,b))
    deriv2.print
    }
    println("error due to incomplete context")
}
"App_statement" should "Thow InvalidDerivationException" in {
    val a = Proposition("a")
    val b = Proposition("b")
    val c = Implies(a,b)
    val d = Proposition("d")
    val ctx = Set[Expression](a)
    val deriv = Var(ctx,d)
    assertThrows [InvalidDerivationException]{
    val deriv2 = App(deriv,Implies(a,b))
    deriv2.print
    }
    println("error due to different statement")
}
"Pair" should "execute" in {
    val a = Proposition("a")
    val b = Proposition("b")
    val c = Proposition("c")
    val d = Var(Set[Expression](b,c),a)
    val e = Var(Set[Expression](a,c),b)
    val deriv=Pair(d,e)
    deriv.print
}
"Pair" should "Throw InvalidVerificationException" in {
    val a = Proposition("a")
    val b = Proposition("b")
    val c = Proposition("c")
    val d = Var(Set[Expression](b),a)
    val e = Var(Set[Expression](a,c),b)
    assertThrows [InvalidDerivationException]{
    val deriv=Pair(d,e) // contexts are not same
    deriv.print
    }
}

"Proj1" should "execute" in {
    val a = Proposition("a")
    val b = Proposition("b")
    val c = Var(Set[Expression](),And(a,b))
    val deriv=Proj1(c,And(a,b))
    deriv.print
}
// I should be able to test mismatch error
"Proj1" should "throw InvalidDerivationException" in {
    val a = Proposition("a")
    val b = Proposition("b")
    val d = Proposition("d")
    val c = Var(Set[Expression](),And(a,b))
    assertThrows [InvalidDerivationException]{
    val deriv=Proj1(c,And(a,d))
    deriv.print
    }
}

"Proj2" should "execute" in {
    val a = Proposition("a")
    val b = Proposition("b")
    val c = Var(Set[Expression](),And(a,b))
    val deriv=Proj2(c,And(a,b))
    deriv.print
}
// I should be able to test mismatch error
"Proj2" should "throw InvalidDerivationException" in {
    val a = Proposition("a")
    val b = Proposition("b")
    val d = Proposition("d")
    val c = Var(Set[Expression](),And(a,b))
    assertThrows [InvalidDerivationException]{
    val deriv=Proj2(c,And(a,d))
    deriv.print
    }
}
"Inj1" should "execute" in {
    val a = Proposition("a")
    val b = Proposition("b")
    val c = Var(Set[Expression](),a)
    val deriv=Inj1(c,b)
    deriv.print
}
"Inj2" should "execute" in {
    val a = Proposition("a")
    val b = Proposition("b")
    val c = Var(Set[Expression](),a)
    val deriv=Inj2(c,b)
    deriv.print
}
  // "HelloWorld" should "execute" in {
  //   val a = Proposition("a")
  //   val b = Proposition("b")
  //   val c = Proposition("c")
  //   val d = Proposition("d")
  //   val impl = Implies(a, b)
  //   val ctx : Set[Expression] = Set(a)

  //   val deriv = Var(ctx, impl)
  //   val proof = App(deriv, impl)
  //   val P = Principal ("P", "P_Keys")
  //   val Q = Principal ("Q", "Q_keys")
  //   val P_says = Says(P,c,P.sign(c))
  //   val Q_says= Says(Q,d,Q.sign(d))

  //   println("deriv: " + deriv.toString())
  //   println("proof: " + proof.toString())
  // }
}

// class LogicalAttestationTrial extends FlatSpec {
    
// }