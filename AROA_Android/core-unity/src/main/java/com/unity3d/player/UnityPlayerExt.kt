package com.unity3d.player

import com.unity3d.player.UnityPlayer.UnitySendMessage

fun UnityPlayer.setTime(second: Int) {
    UnitySendMessage("AndroidMessageReceiver", "SetTime", second.toString())
}

fun UnityPlayer.setDistance(meter: Int) {
    UnitySendMessage("AndroidMessageReceiver", "SetDistance", meter.toString())
}

fun UnityPlayer.setRank(rank: Int) {
    UnitySendMessage("AndroidMessageReceiver", "SetRank", rank.toString())
}