#!/usr/bin/bash

# expects location of LS REST endpoint, name of gameservice as it was registered, access token (admin or matching service role) as argument
# sends a quit request to all affected running sessions

if [[ $# -ne 3 ]]
then
	echo "Error: expected 1 argument, received $# instead"
	exit 1
else
	loc=$1
	gsname=$2
	auth=$3
	"curl -X DELETE $loc/api/gameservices/$gsname?access_token=$auth"
	echo -e "\n"
fi
