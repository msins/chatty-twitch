package edu.fer.unizg.msins.chatty.chat

import android.graphics.Bitmap
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.DynamicDrawableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import edu.fer.unizg.msins.chatty.R
import edu.fer.unizg.msins.chatty.cache.BadgeProvider
import edu.fer.unizg.msins.chatty.cache.EmoteProvider
import edu.fer.unizg.msins.chatty.parsing.MessageContext
import edu.fer.unizg.msins.chatty.parsing.Token
import kotlinx.android.synthetic.main.chat_message.view.*
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.MultiCallback

open class ChatMessage(var messageContext: MessageContext) :
    AbstractItem<ChatMessage.ViewHolder>() {

    override val type: Int
        get() = R.id.chat_message

    override val layoutRes: Int
        get() = R.layout.chat_message

    private var gifCallback = MultiCallback(true)

    class ViewHolder(var view: View) : FastAdapter.ViewHolder<ChatMessage>(view) {
        private var message: TextView = view.chat_message

        //remove logic from bind...
        override fun bindView(item: ChatMessage, payloads: List<Any>) {
            val messageContext = item.messageContext

            val builder = Builder()
            if (messageContext.user.isSubscriber()) {
                val badge = BadgeProvider.getBadge(
                    messageContext.channel,
                    messageContext.user.getBadgeInfo()
                )
                if (badge != null) {
                    builder.appendSubBadge(badge)
                    builder.appendWord(" ")
                }
            }
            builder.appendUsername(
                messageContext.user.getDisplayName(),
                messageContext.user.getColor()
            )
            builder.appendWord(": ")
            var multipleGifs = false
            for (token in messageContext.elements) {
                when (token.type) {
                    Token.Type.WORD -> {
                        builder.appendWord("${token.value} ")
                    }
                    Token.Type.EMOTE -> {
                        val emote =
                            EmoteProvider.forChannel(messageContext.channel).getEmote(token.value)
                        if (emote != null) {
                            builder.appendEmote(emote)
                        }
                    }
                    Token.Type.GIF -> {
                        val gif =
                            EmoteProvider.forChannel(messageContext.channel).getGif(token.value)
                        if (gif != null) {
                            val drawable = GifDrawable(gif)
                            val adjustedWidth =
                                (56 / drawable.intrinsicHeight.toDouble() * drawable.intrinsicWidth).toInt()
                            drawable.setBounds(0, 0, 56, adjustedWidth)
                            builder.appendGif(drawable)
                            drawable.callback = item.gifCallback
                            drawable.start()
                            if (!multipleGifs) {
                                item.gifCallback.addView(this.message)
                                multipleGifs = true
                            }
                        }
                    }
                }

                message.setText(builder.build(), TextView.BufferType.SPANNABLE)
            }
        }

        override fun unbindView(item: ChatMessage) {
            message.text = null
            item.gifCallback.removeView(message)
        }
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    override fun equals(other: Any?): Boolean {
        other as ChatMessage
        if (messageContext != other.messageContext) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + messageContext.hashCode()
        return result
    }

    private class Builder {
        val spanBuilder = SpannableStringBuilder()

        fun appendSubBadge(badge: Bitmap) {
            val span = SpannableString(" ")
            span.setSpan(
                ImageSpan(badge, DynamicDrawableSpan.ALIGN_CENTER),
                0,
                1,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            spanBuilder.append(span)
        }

        fun appendUsername(name: CharSequence, color: Int? = null): Builder {
            val span = SpannableString(name)
            if (color != null) {
                span.setSpan(
                    ForegroundColorSpan(color),
                    0,
                    span.length,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                )
            }

            span.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                span.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            spanBuilder.append(span)
            return this
        }

        fun appendWord(text: CharSequence) {
            spanBuilder.append(text)
        }

        fun appendEmote(emote: Bitmap) {
            val span = SpannableString(" ")
            span.setSpan(
                ImageSpan(emote, DynamicDrawableSpan.ALIGN_CENTER),
                0,
                1,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            spanBuilder.append(span)
        }

        fun appendGif(gif: GifDrawable) {
            val span = SpannableString(" ")
            span.setSpan(
                ImageSpan(gif, DynamicDrawableSpan.ALIGN_CENTER),
                0,
                1,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            spanBuilder.append(span)
        }

        fun build(): SpannableStringBuilder {
            return spanBuilder
        }
    }
}