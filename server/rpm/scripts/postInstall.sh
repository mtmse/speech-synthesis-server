#!/bin/bash
# This script is executed after file copy actions when this specific version of rpm is installed or upgraded from 
# another rpm (upgrade)
# Type of action depends on first argument "1" => Initial installation, "2" => Upgrade from an existing installation

echo "Running postInstall"

CONFIG_ROOT=/etc/opt/speech-synthesis-server
INSTALL_ROOT=/opt/speech-synthesis-server
LOGDIR=/var/log/mtm/speech-synthesis-server
RUN_AS_USER=tpbadmin

if [ ! -f "$CONFIG_ROOT/configuration.yaml" ]; then
    echo "Wrote configuration.yaml from RPM"
    mkdir -p $CONFIG_ROOT
	cp $INSTALL_ROOT/conf/configuration-prod.yaml $CONFIG_ROOT/configuration.yaml
fi

rm -f $INSTALL_ROOT/lib/server-all.jar
ln -s $INSTALL_ROOT/lib/server-*-all.jar $INSTALL_ROOT/lib/server-all.jar

mkdir -p $LOGDIR
chown -R $RUN_AS_USER:$RUN_AS_USER $LOGDIR

#
# if [ "$1" = "1" ]; then # This is a new installation from scratch
# # Perform tasks to prepare for the initial installation
#
#
# 	# Setup the staff account
# 	# TODO Should this really be in the nds package? This may be common to more than one package?
# 	#	groupadd staff
# 	#	usermod -aG staff tpbadmin
#
# 	echo "Damn This is a new installation :)"
# elif [ "$1" = "2" ]; then # This is part of an upgrade
# # Perform whatever maintenance must occur after the upgrade file copy has finished
#
# 	# Was the service stopped before the upgrade?
# 	if [ -f /var/tmp/speech-synthesis-server-was-stopped-before-upgrade ]; then # Yes it was stopped
#     	echo "Restarting the service after upgrade"
# 		# Start the service again
# 		service speech-synthesis-server start
# 		# Check if we successfully started the service
# 		if [ $? != "0" ]; then
# 			echo "Error: Problems restarting the speech-synthesis-server service!!! Aborting rpm install."
# 			exit 1
# 		fi
# 		# Remove the start indicator
# 		rm -f /var/tmp/speech-synthesis-server-was-stopped-before-upgrade
# 	fi
#
# else # Unrecognized action type
#     echo "Invalid/unsupported action type: $1"
#     exit 1
# fi

echo "Done running postInstall"
