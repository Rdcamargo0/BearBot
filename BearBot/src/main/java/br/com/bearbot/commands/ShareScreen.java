package br.com.bearbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ShareScreen {

	public ShareScreen(MessageReceivedEvent event) {
		String idServer = event.getGuild().getId();
		String idChannel = event.getMember().getVoiceState().getChannel().getId();
		String channelName = event.getMember().getVoiceState().getChannel().getName();
		
		String url = "http://discordapp.com/channels/" + idServer + "/" + idChannel;
		
		event.getChannel().sendMessage("Compartilhamento de tela correspondente ao canal de voz: ***" + channelName + "***").queue();
		event.getChannel().sendMessage("URL to share screen: " + url).queue();
	}

}
