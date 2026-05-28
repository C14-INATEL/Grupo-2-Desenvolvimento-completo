pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'chmod +x mvnw'
                        sh './mvnw -B clean compile'
                    } else {
                        bat '.\\mvnw.cmd -B clean compile'
                    }
                }
            }
        }

        stage('Validação do README e Docs') {
            steps {
                script {
                    echo 'Iniciando a checagem automatizada da documentação...'

                    if (isUnix()) {
                        sh 'chmod +x ./scripts/validate_docs.sh'
                        sh './scripts/validate_docs.sh'
                    } else {
                        bat '"%PROGRAMFILES%\\Git\\bin\\sh.exe" ./scripts/validate_docs.sh'
                    }
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts allowEmptyArchive: true, artifacts: 'target/classes/**'
        }
    }
}