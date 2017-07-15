package com.burnett.hellodiscord

import net.dv8tion.jda.core.entities.{Guild, VoiceChannel}
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.events.{Event, ReadyEvent}
import net.dv8tion.jda.core.hooks.{EventListener, ListenerAdapter}
import net.dv8tion.jda.core.{AccountType, JDA, JDABuilder}
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.FileInputStream

object HelloDiscord {

  private val token: String = "MzM1NzgzNDIwOTMwMTYyNjg4.DEuyaA.J7L9hEBqOSM2xOMuiNdoUVgENxU"

  def main(args: Array[String]): Unit = {
    val jda = new JDABuilder(AccountType.BOT).setToken(token)
      .addEventListener(new HelloDiscord())
      .buildBlocking()
  }

  def killAudioHandlers(g: Guild): Unit = {
    val ah = g.getAudioManager.getReceiveHandler.asInstanceOf[AudioReceiveListener]
    if (ah != null) {
      ah.canReceive = false
      ah.compVoiceData = null
      g.getAudioManager.setReceivingHandler(null)
    }
    val sh = g.getAudioManager.getSendingHandler.asInstanceOf[AudioSendListener]
    if (sh != null) {
      sh.canProvideB = false
      sh.voiceData = null
      g.getAudioManager.setSendingHandler(null)
    }
    println("Destroyed audio handlers for " + g.getName)
    System.gc()
  }

  def joinVoiceChannel(vc: VoiceChannel): Unit = {
    vc.getGuild.getAudioManager.openAudioConnection(vc)
  }
}

class HelloDiscord extends ListenerAdapter {

  override def onMessageReceived(event: MessageReceivedEvent): Unit = {
    if (event.getAuthor.getName.contains("Burnett")) {
      if(event.getMessage.getContent.toLowerCase.contains("sound")) {
        val out = new ByteArrayOutputStream
        val in = new BufferedInputStream(new FileInputStream("D:\\Development\\Workspaces\\Hello Discord\\Project\\Hello-Discord\\src\\main\\resources\\piano2.wav"))

        var read = 0
        val buff = new Array[Byte](1024)
        while ( {
          read = in.read(buff)
          read > 0
        }) out.write(buff, 0, read)
        out.flush()
        val audioBytes = out.toByteArray
        val audioSender = AudioSendListener(audioBytes)
        println(event.getGuild.getName)
        HelloDiscord.joinVoiceChannel(event.getMember.getVoiceState.getChannel)
        event.getGuild.getAudioManager.setSendingHandler(audioSender)
      }
    }
  }
}
