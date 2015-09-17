#!/bin/bash

echo "Running postUninstall"

ACTION=$1

UNINSTALL=0
UPGRADE=1

INSTALLATION_ROOT=/opt/speech-synthesis-server
CONFIG_ROOT=/etc/opt/speech-synthesis-server
LOGDIR=/var/log/mtm/speech-synthesis-server

if [ $ACTION -eq $UNINSTALL ]; then
    rm -rf $INSTALLATION_ROOT
    rm -rf $CONFIG_ROOT
    rm -rf $LOGDIR
fi

# if [ $ACTION -eq $INSTALL ]; then
# fi
