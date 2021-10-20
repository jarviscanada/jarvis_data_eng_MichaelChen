# Linux Cluster Monitoring Agent

# Introduction
This project automates the collection of system specifications and the monitoring of resource usage of each node in a Linux server cluster. This is achieved by running info collection scripts automatically every 5 minutes and gathering them in a database. This database can then be queried for average memory usage over 5-minute intervals and detect host node failures.

Server admins can use this tool to gauge load on their servers and allow them to make informed decisions about server upgrades, load allocation, etc. We use PostgreSQL for our database, containerized and managed with Docker. Shell scripts are used for the automated setup of the database and collecting node specs/usage info. Finally, a .sql script compiles and displays useful aggregate information about each node.

# Quick Start
- Start a psql instance using psql_docker.sh:

  `bash ./scripts/psql_docker.sh create [db_username] [db_password]`
  
- Create tables using ddl.sql:
  
  `psql -h [host] -U [db_username] -d host_agent -f sql/ddl.sql`
  
- Insert hardware specs data into the DB using host_info.sh:
  
  `bash ./scripts/host_info.sh [host] [port] host_agent [db_username] [db_password]`
  
- Insert hardware usage data into the DB using host_usage.sh:
  
  `bash scripts/host_usage.sh [host] [port] host_agent [db_username] [db_password]`
  
- Set up crontab:

  `crontab -e`
  
  Then add this to crontab:
  
  `* * * * * bash /path/to/project/host_agent/scripts/host_usage.sh [host] [port] host_agent [db_username] [db_password]`

# Implemenation
The monitoring agent relies on a psql database container managed with Docker to store the data. Bash scripts on each node retrieve usage data, with crontab periodically running the bash script on each node. A collection of SQL queries is provided to compile and retrieve data from the database.

## Architecture
![alt text](assets/architecture.png)

## Scripts
- `psql_docker.sh create [db_username] [db_password]`
  
  Create new psql instance with Docker, with given username and password. Fails if instance already exists.
  
  `psql_docker.sh start`
  
  Starts psql instance on Docker. Fails if no instance exists.
  
  `psql_docker.sh stop`
  
  Stops psql instance on Docker. Fails if no instance exists.
  
- `host_info.sh [host] [port] [db_name] [db_username] [db_password]`
  
  Parses and collects hardware specifications of host machine and inserts to database.
  
- `host_usage.sh [host] [port] [db_name] [db_username] [db_password]`
  
  Parses and collects usage details of host machine and inserts to database.
  
- `crontab`

  Used to manage running host_usage.sh periodically. We add `* * * * * bash /path/to/project/host_agent/scripts/host_usage.sh [host] [port] [db_name] [db_username] [db_password]` to crontab.
  
- `queries.sql`
  
  3 queries are included in this script.
  
    1. Group hosts by hardware info
    2. Fetch average memory usage per 5-minute interval
    3. Detect host failure

## Database Modeling
- `host_info`

Column|Type|Description
---|---|---
id|`SERIAL PRIMARY KEY`|Database ID
hostname|`VARCHAR UNIQUE`|Name of host
cpu_number|`INT`|Number of CPUs
cpu_architecture|`INT`|CPU architecture
cpu_model|`VARCHAR`|Name of CPU model
cpu_mhz|`INT`|Clock speed of CPU in MHz
L2_cache|`INT`|Size of L2 cache in kB
total_mem|`INT`|Total memory in kB
timestamp|`TIMESTAMP`|Time this data was collected

- `host_usage`

Column|Type|Description
---|---|---
timestamp|`TIMESTAMP`|Time this data was collected
host_id|`SERIAL`|ID of host in database
memory_free|`INT`|Amount of free memory in MB
cpu_idle|`INT`|% of time CPU spent idle
cpu_kernel|`INT`|% of time CPU spent used by kernel
disk_io|`INT`|Number of disk I/O
disk_available|`INT`|Amount of available disk space in MB

# Test
Testing was done manually. Bash scripts correctly rejected invalid # of arguments, handled being passed invalid arguments, and properly inserted values into their respective columns in the database. Sample data was loaded into the database and queries.sql was tested to ensure it retrieves the correct data. All tests passed and behaviour is as expected.

# Deployment
This project is deployed with Docker managing the psql database instance and crontab to handle periodic script execution. Source code is managed using git, hosted on GitHub, and a GitFlow model was used to direct feature branches.

# Improvements
- Automatically detect hardware specification changes in a host machine and update database entry.
- Automatically detect host failure and notify admin/advise solutions/resolve failures.
- Provide load balancing suggestions on how to distribute tasks to each node based on usage data.
