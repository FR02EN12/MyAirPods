package eu.darken.capod.reaction.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.capod.common.coroutine.DispatcherProvider
import eu.darken.capod.common.debug.logging.logTag
import eu.darken.capod.common.uix.ViewModel4
import eu.darken.capod.main.core.GeneralSettings
import eu.darken.capod.main.core.MonitorMode
import eu.darken.capod.reaction.core.ReactionSettings
import eu.darken.capod.reaction.core.autoconnect.AutoConnectCondition
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import eu.darken.capod.common.datastore.valueBlocking

@HiltViewModel
class ReactionSettingsViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val reactionSettings: ReactionSettings,
    private val generalSettings: GeneralSettings,
) : ViewModel4(dispatcherProvider) {

    data class State(
        val onePodMode: Boolean,
        val autoPlay: Boolean,
        val autoPause: Boolean,
        val autoConnect: Boolean,
        val autoConnectCondition: AutoConnectCondition,
        val showPopUpOnCaseOpen: Boolean,
        val showPopUpOnConnection: Boolean,
    )

    val state = combine(
        reactionSettings.onePodMode.flow,
        reactionSettings.autoPlay.flow,
        reactionSettings.autoPause.flow,
        reactionSettings.autoConnect.flow,
        reactionSettings.autoConnectCondition.flow,
        reactionSettings.showPopUpOnCaseOpen.flow,
        reactionSettings.showPopUpOnConnection.flow,
    ) { values ->
        State(
            onePodMode = values[0] as Boolean,
            autoPlay = values[1] as Boolean,
            autoPause = values[2] as Boolean,
            autoConnect = values[3] as Boolean,
            autoConnectCondition = values[4] as AutoConnectCondition,
            showPopUpOnCaseOpen = values[5] as Boolean,
            showPopUpOnConnection = values[6] as Boolean,
        )
    }.asLiveState()

    fun setOnePodMode(enabled: Boolean) {
        reactionSettings.onePodMode.valueBlocking = enabled
    }

    fun setAutoPlay(enabled: Boolean) {
        reactionSettings.autoPlay.valueBlocking = enabled
    }

    fun setAutoPause(enabled: Boolean) {
        reactionSettings.autoPause.valueBlocking = enabled
    }

    fun setAutoConnect(enabled: Boolean) {
        reactionSettings.autoConnect.valueBlocking = enabled
        if (enabled) {
            generalSettings.monitorMode.valueBlocking = MonitorMode.ALWAYS
        }
    }

    fun setAutoConnectCondition(condition: AutoConnectCondition) {
        reactionSettings.autoConnectCondition.valueBlocking = condition
    }

    fun setShowPopUpOnCaseOpen(enabled: Boolean) {
        reactionSettings.showPopUpOnCaseOpen.valueBlocking = enabled
    }

    fun setShowPopUpOnConnection(enabled: Boolean) {
        reactionSettings.showPopUpOnConnection.valueBlocking = enabled
    }

    companion object {
        private val TAG = logTag("Settings", "Reaction", "VM")
    }
}
