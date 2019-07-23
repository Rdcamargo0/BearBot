package br.com.bearbot.core;

import br.com.bearbot.admincommands.PrivateConfigurations;
import br.com.bearbot.audiocore.SelectMusic;
import br.com.bearbot.commands.Commands;
import br.com.bearbot.events.OnGuildJoinInNewServer;
import br.com.bearbot.events.OnUserEnter;
import br.com.bearbot.levelsystem.LevelUpNotification;
import br.com.bearbot.levelsystem.MessagesCounter;
import br.com.bearbot.logs.OnReady;
import br.com.bearbot.utils.UTILS;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;

public class BotStart {
	public static JDA bot;

	public static void main(String[] args) throws Exception {
		bot = new JDABuilder(UTILS.TOKEN).build();
		bot.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
		bot.getPresence().setGame(Game.of(GameType.STREAMING, ".help", "https://www.twitch.tv/1Urso"));
		
		bot.addEventListener(new OnReady());
		bot.addEventListener(new PrivateConfigurations());
		bot.addEventListener(new Commands());
		bot.addEventListener(new LevelUpNotification());
		bot.addEventListener(new OnGuildJoinInNewServer());
		bot.addEventListener(new MessagesCounter());
		bot.addEventListener(new OnUserEnter());
		bot.addEventListener(new SelectMusic());
	}

}
