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

        stage('Validacao do README e Docs') {
            steps {
                script {
                    echo 'Iniciando a checagem automatizada da documentacao...'
                    if (isUnix()) {
                        catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                            sh 'chmod +x ./scripts/validate_docs.sh'
                            sh './scripts/validate_docs.sh'
                        }
                    } else {
                        bat '"%PROGRAMFILES%\\Git\\bin\\sh.exe" ./scripts/validate_docs.sh'
                    }
                }
            }
        }

        stage('Validacao RockPaperScissors') {
            steps {
                script {
                    echo 'Iniciando validacao do RockPaperScissors...'
                    if (isUnix()) {
                        catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                            sh 'chmod +x ./scripts/validate_rockpaperscissors.sh'
                            sh './scripts/validate_rockpaperscissors.sh'
                        }
                    } else {
                        bat '"%PROGRAMFILES%\\Git\\bin\\sh.exe" ./scripts/validate_rockpaperscissors.sh'
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