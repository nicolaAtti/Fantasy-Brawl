# Fantasy Brawl - Multiplayer

## Project for Paradigmi di Programmazione e Sviluppo

This project is a mostly original concept for a multiplayer, turn-based, fighting game set in a fantasy world with warriors, thieves, wizards and healers fighting in teams against each other for glory and fame.

## Continuous Integration


[![Build Status](https://travis-ci.org/nicolaAtti/pps-17-fb.svg?branch=master)](https://travis-ci.org/nicolaAtti/pps-17-fb)

## Getting Started

<Strong>Requirements</Strong>

In order to play the game you need to have the Java 8 installed and Scala 2.12, and have access to an internet connection.

After the requirements are met, to play simply run the client .jar file provided with the latest release, as the needed services are uploaded on Heroku.

```
	java -jar pps-17-fb-1.0-client.jar
```

If the services aren't online, a manual launch of all three of them is required.

```
	java -jar pps-17-fb-1.0-login-guest.jar
	java -jar pps-17-fb-1.0-matchmaking.jar
	java -jar pps-18-fb-1.0-turn-ordering.jar
```
In order for the system to work the "AMQP as a service" and "MongoDB as a service" plugins must be online.

## How to play

In order to play you must first login: the system will provide each player with an ephimeral guest id.\
After that, in order to join the matchmaking queue, a team of four characters must be chosen.\
Each character has his unique set of special moves. If in doubt on who to pick for your team, in the character selection screen it's possible to open a window displaying all the moves present in the game along with their effect.\
After the team is built it will be possible to join a random matchmaking queue. \
During the battle each character will have their turn depending on it's speed. \
In order to execute a character's move its necessary to select it from the move list at the bottom of the screen, and by selecting the right amount of targets by clicking on their image.\
The last remaining team wins.

## Releases
It is possible to download the source code and all release executable jars at the following page :                   https://github.com/nicolaAtti/pps-17-fb/releases

## Documentation

The latest documentation is available at the following page :  https://nicolaAtti.github.io/pps-17-fb/

## Team members
Nicola Atti (nicola.atti@studio.unibo.it) *Project Owner*              
Marco Canducci (marco.canducci@studio.unibo.it)  
Daniele Schiavi (daniele.schiavi@studio.unibo.it)
