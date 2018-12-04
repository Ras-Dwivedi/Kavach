import org.scalatest._

import kavach.attestation._
import java.io._

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
    val P = Principal ("P")
    val Q = Principal ("P")
    val P_says = Says(P,c)
    val Q_says= Says(Q,d)

    println("deriv: " + deriv.toString())
    println("proof: " + proof.toString())
  }
}
