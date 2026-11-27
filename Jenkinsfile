pipeline {
    agent any

    stages {
        stage('Backend - Maven & SonarQube') {
            agent {
                docker {
                    image 'maven:3.10.0-eclipse-temurin-21'
                    args '''
                        -v $HOME/.m2:/root/.m2:z
                        -u root
                    '''
                    reuseNode true
                }
            }

            steps {
                dir('space-station-back') {
                    withSonarQubeEnv('sonar') {
                        sh '''
                            mvn -B clean verify \
                                org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                                -Dsonar.projectKey=space-station
                        '''
                    }
                }
            }
        }

        stage('Frontend - Angular') {
            agent {
                docker {
                    image 'node:lts-bullseye'
                    args '''
                        -v $HOME/.npm:/root/.npm:z
                        -u root
                    '''
                    reuseNode true
                }
            }

            steps {
                dir('space-station-front') {
                    sh 'npm ci'
                    sh 'npm run build'
                }
            }
        }
            

        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Docker Compose') {
            steps {
                dir('Docker') {
                    sh 'docker compose up -d --build springboot angular'
                    sh 'docker compose ps'
                }
            }
        }
    }

    post {
        success {
            echo 'Space Station : déploiement réussi.'
        }

        failure {
            echo 'Space Station : échec du pipeline.'
        }
    }
}