package kavach.attestation
import kavach.attestation.definitions._
import java.security.{PrivateKey,PublicKey,Signature,KeyFactory,KeyPairGenerator,KeyPair}
import java.security.spec.{X509EncodedKeySpec, PKCS8EncodedKeySpec}
import scala.util.control.Breaks._
import scala.io.Source
import java.io._
import sys.process._

final case class InvalidDerivationException(
    private val message: String = "Invalid derivation",
    private val cause: Throwable = None.orNull)
  extends java.lang.RuntimeException(message, cause)

final case class InvalidVerificationException(
    private val message: String = "Signature verification failed",
    private val cause: Throwable = None.orNull)
  extends java.lang.RuntimeException(message, cause)

abstract class Expression

case object True extends Expression
// Do we need Sting in the parameters of the Principal. we need it because of creating String for verification
//make it as per the certificate authority
case class Principal (name: String){
    // generating certificates
    ("./Certification/create_cert.sh "+name)!
    def sign(s:Expression, filename:String)= {
        ("./Certification/sign.sh "+ s.toString + " " +filename+ " "+ this.name)!
    }
}

// case class Principal (v : String, filename: String){
//     RSA.genSigningKeyPair(filename)
//     def public_key: PublicKey ={
//         val publicPath = "./key/"+ this.filename+"_public.der"
//         val ois = new ObjectInputStream(new FileInputStream(publicPath)) 
//         val publicKey = ois.readObject.asInstanceOf[PublicKey]
//         ois.close()
//         return publicKey
//     }
//     def sign(s:Expression) : Array[Byte] = {
//         //get the private key
//         val privatePath = "./key/"+ this.filename+"_private.der"
//         val ois = new ObjectInputStream(new FileInputStream(privatePath)) 
//         val privateKey = ois.readObject.asInstanceOf[PrivateKey]
//         ois.close()
//         val encryption = RSA.sign(privateKey, s.toString)
//         // println("prining the encryption of "+s.toString+": \n\n")
//         // print(encryption.toString)
//         return encryption
//         // sign the message
//     }
// }
case class Proposition (v: String) extends Expression

abstract class Says extends Expression
case class Says_P (P: Principal, s: Expression, filename:String) extends Says {
    // There is a problem that this command cannot handle blank spaces
    val v = ("./Certification/verify.sh "+ s.toString+" "+filename+" "+ P.name)!;
    if(v!=0) {
        throw InvalidVerificationException()
    }
}
case class Says_D(P: Principal, s:Expression, d:Derivation) extends Says {
    if(d.st!=s) throw InvalidDerivationException()
}
// case class Says (P: Principal, s: Expression, enc:Array[Byte]) extends Expression {
//     if(!RSA.verify(P.public_key,enc,s)) throw InvalidVerificationException()
//     // def this(P:Principal,s: Expression, d:Derivation)={
//     //     flag = false
//     //     Says(P,d.st,_)

//     //  }
    
// }

case class And(a: Expression, b: Expression) extends Expression 
case class Or(a: Expression, b: Expression) extends Expression 
case class Implies(a: Expression, b: Expression) extends Expression
case class ForAll(x: Proposition, s: Expression) extends Expression

//each case class of this class represent a one step Derivation 

abstract class Derivation { 
    val ctx: Set[Expression]
    val st: Expression
    def print= {
        println("\nContext =  "+ this.ctx+"\nStatement =     "+ this.st)
    }
}
case class Var (ctx1: Set[Expression], s:Expression) extends Derivation {
    override val ctx = ctx1 + s
    override val st = s
}

case class Unit_true (ctx: Set[Expression]) extends Derivation{
    override val st = True
    def this (){
        this(Set[Expression]())
    }
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

// Here use of s is redundant, and it would be better if this could be removed
// case class Proj11(d:Derivation) extends Derivation{
//     // val c : d.st
//     d.st match {
//         case And(a,b) => val d.st = a
//         case _ => throw InvalidDerivationException() 
//     }
//     override val ctx = d.ctx
//     override val  st =d.st
// }
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

//This is problematic as then anystatement could be made true. There should be some other mechanism to generate this statement
case class UnitM(d: Derivation, P: Principal) extends Derivation{
        override val ctx= d.ctx
        override val st=Says_D(P, d.st, d)
}


case class Case (d1: Derivation, d2: Derivation, d3: Derivation) extends Derivation{
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



case class BindM(d1: Derivation, d2: Derivation) extends Derivation{
    d1.st match{
        case Says_P(p1,s1,_) => {d2.st match {
                                case Says_P(p2,s2,_) => {assert(p1==p2);
                                                    assert(d1.ctx == d2.ctx - s1)
                                                   }
                                case Says_D(p2,s2,_) => {assert(p1==p2);
                                                    assert(d1.ctx == d2.ctx - s1)
                                                   }                   
                                case _ => throw InvalidDerivationException()
                                       }
                           }
        case Says_D(p1,s1,_) => {d2.st match {
                                case Says_P(p2,s2,_) => {assert(p1==p2);
                                                    assert(d1.ctx == d2.ctx - s1)
                                                   }
                                case Says_D(p2,s2,_) => {assert(p1==p2);
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

// case class BindM(d1: Derivation, d2: Derivation) extends Derivation{
//     d1.st match{
//         case Says(p1,s1,_) => {d2.st match {
//                                 case Says(p2,s2,_) => {assert(p1==p2);
//                                                     assert(d1.ctx == d2.ctx - s1)
//                                                    }
//                                 case _ => throw InvalidDerivationException()
//                                        }
//                            }
//         case _ => throw InvalidDerivationException() 
//     }

//     override val ctx = d1.ctx
//     override val st= d2.st
// }

case class TLam(d: Derivation, x: Proposition) extends Derivation{
    if(isFree(d.ctx,x)) throw InvalidDerivationException()
    override val ctx = d.ctx
    override val st = ForAll(x, d.st)
}

case class Tapp(d: TLam, t: Proposition) extends Derivation{
    override val ctx= d.ctx
    override val st = substitute (d.st, t, d.x)
}

// object AST {
// // this does the reading part

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

