package br.com.bearbot.audiocore;

import java.awt.Color;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MusicPlayerControl {
	private static final int PLAYLIST_LIMIT = 1000;
	private static Guild guild;
	private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
	private static final Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();
	private boolean isPlaylist;

	public MusicPlayerControl() {
		AudioSourceManagers.registerRemoteSources(MANAGER);
	}

	private AudioPlayer createPlayer(Guild g) {
		AudioPlayer audioPlayer = MANAGER.createPlayer();
		TrackManager trackManager = new TrackManager(audioPlayer);
		audioPlayer.addListener(trackManager);
		guild.getAudioManager().setSendingHandler(new PlayerSendHandler(audioPlayer));
		PLAYERS.put(g, new AbstractMap.SimpleEntry<>(audioPlayer, trackManager));

		return audioPlayer;
	}

	private boolean hasPlayer(Guild g) {
		return PLAYERS.containsKey(g);
	}

	private AudioPlayer getPlayer(Guild g) {
		if (hasPlayer(g)) {
			return PLAYERS.get(g).getKey();
		} else {
			return createPlayer(g);
		}
	}

	private TrackManager getManager(Guild g) {
		return PLAYERS.get(g).getValue();
	}

	private boolean isIdle(Guild g) {
		return !hasPlayer(g) || getPlayer(g).getPlayingTrack() == null;
	}

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
			public void playlistLoaded(AudioPlaylist playlist) {
				int musicsQnt = 0;
				if (isPlaylist) {
					for (int i = 0; i < (playlist.getTracks().size() > PLAYLIST_LIMIT ? PLAYLIST_LIMIT
							: playlist.getTracks().size()); i++) {
						getManager(guild).queue(playlist.getTracks().get(i), author, msg);
						musicsQnt = i;
					}
				} else {
					musicsQnt = 1;
					getManager(guild).queue(playlist.getTracks().get(0), author, msg);
				}

				EmbedBuilder addNew = new EmbedBuilder();

				addNew.setTitle("🎶 Musicas adicionadas: " + musicsQnt);
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

	private void skipMusic(Guild g) {
		getPlayer(g).stopTrack();
	}

	private String getTimestamp(long milis) {
		long seconds = milis / 1000;
		long hours = Math.floorDiv(seconds, 3600);
		seconds = seconds - (hours * 3600);
		long mins = Math.floorDiv(seconds, 60);
		seconds = seconds - (mins * 60);
		return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
	}

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
	}

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
		event.getChannel().sendMessage(stopEmbed.build()).complete();
	}

	public void shuffle(MessageReceivedEvent event) {
		Guild guildEvent = event.getGuild();

		if (isIdle(guildEvent))
			return;
		getManager(guildEvent).shuffleQueue();

		EmbedBuilder addNew = new EmbedBuilder();

		addNew.setTitle("🎶 Musicas misturadas");
		addNew.setColor(Color.MAGENTA);
		event.getChannel().sendMessage(addNew.build()).queue();

	}

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

			queueList += (audioInfo.getTrack().getInfo().title + "\n");
			b++;
			if (b == 20) {
				break;
			}
		}

		queueList += "```";

		queueInfo.addField("Musics", queueList, false);
		event.getChannel().sendMessage(queueInfo.build()).queue();

	}

	public void skip(MessageReceivedEvent event) {
		Guild guildEvent = event.getGuild();

		if (isIdle(guildEvent)) {
			return;
		}

		skipMusic(guildEvent);
	}

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

		event.getTextChannel().sendMessage(infoEmbed.build()).queue();
	}
}
