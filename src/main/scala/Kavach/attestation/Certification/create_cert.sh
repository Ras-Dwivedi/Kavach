#this one creates certificate for users
#Check whether the Cetification Authority exists or not. Not creating certificate authority here, else different Principal might end up creating different CAs
echo "running create_cert"
if !  test -d Certificate_Authority ; then 
	echo "Certificate Authority do not exist. Please create one" 
	exit 1
fi

#Generating Certificates
user=$1
mkdir -p ./$user
#cd $user
openssl genrsa -out ./$user/$user.key 2048
#to export the public key
openssl rsa -in ./$user/$user.key -outform PEM -pubout -out ./$user/"$user"_public.pem
openssl req -new -key ./$user/$user.key -out $user/$user.csr -subj "/C=IN/ST=UP /L=Kanpur/O="$user"/OU=CSE/CN="$user
openssl x509 -req -in ./$user/$user.csr -CA ./Certificate_Authority/rootCA.pem -CAkey ./Certificate_Authority/rootCA.key -CAcreateserial -out ./$user/$user.crt -days 500 -sha256 -passin pass:1234
mkdir ./Certificate_Authority/$user

#copying the certificate to certification authority for future needs
cp ./$user/$user.crt ./Certificate_Authority/$user
# echo "Displaying the certificates "
# echo $pwd
# openssl x509 -text -noout -in ./$user/$user.crt
if [ "$?" = "0" ]; then
	echo "Certificates created successfuly for user " $user
else
	echo "Error in certificates creation"
fi

