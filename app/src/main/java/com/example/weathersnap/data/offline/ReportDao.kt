package com.example.weathersnap.data.offline

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReport(report: ReportEntity): Long

    @Query("SELECT * FROM saved_reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<ReportEntity>>
}