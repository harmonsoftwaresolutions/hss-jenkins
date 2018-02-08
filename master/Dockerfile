FROM amazonlinux:2017.12
LABEL maintainer="evan.harmon@harmonsoftwaresolutions.com"

ARG user=jenkins
ARG group=jenkins
ARG uid=1000
ARG gid=1000
ARG http_port=8080
ARG agent_port=50000
ARG JENKINS_VERSION

ENV JENKINS_HOME /var/jenkins_home
ENV JENKINS_SLAVE_AGENT_PORT ${agent_port}
ENV COPY_REFERENCE_FILE_LOG $JENKINS_HOME/copy_reference_file.log
ENV JENKINS_UC https://updates.jenkins.io
ENV JENKINS_UC_EXPERIMENTAL=https://updates.jenkins.io/experimental
ENV JENKINS_VERSION ${JENKINS_VERSION:-2.99}

ARG JENKINS_URL=https://repo.jenkins-ci.org/releases/org/jenkins-ci/main/jenkins-war/${JENKINS_VERSION}/jenkins-war-${JENKINS_VERSION}.war
ARG JENKINS_PGP_URL=https://repo.jenkins-ci.org/releases/org/jenkins-ci/main/jenkins-war/${JENKINS_VERSION}/jenkins-war-${JENKINS_VERSION}.war.asc


# INSTALLS
RUN yum install -y java-devel git unzip gpa gnupg2 \
  && yum update -y

# Jenkins is run with user `jenkins`, uid = 1000
RUN groupadd -g ${gid} ${group} \
    && useradd -d "$JENKINS_HOME" -u ${uid} -g ${gid} -m -s /bin/bash ${user}

# could use ADD but this one does not check Last-Modified header neither does it allow to control checksum
# see https://github.com/docker/docker/issues/8331
# make /usr/share/jenkins as well as init.groovy.d
RUN mkdir -p /usr/share/jenkins/ref/init.groovy.d
RUN curl -fsSL ${JENKINS_URL} -o /usr/share/jenkins/jenkins.war
RUN curl -fsSL ${JENKINS_PGP_URL} -o /usr/share/jenkins/jenkins.war.asc
RUN curl -fsSl http://pkg.jenkins-ci.org/redhat/jenkins-ci.org.key -o /usr/share/jenkins/jenkins-ci.org.key
RUN gpg --import /usr/share/jenkins/jenkins-ci.org.key
RUN gpg --verify /usr/share/jenkins/jenkins.war.asc /usr/share/jenkins/jenkins.war

# COPY does not respect env expansion - that's a feature request
COPY --chown=1000:0 usr-scripts/* /usr/local/bin/
COPY --chown=1000:0 plugins.txt /usr/share/jenkins/plugins.txt

# `/usr/share/jenkins/ref/` contains all reference configuration we want
# to set on a fresh new installation. Use it to bundle additional plugins
# or config file with your custom jenkins Docker image.
# Pre-Install Plugins
RUN chmod -R +x /usr/local/bin/ \
  && /usr/local/bin/install-plugins.sh < /usr/share/jenkins/plugins.txt

COPY security.groovy /usr/share/jenkins/ref/init.groovy.d/security.groovy
COPY github.groovy /usr/share/jenkins/ref/init.groovy.d/github.groovy

RUN chown -R ${user} "$JENKINS_HOME" /usr/share/jenkins/ref \
  /usr/local/bin

# PORTS
EXPOSE ${http_port} ${agent_port}

USER ${user}
ENTRYPOINT ["/usr/local/bin/jenkins.sh"]
