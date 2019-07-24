package br.com.bearbot.commands;

import br.com.bearbot.audiocore.MusicPlayerControl;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.isFromType(ChannelType.PRIVATE) || event.getAuthor().isBot()) return;
		

		String args[] = event.getMessage().getContentDisplay().toLowerCase().split(" ");
		String musicArgs[] = event.getMessage().getContentDisplay().split(" ");
		
		String music = "";
		
		try {
			for (int i = 1; i < musicArgs.length; i++) music += " " + musicArgs[i];
			music = music.trim();
		} catch (Exception e) {
			
		}		
		
		MusicPlayerControl musicController = new MusicPlayerControl();
		
		if (args[0].equals(".help")) new Help(event);
		if (args[0].equals(".roll")) new Roll(event);
		if (args[0].equals(".sharescreen")) new ShareScreen(event);
		if (args[0].equals(".rank")) new Rank(event);
		if (args[0].equals(".clear")) new Clear(event);
		if (args[0].equals(".play") || args[0].equals(".p")) musicController.play(event, music.trim());
		if (args[0].equals(".skip") || args[0].equals(".s")) musicController.skip(event);
		if (args[0].equals(".info") || args[0].equals(".now")) musicController.info(event);
		if (args[0].equals(".queue") || args[0].equals(".q")) musicController.queue(event);
		if (args[0].equals(".stop"))  musicController.stop(event);
		if (args[0].equals(".shuffle"))  musicController.shuffle(event);	
		if (args[0].equals(".ping")) new Ping(event);
		
		if (args[0].equals(".monstercat") || args[0].equals(".mc")) musicController.play(event, "https://www.twitch.tv/monstercat");
		if (args[0].equals(".list1"))  musicController.play(event, "https://www.youtube.com/watch?v=Pkh8UtuejGw&list=PLx0sYbCqOb8TBPRdmBHs5Iftvv9TPboYG");
			
		
	}
	
}
