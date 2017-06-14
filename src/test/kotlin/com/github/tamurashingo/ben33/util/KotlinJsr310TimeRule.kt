package com.github.tamurashingo.ben33.util

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.time.*

class KotlinJsr310TimeRule : TestRule {
    @Target(AnnotationTarget.FUNCTION)
    annotation class Now(
            val year: Int,
            val month: Int,
            val dayOfMonth: Int,
            val hour: Int,
            val minute: Int,
            val second: Int = 0
    )

    companion object {

        // 多重lock/unlockを検知するためのフラグ. スレッドセーフは保証しない.
        private var locked = false
    }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                val annotation = description.getAnnotation(Now::class.java)
                if (annotation == null) {
                    base.evaluate()  // 現在時刻を固定しない
                    return
                }

                try {
                    lockCurrentTime(parse(annotation))
                    base.evaluate()
                } finally {
                    unlockCurrentTime()
                }
            }
        }
    }

    private fun lockCurrentTime(now: Long) {
        if (KotlinJsr310TimeRule.locked) {
            throw IllegalMonitorStateException("Clock is locked.")
        }
        KotlinJsr310TimeRule.locked = true
        Jsr310Time.fixedCurrentTime(
                Clock.fixed(Instant.ofEpochMilli(now), ZoneOffset.systemDefault()))
    }

    private fun unlockCurrentTime() {
        if (!KotlinJsr310TimeRule.locked) {
            throw IllegalMonitorStateException("Clock is unlocked.")
        }
        Jsr310Time.tickCurrentTime()
        KotlinJsr310TimeRule.locked = false
    }

    private fun parse(a: Now): Long {
        val zoneId = ZoneId.systemDefault()
        val time = ZonedDateTime.of(a.year, a.month, a.dayOfMonth, a.hour, a.minute, a.second, 0, zoneId)
        return time.toInstant().toEpochMilli()
    }
}

