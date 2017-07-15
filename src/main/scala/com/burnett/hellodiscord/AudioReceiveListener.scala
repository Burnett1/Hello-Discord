package com.burnett.hellodiscord

import net.dv8tion.jda.core.audio.{AudioReceiveHandler, CombinedAudio, UserAudio}
import net.dv8tion.jda.core.entities.VoiceChannel
import AudioReceiveListener._
class AudioReceiveListener(volume: Double, voiceChannel: VoiceChannel) extends AudioReceiveHandler {

  var compVoiceData = new Array[Byte]((1024 * 1024 * STARTING_MB).asInstanceOf[Int])

  var canReceive = true

  var afkTimer = 0

  override def canReceiveCombined: Boolean = canReceive

  override def canReceiveUser: Boolean = false

  override def handleCombinedAudio(combinedAudio: CombinedAudio): Unit = {
    if(combinedAudio.getUsers.size() == 0)
      afkTimer = afkTimer + 1 else afkTimer = 0

    if(afkTimer >= 50 * 60 * AFK_LIMIT) {
      voiceChannel.getGuild.getAudioManager.closeAudioConnection
      HelloDiscord.killAudioHandlers(voiceChannel.getGuild)
    }
  }

  override def handleUserAudio(userAudio: UserAudio): Unit = {

  }
}

object AudioReceiveListener {
  val AFK_LIMIT = 2
  var STARTING_MB = 0.5
}
