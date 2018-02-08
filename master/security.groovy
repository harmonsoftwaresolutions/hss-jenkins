#!groovy
import jenkins.model.*
import jenkins.security.s2m.AdminWhitelistRule
import hudson.security.csrf.DefaultCrumbIssuer
import hudson.security.*

def instance = Jenkins.getInstance()

instance.setCrumbIssuer(new DefaultCrumbIssuer(true))
instance.setSlaveAgentPort(-1);
instance.getDescriptor("jenkins.CLI").get().setEnabled(false)
Jenkins.instance.getInjector().getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false)

Set<String> agentProtocolsList = ['JNLP4-connect', 'Ping']
instance.setAgentProtocols(agentProtocolsList);

def user = new File("/run/secrets/jenkins-user").text.trim()
def pass = new File("/run/secrets/jenkins-pass").text.trim()

def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount(user, pass)
instance.setSecurityRealm(hudsonRealm)

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(false)
instance.setAuthorizationStrategy(strategy)

instance.save()
