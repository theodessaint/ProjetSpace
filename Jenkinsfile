pipeline {
    agent any

    options {
        disableConcurrentBuilds()
        timestamps()
    }

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
                                org.sonarsource.scanner.maven:sonar-maven-plugin:5.9.0.7382:sonar \
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

        stage('Backend - Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
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

                        test -s "$JWT_CREDENTIAL"

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

                                chmod 600 /secrets/jwt.key
                                test -s /secrets/jwt.key
                            ' < "$JWT_CREDENTIAL"

                        docker volume inspect space-station-secrets
                    '''
                }
            }
        }

        stage('Docker Compose') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'mysql-g3-credentials',
                    usernameVariable: 'MYSQL_USER',
                    passwordVariable: 'MYSQL_PASSWORD'
                )]) {
                    dir('Docker') {
                        sh '''
                            docker volume inspect space-station-secrets
                            docker compose config --quiet

                            docker compose up -d --build \
                                --force-recreate springboot angular

                            docker compose ps
                        '''
                    }
                }
            }
        }

        stage('Vérification du backend') {
            steps {
                timeout(time: 3, unit: 'MINUTES') {
                    sh '''
                        docker run --rm \
                            --network space-station-network \
                            curlimages/curl \
                            --silent \
                            --show-error \
                            --output /dev/null \
                            --retry 20 \
                            --retry-delay 5 \
                            --retry-connrefused \
                            --connect-timeout 3 \
                            --max-time 5 \
                            --write-out 'HTTP %{http_code}\\n' \
                            http://springboot:8080/
                    '''
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

            sh '''
                echo "=== État des conteneurs ==="
                docker ps -a --filter name=space-station

                echo "=== Logs Spring Boot ==="
                docker logs --tail 100 space-station-back || true

                echo "=== Logs Nginx ==="
                docker logs --tail 50 space-station-front || true
            '''
        }
    }
}