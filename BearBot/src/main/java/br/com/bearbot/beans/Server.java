package br.com.bearbot.beans;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.core.entities.Guild;

public class Server {
	private Guild guild;
	private int musicOption;
	private List<Long> messagesId;

	public Guild getGuild() {
		return guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}

	public int getMusicOption() {
		return musicOption;
	}

	public void setMusicOption(int musicOption) {
		this.musicOption = musicOption;
	}

	public List<Long> getMessagesId() {
		return messagesId;
	}

	public void setMessagesId() {
		this.messagesId = new ArrayList<Long>();
	}



	

}
