#!groovy

node {
    def buildVersion = "3.12.0.${BUILD_NUMBER}"
    def release = RELEASE

    stage('checkout') {
        git url: "https://github.com/sklintyg/common.git", branch: GIT_BRANCH
        util.run { checkout scm }
    }

    stage('build') {
        try {
            shgradle "--refresh-dependencies clean build testReport -PcodeQuality -DgruntColors=false -DbuildVersion=${buildVersion}"
        } finally {
            publishHTML allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'build/reports/allTests', \
                reportFiles: 'index.html', reportName: 'JUnit results'
        }
    }

    stage('tag and upload') {
        shgradle "uploadArchives tagRelease -DbuildVersion=${buildVersion}"
    }

    stage('propagate') {
        if (release == 'true') {
            println "Releases will not trigger downstream builds"
        } else {
            ["intygstjanst"].each {
                try {
                    build job: "dintyg-${it}-test-pipeline", parameters: [string(name: 'GIT_BRANCH', value: GIT_BRANCH)]
                } catch (e) {
                    println "Trigger build error (ignored): ${e.message}"
                }
            }
        }
    }

    stage('notify') {
        util.notifySuccess()
    }
}
