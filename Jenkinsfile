pipeline {
    agent any

    tools {
        jdk 'jdk-21'
        maven 'maven-3.9'
    }

    environment {
        JAVA_HOME = tool('jdk-21')
    }

    options {
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Backend - Build') {
            steps {
                dir('backend') {
                    sh 'mvn clean compile -q'
                }
            }
        }

        stage('Backend - Test') {
            steps {
                dir('backend') {
                    sh 'mvn verify'
                }
            }
            post {
                always {
                    dir('backend') {
                        junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                    }
                }
            }
        }

        stage('Backend - Package') {
            steps {
                dir('backend') {
                    sh 'mvn package -DskipTests -q'
                }
            }
            post {
                success {
                    dir('backend') {
                        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    }
                }
            }
        }

        stage('Frontend - Install') {
            steps {
                dir('frontend') {
                    sh 'npm ci'
                }
            }
        }

        stage('Frontend - Build') {
            steps {
                dir('frontend') {
                    sh 'npm run build'
                }
            }
        }
    }

    post {
        success {
            echo "Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        }
        failure {
            echo "Build FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        }
        always {
            cleanWs()
        }
    }
}
