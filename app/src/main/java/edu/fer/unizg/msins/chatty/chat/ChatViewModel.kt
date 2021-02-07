package edu.fer.unizg.msins.chatty.chat

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.fer.unizg.msins.chatty.login.User
import edu.fer.unizg.msins.chatty.cache.BadgeProvider
import edu.fer.unizg.msins.chatty.emotes.EmoteDescriptor
import edu.fer.unizg.msins.chatty.networking.GlobalEmoteDownloadTask
import edu.fer.unizg.msins.chatty.networking.LocalEmoteDownloadTask
import edu.fer.unizg.msins.chatty.emotes.services.twitchemotes.TwitchEmoteDescriptor
import edu.fer.unizg.msins.chatty.networking.*
import edu.fer.unizg.msins.chatty.networking.services.*
import edu.fer.unizg.msins.chatty.parsing.*
import kotlinx.coroutines.*
import org.pircbotx.hooks.types.GenericMessageEvent
import timber.log.Timber
import java.net.URL
import kotlin.concurrent.thread

class ChatViewModel(
    var channel: TwitchChannel
) : ViewModel() {

    companion object {
        var SIZE = 100
        var globalBttvDownloaded = false
        var globalTwitchEmotesDownloaded = false
    }

    private var _historyUpdated = MutableLiveData<List<ChatMessage>>()
    val historyUpdates: LiveData<List<ChatMessage>>
        get() = _historyUpdated

    private var history: ArrayList<ChatMessage> = ArrayList(SIZE)

    private var _sendMessageEvent = MutableLiveData<String>()
    val sendMessageEvent: LiveData<String>
        get() = _sendMessageEvent

    private val thread = thread(start = true) {
        channel.bot.startBot()
    }

    private var job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    private var lexer = Lexer()

    fun sendMessage(message: String) {
        _sendMessageEvent.value = message
        ioScope.launch {
            channel.bot.sendIRC().message("#${channel.name}", message)
        }
    }

    fun addMessage(event: GenericMessageEvent?) {
        event?.let {
            val elements = lexer.lex(it.message.toCharArray())
            val message = MessageContext(
                user = TwitchUser(
                    it.user,
                    it.v3Tags
                ),
                text = it.message,
                elements = elements,
                channel = this.channel.name
            )

            history.add(ChatMessage(message))

            _historyUpdated.postValue(
                if (history.size > SIZE) history.takeLast(SIZE) else history
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        thread.interrupt()
        job.cancel()
        Timber.d("VM cleared $this")
    }

    fun startDownloadingEmotes() {
        if (User.getCurrent() != User.ANONYMOUS) {
            ioScope.launch {
                val service = Retrofits.helix.create(HelixService::class.java)
                val response = service.getUserInfo(
                    "Bearer ${User.getCurrent().oAuth}",
                    channel.name
                )
                val streamerInfo = response.body()!!.data[0]
                getBttvEmotes(streamerInfo.id)
                getSubBadge(streamerInfo.id)
                getSubEmotes(streamerInfo.id)
                if (!globalTwitchEmotesDownloaded) {
                    getGlobalTwitchEmotes()
                }
            }

            ioScope.launch {
                val service = Retrofits.ffz.create(FfzService::class.java)
                val response = service.getChannelInfo(channel.name)
                response.ifSuccessful {
                    val ffzDto = response.body() ?: return@ifSuccessful
                    val emotes = ffzDto.getEmoteDescriptors()
                    for (emote in emotes) {
                        DownloadScheduler.queue.add(LocalEmoteDownloadTask(emote, channel.name))
                    }

                    lexer.addWordHandler(emotes.map { it.name }, WordHandlers.EMOTE)
                }
            }
        }


        if (!globalBttvDownloaded) {
            ioScope.launch {
                val service = Retrofits.bttv.create(BttvService::class.java)
                val response = service.getGlobalEmotes()
                response.ifSuccessful {
                    val emotes = response.body()!!
                    for (emote in emotes) {
                        DownloadScheduler.queue.add(GlobalEmoteDownloadTask(emote, channel.name))
                    }
                    lexer.addWordHandler(emotes.map { it.name }, WordHandlers.EMOTE)
                    globalBttvDownloaded = true
                }
            }
        }
    }

    fun startIrcBot() {
        if (!thread.isAlive) {
            Timber.d("VM irc thread started $this")
            thread.start()
        } else {
            Timber.d("VM irc thread already up $this")
        }
    }

    private suspend fun getSubEmotes(channelId: String) = withContext(Dispatchers.IO) {
        val service = Retrofits.twitchEmotes.create(TwitchEmotesService::class.java)
        val response = service.getChannelEmotes(channelId)
        response.ifSuccessful { emotesDto ->
            for (emote in emotesDto.emotes) {
                val descriptor = TwitchEmoteDescriptor(
                    emote.id.toString(),
                    emote.name,
                    EmoteDescriptor.Type.STATIC
                )
                DownloadScheduler.queue.add(LocalEmoteDownloadTask(descriptor, channel.name))
            }
            lexer.addWordHandler(emotesDto.emotes.map { it.name }, WordHandlers.EMOTE)
        }
    }

    private suspend fun getGlobalTwitchEmotes() = withContext(Dispatchers.IO) {
        val service = Retrofits.twitchEmotes.create(TwitchEmotesService::class.java)
        val response = service.getChannelEmotes("0")
        response.ifSuccessful { emotesDto ->
            for (emote in emotesDto.emotes) {
                val descriptor = TwitchEmoteDescriptor(
                    emote.id.toString(),
                    emote.name,
                    EmoteDescriptor.Type.STATIC
                )
                DownloadScheduler.queue.add(GlobalEmoteDownloadTask(descriptor, channel.name))
            }
            lexer.addWordHandler(emotesDto.emotes.map { it.name }, WordHandlers.EMOTE)
            globalTwitchEmotesDownloaded = true
        }
    }

    private suspend fun getSubBadge(channelId: String) = withContext(Dispatchers.IO) {
        val service = Retrofits.badges.create(BadgeService::class.java)
        val response = service.getSubBadges(channelId)
        response.ifSuccessful { badgeSet ->
            if (badgeSet.sets.isEmpty()) return@ifSuccessful
            val subscriberSet = badgeSet.sets["subscriber"]
            val version = subscriberSet?.ver ?: return@ifSuccessful
            BadgeProvider.addVersions(channel.name, version.keys.map { it.toInt() })
            for ((ver, url) in version) {
                URL(url.imageUrl).openStream().use { stream ->
                    val badge = Bitmap.createScaledBitmap(
                        BitmapFactory.decodeStream(stream),
                        112,
                        112,
                        true
                    )
                    BadgeProvider.saveBadge(badge, channel.name, ver.toInt())
                }
            }
        }
    }

    private suspend fun getBttvEmotes(channelId: String) = withContext(Dispatchers.IO) {
        val service = Retrofits.bttv.create(BttvService::class.java)
        val response = service.getChannelEmotes(channelId)
        response.ifSuccessful { bttv ->
            for (emote in bttv.sharedEmotes.plus(bttv.channelEmotes)) {
                DownloadScheduler.queue.add(LocalEmoteDownloadTask(emote, channel.name))
            }
            val emotes = bttv.sharedEmotes + bttv.channelEmotes
            lexer.addWordHandler(
                emotes.filter { it.type == EmoteDescriptor.Type.GIF }.map { it.name },
                WordHandlers.GIF
            )
            lexer.addWordHandler(
                emotes.filter { it.type == EmoteDescriptor.Type.STATIC }.map { it.name },
                WordHandlers.EMOTE
            )
        }
    }
}