package br.com.bearbot.commands;

import java.awt.Color;

import br.com.bearbot.utils.CONFIGS;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class OnPrivateMessageRecived extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String args[] = event.getMessage().getContentDisplay().split(" ");

		if (event.isFromType(ChannelType.PRIVATE)) {
			try {
				if (event.getMessage().getContentDisplay().startsWith(CONFIGS.PREFIX) && args[1].equals("config")) {
					if (event.getAuthor().getId().equals("") || event.getAuthor().getId().equals("")) {
						
						EmbedBuilder configHelp = new EmbedBuilder();
						configHelp.setTitle("CONFIG MODE", null);
						configHelp.setColor(Color.ORANGE);
						configHelp.setDescription("Type !bb config <command>");
						configHelp.addField("*SET PREFIX*", "!bb config setprefix <new prefix>", false);
						configHelp.addField("*SET TITLE*", "!bb config settitle <new title>", false);
						configHelp.addField("*ADD ADM*" , "!bb config add adm <adm id>" , false);

						event.getChannel().sendMessage(configHelp.build()).queue();
					} else {
						EmbedBuilder notPermissionError = new EmbedBuilder();
						notPermissionError.setColor(Color.RED);
						notPermissionError.addField("Voce nao tem permissao para fazer isso!!!", "", true);
						event.getChannel().sendMessage(notPermissionError.build()).queue();
					}
				}
			} catch (Exception e) {

			}

		}
	}

}
