#! /bin/bash

#Get CLI arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

#Check # of arguments
if [ $# -ne 5 ]; then
  echo "Error: Invalid number of arguments."
  exit 1
fi

#Store hardware info
hostname=$(hostname -f)
vmstat_mb=$(vmstat --unit M -t | tail -n 1)

#Parse hardware info
timestamp=$(echo "$vmstat_mb" | awk '{print $18, $19}' | xargs) #current timestamp in `2019-11-26 14:40:19` format
host_id="(SELECT id FROM host_info WHERE hostname='$hostname')";
memory_free=$(echo "$vmstat_mb" | awk '{print $4}' | xargs)
cpu_idle=$(echo "$vmstat_mb" | awk '{print $15}' | xargs)
cpu_kernel=$(echo "$vmstat_mb" | awk '{print $14}' | xargs)
disk_io=$(vmstat -d | tail -n 1 | awk '{print $10}' | xargs)
disk_available=$(df -BM / | tail -n 1 | awk -F' ' '{print +$4}' | xargs)

#SQL insert statement
insert_stmt="INSERT INTO host_usage (
               timestamp, host_id, memory_free, cpu_idle,
               cpu_kernel, disk_io, disk_available
             )
             VALUES
               (
                 '$timestamp', $host_id, $memory_free,
                 $cpu_idle, $cpu_kernel, $disk_io,
                 $disk_available
               );"

#Execute SQL statement
export PGPASSWORD=$psql_password
psql -h "$psql_host" -p "$psql_port" -d "$db_name" -U "$psql_user" -c "$insert_stmt"
exit $?