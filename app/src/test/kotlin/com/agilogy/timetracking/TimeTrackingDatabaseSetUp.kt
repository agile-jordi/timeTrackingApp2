package com.agilogy.timetracking

import com.agilogy.db.sql.Sql.sql
import com.agilogy.db.sql.Sql.update
import javax.sql.DataSource

object TimeTrackingDatabaseSetUp {
    context(DataSource)
    private suspend fun createDatabase() = sql{ update("CREATE DATABASE test") }

    context(DataSource)
            private suspend fun setDatabase() = sql{ update("CREATE DATABASE test") }

    context(DataSource)
            suspend fun createTimeEntriesTable() = sql { update("""
        CREATE TABLE IF NOT EXISTS time_entries(
            id SERIAL,
            user_name TEXT NOT NULL,
            project_name TEXT NOT NULL,
            start TIMESTAMPTZ NOT NULL,
            "end" TIMESTAMPTZ NOT NULL,
            PRIMARY KEY (id)
        )
        """.trimIndent()) }

    context(DataSource)
    private suspend fun dropDatabase() = sql{ update("DROP DATABASE IF EXISTS test") }

    context(DataSource)
    suspend fun setUp() {
        dropDatabase()
        createDatabase()
    }
}