package br.com.bearbot.commands;

import br.com.bearbot.utils.CONFIGS;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.isFromType(ChannelType.PRIVATE) || event.getAuthor().isBot()) return;

		String args[] = event.getMessage().getContentDisplay().split(" ");

		if (event.getMessage().getContentDisplay().startsWith(CONFIGS.PREFIX) && args[1].equals("help")) new CommandHelp(event);
		if (event.getMessage().getContentDisplay().startsWith(CONFIGS.PREFIX) && args[1].equals("roll")) new CommandRoll(event);
		if (event.getMessage().getContentDisplay().startsWith(CONFIGS.PREFIX) && args[1].equals("sharescreen")) new ShareScreen(event);
		if (event.getMessage().getContentDisplay().startsWith(CONFIGS.PREFIX) && args[1].equals("rank")) new CommandRank(event);
		if (event.getMessage().getContentDisplay().startsWith(CONFIGS.PREFIX) && args[1].equals("music")) new MusicCommands(event);
	}
}
