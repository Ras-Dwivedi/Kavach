package kavach.attestation

import java.security.{PrivateKey,PublicKey,Signature,KeyFactory,KeyPairGenerator,KeyPair}
import java.security.spec.{X509EncodedKeySpec, PKCS8EncodedKeySpec}
import java.io._ // for writing objects

object RSA{

val KEY_FOLDER="./key/" //This denotes the folder where Keys are stored
    def write_keys(key: KeyPair, file_private: String, file_public: String):Unit= {
        val oos_private = new ObjectOutputStream(new FileOutputStream(file_private))
        val oos_public = new ObjectOutputStream(new FileOutputStream(file_public))
        oos_private.writeObject(key.getPrivate())
        oos_public.writeObject(key.getPublic())  
        oos_private.close()
        oos_public.close()
    }
// This functions returns the key pair of RSA
def genSigningKeyPair(filename:String):Unit = {

        val kpg = java.security.KeyPairGenerator.getInstance("RSA")
        kpg.initialize(4096)
        val kp:KeyPair = kpg.genKeyPair

        // To get private and publickey
        val privateKey:PrivateKey = kp.getPrivate()
        val publicKey:PublicKey = kp.getPublic()

        // println(s"Generated RSA keys. \nPrivate: \n"+privateKey.toString()+"\nPublic:\n"+publicKey.toString())
        val privatePath = "./key/"+ filename+"_private.der"
        val publicPath = "./key/"+ filename+"_public.der"
        write_keys(kp,privatePath, publicPath)
    }
    
def sign(privateKey:PrivateKey, msg: String) : Array[Byte] = {
        val plainText= msg.getBytes()
        val signer = Signature.getInstance("SHA256withRSA")
        signer.initSign(privateKey)
        signer.update(plainText)
        signer.sign()
    }
def verify(publicKey:PublicKey, signedCipherTest:Array[Byte], s:Expression) : Boolean = {
        val plainText=s.toString.getBytes()
        val signer = Signature.getInstance("SHA256withRSA")
        signer.initVerify(publicKey)
        signer.update(plainText)
        signer.verify(signedCipherTest)
    }    
}

// val GENERATED_RSA_PUBLIC_SUFFIX = "_public.der"
// val GENERATED_RSA_PRIVATE_SUFFIX = "_private.der"

//     def writeKeyPairToFiles(kp:KeyPair, filePrefix:String) = {
//         import java.nio.file.{Files,Paths,FileSystems}

//         val privatePath = Paths.get(KEY_FOLDER + filePrefix + GENERATED_RSA_PRIVATE_SUFFIX) 
//         val publicPath  = Paths.get(KEY_FOLDER + filePrefix + GENERATED_RSA_PUBLIC_SUFFIX) 

//         // Files.write(privatePath, kp.getPrivate.getEncoded) // There is error regarding getEncoded functions. you need to check that
//         // Files.write(publicPath, kp.getPublic.getEncoded)

//         // printKeysToScreen(filePrefix)

//     }

//     // Print the Key->Base64->Ascii string to the screen
//     def printKeysToScreen(filePrefix:String) = {

//         val prv = privateKeyFromFile(filePrefix + GENERATED_RSA_PRIVATE_SUFFIX)
//         val pub = publicKeyFromFile(filePrefix + GENERATED_RSA_PUBLIC_SUFFIX)

//         println("\n\nPrivate\n")
//         println(privateKeyAsString(prv))
//         println("\nPublic:\n")
//         println(publicKeyAsString(pub))

//     }
// def test_buildKey = {

//         val filePrefix = "test1"
//         val keyPair = RsaSign.genSigningKeyPair // this only works if the object name is RsaSign
//         RsaSign.writeKeyPairToFiles(keyPair, filePrefix)
    
//     }

