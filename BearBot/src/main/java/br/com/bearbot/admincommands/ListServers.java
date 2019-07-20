package br.com.bearbot.admincommands;

import java.awt.Color;
import java.util.List;

import br.com.bearbot.core.BotStart;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ListServers {

	public ListServers(MessageReceivedEvent event) {
		List<Guild> listOfGuilds = BotStart.bot.getGuilds();
		
		
		EmbedBuilder listOfServers = new EmbedBuilder();
		listOfServers.setColor(Color.YELLOW);
		
		
		for (Guild guild : listOfGuilds) {
			listOfServers.addField(guild.getName(),"Members: " +  guild.getMembers().size() + " ID: " + guild.getIdLong(), false);
		}
		
		event.getChannel().sendMessage(listOfServers.build()).queue();
		
	}

}
