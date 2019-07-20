package br.com.bearbot.events;

import br.com.bearbot.DAO.AddNewUserDAO;
import br.com.bearbot.DAO.VerifyExistenceUserDAO;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class OnUserEnter extends ListenerAdapter {
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		if (event.getUser().isBot()) return;

		VerifyExistenceUserDAO user = new VerifyExistenceUserDAO();
		if (!user.CheckExistenceUserDAO(event.getGuild().getIdLong(), event.getMember().getUser().getIdLong())) {
			try {
				new AddNewUserDAO(event.getMember().getUser().getIdLong(), event.getGuild().getIdLong(), 0);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		user = null;

		
		event.getGuild().getDefaultChannel().sendMessage("👽 Seja bem vindo ao servidor " + event.getUser().getAsMention()).queue();

	}
	
	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		event.getGuild().getDefaultChannel().sendMessage("👽  O ultimo apaga a luz. :: " + event.getUser().getName() + " Acabou de sair 😏").queue();
	}
	
}
