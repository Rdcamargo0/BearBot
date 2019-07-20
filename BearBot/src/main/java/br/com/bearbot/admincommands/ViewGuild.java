package br.com.bearbot.admincommands;

import br.com.bearbot.core.BotStart;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ViewGuild {

	public ViewGuild(MessageReceivedEvent event, String guildId) {
		Guild guild = BotStart.bot.getGuildById(guildId);
		
		EmbedBuilder guildView = new EmbedBuilder();
		guildView.setTitle(guild.getName());
		guildView.setThumbnail(guild.getIconUrl());
		guildView.addField("Guild Owner:" , "Name (" + guild.getOwner().getUser().getName() + ") Id (" + guild.getOwnerIdLong()+")" , false);
		guildView.addField("Guild Members: ", guild.getMembers().size() + "", false);
		
		event.getChannel().sendMessage(guildView.build()).queue();

	}

}
