pipeline {
    agent any

    stages {
        stage('Diagnostic Docker - AVANT') {
            steps {
                sh '''
                    echo "=== Conteneurs existants ==="
                    docker ps -a

                    echo "=== Projets Docker Compose ==="
                    docker compose ls
                '''
            }
        }
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

        stage('Backend - Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
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
        stage('Provision JWT Secret') {
            steps {
                withCredentials([file(
                    credentialsId: 'space-station-jwt',
                    variable: 'JWT_CREDENTIAL'
                )]) {
                    sh '''
                        set +x

                        docker volume create space-station-secrets

                        docker run --rm -i \
                            -v space-station-secrets:/secrets \
                            alpine:3.22 \
                            sh -ec '
                                umask 077

                                cat > /secrets/jwt.key.tmp

                                if cmp -s /secrets/jwt.key.tmp /secrets/jwt.key; then
                                    rm /secrets/jwt.key.tmp
                                    echo "Clé JWT inchangée"
                                else
                                    mv /secrets/jwt.key.tmp /secrets/jwt.key
                                    echo "Clé JWT provisionnée"
                                fi
                            ' < "$JWT_CREDENTIAL"
                    '''
                }
            }
        }

        stage('Docker Compose') {
            steps {
                dir('Docker') {
                    sh 'docker compose up -d --build --force-recreate springboot'
                    sh 'docker restart space-station-front'
                    sh 'docker compose ps'
                }
            }
        }

        stage('Diagnostic frontend') {
            steps {
                sh '''
                    echo "=== Sources Angular ==="
                    grep -rnF 'localhost:8080' space-station-front/src/ || true

                    echo "=== Compilation Angular ==="
                    grep -rlF 'localhost:8080' space-station-front/dist/ || true

                    echo "=== Frontend déployé ==="
                    docker exec space-station-front \
                        grep -rlF 'localhost:8080' /usr/share/nginx/html || true
                '''
            }
        }
        stage('Diagnostic Docker - APRES') {
            steps {
                sh '''
                    echo "=== Conteneurs existants ==="
                    docker ps -a

                    echo "=== Projets Docker Compose ==="
                    docker compose ls
                '''
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