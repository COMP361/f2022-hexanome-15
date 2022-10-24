#!/usr/bin/bash

# expects: location of LS REST endpoint, access token, session id

if [[ $# -ne 3 ]]
then
	echo "Error: expected 3 arguments, received $# instead"
	exit 1
else
	rest=$1
	auth=$2
	id=$3
	"curl -X DELETE $rest/api/sessions/$id?access_token=$auth"
fi
