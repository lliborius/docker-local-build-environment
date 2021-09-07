pipeline {
	agent any
	tools {
		maven 'maven LATEST'
		ant 'ant latest'
	} 
		environment {
        SCM_CREDENTIALS = credentials('Jenkins_GIT')
    }
   stages {
      stage('Preparation') { // for display purposes
         steps {
            // Get source from GIT repository.
            git  branch: 'master', credentialsId: 'Jenkins_GIT	', poll: true, changelog: true, url: 'https://github.com/lliborius/docker-local-build-environment'
         }
      }
      stage('Build') {
         steps {
            // get the application version from pom
            pushPomVersionToEnv('helloworld/HelloWorld/pom.xml')
            // Run the maven build
            ansiColor('xterm') {
            	sh "mvn -f helloworld/HelloWorld/pom.xml -B versions:set -DnewVersion=${APP_SNAPSHOT_VERSION} -DenableJenkinsProfile=true"
            	sh "mvn clean install package -f helloworld/HelloWorld/pom.xml -e -U -fae -DenableJenkinsProfile=true -DfailIfNoTests=false -DskipTests=false -Dapplication.buildNumber=${BUILD_NUMBER}"
            }
	 }
      }	
   }
}
