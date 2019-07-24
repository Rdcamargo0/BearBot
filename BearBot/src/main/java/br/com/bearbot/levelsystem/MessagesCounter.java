package br.com.bearbot.levelsystem;

import br.com.bearbot.DAO.AddNewUserDAO;
import br.com.bearbot.DAO.UpdateMsgCounterDAO;
import br.com.bearbot.DAO.VerifyExistenceUserDAO;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessagesCounter extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot() || event.isFromType(ChannelType.PRIVATE)) return;

		long idUser = event.getAuthor().getIdLong();
		long idServer = event.getGuild().getIdLong();

		VerifyExistenceUserDAO user = new VerifyExistenceUserDAO();
		if (user.CheckExistenceUserDAO(idServer, idUser)) {

			new UpdateMsgCounterDAO(idUser, idServer);
			
		} else {
			try {
				
				new AddNewUserDAO(event.getMember().getUser().getIdLong(), event.getGuild().getIdLong(), 1);

			} catch (Exception e) {
			}
		}
		user = null;

	}
}
