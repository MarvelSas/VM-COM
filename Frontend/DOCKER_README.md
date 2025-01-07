# How create and run docker image

## Create image

```
docker build -t vm-com-image .
```

## Run container

```
docker run -p 4200:80 --name vm-com-app vm-com-image

```
