package br.com.bearbot.audiocore;

import br.com.bearbot.beans.Server;
import br.com.bearbot.utils.UTILS;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class SelectMusic extends ListenerAdapter {

	@SuppressWarnings("unused")
	private int value;

	public SelectMusic(Server server) {
		this.value = server.getMusicOption();
	}

	public SelectMusic() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		if(event.getUser().isBot() || event.getMember().getVoiceState().getAudioChannel() == null) return;
		
		
		if (!(event.getMember().getUser().equals(event.getJDA().getSelfUser()))
				&& event.getReactionEmote().getName().equals("🌓")) {
			UTILS.GUILDS.get(event.getGuild()).setMusicOption(1);
			event.getChannel().deleteMessageById(event.getMessageIdLong()).queue();
		}
		if (!(event.getMember().getUser().equals(event.getJDA().getSelfUser()))
				&& event.getReactionEmote().getName().equals("🌔")) {
			UTILS.GUILDS.get(event.getGuild()).setMusicOption(2);
			event.getChannel().deleteMessageById(event.getMessageIdLong()).queue();
		}
		if (!(event.getMember().getUser().equals(event.getJDA().getSelfUser()))
				&& event.getReactionEmote().getName().equals("🌕")) {
			UTILS.GUILDS.get(event.getGuild()).setMusicOption(3);
			event.getChannel().deleteMessageById(event.getMessageIdLong()).queue();
		}
		if (!(event.getMember().getUser().equals(event.getJDA().getSelfUser()))
				&& event.getReactionEmote().getName().equals("🌖")) {
			UTILS.GUILDS.get(event.getGuild()).setMusicOption(4);
			event.getChannel().deleteMessageById(event.getMessageIdLong()).queue();
		}
	}

	public int getValue(Server server) {
		this.value = server.getMusicOption();
		return server.getMusicOption();
	}

	public void setValue(int value) {
		this.value = value;
	}

}
