# COMP 361 Project

## About
This is a repository for the popular board game Splendor, including the extension packs. In this repository, you will find resources for playing the game online, including code for a java desktop version, as well as rules for the [base game](splendor-rulebook.pdf) and the [extensions](extensions-rulebook.pdf).

## Getting Started
1. Clone this repository: ```https://github.com/COMP361/f2022-hexanome-15.git```
2. Download the [visual resources](https://drive.google.com/drive/folders/1OM5oT-QvH1qWxKP9ePFTfVtoHmGEgN8n) and put them into a folder called resources at the [root of the client directory](client/)
3. [Install Docker Compose](https://docs.docker.com/get-started/08_using_compose/)
4. One player must be the host and startup the LobbyService as well as the Splendor Server backend. First they must enter their public IP address into the [application properties prod file](server/src/main/resources/application-prod.properties). Then, while within the root directory of this repository enter: ```docker compose up``` or to force rebuild of resources instead: ```docker compose up --build```. Once this is complete, all players are ready to play
5. Navigate within the client folder and enter: ```mvn clean javafx:run```

## Authors

 * Svyatoslav Sklokin - [https://github.com/SlavaSklokin]
 * Zachary Hayden - [https://github.com/ZacharyHayden]
 * Lawrence Berardelli - [https://github.com/lberardelli]
 * Ojas Krishnan - [https://github.com/SoraXO]
 * Sofia Fiore - [https://github.com/universe-city]
 * Jeff Léon  -  [https://github.com/jleon9]


