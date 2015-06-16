#!/bin/bash
#
# Script for running speech-synthesis-server as a service
#echo "Running speech-synthesis-server"

INSTALL_ROOT=/opt/speech-synthesis-server
CONFIG_ROOT=/etc/opt/speech-synthesis-server
RUN_AS_USER=tpbadmin

daemon_pid() {
    echo `ps aux | grep speech-synthesis-server | grep -v grep | awk '{ print $2 }'`
}


case "$1" in
    start)
        echo "Starting speech-synthesis-server..."
        su -c "java -jar $INSTALL_ROOT/server-all.jar server $CONFIG_ROOT/configuration.yaml" $RUN_AS_USER
        done
        ;;
    
    stop)
        echo "Stopping speech-synthesis-server..."
        
        # Check if server started
    	if [ -z "$(daemon_pid)" ] ; then 
        	echo "Process speech-synthesis-server not running, no need to stop"
        	exit 0
       	fi
       	
       	# TODO: Somehow kill it a little more gracefully, so running TTS are allowed to complete before termination.
        kill -KILL $PIDS 
        done
        ;;
    
    restart)
        $0 stop
        $0 start
        ;;
    
    status)
        if [ -n "$(daemon_pid)" ]; then
            echo "NDS running with pid $(daemon_pid)"
        else
            echo "NDS not running"
            exit 3
        fi
        ;;
    
    *)
        echo "usage: $0 (start|stop|restart|status)"
esac

exit 0
