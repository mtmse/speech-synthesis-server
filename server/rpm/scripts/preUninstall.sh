# This script is executed when this specific version of rpm is uninstalled or replaced by another rpm (upgrade)
# Type of action depends on first argument "0" => yum erase, "1" => yum upgrade
echo "Running preUninstall"

if [ "$1" = "0" ]; then # This is a complete uninstallation
# Perform tasks to prepare for the ultimate uninstallation (files have not been deleted)
	
    # Graceful shutdown attempt
    service speech-synthesis-server stop

    # Kill the speech-synthesis-server process before removing files (if neccessary)
    PIDS=`ps aux | grep speech-synthesis-server | grep -v grep | awk '{ print $2 }'`
    if [ "$PIDS" != "" ] ; then 
        echo "Killing speech-synthesis-server processes before uninstallation. PIDs are:" $PIDS
        kill -9 $PIDS 
    fi

elif [ "$1" = "1" ]; then # This rpm is being replaced by another rpm (upgrade)
# Perform whatever unistall preparation must occur after an upgrade (to a later version than this one) is finished (!) but before cleansing of files. 
# Observe!!! Executed AFTER the new rpm package has executed the install/upgrade scripts!!!!

    # Currently no cleansing actions efter an upgrade from this version to the (unknown) next (colon required if no shell command is within if clause).
    :
else # Unrecognized action type
    echo "Invalid/unsupported action type: $1"
    exit 1
fi

echo "Done running preUninstall"
