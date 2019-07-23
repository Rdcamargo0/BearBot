package br.com.bearbot.commands;

import java.awt.Color;

import br.com.bearbot.audiocore.MusicPlayerControl;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Music {

	public Music(MessageReceivedEvent event) {
		if (event.getAuthor().isBot() || event.isFromType(ChannelType.PRIVATE)) return;
		
		if (event.getMember().getVoiceState().getAudioChannel() == null) {
			EmbedBuilder nextMusic = new EmbedBuilder();
			nextMusic.setTitle("Voce precisa estar em um canal de audio para usar este comando!");
			nextMusic.setColor(Color.RED);
			event.getChannel().sendMessage(nextMusic.build()).queue();
			return;
		}
		
		String[] args = event.getMessage().getContentDisplay().split(" ");
		String music = "";
		
		
		
		for (int i = 3; i < args.length; i++) music += " " + args[i];		
		
		music = music.trim();
		MusicPlayerControl musicController = new MusicPlayerControl();

		if((args[2].equals("play") || args[2].equals("p")) && args.length >= 3)  musicController.play(event, music);
		if((args[2].equals("skip") || args[2].equals("s")) && args.length >= 3)  musicController.skip(event);
		if((args[2].equals("info") || args[2].equals("now")) && args.length >= 3)  musicController.info(event);
		if((args[2].equals("queue") || args[2].equals("q")) && args.length >= 3)  musicController.queue(event);
		if((args[2].equals("monstercat") || args[2].equals("mc"))&& args.length >= 3)  musicController.play(event, "https://www.twitch.tv/monstercat");
		if(args[2].equals("list1") && args.length >= 3)  musicController.play(event, "https://www.youtube.com/watch?v=Pkh8UtuejGw&list=PLx0sYbCqOb8TBPRdmBHs5Iftvv9TPboYG");
		if(args[2].equals("stop") && args.length >= 3)  musicController.stop(event);
		if(args[2].equals("shuffle") && args.length >= 3)  musicController.shuffle(event);
		
		
		
		
		
	
		
	}

}
