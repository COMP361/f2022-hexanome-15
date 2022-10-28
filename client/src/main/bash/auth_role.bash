#!/usr/bin/bash

# expects: location of LS, access token

if [[ $# -ne 2 ]]
then
	echo "Expected 2 arguments, received $# instead"
	exit 1
else
	loc=$1
	access_token=$2
	curl -X GET "${loc}/oauth/role?access_token=$access_token"
fi
