FROM jenkins/jenkins:lts
LABEL maintainer="evan.harmon@harmonsoftwaresolutions.com"

ARG user=jenkins
ARG group=jenkins
ARG uid=1000
ARG gid=1000
ARG http_port=8080
ARG agent_port=50000

ENV JENKINS_SLAVE_AGENT_PORT ${agent_port}
ENV JENKINS_USER ${JENKINS_USER}
ENV JENKINS_PASS ${JENKINS_PASS}
ENV GITHUB_USER ${GITHUB_USER}
ENV GITHUB_TOKEN ${GITHUB_TOKEN}

USER ${user}

COPY groovy/init.groovy /usr/share/jenkins/ref/init.groovy.d/init.groovy
COPY groovy/security.groovy /usr/share/jenkins/ref/init.groovy.d/security.groovy
COPY groovy/github.groovy /usr/share/jenkins/ref/init.groovy.d/github.groovy
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt

RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

# PORTS
EXPOSE ${http_port} ${agent_port}
