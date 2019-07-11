package br.com.bearbot.events;

import br.com.bearbot.DAO.AddNewGuildDAO;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class OnGuildJoinInNewServer extends ListenerAdapter{
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		System.out.println("Adicionando");
		try {
			new AddNewGuildDAO(event.getGuild().getIdLong());
			System.out.println("Entrou em um novo discord");
		} catch (Exception e) {
			System.out.println("Nao foi adicionado");
		}
		
	}
}
