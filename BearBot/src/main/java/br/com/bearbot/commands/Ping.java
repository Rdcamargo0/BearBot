package br.com.bearbot.commands;

import br.com.bearbot.core.BotStart;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Ping {

	public Ping(MessageReceivedEvent event) {
		long ping = BotStart.bot.getPing();
		
		
		event.getChannel().sendMessage(event.getAuthor().getAsMention() + " O ping do bot e de: " + ping).queue();
	}

}
