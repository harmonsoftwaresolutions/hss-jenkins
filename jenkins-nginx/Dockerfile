FROM amazonlinux:2017.12
LABEL maintainer="evan.harmon@harmonsoftwaresolutions.com"

# INSTALLS
RUN amazon-linux-extras install nginx1.12 \
  && yum install -y git unzip \
  && yum update -y

RUN mkdir -p /data/conf/ /data/conf.d /data/sites-enabled/
COPY conf/nginx.conf /data/conf/
COPY conf/jenkins.conf /data/conf.d/jenkins.conf

EXPOSE 80
CMD ["nginx", "-c", "/data/conf/nginx.conf"]
