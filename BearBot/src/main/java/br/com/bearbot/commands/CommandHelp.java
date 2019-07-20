package br.com.bearbot.commands;

import java.awt.Color;
import java.util.function.Consumer;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandHelp {
	public CommandHelp(MessageReceivedEvent event) {
		EmbedBuilder configHelp = new EmbedBuilder();
		
		configHelp.setTitle("All commands", null);
		configHelp.setColor(Color.BLUE);
		configHelp.setDescription("__***Type !bb <command>***__");
		
		configHelp.addField("***SHARESCREEN***", "!bb sharescreen **To share screen on server**", false);
		configHelp.addField("***RANK***", "!bb rank **To view your rank on the server**", false);
		configHelp.addField("***MUSIC PLAY***", "!bb music play <link/name of music> **To play a new music**", false);
		configHelp.addField("***MUSIC STOP***", "!bb music stop **To stop the music**", false);
		configHelp.addField("***MUSIC SKIP***", "!bb music skip **To skip the music**", false);
		configHelp.addField("***MUSIC SHUFFLE***", "!bb music shuffle **To shuffle the playlist**", false);
		configHelp.addField("***MUSIC QUEUE***", "!bb music queue **To view music queue**", false);
		
		
		if (event.isFromType(ChannelType.PRIVATE) && !event.getAuthor().isBot()) return;

		event.getChannel().sendMessage("👽 Enviado para seu privado").queue();
		
		sendPrivateMessage(event.getAuthor() , configHelp.build());
	}

	public void sendPrivateMessage(User user, final MessageEmbed messageEmbed) {
		user.openPrivateChannel().queue(new Consumer<PrivateChannel>() {
			public void accept(PrivateChannel channel) {
				channel.sendMessage(messageEmbed).queue();
			}
		});
	}
}
