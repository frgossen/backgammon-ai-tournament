# Backgammon AI Tournament 

This is a framework to watch and test Backgammon AIs. 

## Quick build and run 

Build the Maven project and run the JAR file as follows

```sh
mvn package
java -jar ./target/backgammon-tournament-*-SNAPSHOT.jar
```

Select two or more of the predefined AIs and alter other parameters as desired. Click "Start" and let them play a Tournament. 

## Add your own AI

Implement the interface ```com.frederikgossen.backgammon.ai.AI``` or extend the abstract classes ```com.frederikgossen.backgammon.ai.BoardScoringAI``` or ```com.frederikgossen.backgammon.ai.MoveScoringAI``` and define the behaviour of your AI programmatically. 

### Load your AI at runtime 

You can compile your AI with all classes it depends on and export it as a JAR file. When starting the AI tournament you can load the JAR file. All AIs that conform to the AI interface will be added to the list of AIs and you can start the tournament. 

### Add your AI as a Default AI 

Add your AI class to the array of default AIs in ```com.frederikgossen.backgammon.ai.MenuScreenAILoader```. Your AI will appear in the list of selectable AIs when you start the AI tournament. 
