pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Tests') {
            steps {
                bat './mvnw -q clean verify'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Coverage (JaCoCo)') {
            steps {
                bat 'mvn -q jacoco:report'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'target/site/jacoco/**', fingerprint: true
                }
            }
        }

        stage('SonarQube Scan') {
            steps {
                withSonarQubeEnv('sonarqube-local') {
                    bat """
            docker run --rm ^
              -e SONAR_HOST_URL=http://host.docker.internal:9000 ^
              -e SONAR_TOKEN=sqa_e49fd8aa59eed4d0f48af1c68971a5408252dda3 ^
              -v "%WORKSPACE%:/usr/src" ^
              sonarsource/sonar-scanner-cli
            """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 30, unit: 'MINUTES') {
                    script {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline fallida: Quality Gate = ${qg.status}"
                        }
                    }
                }
            }
        }
    }
}
