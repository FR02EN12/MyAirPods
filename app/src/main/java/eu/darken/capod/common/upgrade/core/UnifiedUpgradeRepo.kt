package eu.darken.capod.common.upgrade.core

import eu.darken.capod.common.upgrade.UpgradeRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnifiedUpgradeRepo @Inject constructor() : UpgradeRepo {
    override val upgradeInfo: Flow<UpgradeRepo.Info> = flowOf(Info())

    data class Info(
        override val isPro: Boolean = true,
        override val upgradedAt: Instant? = null,
        override val error: Throwable? = null,
    ) : UpgradeRepo.Info
}
