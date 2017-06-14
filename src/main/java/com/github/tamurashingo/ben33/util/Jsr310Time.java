/*
 * Copyright 2016 yuki312 All Right Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * original
 * https://github.com/YukiMatsumura/Now/blob/master/app/src/main/java/yuki/m/android/now/time/Jsr310Time.java
 *
 * Modified by tamurashingo
 */

package com.github.tamurashingo.ben33.util;

import com.google.common.annotations.VisibleForTesting;
import lombok.NonNull;

import java.time.*;

/**
 * 時刻呼び出しを一部に固定するためのユーティリティ
 */
public abstract class Jsr310Time {

    public static ZoneId TIME_ZONE = ZoneOffset.UTC;

    private static Clock clock = Clock.systemDefaultZone();

    /**
     * 現在時刻を固定する
     * @param clock
     */
    @VisibleForTesting
    protected static void fixedCurrentTime(@NonNull Clock clock) {
        Jsr310Time.clock = clock;
    }

    /**
     * 現在時刻の固定を解除する
     */
    @VisibleForTesting
    protected static void tickCurrentTime() {
        Jsr310Time.clock = Clock.systemDefaultZone();
    }

    public static long now() {
        return Jsr310Time.clock.millis();
    }

    public static LocalDate getDate() {
        return LocalDate.now(Jsr310Time.clock);
    }

    public static LocalTime getTime() {
        return LocalTime.now(Jsr310Time.clock);
    }

    public static LocalDateTime getDateTime() {
        return LocalDateTime.now(Jsr310Time.clock);
    }
}

