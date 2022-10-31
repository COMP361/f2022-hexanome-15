#!/usr/bin/bash

# expects: location of running LS, username of the user, access token

if [[ $# -ne 3 ]]
then
	echo "Expected 3 arguments, received $# instead."
	exit 1
else
	loc=$1
	username=$2
	auth=$3
	curl -X GET "${loc}/api/users/$username?access_token=$auth"
fi
