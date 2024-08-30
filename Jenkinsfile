pipeline {
    agent any

    environment {
        GIT_URL = 'https://github.com/veloori2517/Author-Service.git'
        DOCKER_IMAGE = 'vamsi5563/author-service'
        DOCKER_CREDENTIALS_ID = 'docker-credentials-id'
        DOCKER_REGISTRY_URL = 'https://index.docker.io/v1/'
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

                    // Log in to Docker Registry and check for success
                    def loginStatus = sh(script: "echo ${DOCKER_CREDENTIALS_ID} | docker login ${DOCKER_REGISTRY_URL} -u $(echo ${DOCKER_CREDENTIALS_ID} | cut -d':' -f1) -p $(echo ${DOCKER_CREDENTIALS_ID} | cut -d':' -f2) && echo 'Login successful' || echo 'Login failed'", returnStdout: true).trim()

                    echo "Docker Login Status: ${loginStatus}"

                    if (loginStatus.contains('Login failed')) {
                        error 'Docker login failed, aborting the pipeline.'
                    }

                    // Push Docker Image
                    docker.withRegistry(DOCKER_REGISTRY_URL, DOCKER_CREDENTIALS_ID) {
                        sh "docker push ${imageName}"
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
