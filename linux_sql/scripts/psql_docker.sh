#! /bin/sh

#Set up arguments
cmd=$1
db_username=$2
db_password=$3

#Start Docker in not already running
sudo systemctl status docker || systemctl start docker

#Check if container exists
docker container inspect jrvs-psql
container_status=$?

case $cmd in
  create)

  #If container exists
  if [ $container_status -eq 0 ]; then
		echo 'Container already exists'
		exit 1
	fi

  #If invalid number of CLI arguments
  if [ $# -ne 3 ]; then
    echo 'Create requires username and password'
    exit 1
  fi

  #Create volume and container
	docker volume create pgdata
	docker run --name jrvs-psql -e POSTGRES_USER="$db_username" -e POSTGRES_PASSWORD="$db_password" -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:9.6-alpine
	exit $?
	;;

  start|stop)
  #If container doesn't exist
  if [ $container_status -ne 0 ]; then
    exit 1
  fi

  #Start/stop container
	docker container "$cmd" jrvs-psql
	exit $?
	;;

  *)
	echo 'Illegal command'
	echo 'Commands: start|stop|create'
	exit 1
	;;
esac