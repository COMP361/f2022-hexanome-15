#!/usr/bin/bash

# expects: location of LS REST endpoint, access token, session id, player
# Adds player to unlaunched session

if [[ $# -ne 4 ]]
then
	echo "Error: expected 4 arguments, received $# instead"
	exit 1
else
	rest=$1
	auth=$2
	id=$3
	player=$4
	"curl -X PUT $rest/api/sessions/$id/players/$player?access_token=$auth"
fi
