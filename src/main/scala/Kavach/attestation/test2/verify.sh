#!/bin/bash
# Verify a file with a public key using OpenSSL
# Decode the signature from Base64 format
#
# Usage: verify <file> <signature> <public_key>
#
# NOTE: to generate a public/private key use the following commands:
#
# openssl genrsa -aes128 -passout pass:<passphrase> -out private.pem 2048
# openssl rsa -in private.pem -passin pass:<passphrase> -pubout -out public.pem
#
# where <passphrase> is the passphrase to be used.

#filename=$1
msg=$1
signature=$2
username=$3

if [[ $# -lt 3 ]] ; then
  echo "Usage: verify <file> <signature_file> <username>"
  exit 1
fi

openssl base64 -d -in ./$username/$signature.sha256 -out /tmp/temp_file.sha256
echo -n $msg|openssl dgst -sha256 -verify ./$username/"$username"_public.pem -signature /tmp/temp_file.sha256
rm /tmp/temp_file.sha256
