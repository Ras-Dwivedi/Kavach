#!/bin/bash
# Sign a file with a private key using OpenSSL
# Encode the signature in Base64 format
#
# Usage: sign <file> <private_key>
#
# NOTE: to generate a public/private key use the following commands:
#
# openssl genrsa -aes128 -passout pass:<passphrase> -out private.pem 2048
# openssl rsa -in private.pem -passin pass:<passphrase> -pubout -out public.pem
#
# where <passphrase> is the passphrase to be used.

#filename=$1
msg=$1
filename=$2
username=$3

if [[ $# -lt 3 ]] ; then
  echo "Usage: sign <file> <filename> <username>"
  exit 1
fi
cd ./Certification
echo -n $msg|openssl dgst -sha256 -sign ./$username/$username.key -out /tmp/temp_file.sha256
#openssl dgst -sha256 -sign $username -out /tmp/$filename.sha256 $filename
openssl base64 -in /tmp/temp_file.sha256 -out ./$username/$filename.sha256
rm /tmp/temp_file.sha256
