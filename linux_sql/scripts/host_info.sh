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
lscpu_out=$(lscpu)
meminfo_out=$(cat /proc/meminfo)

#Parse hardware info
hostname=$(hostname -f)
cpu_number=$(echo "$lscpu_out"  | grep -E "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | grep -E "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out"  | grep -E "^Model name:" | cut -d ':' -f 2 | xargs)
cpu_mhz=$(echo "$lscpu_out"  | grep -E "^CPU MHz:" | awk '{print $3}' | xargs)
l2_cache=$(echo "$lscpu_out"  | grep -E "^L2 cache:" | awk '{print +$3}' | xargs)
total_mem=$(echo "$meminfo_out"  | grep -E "^MemTotal:" | awk '{print $2}' | xargs)
timestamp=$(date -u '+%Y-%m-%d %H:%M:%S' | xargs) #current timestamp in `2019-11-26 14:40:19` format

#SQL insert statement
insert_stmt="INSERT INTO host_info (
               hostname, cpu_number, cpu_architecture,
               cpu_model, cpu_mhz, l2_cache, total_mem,
               timestamp
             )
             VALUES
               (
                 '$hostname', $cpu_number, '$cpu_architecture',
                 '$cpu_model', $cpu_mhz, $l2_cache,
                 $total_mem, '$timestamp'
               );"

#Execute SQL statement
export PGPASSWORD=$psql_password
psql -h "$psql_host" -p "$psql_port" -d "$db_name" -U "$psql_user" -c "$insert_stmt"
exit $?