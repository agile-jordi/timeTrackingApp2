package com.agilogy.db.sql

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.PreparedStatement
import javax.sql.DataSource

typealias SqlParameter = context(PreparedStatement) (Int) -> Unit

enum class TransactionIsolationLevel(val value: Int) {
    None(Connection.TRANSACTION_NONE),
    ReadUncommitted(Connection.TRANSACTION_READ_UNCOMMITTED),
    ReadCommitted(Connection.TRANSACTION_READ_COMMITTED),
    RepeatableRead(Connection.TRANSACTION_REPEATABLE_READ),
    Serializable(Connection.TRANSACTION_SERIALIZABLE),
}


object Sql {

    @DslMarker
    annotation class SqlDsl

    context(Connection)
    private suspend fun <A> preparedStatement(sql: String, vararg params: SqlParameter, f: context(PreparedStatement)() -> A): A =
        // Context receivers bug: We should be able to use `useInContext`
        prepareStatement(sql).use {
            with(it) {
                setParameters(*params)
                // Context receivers bug: Should not need `this` argument
                f(this)
            }
        }


    context(DataSource)
    @SqlDsl suspend fun <A> sql(f: suspend context(Connection) () -> A): A = withContext(Dispatchers.IO) {
        connection.use {
            // Context receivers bug: Should not need `it` argument but a `with`
            f(it)
        }
    }

    context(DataSource)
    @SqlDsl suspend fun <A> sqlTransaction(isolationLevel: TransactionIsolationLevel, f: context(Connection) () -> A): A =
        withContext(Dispatchers.IO) {
            connection.useWith {
                autoCommit = false
                transactionIsolation = isolationLevel.value
                // Context receivers bug: Should not need `this` argument
                f(this).also { commit() }
            }
        }

    context(Connection)
    suspend fun <A> select(sql: String, vararg params: SqlParameter, reader: context(ResultSetView) () -> A): List<A> =
        preparedStatement(sql, *params) {
            executeQuery().useWith {
                val res = mutableListOf<A>()
                with(ResultSetView()) {
                    while (resultSet.next()) {
                        // Context receivers bug: Should not need `this` argument
                        res.add(reader(this@with))
                    }
                }
                res
            }
        }

    context(Connection)
    suspend fun <A> selectOne(sql: String, vararg params: SqlParameter, reader: context(ResultSetView) () -> A): A? =
        preparedStatement(sql, *params) {
            executeQuery().useInContext {
                with(ResultSetView()) {
                    if (resultSet.next()) {
                        // Context receivers bug: Should not need `this` argument
                        reader(this).also { if (resultSet.next()) throw IllegalStateException("More than one row found!") }
                    } else null
                }
            }
        }

    context(PreparedStatement)
    private fun setParameters(vararg params: SqlParameter) {
        params.forEachIndexed { pos, param -> param(this@PreparedStatement, pos + 1) }
    }

    context(Connection)
    suspend fun update(sql: String, vararg params: SqlParameter): Int = preparedStatement(sql, *params) { executeUpdate() }

    context(PreparedStatement)
    class BatchUpdate {
        fun add(vararg params: SqlParameter) {
            setParameters(*params)
            addBatch()
        }
    }

    context(Connection)
    suspend fun batchUpdate(sql: String, f: context(BatchUpdate)() -> Unit): List<Int> = preparedStatement(sql) {
        // Context receivers bug: Should not need `this` argument
        with(BatchUpdate()) { f(this) }
        executeBatch().toList()
    }
}