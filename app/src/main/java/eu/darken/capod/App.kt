package eu.darken.capod

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import eu.darken.capod.common.coroutine.AppScope
import eu.darken.capod.common.debug.logging.LogCatLogger
import eu.darken.capod.common.debug.logging.Logging
import eu.darken.capod.common.debug.logging.asLog
import eu.darken.capod.common.debug.logging.log
import eu.darken.capod.common.debug.logging.logTag
import eu.darken.capod.common.flow.throttleLatest
import eu.darken.capod.main.ui.widget.WidgetManager
import eu.darken.capod.monitor.core.PodMonitor
import eu.darken.capod.monitor.core.devicesWithProfiles

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
open class App : Application() {

    @Inject lateinit var podMonitor: PodMonitor
    @Inject lateinit var widgetManager: WidgetManager
    @Inject @AppScope lateinit var appScope: CoroutineScope

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Logging.install(LogCatLogger())

        log(TAG) { "onCreate() done! ${Exception().asLog()}" }

        appScope.launch { widgetManager.refreshWidgets() }

        podMonitor.devicesWithProfiles()
            .distinctUntilChanged()
            .throttleLatest(1000)
            .onEach {
                log(TAG) { "Main device changed, refreshing widgets." }
                widgetManager.refreshWidgets()
            }
            .launchIn(appScope)
    }

    companion object {
        internal val TAG = logTag("CAP")
    }
}
