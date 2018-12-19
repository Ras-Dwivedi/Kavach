# cretaing private key for Certificate authority
openssl genrsa -out key-filename.pem -aes256 -passout pass:1234 2048
openssl req -x509 -new -nodes -key rootCA.key -sha256 -days 1024 -out rootCA.pem
