package br.com.bearbot.commands;

import java.util.List;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Clear {
	public Clear(MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
				
		String args[] = event.getMessage().getContentDisplay().split(" ");
			
		int qnt = Integer.parseInt(args[1]);
		
		if(qnt > 100) {
			event.getChannel().sendMessage("🔴 Limite maximo: 100").queue();
			event.getChannel().deleteMessageById(event.getMessageIdLong()).queue();
			return;
		}
		
		
		List<Message> a = event.getChannel().getHistoryBefore(event.getMessage(), qnt).complete().getRetrievedHistory();
				
		for (Message message : a) {
			event.getChannel().deleteMessageById(message.getIdLong()).queue();
		}
		event.getChannel().deleteMessageById(event.getMessageIdLong()).queue();
		
				
		
	}
}
