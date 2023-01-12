package com.unity3d.player

import com.unity3d.player.UnityPlayer.UnitySendMessage

fun UnityPlayer.showText(message: String) {
    UnitySendMessage("AndroidSystem", "ShowText", message)
}