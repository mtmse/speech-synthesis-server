#!/bin/bash

echo "Running preInstall"

ACTION=$1

INSTALL=1
UPGRADE=2

# if [ $ACTION -eq $INSTALL ]; then
# fi

if [ $ACTION -eq $UPGRADE ]; then
    service speech-synthesis-server stop
fi

echo "Done running preInstall"
