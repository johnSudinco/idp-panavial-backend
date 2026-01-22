FROM ubuntu:latest
LABEL authors="john.arboleda"

ENTRYPOINT ["top", "-b"]