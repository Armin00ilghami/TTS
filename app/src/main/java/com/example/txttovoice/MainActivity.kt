package com.example.txttovoice

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.txttovoice.ui.theme.TxtToVoiceTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TxtToVoiceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TextToSpeechScreen()
                }
            }
        }
    }
}


val sentences = listOf(
    "Kotlin is a programming language for the Java Virtual Machine (JVM) and can, therefore, be used anywhere Java is used today (which is virtually anywhere).",
    "This includes server, client, web, and Android development.",
    "It’s developed by JetBrains who are currently working to bring Kotlin to embedded systems and iOS as well",
    "potentially making it a one-stop language for all application areas.",
    "Thanks for watching ❤"
)

@Composable
fun TextToSpeechScreen() {
    var isSpeaking by remember { mutableStateOf(false) }
    val tts = rememberTextToSpeech()

    Column(modifier = Modifier.padding(16.dp)) {
        isSpeaking = false
        for (sentence in sentences) {
            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                if (tts.value?.isSpeaking == true) {
                    tts.value?.stop()
                    isSpeaking = false
                } else {
                    tts.value?.speak(
                        sentence, TextToSpeech.QUEUE_FLUSH, null, ""
                    )
                    isSpeaking = true
                }
            }) {
                Text(sentence)
            }
        }

    }
}

@Composable
fun rememberTextToSpeech(): MutableState<TextToSpeech?> {
    val context = LocalContext.current
    val tts = remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(context) {
        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.value?.language = Locale.UK
            }
        }
        tts.value = textToSpeech

        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }
    return tts
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TxtToVoiceTheme {
        TextToSpeechScreen()
    }
}