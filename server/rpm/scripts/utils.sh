#!/bin/bash

echo "Running utils"

ACTION=$1

echo "Action: $ACTION"

UNINSTALL=0
INSTALL_OR_UPGRADE=1

INSTALL_ROOT=/opt/speech-synthesis-server

if [ $ACTION -eq $UNINSTALL ]; then
    service speech-synthesis-server stop
fi

echo "Done running utils"
