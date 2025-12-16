pipeline {
    agent any

    environment {
        IMAGE_NAME = "dilaraaydogmus/mood-tracker-api"
        DOCKERHUB_CREDS = credentials('dockerhub')
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/Dilara-Aydogmus/final_project.git'
            }
        }

        stage('Maven Build') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Docker Login') {
            steps {
                sh '''
                echo $DOCKERHUB_CREDS_PSW | docker login \
                  -u $DOCKERHUB_CREDS_USR \
                  --password-stdin
                '''
            }
        }

        stage('Docker Push') {
            steps {
                sh "docker push ${IMAGE_NAME}:latest"
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent(['ec2key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no ubuntu@16.171.9.237 << 'EOF'
                      docker stop mood-app || true
                      docker rm mood-app || true
                      docker pull dilaraydogmus/mood-tracker-api:latest
                      cd /home/ubuntu
                      docker compose down || true
                      docker compose up -d
                    EOF
                    '''
                }
            }
        }
    }

    post {
        success {
            echo " Pipeline SUCCESS – Deploy tamamlandı"
        }
        failure {
            echo " Pipeline FAILED – Logları kontrol et"
        }
    }
}
