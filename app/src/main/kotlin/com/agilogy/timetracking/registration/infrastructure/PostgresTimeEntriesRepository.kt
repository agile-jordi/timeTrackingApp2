package com.agilogy.timetracking.registration.infrastructure

import com.agilogy.db.sql.Sql.batchUpdate
import com.agilogy.db.sql.Sql.sql
import com.agilogy.db.sql.param
import com.agilogy.fp.void
import com.agilogy.timetracking.project.domain.ProjectName
import com.agilogy.timetracking.registration.domain.TimeEntriesRepository
import com.agilogy.timetracking.registration.domain.TimeEntry
import com.agilogy.timetracking.user.domain.UserName
import javax.sql.DataSource

context(DataSource)
class PostgresTimeEntriesRepository : TimeEntriesRepository {
    private val UserName.param get() = value.param
    private val ProjectName.param get() = value.param

    override suspend fun save(timeEntries: Iterable<TimeEntry>) = sql {
        batchUpdate("""INSERT INTO time_entries(user_name, project_name, start, "end") VALUES (?, ?, ?, ?)""") {
            timeEntries.forEach {
                add(it.userName.param, it.projectName.param, it.start.param, it.end.param)
            }
        }.void
    }
}