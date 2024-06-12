#!/bin/bash

source $1

if [[ $? -ne 0 ]]; then
  echo "No env file"
  exit 1
fi

# Check if project has been deployed earlier
if [ -d "$PROJECT_FOLDER/mig-app" ]; then
  cd "$PROJECT_FOLDER/mig-app" || exit 1

  #  Stop service
  if [ -f "$PROJECT_FOLDER/mig-app/compose.yaml" ]; then
    docker compose -f compose.yaml down
  fi

  #  Remove older project version
  cd ../
  rm -rf ./mig-app
fi

# Unpack newer project version
cd "$PROJECT_FOLDER" || exit 1
mkdir -p ./mig-app
unzip ./mig-app.zip -d ./mig-app
cd ./mig-app || exit 1

# Move initial data to tmp folder if it's not been done earlier
if [ ! -d "$PROJECT_TEMP/data" ]; then
  mkdir -p "$PROJECT_TEMP"
  cp -r ./tmp/mig-app/data "$PROJECT_TEMP"
fi

# Disable firewall to be able to download all dependencies
ufw disable

# Run nginx for certificates initializing
docker compose -f compose-setup.yaml up -d nginx
docker compose -f compose-setup.yaml run --rm certbot certonly --webroot --webroot-path=/var/www/certbot --email "$EMAIL" --agree-tos --no-eff-email -d "$DB_DOMAIN" -d "www.$DB_DOMAIN" --preferred-challenges http
docker compose -f compose-setup.yaml run --rm certbot certonly --webroot --webroot-path=/var/www/certbot --email "$EMAIL" --agree-tos --no-eff-email -d "$GRAFANA_DOMAIN" -d "www.$GRAFANA_DOMAIN" --preferred-challenges http
docker compose -f compose-setup.yaml run --rm certbot certonly --webroot --webroot-path=/var/www/certbot --email "$EMAIL" --agree-tos --no-eff-email -d "$DOMAIN" -d "www.$DOMAIN" --preferred-challenges http
docker compose -f compose-setup.yaml down nginx

# Build all docker images and deploy system
docker compose -f compose.yaml up -d --build > build-logs.txt

# Turn on firewall
ufw enable

# Create cron tasks
echo "0 9 * * 1 /usr/bin/docker compose -f $PROJECT_FOLDER/mig-app/compose-setup.yaml run --rm certbot renew >> /var/log/renew-ssl.log && /usr/bin/docker compose -f $PROJECT_FOLDER/mig-app/compose.yaml kill -s SIGHUP nginx" > ./cron
echo "30 7 * * * /usr/bin/docker compose -f $PROJECT_FOLDER/mig-app/compose.yaml run --rm api-backup -a backup >> /var/log/backup.log" >> ./cron
echo "30 7 * * * /usr/bin/docker compose -f $PROJECT_FOLDER/mig-app/compose.yaml exec -i -e PGPASSWORD=$DATASOURCE_PASSWORD db pg_dump -U $DATASOURCE_USERNAME $DATASOURCE_DB | gzip -9 > $PROJECT_TEMP/backup/db-backup.sql.gz" >> ./cron

crontab ./cron

rm ./cron