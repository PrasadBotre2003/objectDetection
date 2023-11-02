package com.example.instalens.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.instalens.R
import com.example.instalens.presentation.common.ImageButton
import com.example.instalens.presentation.home.components.CameraPermissionRequest
import com.example.instalens.presentation.home.components.CameraPreview
import com.example.instalens.presentation.home.components.ObjectCounter
import com.example.instalens.presentation.home.components.ThresholdLevelSlider
import com.example.instalens.presentation.utils.Dimens
import com.example.instalens.utils.Constants


@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val viewModel: HomeViewModel = viewModel()

    // Request Camera Permission
    CameraPermissionRequest()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Prepare Camera Controller
        val cameraController = viewModel.prepareCameraController(context)

        // Combined Column for Camera Preview & Bottom UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.gray_900)),
        ) {
            // Camera Preview Column
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
            ) {
                CameraPreview(
                    controller =  remember {
                        cameraController
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Bottom column with Capture-Image and Threshold Level Slider
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
                    .padding(top = Dimens.Padding8dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                ImageButton(
                    drawableResourceId = R.drawable.ic_capture,
                    contentDescriptionResourceId = R.string.capture_button_description,
                    modifier = Modifier
                        .size(Dimens.CaptureButtonSize)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            // Capture Photo
                            viewModel.capturePhoto(
                                context = context,
                                cameraController = cameraController,
                                viewModel::onPhotoTaken
                            )
                        }
                )

                // Threshold Level Slider
                val sliderValue = remember { mutableFloatStateOf(Constants.INITIAL_CONFIDENCE_SCORE) }
                ThresholdLevelSlider(sliderValue) { sliderValue ->
                    // TODO: Pass to VM then to ML model
                }
            }
        }

        // Column with rotate-camera and detected object count Composable (Overlapping UI)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f)
                .padding(top = Dimens.Padding32dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Rotate Camera Composable
                ImageButton(
                    drawableResourceId = R.drawable.ic_rotate_camera,
                    contentDescriptionResourceId = R.string.rotate_camera_button_description,
                    Modifier
                        .padding(
                            top = Dimens.Padding24dp,
                            start = Dimens.Padding16dp
                        )
                        .size(Dimens.RotateCameraButtonSize)
                        .clickable {
                            cameraController.cameraSelector =
                                viewModel.getSelectedCamera(cameraController)
                        }
                )

                // Detected Object Count Composable
                ObjectCounter(objectCount = 0)  // TODO: Pass from TFLite model
            }
        }
    }
}