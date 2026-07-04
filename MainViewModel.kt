package com.lpu.collegeinformationapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lpu.collegeinformationapp.ui.model.CollegeEvent
import com.lpu.collegeinformationapp.ui.model.Course
import com.lpu.collegeinformationapp.ui.model.Faculty

class MainViewModel : ViewModel() {
    var selectedTabIndex by mutableIntStateOf(0)
        private set

    fun selectTab(index: Int) {
        selectedTabIndex = index
    }

    // Mock data
    val courses = listOf(
        Course(
            id = "c1",
            name = "B.Tech Computer Science",
            duration = "4 years",
            eligibility = "10+2 (PCM)",
            description = "Program focusing on software development, algorithms, and systems.",
        ),
        Course(
            id = "c2",
            name = "MBA",
            duration = "2 years",
            eligibility = "Graduation",
            description = "Business and management program with specializations."
        ),
        Course(
            id = "c3",
            name = "B.Sc Physics",
            duration = "3 years",
            eligibility = "10+2 (Science)",
            description = "Undergraduate program in Physics with lab work."
        )
    )

    val faculty = listOf(
        Faculty(
            id = "f1",
            name = "Dr. Asha Singh",
            department = "Computer Science",
            designation = "Professor",
            email = "asha.singh@college.edu",
            phone = "+91-9876543210"
        ),
        Faculty(
            id = "f2",
            name = "Prof. Raj Kumar",
            department = "Management",
            designation = "Associate Professor",
            email = "raj.kumar@college.edu",
            phone = "+91-9123456780"
        ),
        Faculty(
            id = "f3",
            name = "Dr. Meera Patel",
            department = "Physics",
            designation = "Assistant Professor",
            email = "meera.patel@college.edu",
            phone = "+91-9988776655"
        )
    )

    val events = listOf(
        CollegeEvent(
            id = "e1",
            name = "Freshers Welcome",
            date = "2026-08-15",
            venue = "Main Auditorium",
            description = "Welcome event for new students with cultural programs."
        ),
        CollegeEvent(
            id = "e2",
            name = "Tech Symposium",
            date = "2026-09-20",
            venue = "Block B Seminar Hall",
            description = "Technical talks and poster presentations by students and faculty."
        )
    )
}
