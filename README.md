# Fantasy Brawl - Multiplayer

## Project for Paradigmi di Programmazione e Sviluppo

This project is a mostly original concept for a multiplayer, turn-based, fighting game set in a fantasy world with warriors, thieves, wizards and healers fighting in teams against each other for glory and fame.



## Getting Started

<Strong>Recomended Settings</Strong>

It's recomended to play this game on a Windows operating system, in order to fully experience it's graphic components.
Mac and Linux systems will equally work, but the graphical experience could be reduced.

<Strong>Requirements</Strong>

In order to play the game you need to have the JRE installed (at least 1.8 version), and have access to an internet connection.

After the requirements are met, to play simply run the client .jar file provided with the latest release, as the needed services are uploaded on Heroku.

```
	java -jar pps-17-fb-1.0-client.jar
```

If the services aren't working, a manual launch of all three of them is required.

```
	java -jar pps-17-fb-1.0-login-guest.jar
	java -jar pps-17-fb-1.0-matchmaking.jar
	java -jar pps-18-fb-1.0-turn-ordering.jar
```

## How to play

In order to play you must first login, the system will provide each player an ephimeral guest id.\
After that, before joining the matchmaking queue, a team of four characters must be chosen.\
Each character has his unique set of special moves, if in doubt on who to pick for your team, in the character selection screen it's possible to open a window displaying all the moves present in the game along with their effect.
After the team is built it will be possible to join a random matchmaking queue. \
During the battle each character will have their turn depending on it's speed, in order to execute a character's move it's necessary to select it from the move list at the bottom of the screen, and by selecting the right amount of targets by clicking on their image.
The last remaining team wins.

## Releases
It is possible to download the source code and all release executable jars at the page : https://github.com/nicolaAtti/pps-17-fb/releases

## Documentation

The latest documentation is available at the page :  https://nicolaAtti.github.io/pps-17-fb/
