package com.github.tamurashingo.ben33.util.impl

import com.github.tamurashingo.ben33.util.Jsr310Time
import com.github.tamurashingo.ben33.util.MailUtil
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.subethamail.wiser.Wiser

class MailUtilSendmailImplTest {

    lateinit var wiser: Wiser

    @Before
    fun before() {
        wiser = Wiser()
        wiser.setPort(2500)
        wiser.setHostname("com.github.tamurashingo.ben33")
        wiser.start()
    }

    @After
    fun after() {
        wiser.stop()
    }

    @Test
    fun testMailSuccess() {
        val mailUtil = MailUtilSendmailImpl("localhost", "2500")
        val mailUtilBean = MailUtil.MailUtilBean(
                "tamura.shingo+toaddr@gmail.com",
                "tamura.shingo+fromaddr@gmail.com",
                Jsr310Time.getDateTime(),
                "件名",
                "メール本文"
        )
        mailUtil.sendMail(mailUtilBean)

        val messages = wiser.messages
        assertThat(messages.size).isEqualTo(1)

        val msg = messages[0]
        assertThat(msg.envelopeSender).isEqualTo("tamura.shingo+fromaddr@gmail.com")
        assertThat(msg.envelopeReceiver).isEqualTo("tamura.shingo+toaddr@gmail.com")
    }


    @Test
    fun testInvalidFromAddress() {
        val mailUtil = MailUtilSendmailImpl("localhost", "2500")
        val mailUtilBean = MailUtil.MailUtilBean(
                "tamura.shingo+toaddr@gmail.com",
                "tamura.shingo+fromaddr@",
                Jsr310Time.getDateTime(),
                "件名",
                "メール本文"
        )
        try {
            mailUtil.sendMail(mailUtilBean)
            fail("reach unexpected")
        }
        catch (ex: MailUtil.FromAddrException) {
            // ok
        }
    }

    @Test
    fun testInvalidToAddress() {
        val mailUtil = MailUtilSendmailImpl("localhost", "2500")
        val mailUtilBean = MailUtil.MailUtilBean(
                "tamura.shingo+toaddr@",
                "tamura.shingo+fromaddr@gmail.com",
                Jsr310Time.getDateTime(),
                "件名",
                "メール本文"
        )
        try {
            mailUtil.sendMail(mailUtilBean)
            fail("reach unexpected")
        }
        catch (ex: MailUtil.ToAddrException) {
            // ok
        }
    }


}
