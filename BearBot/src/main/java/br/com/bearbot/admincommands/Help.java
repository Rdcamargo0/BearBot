package br.com.bearbot.admincommands;

import java.awt.Color;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Help {
	public Help(MessageReceivedEvent event) {
		EmbedBuilder configHelp = new EmbedBuilder();
		configHelp.setTitle("Configs commands", null);
		configHelp.setColor(Color.MAGENTA);
		configHelp.setDescription("Type !bb config <command>");
		configHelp.addField("*SET PREFIX*", "!bb config setprefix <new prefix>", false);
		configHelp.addField("*SET TITLE*", "!bb config settitle <new title>", false);
		configHelp.addField("*ADD ADM*", "!bb config add adm <adm id>", false);
		configHelp.addField("*LIST SERVERS*", "!bb listserver", false);

		
		event.getChannel().sendMessage(configHelp.build()).queue();
	}

}
