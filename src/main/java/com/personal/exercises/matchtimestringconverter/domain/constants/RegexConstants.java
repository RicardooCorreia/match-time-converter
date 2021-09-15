package com.personal.exercises.matchtimestringconverter.domain.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegexConstants {

    public static final String PERIOD_GROUP = "period";

    public static final String MINUTES_GROUP = "minutes";

    public static final String SECONDS_GROUP = "seconds";

    public static final String MILLISECONDS_GROUP = "milli" + SECONDS_GROUP;

    public static final String PERIOD = "(?<" + PERIOD_GROUP + ">\\w{2})";

    public static final String MINUTES = "(?<" + MINUTES_GROUP + ">\\d{1,3})";

    public static final String SECONDS = "(?<" + SECONDS_GROUP + ">\\d{2})";

    public static final String MILLISECONDS = "(?<" + MILLISECONDS_GROUP + ">\\d{3})";

    public static final String MATCH_INPUT_REGEX =
        "^\\[" + PERIOD + "\\]\\s" + MINUTES + "\\:" + SECONDS + "\\." + MILLISECONDS + "$";
}
