# How create and run docker image

## Create image

```
docker build -t angular-docker .
```

## Run container

```
docker run -p 4200:4200 angular-docker
docker run -p 4200:80 --name vm-com-app angular-docker

```
