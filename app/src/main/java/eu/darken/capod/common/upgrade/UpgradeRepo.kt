package eu.darken.capod.common.upgrade

import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface UpgradeRepo {
    val upgradeInfo: Flow<Info>

    interface Info {
        val isPro: Boolean
        val upgradedAt: Instant?
        val error: Throwable?
    }
}
