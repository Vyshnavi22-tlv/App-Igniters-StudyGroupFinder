package com.example.studee

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf

class StudyGroupViewModel : ViewModel() {

    // This list updates UI automatically
    val groups = mutableStateListOf(
        "Math Warriors",
        "AI Researchers",
        "Physics Legends",
        "Competitive Programming Squad"
    )

    fun addGroup(name: String) {
        groups.add(name)
    }
}