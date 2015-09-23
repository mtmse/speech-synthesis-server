#!/bin/bash

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

mkdir -p $LOGDIR
chown -R $RUN_AS_USER:$RUN_AS_USER $LOGDIR

echo "Done running postInstall"
