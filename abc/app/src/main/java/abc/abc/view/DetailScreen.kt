package abc.abc.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import abc.abc.R
import abc.abc.common.loadPicture
import abc.abc.entity.HelloEntity
import abc.abc.theme.BlueprintMasterTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
fun DetailScreen(
    entityId: Int?,
    model: DetailScreenModel,
) {
    if (entityId == null) {
        TODO("Show Invalid Detail")
    } else {
        val onLoad = model.onLoad.value
        if (!onLoad) {
            model.onLoad.value = true
            model.loadDetail(entityId = entityId)
        }

        val loading = model.loading.value

        val hello = model.hello.value

        val scaffoldState = rememberScaffoldState()

        BlueprintMasterTheme {
            Scaffold(
                scaffoldState = scaffoldState,
                snackbarHost = {
                    scaffoldState.snackbarHostState
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (loading && hello == null) {
                        Text(text = "Loading")
                    } else if (!loading && hello == null && onLoad) {
                        TODO("Show Invalid Detail")
                    } else {
                        hello?.let { DetailView(entity = it) }
                    }
                }
            }
        }
    }
}

@ExperimentalCoroutinesApi
@Composable
fun DetailView(entity: HelloEntity) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            val image = loadPicture(
                url = entity.avatar,
                defaultImage = R.drawable.dummy_image
            ).value
            image?.let { img ->
                Card(modifier = Modifier.padding(10.dp)) {
                    Image(
                        bitmap = img.asImageBitmap(),
                        contentDescription = "Featured Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .background(color = Color.LightGray),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            Card(
                modifier = Modifier.padding(10.dp), elevation = 5.dp,
                backgroundColor = Color(0xFFe0f7fa)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Name",
                        style = MaterialTheme.typography.caption
                    )
                    Text(
                        text = entity.name,
                        style = MaterialTheme.typography.h6
                    )
                }
            }
            Card(
                modifier = Modifier.padding(10.dp), elevation = 5.dp,
                backgroundColor = Color(0xFFe0f7fa)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Date Of Birth",
                        style = MaterialTheme.typography.caption
                    )
                    Text(
                        text = entity.dateOfBirth,
                        style = MaterialTheme.typography.subtitle2
                    )
                }
            }
            Card(
                modifier = Modifier.padding(10.dp), elevation = 5.dp,
                backgroundColor = Color(0xFFe0f7fa),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.caption
                    )
                    Text(
                        text = entity.description,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
            Card(
                modifier = Modifier.padding(10.dp), elevation = 5.dp,
                backgroundColor = Color(0xFFe8f5e9),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = entity.phrase,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}
