pipeline {
    agent any

    stages {
        stage('Clone') {
            steps {
                git 'https://github.com/Dilara-Aydogmus/final_project.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t moodapp .'
            }
        }

        stage('Docker Run') {
            steps {
                sh 'docker stop moodapp || true'
                sh 'docker rm moodapp || true'
                sh 'docker run -d --name moodapp -p 8081:8080 moodapp'
            }
        }
    }
}
