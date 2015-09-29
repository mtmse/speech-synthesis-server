# Speech synthesis server

A server that acts as a facade for speech synthesis. The only supported synthesis engine
is [Filibuster](http://confluence.mtm.se/display/ITDOCS/IT+Komponent+-+Filibuster).

This is guide book to the implementation.

## Table of content
* [Context](#context)
* [Functional Overview](#functional-overview)
* [Quality Attributes](#quality-attributes)
* [Constraints](#constraints)
* [Principles](#principles)
* [Software Architecture](#software-architecture)
* [External Interfaces](#external-interfaces)
* [Code](#code)
* [Data](#data)
* [Infrastructure Architecture](#infrastructure-architecture)
* [Deployment](#deployment)
* [Operation and Support](#operation-and-support)
* [Decision Log](#decision-log)
* [Old Documentation](#old-documentation)
 
## Context

MTM need to be able to parallelize speech synthesisation and synthesise more than one book at a time.

The solution is to implement a web application that hides concrete speech synthesisers. The web application accepts
request from more than one client and is therefore able to parallelize the synthesization by dispatching jobs to
many synthesise instances.

The primary client is [PipeOnline](http://confluence.mtm.se/display/ITDOCS/IT+Komponent+-+PipeOnline) that
the production team is using when creating books.

## Functional Overview

A sentence that should be synthesised is sent to the system and a synthesised sound is returned.

The synthesisers, filibusters, have a limited life time and will be recreated after this life span. The synthesizers
available are listed on the front page of the web application.

It is possible to verify that the system is able to synthesise any sentence through the user interface. Click on
'Test Synthesize' in the menu bar. You will be able to enter any text that should be synthesised in the text area. The
main purpose is for testing the system and verifying that th ecomponents are possible to connect and sound generated.

The logs generated from the system is also available from the web application. They are offered as a convenience for
the maintainers of the system. They are also available on the executing host. Reading them on the host requires
ssh access.

The configuration of the system is possible to review using the 'Configuration' in the menu bar. The configuration
is the same configuration as the system is started with.

The release history is available from the 'About' menu.

## Quality Attributes

There are no apparent quality attributes that we must honor. Synthesising books takes time and the bottle neck
isn't in the dispatching server.

## Constraints

Speech synthesisers consume a lot of memory. It is possible to configure the maximum number of synthesisers
and the minimum memory that should be available. We have started with 6 filibusters and they requires 4 Gb of memory.
These number should be subject for revision when we know more about how the system behaves.

MTM is a mainly Java shop and the implementation is done using Java.

## Principles

The implementation is done with a focus on the interaction between the components. Anything related to each other lives
in the same package.

The main entry point to each package is a [resource](https://dropwizard.github.io/dropwizard/manual/core.html#resources)
that is reachable from using a web browser or a REST client.

The resources are wired through the [Main](https://github.com/mtmse/speech-synthesis-server/blob/master/server/src/main/java/se/mtm/speech/synthesis/Main.java) class.

## Software Architecture



## External Interfaces

The external interface to the system, except the web based user interface, is one REST resource.
It is hidden behind `/synthesize` and it takes the sentence that should be synthesised as a query parameter.

The returned value is a JSON document with a byte array that should be interpreted as as a wav file.

There is a [Java client](https://github.com/mtmse/speech-synthesis-server/tree/master/client) implemented that
removes the need for low level knowledge about the communication with the server.

## Code

### Server

The server application is built using [Dropwizard](http://www.dropwizard.io/).

The web interface is built using [Mustache](http://mustache.github.io/mustache.5.html) templates.

The styling is done using [Boostrap](http://getbootstrap.com/).

Configuration is done with a [YAML](http://yaml.org/) file and follows the standard
[Dropwizard](http://www.dropwizard.io/manual/core.html#configuration) format.

### Client

The client is implemented using [Jersey](https://jersey.java.net/) and
[Jackson](http://wiki.fasterxml.com/JacksonHome).

### Building

The system is built using [Gradle](https://gradle.org) [wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html).

`./gradlew clean shadowJar`

Creating an RPM for testing is done using

`./gradlew clean buildRpm`

### Local execution

Running the server local

`java -jar server\build\libs\server-1.0.0-all.jar server configuration.yaml`

The version number above is not correct, check your build directory for the correct one.

## Data

There is no specific data associated with this system. It is a facade to other systems and doesn't keep
a state of its own.

## Infrastructure Architecture



## Deployment

It is deployed on [RHEL](http://www.redhat.com/en/technologies/linux-platforms/enterprise-linux) as
an [RPM](http://www.rpm.org/). The latest version is available at MTMs local [YUM](http://yum.baseurl.org/) repository.

Each environment at MTM knows which repository to use. It is therefore easy to use [Puppet](https://puppetlabs.com/)
for installation.

Deploying a new version is done by
* Promote an RPM package to the repo a specific environment fetches its packages from
* Trigger or wait for Puppet to execute. The current, old version, will be updated with the latest found in
the repository
* Repeat the steps above when promoting to test or production

## Operation and Support

A user- and admin-interface is available at

| Environment     | User interface      | Admin interface  |
| --------------- |-------------| ------------------------ |
| Test            | [http://pipeutv1.mtm.se:9090](http://pipeutv1.mtm.se:9090) | [http://pipeutv1.mtm.se:9091](http://pipeutv1.mtm.se:9091) |
| Acceptance test | [http://pipetest1.mtm.se:9090](http://pipetest1.mtm.se:9090) | [http://pipetest1.mtm.se:9091](http://pipetest1.mtm.se:9091) |
| Production      | [http://pipeonline.mtm.se:9090](http://pipeonline.mtm.se:9090) | [http://pipeonline.mtm.se:9091](http://pipeonline.mtm.se:9091) |


Checking that the server is running is done as root with the command

`service speech-synthesis-server status`

Starting is done

`service speech-synthesis-server start`

Restarting is

`service speech-synthesis-server restart`

The logs are available at the location that the configuration `logHome` is set to. This is
probably `/var/log/mtm/speech-synthesis-server`

The configuration is stored in `/etc/opt/speech-synthesis-server/configuration.yaml`

There is an admin interface available. The port is defined in the configuration. It is probably `9090`

## Decision Log

* The decision to use a REST api is based on the need for loose coupling. Other system should be able to connect
without too much knowledge about the inner workings of the implementation. MTM has a record of compile time
dependencies between systems and this has turned out to be unnecessary complicated.
