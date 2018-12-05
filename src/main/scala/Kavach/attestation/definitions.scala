package kavach.attestation

object definitions{
	def isFree(s: Expression, x: Proposition): Boolean = {
    	val flag : Boolean = s match {
	        case Says(p,s1,enc)      => isFree(s1,x)
	        case And(s1,s2)      => isFree(s1,x) && isFree(s2,x)  
	        case Or(s1,s2)       =>  isFree(s1,x) && isFree(s2,x)
	        case Implies(s1,s2)  => isFree(s1,x) && isFree(s2,x)   
	        case ForAll(y,s1)    => if (y== x) false else isFree(s1,x) // there is error in this definition. What about the cases where there is forall x, but no x under that, and in that case it should be free to substitute
	        case Proposition(s1) => true
	        case _ => true    
    	}

    	return flag
	}

	def isFree(ctx: Set[Expression], x:Proposition): Boolean = {
	    var flag: Boolean =true
	    for (s <- ctx) {
	        flag = flag && isFree(s,x)
	    }
	    return flag
	}
// This version of overloaded function return whethere t is free for x in s. but here t is Proposition
	def isFree(s:Expression, t: Proposition, x:Proposition):Boolean  = {
	    val flag:Boolean = s match {
	        case Says(p,s1,enc)      => isFree(s1,t,x)
	        case And(s1,s2)      => isFree(s1,t,x) && isFree(s2,t,x)  
	        case Or(s1,s2)       => isFree(s1,t,x) && isFree(s2,t,x)
	        case Implies(s1,s2)  => isFree(s1,t,x) && isFree(s2,t,x)  
	        case ForAll(y,s1)    => if ( y== x) !isThere(s1,t) else isFree(s1,t,x)
	        case Proposition(s1) => true
	        case _ => true    
	    }
	    return flag
	}

	def isFree(s:Expression, t:Expression, x: Proposition): Boolean = {
	    var flag = true
	    val variables = variablesIn(t)
	    for(v <- variables) {
	        flag = flag && isFree(s,v,x)
	    } 
	    return flag
	}

	def isThere(s:Expression, x: Proposition):Boolean = {
	    val flag = s match {
	        case Says(p,s1,enc)      => isThere(s1,x)
	        case And(s1,s2)      => isThere(s1,x) ||isThere(s2,x)  
	        case Or(s1,s2)       =>  isThere(s1,x) || isThere(s2,x)
	        case Implies(s1,s2)  => isThere(s1,x) ||isThere(s2,x)   
	        case ForAll(y,s1)    => isThere(s1,x)
	        case Proposition(t)  => if(t==x) true else false  
	        case _ => false 
	    }
	    return flag
	}

	def not_Free (ctx: Set[Expression]): Set[Proposition]= {
	    var v = variablesIn(ctx)
	    var notFree = Set[Proposition]()
	    for (x <- v) {
	        var flag = isFree(ctx,x)
	        if(!flag) (notFree = notFree + x)
	    }
	    
	    return notFree

	}

// // the two overloaded function finds out the variables in a context, so that one can generate the list of not free variabls in the context
	def variablesIn (s:Expression): Set[Proposition] ={
	    var v = s match {
	        case Says(p,s1,enc)      => variablesIn(s1)
	        case And(s1,s2)      => variablesIn(s1).++(variablesIn(s2))
	        case Or(s1,s2)       =>  variablesIn(s1).++(variablesIn(s2))
	        case Implies(s1,s2)  => variablesIn(s1).++(variablesIn(s2))
	        case ForAll(y,s1)    => variablesIn(s1) + y
	        case Proposition(s1) => Set[Proposition](Proposition(s1))
	        case _               => Set[Proposition]()  
	        }
	    return v    
	}

	def variablesIn(ctx: Set[Expression]):Set[Proposition] = {
	    var v = Set[Proposition]()
	    for (s<- ctx){
	        v = v.++(variablesIn(s))
	    }
	    return v
	}
// This function retruns the variables not free in context ctx


	def substitute(d: Expression, t: Expression, x: Proposition): Expression={
	    if(!isFree(d,t,x)) InvalidDerivationException()
	    val v: Expression  = d match {
	        case Says(p, s,enc)       => Says(p, substitute (s,t,x),enc) // this would run into problem as signature for all x is not same as signature for t
	        case And(s1,s2)       => And(substitute(s1,t,x), substitute(s2,t,x))
	        case Or(s1,s2)        => Or(substitute(s1,t,x), substitute(s2,t,x))
	        case Implies(s1,s2)   => Implies(substitute(s1,t,x), substitute(s2,t,x)) 
	        case ForAll(y,s)      => if (y==x) d else ForAll(y,substitute(s,t,x)) 
	        case Proposition(y)   => if (d==x) t else d 
	    }
	    return v
	}

}