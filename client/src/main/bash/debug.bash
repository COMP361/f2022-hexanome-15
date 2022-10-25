#!/usr/bin/bash

# expected program arguments are a valid address for REST calls to LS
if [[ $# -ne 1 ]]
then
	echo "Error: expected only 1 argument (address for LS REST calls), received: $#"
	exit 1
fi

address=$1

curl -X GET ${address}/api/online
echo -e "\n"
