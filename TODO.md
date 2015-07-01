# Todo

## Status page
Create a status page that shows
  * Status
  * How many Filibusters are currently being used
  * How old is the oldest
  * How old is the youngest


## Should the server be able to suggest a timeout to a client?
The server might know how long time it is reasonable before the client tries
to send the same request again. Add this suggested resend time in the response.


## Add an optional timeout
The client should be able to increase the timeout for a synthesis job.
If a specific speech unit is large, the client should be allowed to let
the server work on it for a longer period of time than the default for
the server.
The client will have to deal with http timeout etc.


## Priority
Investigate how three different priorities can be implemented.

* Low
* Medium
* High

How should the priorities be consumed? Always the highest priority and then
the others if there is more capacity available?

The priority should be set by the client in the request.

# Done

## Package as an rpm
Package the speech server as an rpm.


## Health check
Create a health check that checks the Filibuster pool, all living
Filibusters and verifies that that they are ok.


## Check resources when creating a new Filibuster
Do some environment check before creating a new Filibuster.
How much memory is available?


## Add Filibuster until the pool maxsize
Add Filibusters until the resources in the host are used
or the max size of the Filibuster pool is reached


## Invalidate all Filibusters
Create a resource that invalidates all Filibusters in the pool and doesn't
add any of the Filibusters being used. Any job that currently is executed
should not be affected.


## Status page
Create a status page that

  * Has a button that does a post to the invalidate resource.
    Return to the status page with a text stating that an invalidation
    was done at a certain point in time
  * Has a link to the release notes
  * Shows the build date, the build date and the version number
