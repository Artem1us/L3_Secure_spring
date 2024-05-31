package ua.kpi.its.lab.security.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import ua.kpi.its.lab.security.dto.MagazineRequestDto
import ua.kpi.its.lab.security.dto.MagazineResponseDto

@Composable
fun SoftScreen(
    token: String,
    scope: CoroutineScope,
    client: HttpClient,
    snackbarHostState: SnackbarHostState
) {
    var magazine1s by remember { mutableStateOf<List<MagazineResponseDto>>(listOf()) }
    var loading by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }
    var selectedMagazine by remember { mutableStateOf<MagazineResponseDto?>(null) }

    LaunchedEffect(token) {
        loading = true
        delay(1000)
        magazine1s = withContext(Dispatchers.IO) {
            try {
                val response = client.get("http://localhost:8080/api/products") {
                    bearerAuth(token)
                }
                loading = false
                response.body()
            } catch (e: Exception) {
                val msg = e.toString()
                snackbarHostState.showSnackbar(msg, withDismissAction = true, duration = SnackbarDuration.Indefinite)
                magazine1s
            }
        }
    }

    if (loading) {
        LinearProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        )
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedMagazine = null
                    openDialog = true
                },
                content = {
                    Icon(Icons.Filled.Add, "add software product")
                }
            )
        }
    ) {
        if (magazine1s.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text("No software products to show", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
        } else {
            LazyColumn(
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant).fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(magazine1s) { magazine1 ->
                    SoftwareItem(
                        magazine1 = magazine1,
                        onEdit = {
                            selectedMagazine = magazine1
                            openDialog = true
                        },
                        onRemove = {
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    try {
                                        val response = client.delete("http://localhost:8080/api/products/${magazine1.id}") {
                                            bearerAuth(token)
                                        }
                                        require(response.status.isSuccess())
                                    } catch (e: Exception) {
                                        val msg = e.toString()
                                        snackbarHostState.showSnackbar(msg, withDismissAction = true, duration = SnackbarDuration.Indefinite)
                                    }
                                }

                                loading = true

                                magazine1s = withContext(Dispatchers.IO) {
                                    try {
                                        val response = client.get("http://localhost:8080/api/products") {
                                            bearerAuth(token)
                                        }
                                        loading = false
                                        response.body()
                                    } catch (e: Exception) {
                                        val msg = e.toString()
                                        snackbarHostState.showSnackbar(msg, withDismissAction = true, duration = SnackbarDuration.Indefinite)
                                        magazine1s
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }

        if (openDialog) {
            SoftwareDialog(
                magazine1 = selectedMagazine,
                token = token,
                scope = scope,
                client = client,
                onDismiss = {
                    openDialog = false
                },
                onError = {
                    scope.launch {
                        snackbarHostState.showSnackbar(it, withDismissAction = true, duration = SnackbarDuration.Indefinite)
                    }
                },
                onConfirm = {
                    openDialog = false
                    loading = true
                    scope.launch {
                        magazine1s = withContext(Dispatchers.IO) {
                            try {
                                val response = client.get("http://localhost:8080/api/products") {
                                    bearerAuth(token)
                                }
                                loading = false
                                response.body()
                            } catch (e: Exception) {
                                loading = false
                                magazine1s
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun SoftwareDialog(
    magazine1: MagazineResponseDto?,
    token: String,
    scope: CoroutineScope,
    client: HttpClient,
    onDismiss: () -> Unit,
    onError: (String) -> Unit,
    onConfirm: () -> Unit,
) {
    var name by remember { mutableStateOf(magazine1?.name ?: "") }
    var topic by remember { mutableStateOf(magazine1?.topic ?: "") }
    var language by remember { mutableStateOf(magazine1?.language ?: "") }
    var establishDate by remember { mutableStateOf(magazine1?.establishDate ?: "") }
    var issn by remember { mutableStateOf(magazine1?.issn?.toString() ?: "") }
    var price by remember { mutableStateOf(magazine1?.price ?: "") }
    var periodicity by remember { mutableStateOf(magazine1?.periodicity ?: false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.padding(16.dp).wrapContentSize()) {
            Column(
                modifier = Modifier.padding(16.dp, 8.dp).width(IntrinsicSize.Max).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (magazine1 == null) {
                    Text("Create Software Product")
                } else {
                    Text("Update Software Product")
                }

                HorizontalDivider()
                TextField(name, { name = it }, label = { Text("Name") })
                TextField(topic, { topic = it }, label = { Text("Topic") })
                TextField(language, { language = it }, label = { Text("Language") })
                TextField(establishDate, { establishDate = it }, label = { Text("Establish Date") })
                TextField(issn, { issn = it }, label = { Text("ISSN") })
                TextField(price, { price = it }, label = { Text("Price") })
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(periodicity, { periodicity = it })
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Periodicity")
                }

                HorizontalDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.fillMaxWidth(0.1f))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            scope.launch {
                                try {
                                    val request = MagazineRequestDto(
                                        name, topic, language, establishDate, issn.toString(), price, periodicity
                                    )
                                    val response = if (magazine1 == null) {
                                        client.post("http://localhost:8080/api/products") {
                                            bearerAuth(token)
                                            setBody(request)
                                            contentType(ContentType.Application.Json)
                                        }
                                    } else {
                                        client.put("http://localhost:8080/api/products/${magazine1.id}") {
                                            bearerAuth(token)
                                            setBody(request)
                                            contentType(ContentType.Application.Json)
                                        }
                                    }
                                    require(response.status.isSuccess())
                                    onConfirm()
                                } catch (e: Exception) {
                                    val msg = e.toString()
                                    onError(msg)
                                }
                            }
                        }
                    ) {
                        if (magazine1 == null) {
                            Text("Create")
                        } else {
                            Text("Update")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SoftwareItem(magazine1: MagazineResponseDto, onEdit: () -> Unit, onRemove: () -> Unit) {
    Card(shape = CardDefaults.elevatedShape, elevation = CardDefaults.elevatedCardElevation()) {
        ListItem(
            overlineContent = {
                Text(magazine1.name)
            },
            headlineContent = {
                Text(magazine1.language)
            },
            supportingContent = {
                Text("${magazine1.topic} - ${magazine1.issn} MB")
            },
            trailingContent = {
                Row(modifier = Modifier.padding(0.dp, 20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clip(CircleShape).clickable(onClick = onEdit)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clip(CircleShape).clickable(onClick = onRemove)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        )
    }
}