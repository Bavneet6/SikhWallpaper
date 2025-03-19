package com.sikhglories.sikhwallpaper

import android.app.WallpaperManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// Set the screen orientation to portrait only
		requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

		setContent {
			MaterialTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					WallpaperScreen()
				}
			}
		}
	}
}

@Composable
fun WallpaperScreen() {
	val context = LocalContext.current
	val scope = rememberCoroutineScope()
	var isLoading by remember { mutableStateOf(false) }

	Column(
		modifier = Modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		// Replace R.drawable.wallpaper with your actual drawable resource
		Image(
			painter = painterResource(id = R.drawable.wallpaper),
			contentDescription = "Wallpaper",
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f),
			contentScale = ContentScale.Crop
		)

		Spacer(modifier = Modifier.height(16.dp))

		Button(
			onClick = {
				scope.launch {
					isLoading = true
					setWallpaper(context)
					isLoading = false
				}
			},
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
			enabled = !isLoading
		) {
			if (isLoading) {
				CircularProgressIndicator(
					modifier = Modifier.size(24.dp),
					color = MaterialTheme.colorScheme.onPrimary
				)
			} else {
				Text("Set as Wallpaper")
			}
		}
	}
}

suspend fun setWallpaper(context: Context) {
	try {
		withContext(Dispatchers.IO) {
			// Get the drawable resource
			val drawable = context.resources.getDrawable(R.drawable.wallpaper, context.theme)

			// Convert drawable to bitmap
			val bitmap = (drawable as BitmapDrawable).bitmap

			// Get wallpaper manager and set wallpaper
			val wallpaperManager = WallpaperManager.getInstance(context)
			wallpaperManager.setBitmap(bitmap)
		}

		withContext(Dispatchers.Main) {
			Toast.makeText(context, "Wallpaper set successfully!", Toast.LENGTH_SHORT).show()
		}
	} catch (e: Exception) {
		withContext(Dispatchers.Main) {
			Toast.makeText(context, "Failed to set wallpaper: ${e.message}", Toast.LENGTH_SHORT).show()
		}
	}
}