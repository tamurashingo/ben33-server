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
package com.github.tamurashingo.ben33.controller

import com.github.tamurashingo.ben33.bean.RegisterAPIRequest
import com.github.tamurashingo.ben33.bean.RegisterAPIResponse
import com.github.tamurashingo.ben33.service.RegisterService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.http.MediaType
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid


/**
 * ユーザ登録用コントローラ
 *
 * @property registerService 登録サービス
 * @property message メッセージ
 */
@RestController
@RequestMapping(value = "/api/register")
class RegisterAPIController(val registerService: RegisterService, val message: MessageSource) {

    /**
     * 仮登録
     *
     * @param form 登録リクエスト
     * @param result リクエストの中身のValidation結果
     * @return 仮登録結果
     */
    @RequestMapping(method = arrayOf(RequestMethod.POST), consumes = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE), produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun provisionalRegister(@Valid @RequestBody form: RegisterAPIRequest, result: BindingResult): RegisterAPIResponse {
        return if (result.hasErrors()) {
            RegisterAPIResponse(false, message.getMessage("common.error.invalidvalues", kotlin.arrayOfNulls(0), Locale.getDefault()))
        }
        else {
            registerService.provisionalRegister(form)
        }
    }
}

