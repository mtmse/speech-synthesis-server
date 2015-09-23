#!/bin/bash

echo "Running utils"

ACTION=$1

UNINSTALL=0
INSTALL_OR_UPGRADE=1

if [ $ACTION -eq $UNINSTALL ]; then
    service speech-synthesis-server stop
fi

if [ $ACTION -eq $INSTALL_OR_UPGRADE ]; then
    rm -f $INSTALL_ROOT/lib/server-all.jar
    ln -s $INSTALL_ROOT/lib/server-*-all.jar $INSTALL_ROOT/lib/server-all.jar
fi
