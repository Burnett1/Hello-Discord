package com.burnett.hellodiscord

import net.dv8tion.jda.core.audio.AudioSendHandler
import java.util

class AudioSendListener(var voiceData: Array[Array[Byte]], var canProvideB: Boolean = true) extends AudioSendHandler {
  var index: Int = 0

  override def canProvide: Boolean = canProvideB

  override def provide20MsAudio: Array[Byte] = {
    if (index == voiceData.length - 1)
      canProvideB = false
    val data = voiceData(index)
    index = index + 1
    data
  }

  override def isOpus: Boolean = false
}

object AudioSendListener {
  def apply(data: Array[Byte]): AudioSendListener = {
    var voiceData: Array[Array[Byte]] = Array.ofDim[Byte](data.length / 3840, 3840)
    var i = 0
    while ( {
      i < voiceData.length
    }) {
      voiceData(i) = util.Arrays.copyOfRange(data, i * 3840, i * 3840 + 3840)

      {
        i += 1; i - 1
      }
    }
    new AudioSendListener(voiceData)
  }
}
