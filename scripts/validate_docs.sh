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
    
    # Validar formato e critérios por história (iterando por seção)

    # Validar rastreabilidade das histórias: Issue/PR reais e testes existentes
    trace_errors=0
    in_story=false
    story_title=""
    issue_or_pr_ok=false
    tests_ok=false
    has_como=false
    has_quero=false
    has_para=false
    has_dado=false
    has_quando=false
    has_entao=false

    finalize_story() {
        if [ "$in_story" = true ]; then
            syntax_ok=true
            if [ "$has_como" != true ] || [ "$has_quero" != true ] || [ "$has_para" != true ]; then
                syntax_ok=false
            fi
            if [ "$has_dado" != true ] || [ "$has_quando" != true ] || [ "$has_entao" != true ]; then
                syntax_ok=false
            fi

            if [ "$syntax_ok" = true ]; then
                echo -e "${GREEN}[OK] Sintaxe validada em '$story_title'.${NC}"
            else
                echo -e "${RED}[ERRO] Sintaxe incompleta em '$story_title' (Como/Eu quero/Para que e Dado/Quando/Então).${NC}"
                trace_errors=$((trace_errors + 1))
            fi

            if [ "$issue_or_pr_ok" = true ] && [ "$tests_ok" = true ]; then
                echo -e "${GREEN}[OK] Rastreabilidade validada em '$story_title'.${NC}"
            else
                echo -e "${RED}[ERRO] Rastreabilidade incompleta em '$story_title' (exige Issue/PR válido e testes existentes).${NC}"
                trace_errors=$((trace_errors + 1))
            fi
        fi
    }

    has_real_issue_or_pr() {
        # Aceita PR com #numero ou Issue/Card com conteúdo não vazio
        local value="$1"
        if echo "$value" | grep -Eq '#[0-9]+'; then
            return 0
        fi
        if echo "$value" | grep -Eq '[A-Za-z0-9]'; then
            return 0
        fi
        return 1
    }

    tests_exist() {
        # Recebe linha completa de testes e valida se cada identificador aparece em src/test/java
        local value="$1"
        local has_any=false
        local fail=false
        local test_name

        # Extrai itens entre crases; se não houver, faz split por vírgula
        if echo "$value" | grep -q '\`'; then
            while IFS= read -r test_name; do
                [ -z "$test_name" ] && continue
                has_any=true
                if ! grep -R -q "$test_name" src/test/java; then
                    fail=true
                fi
            done < <(printf '%s\n' "$value" | sed -n 's/.*`\([^`]*\)`.*/\1/p')
        else
            IFS=',' read -r -a tests_array <<< "$value"
            for test_name in "${tests_array[@]}"; do
                test_name=$(echo "$test_name" | xargs)
                [ -z "$test_name" ] && continue
                has_any=true
                if ! grep -R -q "$test_name" src/test/java; then
                    fail=true
                fi
            done
        fi

        if [ "$has_any" = true ] && [ "$fail" = false ]; then
            return 0
        fi
        return 1
    }

    while IFS= read -r line; do
        case "$line" in
            "## US-"*)
                finalize_story
                story_title="$line"
                in_story=true
                issue_or_pr_ok=false
                tests_ok=false
                has_como=false
                has_quero=false
                has_para=false
                has_dado=false
                has_quando=false
                has_entao=false
                ;;
            "**Como**"*|"**como**"*)
                has_como=true
                ;;
            "**eu quero**"*|"**Eu quero**"*)
                has_quero=true
                ;;
            "**para que**"*|"**Para que**"*)
                has_para=true
                ;;
            "Dado "*|"Given "*)
                has_dado=true
                ;;
            "Quando "*|"When "*)
                has_quando=true
                ;;
            "Então "*|"Entao "*|"Then "*)
                has_entao=true
                ;;
            "**Issue/Card:**"*)
                issue_value="${line#**Issue/Card:**}"
                issue_value=$(echo "$issue_value" | xargs)
                if has_real_issue_or_pr "$issue_value"; then
                    issue_or_pr_ok=true
                fi
                ;;
            "**PR:**"*)
                pr_value="${line#**PR:**}"
                pr_value=$(echo "$pr_value" | xargs)
                if has_real_issue_or_pr "$pr_value"; then
                    issue_or_pr_ok=true
                fi
                ;;
            "**Teste:**"*)
                tests_value="${line#**Teste:**}"
                tests_value=$(echo "$tests_value" | xargs)
                if tests_exist "$tests_value"; then
                    tests_ok=true
                fi
                ;;
        esac
    done < "$HISTORIAS_PATH"

    finalize_story

    if [ "$trace_errors" -gt 0 ]; then
        ERRORS=$((ERRORS + trace_errors))
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