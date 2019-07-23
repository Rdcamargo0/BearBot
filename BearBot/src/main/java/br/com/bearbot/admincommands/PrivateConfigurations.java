package br.com.bearbot.admincommands;

import java.awt.Color;

import br.com.bearbot.utils.UTILS;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class PrivateConfigurations extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;
		if (!event.getMessage().getContentDisplay().startsWith(UTILS.PREFIX)) return;
		if (!event.isFromType(ChannelType.PRIVATE)) return;

		if (!event.getAuthor().getId().equals("237317341359243265")) {
			EmbedBuilder notPermissionError = new EmbedBuilder();
			notPermissionError.setColor(Color.RED);
			notPermissionError.addField("Voce nao tem permissao para fazer isso!!!", "", true);
			event.getChannel().sendMessage(notPermissionError.build()).queue();
			return;
		}

		String args[] = event.getMessage().getContentDisplay().split(" ");

		if (args.length < 1) {
			event.getChannel().sendMessage("❌ Type !bb help").queue();
			return;
		}

		if (args[1].equals("help")) new Help(event);
		if (args[1].equals("listserver") || args[1].equals("ls")) new ListServers(event);
		if (args[1].equals("view") || args[1].equals("v")) new ViewGuild(event , args[2]);

	}

}
