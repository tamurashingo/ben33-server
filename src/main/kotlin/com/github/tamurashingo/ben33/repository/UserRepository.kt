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
package com.github.tamurashingo.ben33.repository

import com.github.tamurashingo.ben33.entity.User
import org.apache.ibatis.annotations.*
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

/**
 * ユーザ情報のDAO
 */
@Repository
@Mapper
interface UserRepository {

    /**
     * 仮登録のユーザ情報をメールアドレスをキーに取得する
     */
    @Select("""
 select
   user_id,
   email,
   uuid,
   username,
   provider,
   status,
   create_date,
   update_date,
   register_date
 from
   m_user
 where
   email = #{email}
 and
   status = ${'$'}{@com.github.tamurashingo.ben33.entity.User${'$'}STATUS@PROVISIONAL.status}
    """)
    @ConstructorArgs(
            Arg(id = true, column = "user_id", javaType = Integer::class),
            Arg(column = "email", javaType = String::class),
            Arg(column = "uuid", javaType = String::class),
            Arg(column = "username", javaType = String::class),
            Arg(column = "provider", javaType = com.github.tamurashingo.ben33.entity.User.PROVIDER::class, typeHandler = com.github.tamurashingo.ben33.entity.User.PROVIDERHandler::class),
            Arg(column = "status", javaType = com.github.tamurashingo.ben33.entity.User.STATUS::class, typeHandler = com.github.tamurashingo.ben33.entity.User.STATUSHandler::class),
            Arg(column = "create_date", javaType = LocalDateTime::class, typeHandler = org.apache.ibatis.type.LocalDateTimeTypeHandler::class),
            Arg(column = "update_date", javaType = LocalDateTime::class, typeHandler = org.apache.ibatis.type.LocalDateTimeTypeHandler::class),
            Arg(column = "register_date", javaType = LocalDateTime::class, typeHandler = org.apache.ibatis.type.LocalDateTimeTypeHandler::class)
    )
    fun findProvisionalByEmail(@Param("email") email: String): User?

    /**
     * 登録済のユーザ情報をメールアドレスをキーに取得する
     */
    @Select("""
 select
   user_id,
   email,
   uuid,
   username,
   provider,
   status,
   create_date,
   update_date,
   register_date
 from
   m_user
 where
   email = #{email}
 and
   status = ${'$'}{@com.github.tamurashingo.ben33.entity.User${'$'}STATUS@REGISTERED.status}
    """)
    @ConstructorArgs(
            Arg(id = true, column = "user_id", javaType = Integer::class),
            Arg(column = "email", javaType = String::class),
            Arg(column = "uuid", javaType = String::class),
            Arg(column = "username", javaType = String::class),
            Arg(column = "provider", javaType = com.github.tamurashingo.ben33.entity.User.PROVIDER::class, typeHandler = com.github.tamurashingo.ben33.entity.User.PROVIDERHandler::class),
            Arg(column = "status", javaType = com.github.tamurashingo.ben33.entity.User.STATUS::class, typeHandler = com.github.tamurashingo.ben33.entity.User.STATUSHandler::class),
            Arg(column = "create_date", javaType = LocalDateTime::class, typeHandler = org.apache.ibatis.type.LocalDateTimeTypeHandler::class),
            Arg(column = "update_date", javaType = LocalDateTime::class, typeHandler = org.apache.ibatis.type.LocalDateTimeTypeHandler::class),
            Arg(column = "register_date", javaType = LocalDateTime::class, typeHandler = org.apache.ibatis.type.LocalDateTimeTypeHandler::class)
    )
    fun findRegisteredByEmail(@Param("email") email: String): User?

    /**
     * 仮登録のユーザ情報をUUIDをキーに取得する
     */
    @Select("""
 select
   user_id,
   email,
   uuid,
   username,
   provider,
   status,
   create_date,
   update_date,
   register_date
 from
   m_user
 where
   uuid = #{uuid}
 and
   status = ${'$'}{@com.github.tamurashingo.ben33.entity.User${'$'}STATUS@PROVISIONAL.status}
    """)
    @ConstructorArgs(
            Arg(id = true, column = "user_id", javaType = Integer::class),
            Arg(column = "email", javaType = String::class),
            Arg(column = "uuid", javaType = String::class),
            Arg(column = "username", javaType = String::class),
            Arg(column = "provider", javaType = com.github.tamurashingo.ben33.entity.User.PROVIDER::class, typeHandler = com.github.tamurashingo.ben33.entity.User.PROVIDERHandler::class),
            Arg(column = "status", javaType = com.github.tamurashingo.ben33.entity.User.STATUS::class, typeHandler = com.github.tamurashingo.ben33.entity.User.STATUSHandler::class),
            Arg(column = "create_date", javaType = LocalDateTime::class, typeHandler = org.apache.ibatis.type.LocalDateTimeTypeHandler::class),
            Arg(column = "update_date", javaType = LocalDateTime::class, typeHandler = org.apache.ibatis.type.LocalDateTimeTypeHandler::class),
            Arg(column = "register_date", javaType = LocalDateTime::class, typeHandler = org.apache.ibatis.type.LocalDateTimeTypeHandler::class)
    )
    fun findProvisionalByUuid(@Param("uuid") uuid: String): User?


    /**
     * 登録済のユーザ情報をUUIDをキーに取得する
     */
    @Select("""
 select
   user_id,
   email,
   uuid,
   username,
   provider,
   status,
   create_date,
   update_date,
   register_date
 from
   m_user
 where
   uuid = #{uuid}
 and
   status = ${'$'}{@com.github.tamurashingo.ben33.entity.User${'$'}STATUS@REGISTERED.status}
    """)
    fun findRegisteredByUuid(@Param("uuid") uuid: String): User?


    /**
     * ユーザ情報を登録する
     */
    @Insert("""
insert into
  m_user
(
  email,
  uuid,
  username,
  provider,
  status,
  create_date,
  update_date,
  register_date
)
values (
  #{email},
  #{uuid},
  #{username},
  #{provider, typeHandler=com.github.tamurashingo.ben33.entity.User${'$'}PROVIDERHandler},
  #{status, typeHandler=com.github.tamurashingo.ben33.entity.User${'$'}STATUSHandler},
  #{createDate},
  #{updateDate},
  #{registerDate}
)
    """)
    fun insertUser(user: User)

    /**
     * ユーザ情報を更新する
     */
    @Update("""
update
  m_user
set
  username = #{username},
  status = #{status, typeHandler=com.github.tamurashingo.ben33.entity.User${'$'}STATUSHandler},
  update_date = #{updateDate},
  register_date = #{registerDate}
where
  user_id = #{userId}
    """)
    fun updateUser(user: User)

}

