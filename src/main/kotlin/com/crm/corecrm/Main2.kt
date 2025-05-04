import com.sun.mail.imap.IMAPFolder
import com.sun.mail.imap.IMAPStore
import jakarta.mail.*
import jakarta.mail.event.MessageCountAdapter
import jakarta.mail.event.MessageCountEvent
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import jakarta.mail.search.FlagTerm

class YandexMailClient(
    private val username: String,
    private val password: String
) {
    private val imapProps = Properties().apply {
        put("mail.store.protocol", "imaps")
        put("mail.imaps.host", "imap.yandex.ru")
        put("mail.imaps.port", "993")
        put("mail.imaps.ssl.enable", "true")
        put("mail.imaps.ssl.trust", "*")
    }

    private val smtpProps = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.host", "smtp.yandex.ru")
        put("mail.smtp.port", "465")
        put("mail.smtp.ssl.enable", "true")
        put("mail.smtp.ssl.trust", "*")
    }

    private val imapSession = Session.getInstance(imapProps)
    private val smtpSession = Session.getDefaultInstance(smtpProps, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(username, password)
        }
    })

    private val dispatcher = Executors.newSingleThreadScheduledExecutor()
    private var folder: IMAPFolder? = null
    private var store: IMAPStore? = null

    fun start() {
        println("Starting Yandex Mail client for $username")
        connectAndListen()
        dispatcher.scheduleAtFixedRate(
            { checkMailbox() },
            0,
            30,
            TimeUnit.SECONDS
        )
    }

    private fun connectAndListen() {
        try {
            // Connect to the mailbox
            store = imapSession.getStore("imaps") as IMAPStore
            store?.connect("imap.yandex.ru", username, password)

            folder = store?.getFolder("INBOX") as IMAPFolder
            folder?.open(Folder.READ_WRITE)

            // Set up listener for new messages
            folder?.addMessageCountListener(object : MessageCountAdapter() {
                override fun messagesAdded(e: MessageCountEvent) {
                    e.messages.forEach { message ->
                        processMessage(message)
                    }
                }
            })

            println("Connected to inbox: ${folder?.fullName}")
        } catch (e: Exception) {
            println("Error connecting to Yandex Mail: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun checkMailbox() {
        try {
            if (folder == null || !folder!!.isOpen) {
                println("Reconnecting to mailbox...")
                connectAndListen()
            }

            // Search for unread messages
            val unreadMessages = folder?.search(FlagTerm(Flags(Flags.Flag.SEEN), false)) ?: emptyArray()

            println("Found ${unreadMessages.size} unread messages")
            unreadMessages.forEach { message ->
                processMessage(message)
            }
        } catch (e: Exception) {
            println("Error checking mailbox: ${e.message}")
            e.printStackTrace()

            // Try to reconnect
            try {
                folder?.close(false)
                store?.close()
            } catch (_: Exception) {}

            connectAndListen()
        }
    }

    private fun processMessage(message: Message) {
        try {
            // Extract message details
            val subject = message.subject ?: "(No subject)"
            val sender = (message.from?.firstOrNull() as? InternetAddress)?.address ?: "unknown"
            val sentDate = message.sentDate ?: Date()

            // Get the message content
            val content = extractContent(message)

            println("\n====== NEW MESSAGE ======")
            println("From: $sender")
            println("Date: $sentDate")
            println("Subject: $subject")
            println("Content: $content")
            println("=========================\n")

            // Mark as read
            message.setFlag(Flags.Flag.SEEN, true)

            // Reply with "ok"
            replyToMessage(message)
        } catch (e: Exception) {
            println("Error processing message: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun extractContent(message: Message): String {
        return try {
            when (message.contentType?.split(";")?.get(0)?.lowercase()) {
                "text/plain" -> message.content as String
                "multipart/alternative", "multipart/mixed" -> {
                    val multipart = message.content as Multipart
                    val stringBuilder = StringBuilder()

                    for (i in 0 until multipart.count) {
                        val part = multipart.getBodyPart(i)
                        if (part.contentType.startsWith("text/plain")) {
                            stringBuilder.append(part.content as String)
                        }
                    }

                    stringBuilder.toString()
                }
                else -> "(Content type not supported: ${message.contentType})"
            }
        } catch (e: Exception) {
            "(Error extracting content: ${e.message})"
        }
    }

    private fun replyToMessage(originalMessage: Message) {
        try {
            val reply = MimeMessage(smtpSession)

            // Set reply details
            val sender = (originalMessage.from?.firstOrNull() as? InternetAddress)?.address ?: return
            reply.setFrom(InternetAddress("$username@yandex.ru"))
            reply.addRecipient(Message.RecipientType.TO, InternetAddress(sender))
            reply.subject = "Re: ${originalMessage.subject}"
            reply.setText("ok")

            // Set reply headers
            val messageId = originalMessage.getHeader("Message-ID")
            if (messageId != null && messageId.isNotEmpty()) {
                reply.setHeader("In-Reply-To", messageId[0])
                reply.setHeader("References", messageId[0])
            }

            // Send the reply
            Transport.send(reply)
            println("Sent reply to $sender with content: ok")
        } catch (e: Exception) {
            println("Error sending reply: ${e.message}")
            e.printStackTrace()
        }
    }

    fun stop() {
        try {
            dispatcher.shutdown()
            folder?.close(false)
            store?.close()
            println("Yandex Mail client stopped")
        } catch (e: Exception) {
            println("Error stopping client: ${e.message}")
        }
    }
}

fun main() {
    val username = "matveynnikishaev"
    val password = "gwcuyhegqgjuegse"

    val client = YandexMailClient(username, password)

    Runtime.getRuntime().addShutdownHook(Thread {
        println("Shutting down Yandex Mail client...")
        client.stop()
    })

    client.start()

    runBlocking {
        println("Yandex Mail client is running. Press Ctrl+C to stop.")
        while (true) {
            delay(1000)
        }
    }
}
