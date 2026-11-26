import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.example.shared.App
import com.example.shared.di.initKoin
import com.example.shared.di.initLogger
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initLogger()
    initKoin()

    ComposeViewport(document.body!!) {
        App()
    }
}
