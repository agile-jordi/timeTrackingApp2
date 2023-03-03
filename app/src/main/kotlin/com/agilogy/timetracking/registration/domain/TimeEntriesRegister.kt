package com.agilogy.timetracking.registration.domain

context(TimeEntriesRepository)
class TimeEntriesRegister {
    suspend fun registerEntries(timeEntries: Iterable<TimeEntry>): Unit = save(timeEntries)
}