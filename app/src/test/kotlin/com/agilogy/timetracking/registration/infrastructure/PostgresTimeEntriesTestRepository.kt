package com.agilogy.timetracking.registration.infrastructure

import com.agilogy.db.sql.Sql.select
import com.agilogy.db.sql.Sql.sql
import com.agilogy.timetracking.project.domain.ProjectName
import com.agilogy.timetracking.registration.domain.TimeEntry
import com.agilogy.timetracking.user.domain.UserName
import javax.sql.DataSource

context(DataSource)
class PostgresTimeEntriesTestRepository {

    suspend fun getAll(): List<TimeEntry> = sql {
        select("SELECT user_name, project_name, start, end FROM time_entries") {
            TimeEntry(UserName(string(1)!!), ProjectName(string(2)!!), timestamp(3)!!, timestamp(4)!!)
        }
    }
}