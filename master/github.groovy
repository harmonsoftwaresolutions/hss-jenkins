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

def instance = Jenkins.getInstance()

def user = new File("/run/secrets/github-user").text.trim()
def token = new File("/run/secrets/github-token").text.trim()

// https://support.cloudbees.com/hc/en-us/articles/217708168-create-credentials-from-groovy
// String id = java.util.UUID.randomUUID().toString()
// Credentials c = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, id, "github jenkins", "user", "password")
// SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)

secretText = new StringCredentialsImpl(CredentialsScope.GLOBAL,token, "github jenkins token", Secret.fromString("some secret text goes here"))
SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), secretText)

// String githubWebUri = 'https://github.com'
// String githubApiUri = 'https://api.github.com'
// String clientID = user
// String clientSecret = token
// String oauthScopes = 'read:org'
// SecurityRealm github_realm = new GithubSecurityRealm(githubWebUri, githubApiUri, clientID, clientSecret, oauthScopes)

// if (!github_realm.equals(instance.getSecurityRealm())) {
  // instance.setSecurityRealm(github_realm)
  // instance.save()
// }
