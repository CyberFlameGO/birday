package com.minar.birday.persistence

import androidx.room.*
import java.time.LocalDate

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(event: Event)

    @Update
    fun updateEvent(event: Event)

    @Delete
    fun deleteEvent(event: Event)

    @Query("SELECT * FROM Event WHERE name == :name")
    fun getEventByName(name: String): List<Event>

    @Query("SELECT * FROM Event")
    fun getEvents(): List<Event>

    @Query("SELECT *, CASE WHEN (strftime('%m', 'now') > strftime('%m', originalDate) OR (strftime('%m', 'now') = strftime('%m', originalDate) AND strftime('%d', 'now') > strftime('%d', originalDate))) THEN date(strftime('%Y', 'now') || '-' || strftime('%m', originalDate) || '-' || strftime('%d', originalDate), '+1 year') ELSE date(strftime('%Y', 'now') || '-' || strftime('%m', originalDate) || '-' || strftime('%d', originalDate)) END AS nextDate FROM Event ORDER BY nextDate, originalDate")
    fun getOrderedEvents(): List<EventResult>

    @Query("SELECT *, CASE WHEN (strftime('%m', 'now') > strftime('%m', originalDate) OR (strftime('%m', 'now') = strftime('%m', originalDate) AND strftime('%d', 'now') > strftime('%d', originalDate))) THEN date(strftime('%Y', 'now') || '-' || strftime('%m', originalDate) || '-' || strftime('%d', originalDate),'+1 year') ELSE date(strftime('%Y', 'now') || '-' || strftime('%m', originalDate) || '-' || strftime('%d', originalDate)) END AS nextDate FROM Event WHERE nextDate <> :date ORDER BY nextDate, originalDate")
    fun getOrderedEventsExceptNext(date: LocalDate): List<EventResult>

    @Query("SELECT *, CASE WHEN (strftime('%m', 'now') > strftime('%m', originalDate) OR (strftime('%m', 'now') = strftime('%m', originalDate) AND strftime('%d', 'now') > strftime('%d', originalDate))) THEN date(strftime('%Y', 'now') || '-' || strftime('%m', originalDate) || '-' || strftime('%d', originalDate), '+1 year') ELSE date(strftime('%Y', 'now') || '-' || strftime('%m', originalDate) || '-' || strftime('%d', originalDate)) END AS nextDate FROM Event WHERE strftime('%Y', nextDate)-strftime('%Y', originalDate) = :age AND type = :type ORDER BY nextDate, originalDate;")
    fun getSpecialAgeEvents(age: Int, type: String): List<Event>
}