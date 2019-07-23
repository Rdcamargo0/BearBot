package br.com.bearbot.commands;

import java.awt.Color;

import br.com.bearbot.DAO.NumberOfMessagesDAO;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Rank {

	public Rank(MessageReceivedEvent event) {
		if (event.getAuthor().isBot() || event.isFromType(ChannelType.PRIVATE)) return;
		
		
		String rankName = "***Silver***";
		String name = event.getAuthor().getName();
		
		long idUser = event.getAuthor().getIdLong();
		long idServer = event.getGuild().getIdLong();
		
		NumberOfMessagesDAO qtns = new NumberOfMessagesDAO();

//		event.getGuild().getController().createVoiceChannel("teste").complete();
//		event.getGuild().getController().createCategory("catteste").complete();
//		List<Category> a = event.getGuild().getCategoriesByName("catteste", true);
//		event.getGuild().getCategoryById(a.get(0).getIdLong()).createVoiceChannel("teste de cabak").complete();		

		long msgConuter = qtns.QntMessages(idUser, idServer);

		long xpAmount = msgConuter * 10;

		EmbedBuilder rankEmbed = new EmbedBuilder();

		Color[] colorPallet = new Color[10];
		colorPallet[0] = new Color(148, 0, 211);
		colorPallet[1] = new Color(75, 0, 130);
		colorPallet[2] = new Color(0, 0, 255);
		colorPallet[3] = new Color(0, 255, 0);
		colorPallet[4] = new Color(255, 255, 0);
		colorPallet[5] = new Color(255, 127, 0);
		colorPallet[6] = new Color(255, 0, 0);

		if (xpAmount >= 1000) {
			rankName = "***Wood***";
			rankEmbed.setColor(colorPallet[0]);
		}
		if (xpAmount >= 4000) {
			rankName = "***Plastic***";
			rankEmbed.setColor(colorPallet[1]);
		}
		if (xpAmount >= 6000) {
			rankName = "***Glass***";
			rankEmbed.setColor(colorPallet[2]);
		}
		if (xpAmount >= 8000) {
			rankName = "***Aluminium***";
			rankEmbed.setColor(colorPallet[3]);
		}
		if (xpAmount >= 10000) {
			rankName = "***Steel***";
			rankEmbed.setColor(colorPallet[4]);
		}
		if (xpAmount >= 12000) {
			rankName = "***Gold***";
			rankEmbed.setColor(colorPallet[5]);
		}
		if (xpAmount >= 20000) {
			rankName = "***Diamond***";
			rankEmbed.setColor(colorPallet[6]);
		}

		rankEmbed.setAuthor(name, event.getAuthor().getAvatarUrl(), event.getAuthor().getAvatarUrl());
		rankEmbed.setThumbnail(event.getAuthor().getAvatarUrl());
		rankEmbed.setTitle("Seu rank atual");
		rankEmbed.setDescription("**" + name + "**");

		rankEmbed.addField("YOUR LEVEL", "	__***" + Math.ceil(xpAmount / 1000) + "***__", true);
		rankEmbed.addField("XP", "	__***" + xpAmount + "***__", true);
		rankEmbed.addField("YOUR RANK", rankName, true);

		rankEmbed.setFooter(event.getGuild().getName(), event.getGuild().getIconUrl());

		event.getChannel().sendMessage(rankEmbed.build()).queue();

	}

}
