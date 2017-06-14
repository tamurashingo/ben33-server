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
package com.github.tamurashingo.ben33.util

import io.jsonwebtoken.*
import java.time.LocalDateTime
import java.util.*

/**
 * @property password トークン暗号化用パスワード
 * @property timeout タイムアウト(分)
 */
class SessionUtil(val password: String, val timeout: Long) {

    enum class ErrorCode(val code: Int) {
        /** なし */
        NONE(0),
        /** 時間切れ */
        EXPIRED(1),
        /** パスワード無効 */
        SIGNATURE(2),
        /** 無効なJWT */
        MALFORMED(3),
        /** その他 */
        OTHER(999)
    }

    /**
     * セッション情報を生成する
     *
     * @param info 情報
     * @return セッション情報
     */
    fun genToken(info: String) : String = Jwts.builder()
            .setExpiration(genExpire())
            .setSubject(info)
            .signWith(SignatureAlgorithm.HS512, password)
            .compact()


    data class Info(val session: String?, val errorCode: ErrorCode)
    /**
     * セッション情報から格納されている情報を取得する
     *
     * @param セッション情報
     * @return 格納されている情報
     */
    fun getInfo(session: String): Info = try {
        Info(Jwts.parser()
                .setSigningKey(password)
                .parseClaimsJws(session)
                .body
                .subject,
                ErrorCode.NONE)
    }
    catch (ex: ExpiredJwtException) {
        Info(null, ErrorCode.EXPIRED)
    }
    catch (ex: UnsupportedJwtException) {
        Info(null, ErrorCode.OTHER)
    }
    catch (ex: MalformedJwtException) {
        Info(null, ErrorCode.MALFORMED)
    }
    catch (ex: SignatureException) {
        Info(null, ErrorCode.SIGNATURE)
    }

    fun isValid(session: String): ErrorCode = try {
        Jwts.parser()
                .setSigningKey(password)
                .parseClaimsJws(session)
        ErrorCode.NONE
    }
    catch (ex: ExpiredJwtException) {
        ErrorCode.EXPIRED
    }
    catch (ex: UnsupportedJwtException) {
        ErrorCode.OTHER
    }
    catch (ex: MalformedJwtException) {
        ErrorCode.MALFORMED
    }
    catch (ex: SignatureException) {
        ErrorCode.SIGNATURE
    }

    /**
     * 現在日時を基準にしたタイムアウト日時を取得する
     *
     * @return タイムアウト日時
     */
    fun genExpire() : Date = getExpire(Jsr310Time.getDateTime())

    /**
     * 指定日時を基準にしたタイムアウト日時を取得する
     *
     * @param ldt 指定日時
     * @return タイムアウト日時
     */
    fun getExpire(ldt: LocalDateTime) : Date = Date.from(ldt.plusMinutes(timeout).atZone(Jsr310Time.TIME_ZONE).toInstant())
}

