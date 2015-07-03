# Speech synthesis server

A server that acts as a facade for speech synthesis. The current implementation
is a Facade to Filibuster.


[Usage](usage)

[Manual installation](manual_installation)


## Usage (#usage)

A user interface is available at [http://pipeutv1.mtm.se:9090](http://pipeutv1.mtm.se:9090).
An admin interface is available at [http://pipeutv1.mtm.se:9091](http://pipeutv1.mtm.se:9091)

The host above may have to be varied depending on the environment you want to look at.

## Installation

Install on the target is either automated or manual.

### Automated installation

Not yet implemented.

### Manual installation (#manual_installation)

As root:
```
yum install speech-synthesis-server
```

Start it

```
service speech-synthesis-server start
```

#### Trouble shooting

The yum repo must be aware of a repo where the package is available.

The repository is defined in
```
/etc/yum.repos.d/mtm.repo
```

The content should look something like this:
```
[mtm]
name=MTM utv repository
baseurl=http://artifactory.mtm.se:8081/artifactory/mtm-utv/
enabled=1
gpgcheck=0
```

Where this repository is aware of packages available in the utv repo.

New packages are automatically added to the utv repo on each build.

The latest version should be retrieved when 'yum install' is executed. It turns
out that this fails sometimes. It seems necessary to clear the yum cache or
allow some time to pass before trying again.

Clearing the yum caches can be done as root:
```
yum clean all
```

### Manual uninstallation

The speech server can be uninstalled with
```
yum erase speech-synthesis-server
```

This will not remove the configuration in
```
/etc/opt/speech-synthesis-server/configuration.yaml
```

An unstallation will not remove any logs from
```
/var/log/mtm/speech-synthesis-server/
```

## Configuration

The settings can be changed in

```
/etc/opt/speech-synthesis-server/configuration.yaml
```

## Logging

The logs are available in
```
/var/log/mtm/speech-synthesis-server/
```

The server log is available in
```
/var/log/mtm/speech-synthesis-server/speech-synthesis.log
```

The access log is available in
```
/var/log/mtm/speech-synthesis-server/speech-synthesis-access.log
```

Each Filibuster logs to a log file that follows the pattern
```
/var/log/mtm/speech-synthesis-server/filibuster-date-number.log
```

### Log rotation
The server and access log are rotated as defined in the configuration file.

The Filibuster logs are currently not rotated.

## Build

```
./gradlew clean build shadowJar
```

## Run local

```
java -jar server\build\libs\server-1.0.0-all.jar server configuration.yaml
```

## References

The server is built using [Dropwizard](http://www.dropwizard.io/).
