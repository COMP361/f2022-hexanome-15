#!/usr/bin/bash

# this script should be housed and run and directory where you wish for the LS to be downloaded
# expects: working installation of git, working installation of maven, installation of java JDK

# clone max's repo
git clone https://github.com/kartoffelquadrat/LobbyService.git
# create a docker container from the provided dockerfile in the cloned repo directory
# if at any point you reach a permission error for a docker.sock file, run chmod 666 LOCATION/docker.sock to fix permission
(cd LobbyService && docker build -t "ls-db:Dockerfile" . -f Dockerfile-ls-db)
(cd LobbyService && docker run --platform linux/x86_64 --name=ls-db -p 3453:3306 -d ls-db:Dockerfile)

echo -e "Lobby Service Setup Complete\n"
# note that future startups of LS DB only require: docker start ls-db
