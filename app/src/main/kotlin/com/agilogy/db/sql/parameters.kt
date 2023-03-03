package com.agilogy.db.sql

import java.io.InputStream
import java.io.Reader
import java.math.BigDecimal
import java.sql.Blob
import java.sql.Clob
import java.sql.NClob
import java.sql.Ref
import java.sql.RowId
import java.sql.SQLXML
import java.sql.Timestamp
import java.sql.Types
import java.time.Instant

val Boolean?.param: SqlParameter get() = { pos -> this?.let { setBoolean(pos, it) } ?: setNull(pos, Types.BOOLEAN) }
val Byte?.param: SqlParameter get() = { pos -> this?.let { setByte(pos, it) } ?: setNull(pos, Types.TINYINT) }
val Short?.param: SqlParameter get() = { pos -> this?.let { setShort(pos, it) } ?: setNull(pos, Types.SMALLINT) }
val Int?.param: SqlParameter get() = { pos -> this?.let { setInt(pos, it) } ?: setNull(pos, Types.INTEGER) }
val Long?.param: SqlParameter get() = { pos -> this?.let { setLong(pos, it) } ?: setNull(pos, Types.BIGINT) }
val Float?.param: SqlParameter get() = { pos -> this?.let { setFloat(pos, it) } ?: setNull(pos, Types.FLOAT) }
val Double?.param: SqlParameter get() = { pos -> this?.let { setDouble(pos, it) } ?: setNull(pos, Types.DOUBLE) }

val BigDecimal?.param: SqlParameter get() = { pos -> setBigDecimal(pos, this) }
val String?.param: SqlParameter get() = { pos -> setString(pos, this) }
val ByteArray?.param: SqlParameter get() = { pos -> setBytes(pos, this) }
val Instant?.param: SqlParameter get() = { pos -> setTimestamp(pos, this?.let { Timestamp(it.toEpochMilli()) }) }
val Reader?.param: SqlParameter get() = { pos -> setCharacterStream(pos, this) }
val InputStream?.param: SqlParameter get() = { pos -> setBinaryStream(pos, this) }
val Ref?.param: SqlParameter get() = { pos -> setRef(pos, this) }
val RowId?.param: SqlParameter get() = { pos -> setRowId(pos, this) }
val String?.paramNSString: SqlParameter get() = { pos -> setNString(pos, this) }
val Reader?.paramNsString: SqlParameter get() = { pos -> setNCharacterStream(pos, this) }
val Blob?.param: SqlParameter get() = { pos -> setBlob(pos, this) }
val Clob?.param: SqlParameter get() = { pos -> setClob(pos, this) }
val NClob?.param: SqlParameter get() = { pos -> setNClob(pos, this) }
val SQLXML?.param: SqlParameter get() = { pos -> setSQLXML(pos, this) }