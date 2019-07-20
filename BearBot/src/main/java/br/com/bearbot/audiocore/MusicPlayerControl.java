package br.com.bearbot.audiocore;

import java.awt.Color;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import br.com.bearbot.beans.Server;
import br.com.bearbot.utils.UTILS;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

public class MusicPlayerControl {
	private static final int PLAYLIST_LIMIT = 1000;
	private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
	private static final Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();
	private static Guild guild;
	private boolean isPlaylist;

	public MusicPlayerControl() {
		AudioSourceManagers.registerRemoteSources(MANAGER);
	}

	
	// Cria um player para o guild
	private AudioPlayer createPlayer(Guild g) {
		AudioPlayer audioPlayer = MANAGER.createPlayer();
		TrackManager trackManager = new TrackManager(audioPlayer);
		audioPlayer.addListener(trackManager);
		guild.getAudioManager().setSendingHandler(new PlayerSendHandler(audioPlayer));
		PLAYERS.put(g, new AbstractMap.SimpleEntry<AudioPlayer , TrackManager>(audioPlayer, trackManager));

		return audioPlayer;
	}

	// Verifica se existe um play no guild
	private boolean hasPlayer(Guild g) {
		return PLAYERS.containsKey(g);
	}

	// Pega o player 
	private AudioPlayer getPlayer(Guild g) {
		if (hasPlayer(g)) {
			return PLAYERS.get(g).getKey();
		} else {
			return createPlayer(g);
		}
	}

	// Verifica se o bot esta tocando algo
		private boolean isIdle(Guild g) {
			return !hasPlayer(g) || getPlayer(g).getPlayingTrack() == null;
		}
	
	private TrackManager getManager(Guild g) {
		return PLAYERS.get(g).getValue();
	}

	
	// Carrega as musicas
	private void loadTrack(String identifier, final Member author, final Message msg) {

		final Guild guild = author.getGuild();
		getPlayer(guild);

		MANAGER.setFrameBufferDuration(5000);
		MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

			@Override
			public void trackLoaded(AudioTrack track) {
				getManager(guild).queue(track, author, msg);
			}

			@Override
			public void playlistLoaded(final AudioPlaylist playlist) {
				int musicsQnt = 0;
				final EmbedBuilder addNew = new EmbedBuilder();
				if (isPlaylist) {
					for (int i = 0; i < (playlist.getTracks().size() > PLAYLIST_LIMIT ? PLAYLIST_LIMIT
							: playlist.getTracks().size()); i++) {
						getManager(guild).queue(playlist.getTracks().get(i), author, msg);
						musicsQnt = i;
					}
					addNew.setTitle("🎶 Musicas adicionadas: " + musicsQnt);
				} else {
					musicsQnt = 1;
					
					EmbedBuilder nextMusic = new EmbedBuilder();
						nextMusic.setColor(Color.CYAN);	
						nextMusic.addField("🌓  " + playlist.getTracks().get(0).getInfo().title, "", false);
						nextMusic.addField("🌔  " + playlist.getTracks().get(1).getInfo().title, "", false);
						nextMusic.addField("🌕  " + playlist.getTracks().get(2).getInfo().title, "", false);
						nextMusic.addField("🌖  " + playlist.getTracks().get(3).getInfo().title, "", false);
						
					
					long idMessage = msg.getChannel().sendMessage(nextMusic.build()).complete().getIdLong();
						msg.getChannel().addReactionById(idMessage, "🌓").queue();
						msg.getChannel().addReactionById(idMessage, "🌔").queue();
						msg.getChannel().addReactionById(idMessage, "🌕").queue();
						msg.getChannel().addReactionById(idMessage, "🌖").queue();
						
						/*
						 * Cria uma thread e espera que o usuario que solicitou a musica escolha a musica
						 * se depois de 10 segundos sem escolher ele pega a primeira musica da lista
						 */
					new Thread() {
						@SuppressWarnings("deprecation")
						@Override
						public void run() {
							Server serverGuild = UTILS.GUILDS.get(msg.getGuild());
							serverGuild.setMusicOption(0);
							SelectMusic musicSelector = new SelectMusic(serverGuild);

							for (int i = 0; i < 10000; i++) {
								try {
									TimeUnit.MILLISECONDS.sleep(1);
									if (musicSelector.getValue(serverGuild) == 1) {
										getManager(guild).queue(playlist.getTracks().get(0), author, msg);
										msg.getChannel().sendMessage("Music selected: " + playlist.getTracks().get(0).getInfo().title).queue();
										interrupt();
										stop();
										break;
									} else if (musicSelector.getValue(serverGuild) == 2) {
										getManager(guild).queue(playlist.getTracks().get(1), author, msg);
										msg.getChannel().sendMessage(
												"Music selected: " + playlist.getTracks().get(1).getInfo().title)
												.queue();
										interrupt();
										stop();
										break;
									} else if (musicSelector.getValue(serverGuild) == 3) {
										getManager(guild).queue(playlist.getTracks().get(2), author, msg);
										msg.getChannel().sendMessage(
												"Music selected: " + playlist.getTracks().get(2).getInfo().title)
												.queue();
										interrupt();
										stop();
										break;
									} else if (musicSelector.getValue(serverGuild) == 4) {
										getManager(guild).queue(playlist.getTracks().get(3), author, msg);
										msg.getChannel().sendMessage("Music selected: " + playlist.getTracks().get(3).getInfo().title).queue();
										interrupt();
										stop();
										break;
									}
								} catch (Exception e) {

								}
							}
							System.out.println("Interruped");
							getManager(guild).queue(playlist.getTracks().get(0), author, msg);
							addNew.setTitle("🎶 Adicionado: " + playlist.getTracks().get(0).getInfo().title);
							interrupt();
						}
					}.start();

				}

				addNew.setColor(Color.GREEN);
				msg.getChannel().sendMessage(addNew.build()).queue();
			}

			@Override
			public void noMatches() {

			}

			@Override
			public void loadFailed(FriendlyException exception) {
				exception.printStackTrace();
			}
		});

	}
	
	// Pula musica
	private void skipMusic(Guild g) {
		getPlayer(g).stopTrack();
	}

	// Converte o timestamp em tempo 00:00
	private String getTimestamp(long milis) {
		long seconds = milis / 1000;
		long hours = Math.floorDiv(seconds, 3600);
		seconds = seconds - (hours * 3600);
		long mins = Math.floorDiv(seconds, 60);
		seconds = seconds - (mins * 60);
		return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
	}

	// Comando para colocar uma musica
	public void play(MessageReceivedEvent event, String music) {
		guild = event.getGuild();
		String input = music;

		if (!(input.startsWith("http://") || input.startsWith("https://"))) {
			input = "ytsearch: " + input;
			this.isPlaylist = false;
			loadTrack(input, event.getMember(), event.getMessage());
		} else {
			this.isPlaylist = true;
			loadTrack(input, event.getMember(), event.getMessage());
		}

		event.getMessage().delete().queue();
	}
	
	// Para o bot via comando
	public void stop(MessageReceivedEvent event) {
		Guild guildEvent = event.getGuild();

		if (isIdle(guildEvent))
			return;

		getManager(guildEvent).purgeQueue();

		skipMusic(guildEvent);
		guildEvent.getAudioManager().closeAudioConnection();

		EmbedBuilder stopEmbed = new EmbedBuilder();

		stopEmbed.setTitle("🛑🛑 O bot foi parado !");
		stopEmbed.setColor(Color.RED);
		event.getTextChannel().getManager().setTopic("").queue();
		event.getChannel().sendMessage(stopEmbed.build()).complete();
	}

	// Para o bot pelo emoji
	public void stopEmote(MessageReactionAddEvent event) {
		Guild guildEvent = event.getGuild();

		if (isIdle(guildEvent))
			return;

		getManager(guildEvent).purgeQueue();

		skipMusic(guildEvent);
		guildEvent.getAudioManager().closeAudioConnection();

		EmbedBuilder stopEmbed = new EmbedBuilder();

		stopEmbed.setTitle("🛑🛑 O bot foi parado !");
		stopEmbed.setColor(Color.RED);
		event.getTextChannel().getManager().setTopic("").queue();
		event.getChannel().sendMessage(stopEmbed.build()).complete();

	}

	// Embaralha a playlist
	public void shuffle(MessageReceivedEvent event) {
		Guild guildEvent = event.getGuild();

		if (isIdle(guildEvent)) return;
		getManager(guildEvent).shuffleQueue();

		EmbedBuilder addNew = new EmbedBuilder();

		addNew.setTitle("🎶 Musicas misturadas");
		addNew.setColor(Color.MAGENTA);
		event.getChannel().sendMessage(addNew.build()).queue();

	}
	
	// Exibe toda lista de musicas adicionadas
	public void queue(MessageReceivedEvent event) {
		Guild guildEvent = event.getGuild();

		if (isIdle(guildEvent)) {
			return;
		}

		Set<AudioInfo> a = getManager(guildEvent).getQueue();
		EmbedBuilder queueInfo = new EmbedBuilder();
		queueInfo.setTitle("Quantidade de musicas restantes: " + a.size());
		queueInfo.setColor(Color.CYAN);
		String queueList = "```";
		int b = 0;
		for (AudioInfo audioInfo : a) {

			queueList += (b + "." + audioInfo.getTrack().getInfo().title + "\n");
			b++;
			if (b == 10) {
				break;
			}
		}

		queueList += "```";

		queueInfo.addField("Musics", queueList, false);
		event.getChannel().sendMessage(queueInfo.build()).queue();

	}
	// Pula a musica atravez do comando !bb music skip
	public void skip(MessageReceivedEvent event) {
		Guild guildEvent = event.getGuild();

		if (isIdle(guildEvent)) {
			return;
		}

		skipMusic(guildEvent);
	}
	// Pula a musica atravez do emoji
	public void skipEmote(MessageReactionAddEvent event) {
		Guild guildEvent = event.getGuild();

		if (isIdle(guildEvent)) {
			return;
		}

		skipMusic(guildEvent);

	}
	// Exibe info da musica tocando via comando
	public void info(MessageReceivedEvent event) {
		Guild guildEvent = event.getGuild();

		if (isIdle(guildEvent)) {
			System.out.println("Nao ta tocando nada man");
			return;
		}

		AudioTrack track = getPlayer(guildEvent).getPlayingTrack();
		AudioTrackInfo info = track.getInfo();

		EmbedBuilder infoEmbed = new EmbedBuilder().setDescription("**CURRENT TRACK INFO:**");
		infoEmbed.addField("Title", info.title, false);
		infoEmbed.addField("Duration",
				"`[ " + getTimestamp(track.getPosition()) + "/ " + getTimestamp(track.getDuration()) + " ]`", false);
		infoEmbed.addField("Author", info.author, false).build();
		infoEmbed.setColor(Color.YELLOW);

		long idMessage = event.getTextChannel().sendMessage(infoEmbed.build()).complete().getIdLong();
		event.getChannel().addReactionById(idMessage, "ℹ").queue();
		event.getChannel().addReactionById(idMessage, "⏹").queue();
		event.getChannel().addReactionById(idMessage, "⏩").queue();
	}
	// exibe info da musica tocando via emoji
	public void infoEmote(MessageReactionAddEvent event) {
		Guild guildEvent = event.getGuild();

		if (isIdle(guildEvent)) {
			System.out.println("Nao ta tocando nada man");
			return;
		}

		AudioTrack track = getPlayer(guildEvent).getPlayingTrack();
		AudioTrackInfo info = track.getInfo();

		EmbedBuilder infoEmbed = new EmbedBuilder().setDescription("**CURRENT TRACK INFO:**");
		infoEmbed.addField("Title", info.title, false);
		infoEmbed.addField("Duration",
				"`[ " + getTimestamp(track.getPosition()) + "/ " + getTimestamp(track.getDuration()) + " ]`", false);
		infoEmbed.addField("Author", info.author, false).build();
		infoEmbed.setColor(Color.YELLOW);

		long idMessage = event.getTextChannel().sendMessage(infoEmbed.build()).complete().getIdLong();
		event.getChannel().addReactionById(idMessage, "ℹ").queue();
		event.getChannel().addReactionById(idMessage, "⏹").queue();
		event.getChannel().addReactionById(idMessage, "⏩").queue();

	}

}
