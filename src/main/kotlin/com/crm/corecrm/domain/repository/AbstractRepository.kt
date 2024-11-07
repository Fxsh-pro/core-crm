package com.crm.corecrm.domain.repository

import org.jooq.DSLContext

abstract class AbstractRepository (protected val db: DSLContext)