pipeline {
    agent any

    environment {
        GIT_URL = 'https://github.com/veloori2517/Author-Service.git'
        DOCKER_IMAGE = 'vamsi5563/author-service'
        DOCKER_CREDENTIALS_ID = 'docker-credentials-id'
        DOCKER_REGISTRY_URL = 'https://index.docker.io/'
        GIT_CREDENTIALS_ID = 'github_credentials_id'
        BUILD_ID = "${env.BUILD_ID}"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: GIT_URL, credentialsId: GIT_CREDENTIALS_ID
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def imageName = "${DOCKER_IMAGE}:${BUILD_ID}"
                    sh "docker build -t ${imageName} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                   def imageName = "${DOCKER_IMAGE}:${BUILD_ID}"
                               echo "Pushing Docker image: ${imageName}"

                               // Login to Docker registry
                               withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                                   sh """
                                   echo ${DOCKER_PASSWORD} | docker login ${DOCKER_REGISTRY_URL} -u ${DOCKER_USERNAME} --password-stdin
                                   """

                                   // Check login status
                                   def loginStatus = sh(script: 'docker info', returnStatus: true)
                                   if (loginStatus != 0) {
                                       error 'Docker login failed, aborting the pipeline.'
                                   }

                                   // Push Docker Image
                                   def pushStatus = sh(script: "docker push ${imageName}", returnStatus: true)
                                   if (pushStatus != 0) {
                                       error 'Docker push failed, aborting the pipeline.'
                                   }
                               }

                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    def imageName = "${DOCKER_IMAGE}:${BUILD_ID}"

                    // Check and remove existing container
                    sh """
                    docker stop author-service_dev || true
                    docker rm author-service_dev || true
                    docker run -d -p 9090:9090 --name author-service_dev ${imageName}
                    """
                }
            }
        }
    }

    post {
        always {
            sh 'docker system prune -f'
        }
        success {
            echo 'Build, Test, and Deployment were successful'
        }
        failure {
            echo 'Build, Test, or Deployment failed.'
        }
    }
}
