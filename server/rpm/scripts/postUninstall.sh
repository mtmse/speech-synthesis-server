#!/bin/bash
# This script is executed when this specific version of rpm is uninstalled or replaced by another rpm (upgrade)
# Type of action depends on first argument "0" => yum erase, "1" => yum upgrade
# !!!!! Observe that the output from this script might not be output to screen since yum deas NOT print it out (due to a bug in yum). Direct RPM execution should work, though.
echo "Running postUninstall"

if [ "$1" = "0" ]; then # This is a complete uninstallation
# Perform tasks for cleanup after ultimate uninstallation (files are already deleted)
	
	# Save the machine specific config file in case we want a reinstallation soon
	rm -f /etc/init.d/speech-synthesis-server
	rm -rf /opt/speech-synthesis-server

elif [ "$1" = "1" ]; then # This rpm is being replaced by another rpm (upgrade)
# Perform whatever maintenance must occur after an upgrade (to a later version than this one) is finished (!). 
# Observe!!! Executed AFTER the new rpm package has executed the install/upgrade scripts!!!!

	# Currently no cleansing actions efter an upgrade from this version to the (unknown) next
    echo "postUninstal This rpm is being replaced by another rpm (upgrade)"
else # Unrecognized action type
    echo "Invalid/unsupported action type: $1"
    exit 1
fi

echo "Done running postUninstall"
