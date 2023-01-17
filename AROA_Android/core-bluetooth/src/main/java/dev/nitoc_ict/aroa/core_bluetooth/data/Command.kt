package dev.nitoc_ict.aroa.core_bluetooth.data

import java.io.Serializable

sealed class Command: Serializable {
    object StopTimer: Command() { private fun readResolve(): Any = StopTimer }
    object ResumeTimer: Command() { private fun readResolve(): Any = ResumeTimer }
    object ResetTimer: Command() { private fun readResolve(): Any = ResetTimer }

    data class SetSpeed(val meterParSec: Int): Command()
    data class SetRank(val rank: Int): Command()
    data class SetDistance(val meter: Int): Command()

    object ShowResult: Command() { private fun readResolve(): Any = ShowResult }
}
