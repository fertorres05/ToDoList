package com.example.todolist


    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.activity.enableEdgeToEdge
    import androidx.compose.foundation.background
    import androidx.compose.foundation.border
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.consumeWindowInsets
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Add
    import androidx.compose.material.icons.filled.Check
    import androidx.compose.material.icons.filled.Close
    import androidx.compose.material.icons.filled.Favorite
    import androidx.compose.material.icons.filled.Home
    import androidx.compose.material.icons.filled.Menu
    import androidx.compose.material.icons.filled.Person
    import androidx.compose.material3.Checkbox
    import androidx.compose.material3.DrawerValue
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.FabPosition
    import androidx.compose.material3.FloatingActionButton
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.ModalDrawerSheet
    import androidx.compose.material3.ModalNavigationDrawer
    import androidx.compose.material3.NavigationBar
    import androidx.compose.material3.NavigationBarItem
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.material3.TopAppBar
    import androidx.compose.material3.TopAppBarDefaults
    import androidx.compose.material3.rememberDrawerState
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableIntStateOf
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.rememberCoroutineScope
    import androidx.compose.runtime.saveable.rememberSaveable
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import com.example.todolist.ui.theme.ToDoListTheme
    import kotlinx.coroutines.launch

    class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContent {
                ToDoListTheme {
                    var zurdorium = false
                    val scope = rememberCoroutineScope()
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        gesturesEnabled = true,
                        drawerContent = {
                            ModalDrawerSheet {
                                MyNavigationDrawer { scope.launch { drawerState.close() } }
                            }
                        }
                    ) {
                        Scaffold(
                            topBar = { MyTopAppBar { scope.launch { drawerState.open() } } },
                            content = { innerPadding ->
                                MyContent(innerPadding)
                            },
                            bottomBar = { MyBottomNavigation() },
                            floatingActionButtonPosition =
                            if (zurdorium) FabPosition.Start
                            else FabPosition.End,
                            floatingActionButton = { MyFAB() }
                        )
                    }
                }
            }
        }

        @Composable
        fun MyNavigationDrawer(onCloseDrawer: () -> Unit) {
            Column(modifier = Modifier.padding(8.dp)) {
                repeat(5) {
                    Text(
                        text = "Opción ${it + 1}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onCloseDrawer() }
                    )
                }
            }
        }

        @Composable
        private fun MyFAB() {
            FloatingActionButton(
                onClick = { /* fab click handler */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "FAB Check"
                )
            }
        }

        @Composable
        private fun MyBottomNavigation() {
            var index by rememberSaveable { mutableIntStateOf(0) }
            NavigationBar(
                containerColor = Color.Red,
                contentColor = Color.White
            ) {
                NavigationBarItem(
                    selected = index == 0,
                    onClick = { index = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home Icon"
                        )
                    },
                    label = { Text("TAREAS") }
                )
                NavigationBarItem(
                    selected = index == 1,
                    onClick = { index = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Fav Icon"
                        )
                    },
                    label = { Text("COMPLETADAS") }
                )
            }
        }

        @Composable
        fun MyContent(innerPadding: PaddingValues) {
            LazyColumn(
                modifier = Modifier.consumeWindowInsets(innerPadding),
                contentPadding = innerPadding
            ) {
                items(10) { count ->
                    var checked by remember { mutableStateOf(false) }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(16.dp)) // Bordes redondeados
                            .background(Color.Gray)
                            .border(1.dp, Color.White, RoundedCornerShape(16.dp))
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { checked = it },
                            modifier= Modifier
                                .size(40.dp)
                                .padding(10.dp)
                        )
                        Text(
                            text = "Tarea $count",
                            color = Color.White,
                            modifier = Modifier
                                
                        )
                    }
                }
            }
        }

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        private fun MyTopAppBar(onMenuClick: () -> Unit) {
            TopAppBar(
                title = { Text("TO DO LIST") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.LightGray),
                navigationIcon = {
                    IconButton(onClick = { onMenuClick() }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Open Drawer")
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Filled.Add, contentDescription = "Add") }
                    IconButton(onClick = {}) { Icon(Icons.Filled.Close, contentDescription = "Close") }
                }
            )
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ToDoListTheme {
            Greeting("Android")
        }
    }
