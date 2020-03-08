FROM ubuntu:18.04

RUN apt-get update && apt-get install -y \
 openjdk-8-jdk \
 vim \
 curl \
 git \
 unzip \
 nodejs \
 npm

RUN npm install -g npm@6.8.0

RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
RUN apt-get update && apt-get install -y \
 sbt 

RUN curl -O https://downloads.lightbend.com/scala/2.12.8/scala-2.12.8.deb
RUN dpkg -i scala*.deb

EXPOSE 8000
EXPOSE 9000
EXPOSE 5000
EXPOSE 8888

WORKDIR /home/wojciechlasak/
RUN mkdir /home/wojciechlasak/projekt
VOLUME /home/wojciechlasak/projekt
