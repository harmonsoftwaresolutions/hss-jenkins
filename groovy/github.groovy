#!groovy
import jenkins.model.*
import hudson.security.*
import hudson.util.Secret
import org.jenkinsci.plugins.GithubSecurityRealm
import org.jenkinsci.plugins.plaincredentials.*
import org.jenkinsci.plugins.plaincredentials.impl.*
import com.cloudbees.plugins.credentials.impl.*;
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.domains.*;

def env = System.getenv()

String user = env['GITHUB_USER']
String token = env['GITHUB_TOKEN']
String email = env['GITHUB_EMAIL']

def instance = Jenkins.getInstance()

def desc = instance.getDescriptor("hudson.plugins.git.GitSCM")

desc.setGlobalConfigName(user)
desc.setGlobalConfigEmail(email)
desc.save()

// https://support.cloudbees.com/hc/en-us/articles/217708168-create-credentials-from-groovy
String secret_id = java.util.UUID.randomUUID().toString()
secretText = new StringCredentialsImpl(
  CredentialsScope.GLOBAL,
  secret_id,
  "hss-github-token",
  Secret.fromString(token)
)
SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), secretText)

String userpass_id = java.util.UUID.randomUUID().toString()
Credentials userpass = new UsernamePasswordCredentialsImpl(
  CredentialsScope.GLOBAL,
  userpass_id,
  "hss-github-token",
  user,
  token
)
SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), userpass)

String githubWebUri = 'https://github.com'
String githubApiUri = 'https://api.github.com'
String clientID = user
String clientSecret = token
String oauthScopes = 'read:org'
SecurityRealm github_realm = new GithubSecurityRealm(githubWebUri, githubApiUri, clientID, clientSecret, oauthScopes)


if (!github_realm.equals(instance.getSecurityRealm())) {
  instance.setSecurityRealm(github_realm)
  instance.save()
}
