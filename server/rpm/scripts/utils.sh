#!/bin/bash

echo "Running utils"

ACTION=$1

UNINSTALL=0

if [ $ACTION -eq $UNINSTALL ]; then
    service speech-synthesis-server stop
fi
