#!/usr/bin/bash

# expects: location of LS REST endpoint, access token, creater, gameName, savegame (optional)
# TODO: implement p2p mode

if [[ $# -eq 5 ]]
then
	loc=$1
	auth=$2
	creator=$3
	game=$4
	savegame=$5
	curl -X POST --header 'Content-Type: application/json' --data "{game:$game, creator:$creator, savegame:$savegame} $loc/api/sessions?access_token=$auth"
	echo -e "\n"
elif [[ $# -eq 4 ]]
	loc=$1
        auth=$2
        creator=$3
        game=$4
	curl -X POST --header 'Content-Type: application/json' --data "{game:$game, creator:$creator, savegame:} $loc/api/sessions?access_token=$auth"
	echo -e "\n"
else
	echo "Error: expected 4 or 5 arguments depending on optional savegame, received $# instead"
	exit 1
fi
