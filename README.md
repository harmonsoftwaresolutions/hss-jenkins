# hss-jenkins
Docker Jenkins is doing rounds

## REQUIREMENTS
* docker
* jq
* aws cli installed and configured

## SETUP
### Export necessary env variables for your shell, replace with your own info
```
export JENKINS_USER='myuser'
export JENKINS_PASS='mypass'
export GITHUB_USER='myuser'
export GITHUB_TOKEN='mytoken'
export GITHUB_EMAIL='myemail@email.com'
export AWS_PROFILE='myprofile'
export AWS_DEFAULT_REGION='myregion'
export AWS_ACCOUNT='12345566'
```
## USE
### Build Image
`docker build -t "${AWS_ACCOUNT}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/hss/jenkins-master" -f ./Dockerfile .`

### Run with Docker
`docker-compose up --build -d`

### Stop with Docker
#### `-v` flag removes data volume. You want a fresh start next time!
`docker-compose down -v`

### Restart with Docker
`docker-compose down -v`
`docker-compose up --build -d`

## PUSH TO ECR
```
./docker-login.sh
docker push ${AWS_ACCOUNT}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/hss/jenkins-master:latest
```
