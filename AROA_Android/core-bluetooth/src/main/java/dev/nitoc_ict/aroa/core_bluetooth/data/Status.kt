package dev.nitoc_ict.aroa.core_bluetooth.data

import java.io.Serializable

class Status(
    val time: Int,
    val speed: Int,
    val rank: Int,
    val distance: Int,
): Serializable
