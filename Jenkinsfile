pipeline {
    agent any

    environment {
        APP_JAR = 'target/grupo2projeto-1.0-SNAPSHOT.jar'
        DEPLOY_DIR = 'target/deploy/game-hub'
        MAIN_CLASS = 'br.inatel.grupo2.app.Launcher'
    }

    stages {
        stage('Build') {
            steps {
                script {
                    runMaven('clean compile')
                }
            }
        }

        stage('Testes Automatizados') {
            steps {
                script {
                    echo 'Executando testes automatizados do projeto...'

                    if (isUnix()) {
                        runMaven('test "-Dtest=br.inatel.grupo2.model.**.*Test,testes_unitarios.BattleshipsTest,testes_unitarios.MenuLogicaTest,testes_unitarios.MinesweeperModelTest,testes_unitarios.RockPaperScissorsModelTest"')
                    } else {
                        runMaven('test')
                    }
                }
            }

            post {
                always {
                    junit allowEmptyResults: false, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    echo 'Gerando artefato da aplicacao...'
                    runMaven('package -DskipTests')
                    validateApplicationJar()
                }
            }

            post {
                success {
                    archiveArtifacts artifacts: "${APP_JAR}", fingerprint: true
                }
            }
        }

        stage('OWASP Dependency Check') {
            steps {
                script {
                    echo 'Executando analise de vulnerabilidades nas dependencias...'

                    if (isUnix()) {
                        sh '''
                            set +e
                            chmod +x mvnw
                            timeout 180s ./mvnw -B dependency-check:check
                            status=$?
                            if [ "$status" -eq 124 ]; then
                                echo "OWASP Dependency Check excedeu 3 minutos sem NVD API Key; seguindo pipeline como analise nao bloqueante."
                            elif [ "$status" -ne 0 ]; then
                                echo "OWASP Dependency Check retornou codigo $status; seguindo pipeline como analise nao bloqueante."
                            fi
                            exit 0
                        '''
                    } else {
                        bat '''
                            .\\mvnw.cmd -B dependency-check:check
                            if errorlevel 1 echo OWASP Dependency Check falhou; seguindo pipeline como analise nao bloqueante.
                            exit /b 0
                        '''
                    }
                }
            }

            post {
                always {
                    archiveArtifacts allowEmptyArchive: true, artifacts: 'target/dependency-check-report.*'
                }
            }
        }

        stage('Validacao do README') {
            steps {
                script {
                    echo 'Validando informacoes obrigatorias do README...'

                    if (isUnix()) {
                        sh '''
                            set -e
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
                    echo 'Validando historias de usuario e rastreabilidade...'

                    if (isUnix()) {
                        sh '''
                            chmod +x ./scripts/validate_docs.sh
                            ./scripts/validate_docs.sh
                        '''
                    } else {
                        bat '"%PROGRAMFILES%\\Git\\bin\\bash.exe" -lc "./scripts/validate_docs.sh"'
                    }
                }
            }
        }

        stage('Deploy Local') {
            steps {
                script {
                    echo "Publicando pacote da aplicacao em ${DEPLOY_DIR}..."

                    if (isUnix()) {
                        sh '''
                            set -e
                            rm -rf "$DEPLOY_DIR"
                            mkdir -p "$DEPLOY_DIR"
                            cp "$APP_JAR" "$DEPLOY_DIR/"
                            cat > "$DEPLOY_DIR/README_DEPLOY.txt" <<EOF
Game Hub - Deploy local

Artefato: grupo2projeto-1.0-SNAPSHOT.jar
Classe principal: $MAIN_CLASS

Este diretorio representa o deploy local da aplicacao desktop gerado pela pipeline Jenkins.
Para executar em ambiente de desenvolvimento, use:
./mvnw javafx:run
EOF
                            (cd "$DEPLOY_DIR" && sha256sum *.jar > SHA256SUMS.txt)
                        '''
                    } else {
                        bat '''
                            if exist "%DEPLOY_DIR%" rmdir /S /Q "%DEPLOY_DIR%"
                            mkdir "%DEPLOY_DIR%"
                            copy "%APP_JAR%" "%DEPLOY_DIR%\\"
                            (
                                echo Game Hub - Deploy local
                                echo.
                                echo Artefato: grupo2projeto-1.0-SNAPSHOT.jar
                                echo Classe principal: %MAIN_CLASS%
                                echo.
                                echo Este diretorio representa o deploy local da aplicacao desktop gerado pela pipeline Jenkins.
                                echo Para executar em ambiente de desenvolvimento, use:
                                echo .\\mvnw.cmd javafx:run
                            ) > "%DEPLOY_DIR%\\README_DEPLOY.txt"
                            certutil -hashfile "%APP_JAR%" SHA256 > "%DEPLOY_DIR%\\SHA256SUMS.txt"
                        '''
                    }
                }
            }

            post {
                success {
                    archiveArtifacts artifacts: 'target/deploy/game-hub/**', fingerprint: true
                }
            }
        }
    }
}

def runMaven(String goals) {
    if (isUnix()) {
        sh 'chmod +x mvnw'
        sh "./mvnw -B ${goals}"
    } else {
        bat ".\\mvnw.cmd -B ${goals}"
    }
}

def validateApplicationJar() {
    if (isUnix()) {
        sh '''
            set -e
            test -f "$APP_JAR"
            rm -rf META-INF
            jar xf "$APP_JAR" META-INF/MANIFEST.MF
            grep -q "Main-Class: $MAIN_CLASS" META-INF/MANIFEST.MF
            rm -rf META-INF
        '''
    } else {
        bat '''
            if not exist "%APP_JAR%" exit /b 1
            if exist META-INF rmdir /S /Q META-INF
            jar xf "%APP_JAR%" META-INF/MANIFEST.MF
            findstr /C:"Main-Class: %MAIN_CLASS%" META-INF\\MANIFEST.MF >nul
            if errorlevel 1 exit /b 1
            rmdir /S /Q META-INF
        '''
    }
}
