#!groovy

def buildVersion = "3.0.${BUILD_NUMBER}"

def javaEnv() {
    def javaHome = tool 'JDK8u66'
    ["PATH=${env.PATH}:${javaHome}/bin", "JAVA_HOME=${javaHome}"]
}

stage('checkout') {
    node {
        try {
            checkout scm
        } catch (e) {
            notifyFailed()
            throw e
        }
    }
}

stage('build') {
    node {
        bGradle "./gradlew --refresh-dependencies goffa clean build sonarqube -PcodeQuality -DgruntColors=false -DbuildVersion=${buildVersion}"
    }
}

stage('tag and upload') {
    node {
        try {
            withEnv(javaEnv()) {
                sh "./gradlew uploadArchives tagRelease -DnexusUsername=$NEXUS_USERNAME -DnexusPassword=$NEXUS_PASSWORD \
                    -DgithubUser=$GITHUB_USERNAME -DgithubPassword=$GITHUB_PASSWORD -DbuildVersion=${buildVersion}"
            }
        } catch (e) {
            notifyFailed()
            throw e
        }
    }
}

stage ('propagate') {
    build job: 'intyg-intygstyper-pipeline', wait: false, parameters: [[$class: 'StringParameterValue', name: 'GIT_BRANCH', value: GIT_BRANCH]]
}
