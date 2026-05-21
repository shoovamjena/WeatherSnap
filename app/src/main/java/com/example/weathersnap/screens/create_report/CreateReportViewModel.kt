package com.example.weathersnap.screens.create_report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathersnap.data.repo.ReportRepository
import com.example.weathersnap.screens.saved_reports.SavedReport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : ViewModel() {

    fun saveReport(report: SavedReport, onSaveComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertReport(report)
            withContext(Dispatchers.Main) {
                onSaveComplete()
            }
        }
    }
}