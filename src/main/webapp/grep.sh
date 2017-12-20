#!/bin/bash
localIp=$(/sbin/ifconfig -a|grep inet|grep -v 127.0.0|grep -v inet6|awk '{print $2}'|tr -d "addr:")
if [[ "$1"x = "$localIp"x ]]; then
  cd $2
  cd logs
  echo "start_execute"
  echo " "
  echo " "
  cat $3".log"|$4
  echo " "
  echo " "
  echo " "
  echo " "
else
  /usr/bin/kinit -kt /etc/krb5.keytab
  /usr/local/bin/expect /usr/local/tomcat8/webapps/ConfigJobService/grep.expect $1 $2 $3 "$4" 
fi
