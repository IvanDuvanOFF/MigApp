# Инструкция по развертыванию системы

---

### 1) Сборка образа API
Для сборки Docker-образа REST API необходимо выполнить команду:
```bash
docker build -f ./backend/docker/prod/Dockerfile -t mig-api .
```

---

### 2) Запуск без домена
Если у вас нет еще доменного имени, то получить SSL сертификат никак не получится,
так как для получения сертификата используется cetrtbot от Let'sEncrypt, 
а для выдачи сертификата с его помощью должна произойти проверка домена (DV).  

Запускать будем при помощи файла ```compose-local.yaml```.  
Так же эти действия все можно проделать и на персональном компьютере.

Для начала установим плагин для Loki, чтобы можно было собирать логи:
```bash
docker plugin install grafana/loki-docker-driver --alias loki --grant-all-permissions
```

В терминале запускаем команду:
```bash
docker compose -f compose-local.yaml up -d
```
Как только увидим, что все контейнеры запущены, через несколько секунд проверяем
их состояние
```bash
docker ps
```
Проверяем, что контейнеры работают.  
Далее проверим состояние API. Можно глянуть логи 
```bash
docker logs -f app
```
Где app имя контейнера API-шки.
А можно сделать запрос на актуатор Spring.
```bash
curl localhost/actuator/health
```
Если пришел ответ ```{"status":"UP"}```, то все хорошо, если же ```{"status":"DOWN"}```,
то что-то не так, если же ответа вовсе не последовало, или пришла 502-я ошибка от nginx,
следует проверить маппинг томов, или очистить их командой:
```bash
docker volume prune
```
или
```bash
docker volume rm <имя тома>
```
Только надо быть осторожным и не удалить нужные тома

---

### 3) Запуск на сервере с доменным именем
Чтобы запустить информационную систему на сервере при наличии доменного имени, необходимо:
1. Запустить nginx с конфигурацией для валидации доменного имени  
    ```bash
    docker compose -f compose-setup.yaml up -d nginx
    ```

2. Запустить certbot для домена и каждого поддомена сервера. В нашем случае:
   ```bash
   docker compose -f compose-setup.yaml run --rm certbot certonly --webroot --webroot-path=/var/www/certbot --email <email> --agree-tos --no-eff-email -d db.cute-example-deeplom.ru -d www.db.cute-example-deeplom.ru --preferred-challenges http
   docker compose -f compose-setup.yaml run --rm certbot certonly --webroot --webroot-path=/var/www/certbot --email <email> --agree-tos --no-eff-email -d graf.cute-example-deeplom.ru -d www.graf.cute-example-deeplom.ru --preferred-challenges http
   docker compose -f compose-setup.yaml run --rm certbot certonly --webroot --webroot-path=/var/www/certbot --email <email> --agree-tos --no-eff-email -d cute-example-deeplom.ru -d www.cute-example-deeplom.ru --preferred-challenges http
   ```
   Здесь мы для каждого поддомена запускаем процедуру выпуска сертификатов, указывая папку webroot и способ верификации домена,
   в нашем случае - по http.

3. Выключаем nginx
   ```bash
   docker compose -f compose-setup.yaml down nginx
   ```
4. Запускаем приложение
   ```bash
   docker compose -f compose.yaml up -d
   ```
5. Далее необходимо добавить записи в планировщик crontab
   ```bash
   echo "0 9 * * 1 /usr/bin/docker compose -f /home/admin/app/mig-app/compose-setup.yaml run --rm certbot renew >> /var/log/renew-ssl.log && /usr/bin/docker compose -f /home/admin/app/mig-app/compose.yaml kill -s SIGHUP nginx" | crontab -
   ```
   Теперь каждый понедельник в 9 утра будет производить автоматическая проверка сертификатов.  
   если они близки к тому, чтобы просрочиться, они обновятся.  
   Сертификаты от Let's Encrypt имеют срок годности в 90 дней.
   ```bash
   echo "30 7 * * * /usr/bin/docker compose -f /home/admin/app/mig-app/compose.yaml run --rm api-backup -a backup >> /var/log/backup.log" | crontab -
   ```
   Данная команда каждый день в 7:30 утра запускает процедуру бэкапа данных 
   из файлового хранилища API.  
   Хранятся они в томе ```api-backup-volume```.
   ```bash
   echo "30 7 * * * /usr/bin/docker compose -f /home/admin/app/mig-app/compose.yaml exec -i -e PGPASSWORD=pASSword db pg_dump -U ivan mig | gzip -9 > /tmp/mig-app/backup/db-backup.sql.gz" | crontab -
   ```
   Здесь так же каждые 7 утра будет происходить бэкап базы данных.  
   **Оба бэкапа можно найти в директории ```/tmp/mig-app/backup```**
6. Проверка работоспособности системы  
   Тут все аналогично варианту на локальной машине.