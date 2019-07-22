package br.com.bearbot.audiocore;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import br.com.bearbot.utils.UTILS;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.VoiceChannel;

public class TrackManager extends AudioEventAdapter {

	private final AudioPlayer PLAYER;
	private final Queue<AudioInfo> queue;
	private Message msg;

	public TrackManager(AudioPlayer player) {
		this.PLAYER = player;
		this.queue = new LinkedBlockingQueue<AudioInfo>();
	}

	public void queue(AudioTrack track, Member author, Message msg) {
		AudioInfo info = new AudioInfo(track, author);
		queue.add(info);
		this.msg = msg;
		if (PLAYER.getPlayingTrack() == null) {
			PLAYER.playTrack(track);
		}

	}

	public Set<AudioInfo> getQueue() {
		return new LinkedHashSet<>(queue);
	}

	public AudioInfo getInfo(final AudioTrack track) {
		return queue.stream().filter(new Predicate<AudioInfo>() {
			@Override
			public boolean test(AudioInfo info) {
				return info.getTrack().equals(track);
			}
		}).findFirst().orElse(null);
	}

	public void purgeQueue() {
		queue.clear();
	}

	public void shuffleQueue() {
		List<AudioInfo> cQueue = new ArrayList<>(getQueue());
		AudioInfo current = cQueue.get(0);
		cQueue.remove(0);
		Collections.shuffle(cQueue);
		cQueue.add(0, current);
		purgeQueue();
		queue.addAll(cQueue);
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		AudioInfo info = queue.element();
		VoiceChannel vChan = info.getAuthor().getVoiceState().getChannel();

		if (vChan == null) {
			player.stopTrack();
		} else {
			info.getAuthor().getGuild().getAudioManager().openAudioConnection(vChan);
		}

		String THUMB = "https://img.youtube.com/vi/" + player.getPlayingTrack().getInfo().identifier +"/default.jpg";
		
		
		EmbedBuilder nextMusic = new EmbedBuilder();
		nextMusic.setTitle("🎶  Now playing ");
		nextMusic.setThumbnail(THUMB);
		nextMusic.setColor(Color.MAGENTA);
		nextMusic.addField("Music name: ", "```" + player.getPlayingTrack().getInfo().title + "```" , false);
		nextMusic.addField("Duration: " , "```" + getTimestamp(player.getPlayingTrack().getDuration()) + "```" , false);
		nextMusic.addField("Author: " , "```" + player.getPlayingTrack().getInfo().author + "```" , false);
		nextMusic.addField("Video link:" , "[Video here]("+player.getPlayingTrack().getInfo().uri+")" , false);

		
		
		
		long idMessage = msg.getChannel().sendMessage(nextMusic.build()).complete().getIdLong();
		msg.getTextChannel().getManager().setTopic("🎶  Now playing :: " + player.getPlayingTrack().getInfo().title).queue();
		msg.getChannel().addReactionById(idMessage, "ℹ").queue();
		msg.getChannel().addReactionById(idMessage, "⏹").queue();
		msg.getChannel().addReactionById(idMessage, "⏩").queue();
		
		List<Long> listOfMessages = UTILS.GUILDS.get(msg.getGuild()).getMessagesId();
		
		for (Long long1 : listOfMessages) {
			msg.getChannel().deleteMessageById(long1).queue();;
			UTILS.GUILDS.get(msg.getGuild()).getMessagesId().remove(long1);
		}
		UTILS.GUILDS.get(msg.getGuild()).getMessagesId().add(idMessage);
		
		
	}
	
	
	@Override
	public void onPlayerPause(AudioPlayer player) {
		super.onPlayerPause(player);
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		Guild g = queue.poll().getAuthor().getGuild();
		
		if (queue.isEmpty()) {
			msg.getTextChannel().getManager().setTopic("").queue();
			g.getAudioManager().closeAudioConnection();
		} else {
			player.playTrack(queue.element().getTrack());
		}
		
		List<Long> listOfMessages = UTILS.GUILDS.get(msg.getGuild()).getMessagesId();
		
		for (Long long1 : listOfMessages) {
			msg.getChannel().deleteMessageById(long1).queue();;
			UTILS.GUILDS.get(msg.getGuild()).getMessagesId().remove(long1);
		}
		
		
		
	}

	private String getTimestamp(long milis) {
		long seconds = milis / 1000;
		long hours = Math.floorDiv(seconds, 3600);
		seconds = seconds - (hours * 3600);
		long mins = Math.floorDiv(seconds, 60);
		seconds = seconds - (mins * 60);
		return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
	}

}
