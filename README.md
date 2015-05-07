# Speech synthesis server

A server that could act as a facade for speech synthesis

## Build

```
./gradlew clean build shadowJar
```

## Run

```
java -jar build/libs/speech-synthesis-server-1.0.0-all.jar server configuration.yaml 
```

## References

The server is built using [Dropwizard](http://www.dropwizard.io/).
