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
package com.github.tamurashingo.ben33.entity

import com.github.tamurashingo.ben33.util.Jsr310Time
import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import java.io.Serializable
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.LocalDateTime
import java.util.*

/**
 * ユーザ情報
 *
 */
data class User (
        /** ユーザを一意に特定するためのPrimaryKey */
        var userId: Integer? = null,

        /** 登録メールアドレス */
        var email: String,

        /** UUID */
        var uuid: String,

        /** ユーザ名 */
        var username: String? = null,

        /** 登録方法 */
        var provider: PROVIDER,

        /** ステータス */
        var status: STATUS,

        /** 作成日時 */
        var createDate: LocalDateTime = Jsr310Time.getDateTime(),

        /** 更新日時 */
        var updateDate: LocalDateTime = Jsr310Time.getDateTime(),

        /** 本登録日時 */
        var registerDate: LocalDateTime? = null

) : Serializable {

    /**
     * 登録方法
     */
    enum class PROVIDER(val provider: String) {
        WEB("0")
    }

    /**
     * 登録方法のenum値とデータベース値を変換
     */
    class PROVIDERHandler : BaseTypeHandler<PROVIDER>() {

        override fun setNonNullParameter(ps: PreparedStatement?, i: Int, parameter: PROVIDER?, jdbcType: JdbcType?) {
            System.out.println("called here")
            ps?.setString(i, parameter?.provider ?: PROVIDER.WEB.provider)
        }

        override fun getNullableResult(cs: CallableStatement?, columnIndex: Int): PROVIDER {
            return typeChanger(cs?.getString(columnIndex))
        }

        override fun getNullableResult(rs: ResultSet?, columnIndex: Int): PROVIDER {
            return typeChanger(rs?.getString(columnIndex))
        }

        override fun getNullableResult(rs: ResultSet?, columnName: String?): PROVIDER {
            return typeChanger(rs?.getString(columnName))
        }

        fun typeChanger(dbVal: String?): PROVIDER {
            return try {
                PROVIDER.values().filter { provider -> provider.provider == dbVal }.first()
            }
            catch (ex: NoSuchElementException) {
                // DBに無効な値が入っていたときはとりあえずWEBを返す
                PROVIDER.WEB
            }
        }
    }

    /**
     * ステータス
     */
    enum class STATUS(val status: String) {
        /// 無効
        INVALID("0"),
        /// 登録済み
        REGISTERED("1"),
        /// 仮登録中
        PROVISIONAL("2")
    }

    /**
     * ステータスのenum値とデータベース値を変換
     */
    class STATUSHandler : BaseTypeHandler<STATUS>() {
        override fun setNonNullParameter(ps: PreparedStatement?, i: Int, parameter: STATUS?, jdbcType: JdbcType?) {
            ps?.setString(i, parameter?.status ?: STATUS.INVALID.status)
        }

        override fun getNullableResult(cs: CallableStatement?, columnIndex: Int): STATUS {
            return typeChanger(cs?.getString(columnIndex))
        }

        override fun getNullableResult(rs: ResultSet?, columnIndex: Int): STATUS {
            return typeChanger(rs?.getString(columnIndex))
        }

        override fun getNullableResult(rs: ResultSet?, columnName: String?): STATUS {
            return typeChanger(rs?.getString(columnName))
        }

        fun typeChanger(dbVal: String?): STATUS {
            return try {
                STATUS.values().filter { status -> status.status == dbVal }.first()
            }
            catch (ex: NoSuchElementException) {
                // DBに変な値が入っていたときはすべて無効とする
                STATUS.INVALID
            }
        }
    }
}

