package br.com.bearbot.beans;

import net.dv8tion.jda.core.entities.Guild;

public class Server {
	private Guild guild;
	private int musicOption;

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

}
