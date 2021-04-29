package ${pkg}.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import ${pkg}.R
import ${pkg}.common.TAG
import ${pkg}.common.loadPicture
import ${pkg}.theme.BlueprintMasterTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun ListScreen(
    controller: NavHostController,
    model: ListScreenModel,
) {
    Log.d(TAG, "HelloListScreen: $model")

    val hellos = model.hellos.value

    val loading = model.loading.value

    val scaffoldState = rememberScaffoldState()

    BlueprintMasterTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = "Quick App") })
            },
            scaffoldState = scaffoldState,
            snackbarHost = {
                scaffoldState.snackbarHostState
            },
        ) {
            Box(modifier = Modifier.background(color = MaterialTheme.colors.surface)) {
                if (loading && hellos.isEmpty()) {
                    Text(text = "Loading")
                } else if (hellos.isEmpty()) {
                    Text(text = "Nothing")
                } else {
                    LazyColumn {
                        itemsIndexed(items = hellos) { index, hello ->

                            if (index + 1 == hellos.size) {
                                if (!loading) {
                                    hello.id?.let { it1 -> model.loadMore(lastId = it1) }
                                        ?: run {
                                            Text(text = "Nothing")
                                        }
                                }
                            }

                            Card(
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        val route = "/detail/${hello.id}"
                                        controller.navigate(route = route)
                                    },
                                elevation = 5.dp,
                            ) {
                                ListItem(
                                    icon = {
                                        val image = loadPicture(
                                            url = hello.avatar,
                                            defaultImage = R.drawable.dummy_image
                                        ).value
                                        image?.let { img ->
                                            Image(
                                                bitmap = img.asImageBitmap(),
                                                contentDescription = "Featured Image",
                                                modifier = Modifier
                                                    .size(80.dp)
                                                    .clip(shape = RoundedCornerShape(8.dp))
                                                    .background(color = Color.LightGray),
                                                contentScale = ContentScale.Crop,
                                            )
                                        }
                                    },
                                    text = { Text(text = hello.name) },
                                    secondaryText = { Text(text = hello.description) },
                                    modifier = Modifier.background(
                                        color = if (index % 2 == 0) Color(0xFFe0f7fa) else Color(
                                            0xffe3f2fd
                                        )
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

