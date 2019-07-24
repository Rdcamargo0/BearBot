package br.com.bearbot.commands;

import java.awt.Color;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ShareScreen {

	public ShareScreen(MessageReceivedEvent event) {
		String idServer = event.getGuild().getId();
		String idChannel = event.getMember().getVoiceState().getChannel().getId();
		String channelName = event.getMember().getVoiceState().getChannel().getName();

		String url = "http://discordapp.com/channels/" + idServer + "/" + idChannel;

		EmbedBuilder share = new EmbedBuilder();
		share.setTitle("Share screen :: " + event.getMember().getVoiceState().getAudioChannel());
		share.setColor(Color.MAGENTA);
		share.addField("URL: ", url, true);

		EmbedBuilder sharescreen = new EmbedBuilder();
		sharescreen.setTitle("ShareScreen ", event.getGuild().getIconUrl());
		sharescreen.setColor(Color.RED);
		sharescreen.setDescription("Voice channel: " + channelName);
		sharescreen.addField("Para entrar no compartilhamente", "[Click here](" + url + ")", false);
		sharescreen.setFooter(event.getGuild().getName(), event.getGuild().getIconUrl());

		event.getChannel().sendMessage(sharescreen.build()).queue();
	}

}
