#!/usr/bin/bash

# expects: access token, location of game : string, maxsessionplayers: int, minSessionPlayers: int, name: string, displayName: string, webSupport: string, location of LS REST endpoint

if [[ $# -ne 8 ]]
then
	echo "Error: expected 8 arguments, received $# instead"
	exit 1
else
	auth=$1
	loc=$2
	maxplayers=$3
	minplayers=$4
	name=$5
	displayname=$6
	websupport=$7
	rest=$8
	curl -X PUT --header 'Content-Type: application/json' --data "{'name':$name,'displayName':$displayname,'location':$location,'minSessionPlayers':$minplayers,'maxSessionPlayers':$maxplayers, 'webSupport':$websupport}" "${rest}/api/gameservice/$name?access_token=$auth"
fi
