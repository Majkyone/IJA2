IJA Project: Electrician

Language: java
GUI: javafx 

Team
- Marian Šuľa (xsulama00) 
- Samuel Kundrár (xkundrs00)


Description: Wire Puzzle game
Packages:
- common: observer model, model for node
- game: game model
- gui: views

Levels:
path: myapp/data

Level syntax:
first line(game size): <row> <column>
<nodeType> <row> <column> <connector side>*

<nodeType>:
- L: connector
- B: bulb
- P: power(source)

<row> && <column> : number

<connector side>:
- NORTH
- EAST
- SOUTH
- WEST

startup:

- get inside of myapp
- mvn clean package
- mvn javafx:run

Maven support
- macOS (arm CPU)