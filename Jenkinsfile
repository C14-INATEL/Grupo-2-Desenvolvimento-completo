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

        stage('Testes Automatizados') {
            steps {
                script {
                    echo 'Executando testes automatizados do projeto...'

                    if (isUnix()) {
                        sh 'chmod +x mvnw'
                        sh './mvnw -B test'
                    } else {
                        bat '.\\mvnw.cmd -B test'
                    }
                }
            }

            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Validacao do README') {
            steps {
                script {
                    echo 'Iniciando validacao geral do README...'

                    if (isUnix()) {
                        sh '''
                            test -f README.md
                            grep -qi "Jenkins" README.md
                            grep -Eqi "Uso de IA|Intelig|Artificial" README.md
                            grep -qi "prompt" README.md
                        '''
                    } else {
                        bat '''
                            if not exist README.md exit /b 1
                            findstr /I "Jenkins" README.md >nul
                            if errorlevel 1 exit /b 1
                            findstr /I "Artificial IA" README.md >nul
                            if errorlevel 1 exit /b 1
                            findstr /I "prompt" README.md >nul
                            if errorlevel 1 exit /b 1
                        '''
                    }
                }
            }
        }

        stage('Validacao das Historias e Rastreabilidade') {
            steps {
                script {
                    echo 'Iniciando validacao das historias de usuario e rastreabilidade...'

                    if (isUnix()) {
                        sh 'chmod +x ./scripts/validate_docs.sh'
                        sh './scripts/validate_docs.sh'
                    } else {
                        bat '"%PROGRAMFILES%\\Git\\bin\\bash.exe" -lc "./scripts/validate_docs.sh"'
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
