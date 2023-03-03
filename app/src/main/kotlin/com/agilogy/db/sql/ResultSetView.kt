package com.agilogy.db.sql

import java.io.InputStream
import java.io.Reader
import java.math.BigDecimal
import java.sql.ResultSet
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

context(ResultSet)
class ResultSetView {

    private fun <A> A.orNull(): A? = if (wasNull()) null else this

    fun string(columnIndex: Int): String? = getString(columnIndex)

    fun boolean(columnIndex: Int): Boolean? = getBoolean(columnIndex).orNull()

    fun byte(columnIndex: Int): Byte? = getByte(columnIndex).orNull()

    fun short(columnIndex: Int): Short? = getShort(columnIndex).orNull()

    fun int(columnIndex: Int): Int? = getInt(columnIndex).orNull()

    fun long(columnIndex: Int): Long? = getLong(columnIndex).orNull()

    fun float(columnIndex: Int): Float? = getFloat(columnIndex).orNull()

    fun double(columnIndex: Int): Double? = getDouble(columnIndex).orNull()

    fun bigDecimal(columnIndex: Int): BigDecimal? = getBigDecimal(columnIndex)

    fun bytes(columnIndex: Int): ByteArray? = getBytes(columnIndex)

    fun date(columnIndex: Int): LocalDate? = getDate(columnIndex)?.toLocalDate()

    fun time(columnIndex: Int): LocalTime? = getTime(columnIndex)?.toLocalTime()

    fun timestamp(columnIndex: Int): Instant? = getTimestamp(columnIndex)?.toInstant()

    fun characterStream(columnIndex: Int): Reader? = getCharacterStream(columnIndex)

    fun binaryStream(columnIndex: Int): InputStream? = getBinaryStream(columnIndex)
}