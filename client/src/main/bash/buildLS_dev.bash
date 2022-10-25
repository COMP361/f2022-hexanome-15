#!/usr/bin/bash

# expects: location of working LS folder
# runs the "Default profile for development environments. Starts the LS as a native java application, TLS disabled. Accesses the DB as a dockerized mySQL instance."

if [[ $# -ne 1 ]]
then
	echo "Error: expected 1 argument, received $#"
	exit 1
fi

# assumes that the needed docker container already created, calling startup
# may need to run: sudo chmod 666 /var/run/docker.sock : if there's an error starting up the database
(cd $1 && docker start ls-db)
(cd $1 && mvn clean package spring-boot:run)
