#!groovy
import hudson.model.*
import hudson.security.*
import hudson.security.csrf.DefaultCrumbIssuer
import jenkins.model.*
import jenkins.security.s2m.AdminWhitelistRule

def env = System.getenv()

String user = env['JENKINS_USER']
String pass = env['JENKINS_PASS']

def instance = Jenkins.getInstance()

println("--- Configuring Remote")
instance.getDescriptor("jenkins.CLI").get().setEnabled(false)
Set<String> agentProtocolsList = ['JNLP4-connect']
instance.setAgentProtocols(agentProtocolsList);

println("-- Slave -> Master Access Control")
instance.getInjector().getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false)

println("-- Checking CSRF protection")
if (instance.crumbIssuer == null) {
  instance.setCrumbIssuer(new DefaultCrumbIssuer(true))
}

println("--- Creating Admin User")
println(user)
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount(user, pass)
instance.setSecurityRealm(hudsonRealm)

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(false)
instance.setAuthorizationStrategy(strategy)

instance.save()
