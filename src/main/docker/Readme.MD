## Guide to setup MySQL databse for the project

1. Install docker and docker-compose if you do not have it.
2. Go to 'docker' directory
3. Run

```
docker-compose build
```

4. Run

```
docker-compose up
```

Once it's completed, you have database with user: root; password:secret.
You can start your web application as it's configured to connect to this DB.

If you want to run containers in detached mode, add -d flag
as below:

```
docker-compose up -d
```

#How to stop docker container and database

Use next command to stop docker container:

```
docker-compose down
```

If you'd liket o remove all volumes created by docker, add -v flag
as below:
```
docker-compose down -v
```