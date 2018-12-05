import org.scalatest._

import kavach.attestation._
import java.io._
// @Ignore
class LogicalAttestationSpec extends FlatSpec {
  "HelloWorld" should "execute" in {
    val a = Proposition("a")
    val b = Proposition("b")
    val c = Proposition("c")
    val d = Proposition("d")
    val impl = Implies(a, b)
    val ctx : Set[Expression] = Set(a)

    val deriv = Var(ctx, impl)
    val proof = App(deriv, impl)
    val P = Principal ("P", "P_Keys")
    val Q = Principal ("Q", "Q_keys")
    val P_says = Says(P,c,P.sign(c))
    val Q_says= Says(Q,d,Q.sign(d))

    println("deriv: " + deriv.toString())
    println("proof: " + proof.toString())
  }
  // "Says" should "throw Invalid verification error" in {
  //   val P_says=Says("P","the")
  //   assert(false)
  // }
}
