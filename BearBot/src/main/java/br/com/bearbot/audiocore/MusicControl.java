package br.com.bearbot.audiocore;

import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MusicControl extends ListenerAdapter{	
	private String djRoleName;
	private boolean autorizate;
	
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		if(event.getUser().isBot()) return;	
		//if(!(event.getReactionEmote().getName().equals("⏹") || event.getReactionEmote().getName().equals("⏩") || event.getReactionEmote().getName().equals("ℹ"))) return;
		
		
		
		
		
		MusicPlayerControl musicController = new MusicPlayerControl();
		
		if(!(event.getMember().getUser().equals(event.getJDA().getSelfUser())) && event.getReactionEmote().getName().equals("⏹")) {
			musicController.stopEmote(event);
			event.getChannel().deleteMessageById(event.getMessageIdLong()).queue();
			
		}
		if(!(event.getMember().getUser().equals(event.getJDA().getSelfUser())) && event.getReactionEmote().getName().equals("⏩")) {
			musicController.skipEmote(event);
			event.getChannel().deleteMessageById(event.getMessageIdLong()).queue();
		}
		if(!(event.getMember().getUser().equals(event.getJDA().getSelfUser()))
				&& event.getReactionEmote().getName().equals("ℹ")) {
			musicController.infoEmote(event);
			event.getChannel().deleteMessageById(event.getMessageIdLong()).queue();
		}

	}

}
