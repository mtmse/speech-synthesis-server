#!/bin/bash
# This script is executed before file copy actions when this specific version of rpm is installed or upgraded from another rpm (upgrade)
# Type of action depends on first argument "1" => Initial installation, "2" => Upgrade from an existing installation

echo "Running preInstall"

if [ "$1" = "1" ]; then # This is a new installation from scratch
# Perform tasks to prepare for the initial installation
	# If this machine has been uninstalled recently there may be a machine specific config file to reuse
	if [ -f /var/tmp/somefile ]; then # There was an old saved version of the machine run configuration. Let us reuse it
	fi

elif [ "$1" = "2" ]; then # This is part of an upgrade
# Perform whatever maintenance must occur before the upgrade begins
	# Check if the service is running. Make sure error message (unrecognized service if not installed) is passed to grep as well
	service speech-synthesis-server status 2>&1 |grep -q 'pid' -
	if [ "$?" = "0" ]; then # Service is running
    	echo "Stopping the service before upgrade. It will be restarted again after upgrade."
		# Stop the service before upgrading
		service speech-synthesis-server stop
		# Make really, really sure we have stopped the processes
		    PIDS=`ps aux | grep speech-synthesis-server | grep -v grep | awk '{ print $2 }'`
		    if [ "$PIDS" != "" ] ; then 
		    	echo "Killing remaining (stalling) speech-synthesis-server processes before upgrade. PIDs:" $PIDS 
		        kill -9 $PIDS 
		    fi
		# Remember that we stopped the service (used by postIntall script to start again afterwards)
		touch /var/tmp/speech-synthesis-server-was-stopped-before-upgrade
	else
    	echo "Service was not running, and will therefore not be started after upgrade"
	fi
else # Unrecognized action type
    echo "Invalid/unsupported action type: $1"
    exit 1
fi

echo "Done running preInstall"
