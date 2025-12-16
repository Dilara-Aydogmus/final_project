pipeline {
    agent any

    environment {
        IMAGE_NAME = "dilaraaydogmus/mood-tracker-api"
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
                sh './mvnw -B clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                }
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
                    ssh -o StrictHostKeyChecking=no ubuntu@16.171.9.237 "
                        docker stop moodapp || true
                        docker rm moodapp || true
                        docker pull dilaraydogmus/mood-tracker-api:latest
                        cd /home/ubuntu
                        docker compose down || true
                        docker compose up -d
                    "
                    '''
                }
            }
        }
    }
}
