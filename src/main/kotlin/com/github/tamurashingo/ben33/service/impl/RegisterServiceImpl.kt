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
package com.github.tamurashingo.ben33.service.impl

import com.github.tamurashingo.ben33.bean.RegisterAPIRequest
import com.github.tamurashingo.ben33.bean.RegisterAPIResponse
import com.github.tamurashingo.ben33.entity.User
import com.github.tamurashingo.ben33.repository.UserRepository
import com.github.tamurashingo.ben33.service.RegisterService
import com.github.tamurashingo.ben33.util.Jsr310Time
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * ユーザ情報登録サービス実装クラス
 */
@Service
open class RegisterServiceImpl(val userRepository: UserRepository, val msg: MessageSource) : RegisterService {

    private val LOGGER: Logger = LoggerFactory.getLogger(RegisterService::class.java)

    @Transactional
    override fun provisionalRegister(request: RegisterAPIRequest): RegisterAPIResponse {

        val registeredUser = userRepository.findRegisteredByEmail(request.email)

        return if (registeredUser != null) {
            LOGGER.debug("すでに登録済み")
            RegisterAPIResponse(false, msg.getMessage("user.error.alreadyregistered", kotlin.arrayOfNulls(0), Locale.getDefault()))
        }
        else {
            var user = userRepository.findProvisionalByEmail(request.email)
            if (user == null) {
                LOGGER.debug("初回登録")
                val uuid = UUID.nameUUIDFromBytes(request.email.toByteArray()).toString()
                user = User(null, request.email, uuid, request.username, User.PROVIDER.WEB, User.STATUS.PROVISIONAL)
                userRepository.insertUser(user)
            }
            else {
                LOGGER.debug("仮登録中の更新")
                user.username = request.username
                user.updateDate = Jsr310Time.getDateTime()
                userRepository.updateUser(user)
            }

            RegisterAPIResponse(true, msg.getMessage("user.info.provisional", kotlin.arrayOfNulls(0), Locale.getDefault()))
        }
    }
}

