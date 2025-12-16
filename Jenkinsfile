pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        EC2_CREDENTIALS = credentials('ec2key')
        IMAGE_NAME = "dilaraydogmus/mood-tracker-api"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/Dilara-Aydogmus/final_project.git'
            }
        }

        stage('Maven Build') {
            steps {
                sh 'chmod +x mvnw'   // <-- ÇALIŞTIRMA İZNİ VERİYORUZ
                sh './mvnw -B clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Docker Login') {
            steps {
                sh "echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u ${DOCKERHUB_CREDENTIALS_USR} --password-stdin"
            }
        }

        stage('Push Docker Image') {
            steps {
                sh "docker push ${IMAGE_NAME}:latest"
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent(['ec2key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no ubuntu@16.171.9.237 '
                        sudo docker stop moodapp || true &&
                        sudo docker rm moodapp || true &&
                        sudo docker pull dilaraydogmus/mood-tracker-api:latest &&
                        cd /home/ubuntu &&
                        sudo docker compose down || true &&
                        sudo docker compose up -d
                    '
                    '''
                }
            }
        }
    }
}
