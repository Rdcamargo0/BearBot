package br.com.bearbot.logs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.bearbot.DAO.AddNewGuildDAO;
import br.com.bearbot.DAO.VerifyExistenceDAO;
import br.com.bearbot.beans.Server;
import br.com.bearbot.utils.UTILS;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class OnReady extends ListenerAdapter {
	public void onReady(ReadyEvent event) {

		UTILS.GUILDS = new HashMap<Guild, Server>();
		List<Guild> guildsList = event.getJDA().getGuilds();

		for (Guild guild : guildsList) {
			Server server = new Server();
			server.setMessagesId(new ArrayList<Long>());
			UTILS.GUILDS.put(guild, server);

			VerifyExistenceDAO ver = new VerifyExistenceDAO();
			if (ver.checkExistence(guild.getIdLong())) {
			} else {
				try {
					new AddNewGuildDAO(guild.getIdLong());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			ver = null;
		}

		event.getJDA().getTextChannelById("537965147625357313").sendMessage("✅ BearBot is ready.").queue();
	}

	public void onDisconnect(DisconnectEvent event) {
		event.getJDA().getTextChannelById("537965147625357313").sendMessage("BearBot is not ready.").queue();
	}
}