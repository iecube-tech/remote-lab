#!/bin/bash

a=$(ps -ef | grep -w frpc_http.ini | grep -v grep | awk '{print $2}')
b=$(ps -ef | grep -w frpc_tcp.ini | grep -v grep | awk '{print $2}')

function network()
{
    local timeout=1

    local target=www.baidu.com

    local ret_code=`curl -I -s --connect-timeout ${timeout} ${target} -w %{http_code} | tail -n1`

    if [ "x$ret_code" = "x200" ]; then
        # yes
        return 1
    else
        # no
        return 0
    fi

    return 0
}


function main()
{
if [ -z $a ];then
    #frp_http is not running
    echo "frp_http is not running"
    network
    if [ $? -eq 0 ];then
        echo “network error”
	return 1
    else
        nohup /usr1/frp/frpc -c /usr1/frp/frpc_http.ini > /dev/null 2>&1 &
    fi	
else
    #frp_http is running
    echo "frp_http is running"
fi

if [ -z $b ];then
    #frp_tcp is not running
    echo "frp_tcp is not running"
    network
    if [ $? -eq 0 ];then
        echo “network error”
	return 1
    else
        nohup /usr1/frp/frpc -c /usr1/frp/frpc_tcp.ini > /dev/null 2>&1 &
    fi

else
    #frp_tcp is running
    echo "frp_tcp is running"
fi
}

main
