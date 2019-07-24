package br.com.bearbot.commands;

import java.util.Random;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Roll {

	public Roll(MessageReceivedEvent event) {
		String args[] = event.getMessage().getContentDisplay().split(" ");
		
		try {

			int rollNumber = Integer.parseInt(args[1]);


			Random rollRandomNumber = new Random();
			
			int responseNumber = rollRandomNumber.nextInt(rollNumber);
			
			event.getChannel().sendMessage("O numero sorteado foi : " + responseNumber).queue();
		} catch (Exception e) {
			if(args.length < 3) {
				event.getChannel().sendMessage("Para usar o comando digite !bb roll <numero>").queue();
			}
		}
	}

}
