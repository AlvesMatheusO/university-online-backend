# Database Migrations

Este projeto usa **Flyway** para gerenciar o schema do banco de dados de forma versionada e reproduzível.

## Estrutura
```
db/migration/
├── V1__create_initial_schema.sql    # Schema inicial (tables, sequences, indexes)
├── V2__seed_subjects.sql            # 15 disciplinas
├── V3__seed_professors.sql          # 6 professores
├── V4__seed_schedules.sql           # 30 horários (Seg-Sex)
└── README.md                        # Esta documentação
```

## Naming Convention

- **V{version}__{description}.sql** - Migrations versionadas (DDL/DML)
- **U{version}__{description}.sql** - Undo migrations (rollback)
- **R__{description}.sql** - Repeatable migrations (views, procedures)

## Como Funciona

1. Flyway executa migrations na ordem (V1 → V2 → V3...)
2. Armazena histórico na tabela `flyway_schema_history`
3. Migrations já executadas nunca rodam novamente (idempotente)
4. Garante consistência entre ambientes (dev, staging, prod)

## Comandos Úteis
```bash
# Ver status
./mvnw flyway:info

# Limpar banco (CUIDADO!)
./mvnw flyway:clean

# Migrar
./mvnw flyway:migrate

# Validar
./mvnw flyway:validate
```

## Adicionar Nova Migration

1. Criar arquivo `V{N}__description.sql`
2. Reiniciar aplicação (Quarkus executa automaticamente)
3. Verificar com `flyway:info`

## Rollback

Criar `U{N}__description.sql` com comandos reversos:
```sql
-- U2__seed_subjects.sql
DELETE FROM subjects;
```

## Best Practices

- ✅ Nunca modificar migrations já executadas
- ✅ Sempre testar em dev antes de prod
- ✅ Manter migrations pequenas e focadas
- ✅ Usar transações quando possível
- ✅ Documentar mudanças complexas