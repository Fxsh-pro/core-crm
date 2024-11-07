package com.crm.corecrm.domain.repository

import com.crm.corecrm.domain.db.tables.Operator.OPERATOR
import com.crm.corecrm.domain.db.tables.records.OperatorRecord
import com.crm.corecrm.domain.model.Operator
import jakarta.annotation.PostConstruct
import org.jooq.DSLContext
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UsernameNotFoundException
// import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Repository

@Repository
class OperatorRepository(dsl: DSLContext) : AbstractRepository(dsl) {
    @PostConstruct
    fun t() {
        val a = LoggerFactory.getLogger(OperatorRepository::class.java)
        println(db.selectFrom(OPERATOR).fetch())
    }

    fun findByLogin(login: String): Operator {
        val record = db.selectFrom(OPERATOR)
            .where(OPERATOR.LOGIN.eq(login))
            .fetchOne() ?: throw UsernameNotFoundException("Operator not found")

        return record.toModel()
    }

    fun save(operator: Operator) {
        // val lastId = dsl.select(OPERATOR.ID)
        //     .from(OPERATOR)
        //     .orderBy(OPERATOR.ID.desc())
        //     .limit(1)
        //     .fetchOneInto(Int::class.java)!!


        // operator.setId(lastId + 1)
        db
            .insertInto(OPERATOR, OPERATOR.LOGIN, OPERATOR.PASSWORD, OPERATOR.NAME)
            .values(operator.username, operator.password, operator.getName())
            .execute()

    }

    fun OperatorRecord.toModel(): Operator {
        return Operator(
            id = this.id,
            login = this.login,
            name = this.name,
            password = this.password
        )
    }

    fun Operator.toRecord(): OperatorRecord {
        return OperatorRecord(
            null,
            this.username,
            this.password,
            this.getName()
        )
    }
}