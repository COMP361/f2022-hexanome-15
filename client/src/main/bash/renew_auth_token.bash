#!/usr/bin/bash

# expects: location of lobby service, refresh token

if [[ $# -ne 2 ]] # usage checks: number of arguments
then
	echo "Error: expected 2 arguments, received $#"
	exit 1
else
	loc=$1
	refresh=$2
	curl -X POST --user bgp-client-name:bgp-client-pw "${loc}/oauth/token?grant_type=refresh_token&refresh_token=$refresh"
fi
