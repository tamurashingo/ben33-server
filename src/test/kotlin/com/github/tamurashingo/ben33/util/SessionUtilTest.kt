package com.github.tamurashingo.ben33.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test


class SessionUtilTest {

    @Rule @JvmField val kotlinJsr310TimeRule = KotlinJsr310TimeRule()

    /**
     * 正常系
     */
    @Test
    fun testIsValidSuccess() {
        val sessionUtil: SessionUtil = SessionUtil("password", 30)
        val token: String = sessionUtil.genToken("test@test.com")

        assertThat(sessionUtil.isValid(token)).isEqualTo(SessionUtil.ErrorCode.NONE)

        val (info, errorCode) = sessionUtil.getInfo(token)

        assertThat(errorCode).isEqualTo(SessionUtil.ErrorCode.NONE)
        assertThat(info).isEqualTo("test@test.com")
    }

    /**
     * パスワード間違い
     */
    @Test
    fun testIsValidNotCorrectPassword() {
        val sessionUtil: SessionUtil = SessionUtil("password", 30)
        val token: String = sessionUtil.genToken("test@test.com")

        val anotherSessionUtil: SessionUtil = SessionUtil("p@ssword", 30)

        assertThat(anotherSessionUtil.isValid(token)).isEqualTo(SessionUtil.ErrorCode.SIGNATURE)
    }

    /**
     * タイムアウト
     */
    @Test
    @KotlinJsr310TimeRule.Now(2017, 1, 1, 0, 0, 0)
    fun testIsValidTimeout() {
        val sessionUtil: SessionUtil = SessionUtil("password", 30)
        val token: String = sessionUtil.genToken("test@test.com")

        assertThat(sessionUtil.isValid(token)).isEqualTo(SessionUtil.ErrorCode.EXPIRED)
    }


    /**
     * 変な文字列
     */
    @Test
    fun testIsValidMalformedString() {
        val sessionUtil: SessionUtil = SessionUtil("password", 30)

        assertThat(sessionUtil.isValid("Malformed String")).isEqualTo(SessionUtil.ErrorCode.MALFORMED)
    }

}
