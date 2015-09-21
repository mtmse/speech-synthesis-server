#!/bin/bash
#
# speech-synthesis-server Control Script
#
# chkconfig: 2345 80 20
#

INSTALL_ROOT=/opt/speech-synthesis-server
CONFIG_ROOT=/etc/opt/speech-synthesis-server
RUN_AS_USER=tpbadmin

daemon_pid() {
    echo `ps aux | grep speech-synthesis-server | grep jar | grep -v su | awk '{ print $2 }'`
}

case "$1" in
    start)
        echo "Starting speech-synthesis-server..."

        LANGTECH_ROOT=/opt/langtech
        chmod -R a+rwx $LANGTECH_ROOT # We have had problems with permissions and this is a work around
        chown -R tpbadmin:tpbadmin $LANGTECH_ROOT

        su -c "java -jar $INSTALL_ROOT/lib/server-all.jar server $CONFIG_ROOT/configuration.yaml" $RUN_AS_USER &
        ;;
    
    stop)
        echo "Stopping speech-synthesis-server..."
        
        # Check if server started
    	if [ -z "$(daemon_pid)" ] ; then 
        	echo "Process speech-synthesis-server not running, no need to stop"
        	exit 0
       	fi
       	
        kill $(daemon_pid)
        exit 0
        ;;
    
    restart)
        $0 stop
        $0 start
        ;;
    
    status)
        if [ -n "$(daemon_pid)" ]; then
            echo "speech-synthesis-server running with pid $(daemon_pid)"
        else
            echo "speech-synthesis-server not running"
            exit 3
        fi
        ;;
    
    *)
        echo "usage: $0 (start|stop|restart|status)"
esac

exit 0
