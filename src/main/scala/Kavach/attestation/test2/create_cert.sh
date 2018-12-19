#this one creates certificate for users
mkdir -p ./$1
#cd $1
openssl genrsa -out ./$1/$1.key 2048
#to export the public key
openssl rsa -in ./$1/$1.key -outform PEM -pubout -out ./$1/$1_public.pem
openssl req -new -key ./$1/$1.key -out $1/$1.csr -subj "/C=IN/ST=UP /L=Kanpur/O="$1"/OU=CSE/CN="$1
openssl x509 -req -in ./$1/$1.csr -CA ./Certificate_Authority/rootCA.pem -CAkey ./Certificate_Authority/rootCA.key -CAcreateserial -out ./$1/$1.crt -days 500 -sha256 -passin pass:1234
echo "Displaying the certificates "
echo $pwd
openssl x509 -text -noout -in ./$1/$1.crt

