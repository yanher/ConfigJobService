#!/bin/bash
localIp=$(/sbin/ifconfig -a|grep inet|grep -v 127.0.0|grep -v inet6|awk '{print $2}'|tr -d "addr:")
if [[ "$1"x = "$localIp"x ]]; then
  cd $2
  cd logs
  echo "start_retrieve"
  echo " "
  if [[ "$4"x = "0"x ]]; then
    tail -n 100 $3".log"
  else
    cat $3".log"|tail -n +`expr $4 + 1`
  fi
  echo " "
  echo " "
  echo " "
  echo " "
  echo last_row_number`cat $3".log"|wc -l`
else 
  /usr/bin/kinit -kt /etc/krb5.keytab
  /usr/local/bin/expect /usr/local/tomcat8/webapps/ConfigJobService/tail.expect $1 $2 $3 $4
fi

