FROM ubuntu:latest
RUN apt-get update && apt-get install -y wget bzr cvs git mercurial subversion && rm -rf /var/lib/apt/lists/*
COPY multi.sh ./multi.sh
COPY download.sh ./download.sh
RUN chmod +x multi.sh download.sh
CMD ["./multi.sh"]
