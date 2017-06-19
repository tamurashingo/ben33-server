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

import java.util.*
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session

/**
 * Gmailのようにパスワード認証つきのSMTPに対する実装
 */
class MailUtilGMailImpl(
        /** SMTPサーバ */
        override val smtpHost: String,
        /** SMTPポート番号 */
        override val smtpPort: String,
        /** ユーザ名 */
        val gmailUserName: String,
        /** パスワード */
        val gmailPassword: String) : MailUtilSendmailImpl(smtpHost, smtpPort) {

    override fun genSession(): Session {
        val prop = Properties()
        prop.put("mail.smtp.host", smtpHost)
        prop.put("mail.smtp.port", smtpPort)
        return Session.getInstance(prop, genAuthenticator())
    }

    fun genAuthenticator(): Authenticator {
        return object : Authenticator() {
            @Override
            protected override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(gmailUserName, gmailPassword)
            }
        }
    }
}
