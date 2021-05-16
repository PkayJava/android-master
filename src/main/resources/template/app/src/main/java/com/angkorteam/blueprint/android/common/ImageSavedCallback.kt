package ${pkg}.common

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException

class ImageSavedCallback(
    val imageSaved: (outputFileResults: ImageCapture.OutputFileResults) -> Unit,
    val error: (exception: ImageCaptureException) -> Unit,
) : ImageCapture.OnImageSavedCallback {

    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
        this.imageSaved(outputFileResults)
    }

    override fun onError(exception: ImageCaptureException) {
        this.error(exception)
    }

}