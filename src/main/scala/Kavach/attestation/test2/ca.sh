# cretaing private key for Certificate authority 
mkdir -p ./Certificate_Authority
openssl genrsa -out ./Certificate_Authority/rootCA.key -aes256 -passout pass:1234 2048
echo "Default password is 1234"
openssl req -x509 -new -nodes -key ./Certificate_Authority/rootCA.key -sha256 -days 1024 -out ./Certificate_Authority/rootCA.pem -subj "/C=IN/ST=UP/L=Kanpur/O=IITK/OU=Certification Authority/CN=CA" 
