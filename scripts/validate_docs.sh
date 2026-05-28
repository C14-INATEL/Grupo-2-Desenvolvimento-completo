#!/bin/bash

# Configuração de cores para o terminal do Jenkins
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m' # Sem cor

ERRORS=0

echo -e "${YELLOW}=== INICIANDO VALIDAÇÃO DA DOCUMENTAÇÃO DO PROJETO ===${NC}\n"

# 1. Verificar se o README.md existe
if [ -f "README.md" ]; then
    echo -e "${GREEN}[OK] README.md encontrado.${NC}"
    
    # Validar seção de Uso de IA
    if grep -qi "Uso de IA" README.md || grep -qi "Inteligência Artificial" README.md; then
        echo -e "${GREEN}[OK] Seção 'Uso de IA' detectada no README.${NC}"
        # Validar se há menção a prompts (mínimo exigido)
        if grep -qi "prompt" README.md; then
            echo -e "${GREEN}[OK] Evidência de prompts de IA encontrada no README.${NC}"
        else
            echo -e "${RED}[ERRO] Seção de IA encontrada, mas não detectamos a palavra 'prompt'. Lembre-se de colocar pelo menos 3 exemplos.${NC}"
            ERRORS=$((ERRORS + 1))
        fi
    else
        echo -e "${RED}[ERRO] Seção 'Uso de IA' NÃO encontrada no README.md (Obrigatório!).${NC}"
        ERRORS=$((ERRORS + 1))
    fi

    # Validar seção de Jenkins / CI/CD
    if grep -qi "Jenkins" README.md || grep -qi "CI/CD" README.md; then
        echo -e "${GREEN}[OK] Seção de Jenkins/Pipeline detectada no README.${NC}"
    else
        echo -e "${YELLOW}[AVISO] Menção explícita ao Jenkins não encontrada no README. Considere documentar como a pipeline funciona.${NC}"
    fi

    # Validar seções de Metodologia e Dinâmica
    if grep -qi "Metodologia" README.md || grep -qi "Dinâmica" README.md; then
        echo -e "${GREEN}[OK] Menções à Metodologia/Dinâmica encontradas no README.${NC}"
    else
        echo -e "${YELLOW}[AVISO] Seções de Metodologia/Dinâmica não estão explícitas no README (podem estar no arquivo separado).${NC}"
    fi
else
    echo -e "${RED}[ERRO CRÍTICO] Arquivo README.md principal NÃO foi encontrado na raiz!${NC}"
    ERRORS=$((ERRORS + 1))
fi

echo -e "\n--------------------------------------------------\n"

# 2. Verificar se a pasta docs e o arquivo de histórias existem
HISTORIAS_PATH="docs/historias-usuario.md"
if [ -f "$HISTORIAS_PATH" ]; then
    echo -e "${GREEN}[OK] Arquivo '$HISTORIAS_PATH' encontrado.${NC}"
    
    # Validar formato clássico: Como <perfil>, eu quero <ação> para que <benefício>
    if grep -qi "Como " "$HISTORIAS_PATH" && grep -qi "eu quero " "$HISTORIAS_PATH" && grep -qi "para que " "$HISTORIAS_PATH"; then
        echo -e "${GREEN}[OK] Padrão 'Como/Eu quero/Para que' de Histórias de Usuário detectado.${NC}"
    else
        echo -e "${RED}[ERRO] O formato das histórias em '$HISTORIAS_PATH' parece estar fora do padrão exigido pelo edital.${NC}"
        ERRORS=$((ERRORS + 1))
    fi

    # Validar critérios de aceitação (Given/When/Then ou Dado/Quando/Então)
    if grep -qi "Given" "$HISTORIAS_PATH" || grep -qi "Dado" "$HISTORIAS_PATH"; then
        echo -e "${GREEN}[OK] Critérios de aceitação detectados nas histórias.${NC}"
    else
        echo -e "${RED}[ERRO] Não encontramos critérios de aceitação (Given/When/Then) em '$HISTORIAS_PATH'.${NC}"
        ERRORS=$((ERRORS + 1))
    fi
else
    echo -e "${RED}[ERRO] Arquivo '$HISTORIAS_PATH' NÃO foi encontrado!${NC}"
    ERRORS=$((ERRORS + 1))
fi

echo -e "\n--------------------------------------------------\n"

# Finalização do Job
if [ $ERRORS -eq 0 ]; then
    echo -e "${GREEN}=== VALIDAÇÃO CONCLUÍDA COM SUCESSO! Documentação cumpre os requisitos avaliados. ===${NC}"
    exit 0
else
    echo -e "${RED}=== VALIDAÇÃO FALHOU: $ERRORS erro(s) de documentação encontrado(s). Verifique os logs acima. ===${NC}"
    exit 1
fi