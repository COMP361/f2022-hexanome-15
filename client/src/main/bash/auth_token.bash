#!/usr/bin/bash

# takes as parameters username, password; NOTE: DEFAULT ADMIN USED TO CREATE CUSTOM USERS / ADMINS: maex : abc123_ABC123

if [[ $# -ne 2 ]]
then
	echo "Error: expected 2 arguments, received $#"
	exit 1
else
	username=$1
	password=$2
	curl -X POST --user bgp-client-name:bgp-client-pw "http://127.0.0.1:4242/oauth/token?grant_type=password&username=$username&password=$password"
fi
