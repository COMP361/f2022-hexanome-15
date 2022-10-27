#!/usr/bin/bash

# takes as parameters location of ls, username, password; NOTE: DEFAULT ADMIN USED TO CREATE CUSTOM USERS / ADMINS: maex : abc123_ABC123

if [[ $# -ne 3 ]]
then
	echo "Error: expected 3 arguments, received $#"
	exit 1
else
	loc=$1
	username=$2
	password=$3
	curl -X POST --user bgp-client-name:bgp-client-pw "${loc}/oauth/token?grant_type=password&username=$username&password=$password"
fi
