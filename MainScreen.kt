package com.lpu.collegeinformationapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lpu.collegeinformationapp.ui.model.CollegeEvent
import com.lpu.collegeinformationapp.ui.model.Course
import com.lpu.collegeinformationapp.ui.model.Faculty
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    onHomeSelected = {
                        scope.launch { drawerState.close() }
                        viewModel.selectTab(0)
                    },
                    onAboutSelected = { scope.launch { drawerState.close() } },
                    onContactSelected = { scope.launch { drawerState.close() } },
                    onSettingsSelected = { scope.launch { drawerState.close() } }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("College Info") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            CollegeInfoTabsScreen(
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

@Composable
fun CollegeInfoTabsScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    val courses = remember { mutableStateListOf(*viewModel.courses.toTypedArray()) }
    val faculty = remember { mutableStateListOf(*viewModel.faculty.toTypedArray()) }
    val events = remember { mutableStateListOf(*viewModel.events.toTypedArray()) }

    var showCourseDialog by remember { mutableStateOf(false) }
    var showFacultyDialog by remember { mutableStateOf(false) }
    var showEventDialog by remember { mutableStateOf(false) }

    var editingCourse by remember { mutableStateOf<Course?>(null) }
    var editingFaculty by remember { mutableStateOf<Faculty?>(null) }
    var editingEvent by remember { mutableStateOf<CollegeEvent?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        val tabs = listOf(
            TabItem("Courses", Icons.Default.School),
            TabItem("Faculty", Icons.Default.People),
            TabItem("Events", Icons.Default.Event)
        )

        TabRow(selectedTabIndex = viewModel.selectedTabIndex) {
            tabs.forEachIndexed { index, tab ->
                Tab(selected = viewModel.selectedTabIndex == index, onClick = { viewModel.selectTab(index) }) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(12.dp)) {
                        Icon(tab.icon, contentDescription = tab.title)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(tab.title)
                    }
                }
            }
        }

        when (viewModel.selectedTabIndex) {
            0 -> CoursesList(
                courses = courses,
                onAdd = {
                    editingCourse = null
                    showCourseDialog = true
                },
                onEdit = { course ->
                    editingCourse = course
                    showCourseDialog = true
                },
                onDelete = { course ->
                    courses.remove(course)
                    scope.launch { snackbarHostState.showSnackbar("Item deleted") }
                }
            )
            1 -> FacultyList(
                faculty = faculty,
                onAdd = {
                    editingFaculty = null
                    showFacultyDialog = true
                },
                onEdit = { item ->
                    editingFaculty = item
                    showFacultyDialog = true
                },
                onDelete = { item ->
                    faculty.remove(item)
                    scope.launch { snackbarHostState.showSnackbar("Item deleted") }
                }
            )
            2 -> EventsList(
                events = events,
                onAdd = {
                    editingEvent = null
                    showEventDialog = true
                },
                onEdit = { item ->
                    editingEvent = item
                    showEventDialog = true
                },
                onDelete = { item ->
                    events.remove(item)
                    scope.launch { snackbarHostState.showSnackbar("Item deleted") }
                }
            )
        }
    }

    if (showCourseDialog) {
        AddCourseDialog(
            initialCourse = editingCourse,
            onDismissRequest = {
                showCourseDialog = false
                editingCourse = null
            },
            onConfirm = { course ->
                if (editingCourse != null) {
                    val index = courses.indexOfFirst { it.id == course.id }
                    if (index >= 0) courses[index] = course
                } else {
                    courses.add(course)
                    scope.launch { snackbarHostState.showSnackbar("Course added successfully") }
                }
                showCourseDialog = false
                editingCourse = null
            }
        )
    }

    if (showFacultyDialog) {
        AddFacultyDialog(
            initialFaculty = editingFaculty,
            onDismissRequest = {
                showFacultyDialog = false
                editingFaculty = null
            },
            onConfirm = { facultyItem ->
                if (editingFaculty != null) {
                    val index = faculty.indexOfFirst { it.id == facultyItem.id }
                    if (index >= 0) faculty[index] = facultyItem
                } else {
                    faculty.add(facultyItem)
                    scope.launch { snackbarHostState.showSnackbar("Faculty added successfully") }
                }
                showFacultyDialog = false
                editingFaculty = null
            }
        )
    }

    if (showEventDialog) {
        AddEventDialog(
            initialEvent = editingEvent,
            onDismissRequest = {
                showEventDialog = false
                editingEvent = null
            },
            onConfirm = { eventItem ->
                if (editingEvent != null) {
                    val index = events.indexOfFirst { it.id == eventItem.id }
                    if (index >= 0) events[index] = eventItem
                } else {
                    events.add(eventItem)
                    scope.launch { snackbarHostState.showSnackbar("Event added successfully") }
                }
                showEventDialog = false
                editingEvent = null
            }
        )
    }
}

@Composable
fun CoursesList(
    courses: List<Course>,
    onAdd: () -> Unit,
    onEdit: (Course) -> Unit,
    onDelete: (Course) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ListHeader(title = "Courses", onAdd = onAdd, addContentDescription = "Add course")

        if (courses.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No courses added yet. Tap + to add one.")
            }
            return
        }

        LazyColumn(contentPadding = PaddingValues(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(courses, key = { it.id }) { course ->
                InfoCard(
                    title = course.name,
                    subtitle = "${course.duration} • ${course.eligibility}",
                    description = course.description,
                    onEdit = { onEdit(course) },
                    onDelete = { onDelete(course) }
                )
            }
        }
    }
}

@Composable
fun FacultyList(
    faculty: List<Faculty>,
    onAdd: () -> Unit,
    onEdit: (Faculty) -> Unit,
    onDelete: (Faculty) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ListHeader(title = "Faculty", onAdd = onAdd, addContentDescription = "Add faculty")

        if (faculty.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No faculty added yet. Tap + to add one.")
            }
            return
        }

        LazyColumn(contentPadding = PaddingValues(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(faculty, key = { it.id }) { item ->
                InfoCard(
                    title = item.name,
                    subtitle = "${item.designation}, ${item.department}",
                    description = "${item.email}\n${item.phone}",
                    onEdit = { onEdit(item) },
                    onDelete = { onDelete(item) }
                )
            }
        }
    }
}

@Composable
fun EventsList(
    events: List<CollegeEvent>,
    onAdd: () -> Unit,
    onEdit: (CollegeEvent) -> Unit,
    onDelete: (CollegeEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ListHeader(title = "Events", onAdd = onAdd, addContentDescription = "Add event")

        if (events.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No events added yet. Tap + to add one.")
            }
            return
        }

        LazyColumn(contentPadding = PaddingValues(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(events, key = { it.id }) { item ->
                InfoCard(
                    title = item.name,
                    subtitle = "${item.date} • ${item.venue}",
                    description = item.description,
                    onEdit = { onEdit(item) },
                    onDelete = { onDelete(item) }
                )
            }
        }
    }
}

@Composable
fun ListHeader(title: String, onAdd: () -> Unit, addContentDescription: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        IconButton(onClick = onAdd) {
            Icon(Icons.Default.Add, contentDescription = addContentDescription)
        }
    }
}

@Composable
fun AddCourseDialog(
    initialCourse: Course? = null,
    onDismissRequest: () -> Unit,
    onConfirm: (Course) -> Unit
) {
    var name by rememberSaveable { mutableStateOf(initialCourse?.name.orEmpty()) }
    var duration by rememberSaveable { mutableStateOf(initialCourse?.duration.orEmpty()) }
    var eligibility by rememberSaveable { mutableStateOf(initialCourse?.eligibility.orEmpty()) }
    var description by rememberSaveable { mutableStateOf(initialCourse?.description.orEmpty()) }

    LaunchedEffect(initialCourse) {
        name = initialCourse?.name.orEmpty()
        duration = initialCourse?.duration.orEmpty()
        eligibility = initialCourse?.eligibility.orEmpty()
        description = initialCourse?.description.orEmpty()
    }

    val canSubmit = name.isNotBlank() && duration.isNotBlank() && eligibility.isNotBlank() && description.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(if (initialCourse == null) "Add Course" else "Edit Course") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, singleLine = true)
                OutlinedTextField(value = duration, onValueChange = { duration = it }, label = { Text("Duration") }, singleLine = true)
                OutlinedTextField(value = eligibility, onValueChange = { eligibility = it }, label = { Text("Eligibility") }, singleLine = true)
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (canSubmit) {
                        onConfirm(
                            Course(
                                id = initialCourse?.id ?: UUID.randomUUID().toString(),
                                name = name.trim(),
                                duration = duration.trim(),
                                eligibility = eligibility.trim(),
                                description = description.trim()
                            )
                        )
                        name = ""
                        duration = ""
                        eligibility = ""
                        description = ""
                    }
                },
                enabled = canSubmit
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddFacultyDialog(
    initialFaculty: Faculty? = null,
    onDismissRequest: () -> Unit,
    onConfirm: (Faculty) -> Unit
) {
    var name by rememberSaveable { mutableStateOf(initialFaculty?.name.orEmpty()) }
    var department by rememberSaveable { mutableStateOf(initialFaculty?.department.orEmpty()) }
    var designation by rememberSaveable { mutableStateOf(initialFaculty?.designation.orEmpty()) }
    var email by rememberSaveable { mutableStateOf(initialFaculty?.email.orEmpty()) }
    var phone by rememberSaveable { mutableStateOf(initialFaculty?.phone.orEmpty()) }

    LaunchedEffect(initialFaculty) {
        name = initialFaculty?.name.orEmpty()
        department = initialFaculty?.department.orEmpty()
        designation = initialFaculty?.designation.orEmpty()
        email = initialFaculty?.email.orEmpty()
        phone = initialFaculty?.phone.orEmpty()
    }

    val canSubmit = name.isNotBlank() && department.isNotBlank() && designation.isNotBlank() && email.isNotBlank() && phone.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(if (initialFaculty == null) "Add Faculty" else "Edit Faculty") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, singleLine = true)
                OutlinedTextField(value = department, onValueChange = { department = it }, label = { Text("Department") }, singleLine = true)
                OutlinedTextField(value = designation, onValueChange = { designation = it }, label = { Text("Designation") }, singleLine = true)
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, singleLine = true)
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") }, singleLine = true)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (canSubmit) {
                        onConfirm(
                            Faculty(
                                id = initialFaculty?.id ?: UUID.randomUUID().toString(),
                                name = name.trim(),
                                department = department.trim(),
                                designation = designation.trim(),
                                email = email.trim(),
                                phone = phone.trim()
                            )
                        )
                        name = ""
                        department = ""
                        designation = ""
                        email = ""
                        phone = ""
                    }
                },
                enabled = canSubmit
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddEventDialog(
    initialEvent: CollegeEvent? = null,
    onDismissRequest: () -> Unit,
    onConfirm: (CollegeEvent) -> Unit
) {
    var name by rememberSaveable { mutableStateOf(initialEvent?.name.orEmpty()) }
    var date by rememberSaveable { mutableStateOf(initialEvent?.date.orEmpty()) }
    var venue by rememberSaveable { mutableStateOf(initialEvent?.venue.orEmpty()) }
    var description by rememberSaveable { mutableStateOf(initialEvent?.description.orEmpty()) }

    LaunchedEffect(initialEvent) {
        name = initialEvent?.name.orEmpty()
        date = initialEvent?.date.orEmpty()
        venue = initialEvent?.venue.orEmpty()
        description = initialEvent?.description.orEmpty()
    }

    val canSubmit = name.isNotBlank() && date.isNotBlank() && venue.isNotBlank() && description.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(if (initialEvent == null) "Add Event" else "Edit Event") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, singleLine = true)
                OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date") }, singleLine = true)
                OutlinedTextField(value = venue, onValueChange = { venue = it }, label = { Text("Venue") }, singleLine = true)
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (canSubmit) {
                        onConfirm(
                            CollegeEvent(
                                id = initialEvent?.id ?: UUID.randomUUID().toString(),
                                name = name.trim(),
                                date = date.trim(),
                                venue = venue.trim(),
                                description = description.trim()
                            )
                        )
                        name = ""
                        date = ""
                        venue = ""
                        description = ""
                    }
                },
                enabled = canSubmit
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}
