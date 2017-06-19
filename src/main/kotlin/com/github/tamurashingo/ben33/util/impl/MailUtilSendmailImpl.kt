/*-
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 tamura shingo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.tamurashingo.ben33.util.impl

import com.github.tamurashingo.ben33.util.Jsr310Time
import com.github.tamurashingo.ben33.util.MailUtil
import java.time.LocalDateTime
import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


/**
 * sendmailに向けてメール送信を行う
 */
open class MailUtilSendmailImpl(
        /** SMTPサーバ */
        open val smtpHost: String,
        /** SMTPポート番号 */
        open val smtpPort: String) : MailUtil {

    @Throws(MailUtil.MailUtilException::class)
    override fun sendMail(mailUtilBean: MailUtil.MailUtilBean) {
        val message = genMimeMessage()
        setFromAddr(message, mailUtilBean.fromAddr)
        setToAddr(message, mailUtilBean.toAddr)
        setSentDate(message, mailUtilBean.sentDate)
        setSubject(message, mailUtilBean.subject)
        setContent(message, mailUtilBean.content)

        try {
            Transport.send(message)
        }
        catch (ex:MessagingException) {
            throw MailUtil.MailUtilException("メール送信に失敗しました", ex)
        }
    }

    @Throws(MailUtil.FromAddrException::class, MailUtil.MailUtilException::class)
    fun setFromAddr(message: MimeMessage, fromAddr: String) {
        try {
            message.setFrom(InternetAddress(fromAddr))
        }
        catch (ex: AddressException) {
            throw MailUtil.FromAddrException("送信元アドレスの書式が間違っています:${fromAddr}", ex)
        }
        catch (ex: MessagingException) {
            throw MailUtil.MailUtilException("送信元アドレスの設定に失敗しました", ex)
        }
    }

    @Throws(MailUtil.ToAddrException::class, MailUtil.MailUtilException::class)
    fun setToAddr(message: MimeMessage, toAddr: String) {
        try {
            message.setRecipient(Message.RecipientType.TO, InternetAddress(toAddr))
        }
        catch (ex: AddressException) {
            throw MailUtil.ToAddrException("送信先アドレスの書式が間違っています:${toAddr}", ex)
        }
        catch (ex: MessagingException) {
            throw MailUtil.MailUtilException("送信先アドレスの設定に失敗しました", ex)
        }
    }

    @Throws(MailUtil.MailUtilException::class)
    fun setSentDate(message: MimeMessage, dt: LocalDateTime) {
        val zdt = dt.atZone(Jsr310Time.TIME_ZONE)
        val date = Date.from(zdt.toInstant())
        try {
            message.setSentDate(date)
        }
        catch (ex: MessagingException) {
            throw MailUtil.MailUtilException("送信日時の設定に失敗しました", ex)
        }
    }

    @Throws(MailUtil.MailUtilException::class)
    fun setSubject(message: MimeMessage, subject: String) {
        try {
            message.setSubject(subject)
        }
        catch (ex: MessagingException) {
            throw MailUtil.MailUtilException("件名の設定に失敗しました", ex)
        }
    }

    @Throws(MailUtil.MailUtilException::class)
    fun setContent(message: MimeMessage, content: String) {
        try {
            message.setText(content)
        }
        catch (ex: MessagingException) {
            throw MailUtil.MailUtilException("本文の設定に失敗しました", ex)
        }
    }

    fun genMimeMessage() : MimeMessage {
        return MimeMessage(genSession())
    }

    open fun genSession(): Session {
        val prop = Properties()
        prop.put("mail.smtp.host", smtpHost)
        prop.put("mail.smtp.port", smtpPort)
        return Session.getInstance(prop)
    }
}
