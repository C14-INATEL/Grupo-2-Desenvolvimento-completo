RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m'

ERRORS=0

echo -e "${YELLOW}=== INICIANDO VALIDACAO DO ROCKPAPERSCISSORS ===${NC}\n"

MODEL_PATH="src/main/java/br/inatel/grupo2/model/rockpaperscissors/RockPaperScissorsModel.java"
CONTROLLER_PATH="src/main/java/br/inatel/grupo2/controller/rockpaperscissors/RockPaperScissorsController.java"
VIEW_CONTROLLER_PATH="src/main/java/br/inatel/grupo2/controller/rockpaperscissors/RockPaperScissorsViewController.java"

if [ -f "$MODEL_PATH" ]; then
    echo -e "${GREEN}[OK] RockPaperScissorsModel.java encontrado.${NC}"
else
    echo -e "${RED}[ERRO] RockPaperScissorsModel.java NAO encontrado em $MODEL_PATH${NC}"
    ERRORS=$((ERRORS + 1))
fi

if [ -f "$CONTROLLER_PATH" ]; then
    echo -e "${GREEN}[OK] RockPaperScissorsController.java encontrado.${NC}"
else
    echo -e "${RED}[ERRO] RockPaperScissorsController.java NAO encontrado em $CONTROLLER_PATH${NC}"
    ERRORS=$((ERRORS + 1))
fi

if [ -f "$VIEW_CONTROLLER_PATH" ]; then
    echo -e "${GREEN}[OK] RockPaperScissorsViewController.java encontrado.${NC}"
else
    echo -e "${RED}[ERRO] RockPaperScissorsViewController.java NAO encontrado em $VIEW_CONTROLLER_PATH${NC}"
    ERRORS=$((ERRORS + 1))
fi

echo -e "\n--------------------------------------------------\n"

TEST_MODEL_PATH="src/test/java/testes_unitarios/RockPaperScissorsModelTest.java"
TEST_MOCK_PATH="src/test/java/mocks/RockPaperScissorsMockTest.java"

if [ -f "$TEST_MODEL_PATH" ]; then
    echo -e "${GREEN}[OK] RockPaperScissorsModelTest.java encontrado.${NC}"
else
    echo -e "${RED}[ERRO] RockPaperScissorsModelTest.java NAO encontrado em $TEST_MODEL_PATH${NC}"
    ERRORS=$((ERRORS + 1))
fi

if [ -f "$TEST_MOCK_PATH" ]; then
    echo -e "${GREEN}[OK] RockPaperScissorsMockTest.java encontrado.${NC}"
else
    echo -e "${YELLOW}[AVISO] RockPaperScissorsMockTest.java NAO encontrado (opcional em ambiente sem display).${NC}"
fi

echo -e "\n--------------------------------------------------\n"

echo -e "${YELLOW}Executando testes unitarios do RockPaperScissors...${NC}"
chmod +x mvnw
./mvnw -B test -Dtest=RockPaperScissorsModelTest -Dsurefire.failIfNoSpecifiedTests=false -q

if [ $? -eq 0 ]; then
    echo -e "${GREEN}[OK] Testes unitarios passaram com sucesso.${NC}"
else
    echo -e "${RED}[ERRO] Testes unitarios falharam.${NC}"
    ERRORS=$((ERRORS + 1))
fi

echo -e "\n--------------------------------------------------\n"

if [ $ERRORS -eq 0 ]; then
    echo -e "${GREEN}=== VALIDACAO DO ROCKPAPERSCISSORS CONCLUIDA COM SUCESSO! ===${NC}"
    exit 0
else
    echo -e "${RED}=== VALIDACAO FALHOU: $ERRORS erro(s) encontrado(s). Verifique os logs acima. ===${NC}"
    exit 1
fi