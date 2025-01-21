package com.noblesoftware.portalcore.util

import com.noblesoftware.portalcore.util.extension.orZero
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Calendar.MILLISECOND
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


/**
 * Created by fizhu on 05,June,2020
 * Hvyz.anbiya@gmail.com
 */
object DateTimeHelper {
    const val FORMAT_DATE_DMY = "dd-MM-yyyy"
    const val FORMAT_DATE_DMYHM = "dd-MM-yyyy HH:mm"
    const val FORMAT_DATE_DMYHM_WITH_SLASH = "dd/MM/yyyy HH:mm"
    const val FORMAT_DATE_DMYHMS = "dd-MM-yyyy HH:mm:ss"
    const val FORMAT_DATE_DMY_LONG_MONTH = "dd-MMMM-yyyy"
    const val FORMAT_DATE_DM_SHORT_MONTH_NO_SEPARATOR = "dd MMM"
    const val FORMAT_DATE_ONLY_MONTH = "MMMM"
    const val FORMAT_DATE_DMY_LONG_MONTH_NO_SEPARATOR = "dd MMMM yyyy"
    const val FORMAT_DATE_DMY_SHORT_MONTH_NO_SEPARATOR = "dd MMM yyyy"
    const val FORMAT_DATE_DMY_SLASH = "dd/MMMM/yyyy"
    const val FORMAT_DATE_DMY_LONG_DAY = "dd MMMM yyyy"
    const val FORMAT_DATE_DMY_SHORT_SLASH = "dd/MM/yyyy"
    const val FORMAT_DATE_EDMY_LONG_MONTH = "EEEE, dd MMMM yyyy"
    const val FORMAT_DATE_EDMY_SHORT_MONTH = "EEEE, dd MMM yyyy"
    const val FORMAT_DATE_EDMYHM_LONG_MONTH = "EEEE, dd MMMM yyyy HH:mm"
    const val FORMAT_DATE_EDMYHMS_LONG_MONTH = "EEEE, dd MMMM yyyy HH:mm:ss"
    const val FORMAT_DATE_DAY = "EEEE"
    const val FORMAT_DATE_YMD = "yyyy-MM-dd"
    const val FORMAT_DATE_YMD_NO_SEPARATOR = "yyyyMMdd"
    const val FORMAT_DATE_YMD_NO_SEPARATOR_SHORT = "yyMMdd"
    const val FORMAT_DATE_MDY_SLASH = "MM/dd/yyyy"
    const val FORMAT_DATE_YMD_SLASH = "yyyy/MM/dd"
    const val FORMAT_DATE_YMD_SEPARATOR = "yyyy-MM-dd"
    const val FORMAT_DATE_TIME_YMDHM = "yyyy-MM-dd HH:mm"
    const val FORMAT_DATE_TIME_YMDHMS = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_DATE_TIME_DMY_SHORT_MONTH_WITH_SPACE = "dd MMM yyyy"
    const val FORMAT_DATE_TIME_MDY_SHORT_MONTH_WITH_SPACE = "MMMM dd, yyyy"
    const val FORMAT_DATE_TIME_WITH_DAY_NAME = "EEEE, dd MMM HH:mm"
    const val FORMAT_DATE_TIME_WITH_DAY_NAME_AND_MONTH_SHORT_NAME = "EEEE, dd MMM"
    const val FORMAT_DATE_TIME_YMDHMS_NO_SEPARATOR = "yyyyMMddHHmmss"
    const val FORMAT_DATE_TIME_DMYHM_LONG_MONTH = "dd-MMMM-yyyy HH:mm"
    const val FORMAT_DATE_TIME_DMYHM_LONG_MONTH_NO_SEPARATOR = "dd MMMM yyyy HH:mm"
    const val FORMAT_DATE_TIME_DMYHM_LONG_MONTH_NO_SEPARATOR_BUT_WITH_DOT = "dd MMMM yyyy . HH:mm"
    const val FORMAT_DATE_TIME_DMYHM_LONG_WITH_STRIP = "dd MMM yyyy - HH:mm"
    const val FORMAT_DATE_TIME_DMYHM_SHORT_MONTH_WITH_LINE = "dd MMM yyyy | HH:mm"
    const val FORMAT_DATE_TIME_DMYHM_SHORT_MONTH_WITH_LINE_LONG_DAY = "EEEE, dd/MM/yyyy | HH:mm"
    const val FORMAT_DATE_TIME_DMYHM_SHORT_MONTH_WITH_COMMA = "dd MMM yyyy, HH:mm"
    const val FORMAT_DATE_TIME_DMYHMS_SHORT_MONTH_WITH_COMMA = "dd MMM yyyy, HH:mm:ss"
    const val FORMAT_TIME_HMS = "HH:mm:ss"
    const val FORMAT_TIME_HM = "HH:mm"
    const val FORMAT_TIME_MS = "mm:ss"
    const val FORMAT_TIME_HM_NO_SEPARATOR = "HHmm"
    const val FORMAT_yyyy_MM_dd_T_HHmmssSSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val FORMAT_yyyy_MM_dd_T_HHmmssZ = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val FORMAT_yyyy_MM_dd_T_HHmmssSSSZ_07 = "yyyy-MM-dd'T'HH:mm:ss.SSS'+07:00'"
    const val FORMAT_yyyy_MM_dd_T_HHmmssZ_07 = "yyyy-MM-dd'T'HH:mm:ss'+07:00'"
    const val FORMAT_yyyy_MM_dd_T_HHmmssSSSXXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    const val FORMAT_yyyy_MM_dd_T_HHmmssSSSSSSX = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSX"
    const val FORMAT_yyyy_MM_dd_T_HHmmssSSSSSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"
    const val FORMAT_yyyy_MM_dd_T_HHmmssXXX = "yyyy-MM-dd'T'HH:mm:ssXXX"
    const val FORMAT_MMM_YYYY = "MMM yyyy"
    const val FORMAT_MMMM_YYYY = "MMMM yyyy"
    const val FORMAT_TIME_HMS_SSS = "HH:mm:ss.SSS"
    const val FORMAT_MM = "MM"
    const val FORMAT_YYYY = "yyyy"
    const val FORMAT_DATE_DMYHM_WITH_STRIP = "dd/MM/yyyy - HH:mm"
    const val FORMAT_DATE_E = "EEEE"
    private const val CURRENT_TIME_ZONE =
        "Asia/Jakarta" // if you want to reset it, just change it to UTC

    fun getLastMidnight(): Date {
        val cal = Calendar.getInstance()
        cal[Calendar.HOUR] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[MILLISECOND] = 0
        return cal.time
    }

    fun milliToDateFormat(millis: Long, format: String): String {
        val sdf = SimpleDateFormat(format, Locale.ENGLISH)
        sdf.timeZone = TimeZone.getDefault()
        val dt = Date(millis)
        return sdf.format(dt)
    }

    fun milliToTimeMinsFormattedWithNoSec(millis: Long): String {
        var time = ""
        try {
            val minutes: Int = (millis / (1000 * 60)).toInt()

            val min = (if (minutes < 10) "0$minutes" else minutes).toString()
            time = min
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return time
    }

    fun format(date: String, frm_format: String? = null): Date? {
        var from_format = frm_format

        if (from_format.equals(FORMAT_yyyy_MM_dd_T_HHmmssSSSZ)) {
            if (!Pattern.matches(
                    "^\\d{4}-\\d{1,2}-\\d{1,2}[T]\\d{2}:\\d{2}:\\d{2}.\\d{3,6}[Z]\$",
                    date
                )
            ) {
                from_format = FORMAT_yyyy_MM_dd_T_HHmmssZ
            }
        } else if (from_format.equals(FORMAT_yyyy_MM_dd_T_HHmmssZ)) {
            if (!Pattern.matches(
                    "^\\d{4}-\\d{1,2}-\\d{1,2}[T]\\d{2}:\\d{2}:\\d{2}[Z]\$",
                    date
                )
            ) {
                from_format = FORMAT_yyyy_MM_dd_T_HHmmssSSSZ
            }
        }


        val fsdf = SimpleDateFormat(from_format, Locale.ENGLISH)
        fsdf.timeZone = TimeZone.getTimeZone("UTC")
        return fsdf.parse(date)
    }

    fun formatDateUTC(date: String, frm_format: String? = null): Date? {
        var from_format = frm_format

        if (from_format.equals(FORMAT_yyyy_MM_dd_T_HHmmssSSSZ)) {
            if (!Pattern.matches(
                    "^\\d{4}-\\d{1,2}-\\d{1,2}[T]\\d{2}:\\d{2}:\\d{2}.\\d{3,6}[Z]\$",
                    date
                )
            ) {
                from_format = FORMAT_yyyy_MM_dd_T_HHmmssZ
            }
        } else if (from_format.equals(FORMAT_yyyy_MM_dd_T_HHmmssZ)) {
            if (!Pattern.matches(
                    "^\\d{4}-\\d{1,2}-\\d{1,2}[T]\\d{2}:\\d{2}:\\d{2}[Z]\$",
                    date
                )
            ) {
                from_format = FORMAT_yyyy_MM_dd_T_HHmmssSSSZ
            }
        }


        val fsdf = SimpleDateFormat(from_format, Locale.ENGLISH)
        fsdf.timeZone = TimeZone.getDefault()
        return fsdf.parse(date)
    }

    fun formatDateUTC(
        date: String,
        frm_format: String? = null,
        to_format: String? = null
    ): String {
        var fromFormat = frm_format

        if (fromFormat.equals(FORMAT_yyyy_MM_dd_T_HHmmssSSSZ)) {
            if (!Pattern.matches(
                    "^\\d{4}-\\d{1,2}-\\d{1,2}[T]\\d{2}:\\d{2}:\\d{2}.\\d{3,6}[Z]\$",
                    date
                )
            ) {
                fromFormat = FORMAT_yyyy_MM_dd_T_HHmmssZ
            }
        } else if (fromFormat.equals(FORMAT_yyyy_MM_dd_T_HHmmssZ)) {
            if (!Pattern.matches(
                    "^\\d{4}-\\d{1,2}-\\d{1,2}[T]\\d{2}:\\d{2}:\\d{2}[Z]\$",
                    date
                )
            ) {
                fromFormat = FORMAT_yyyy_MM_dd_T_HHmmssSSSZ
            }
        }

        val fsdf = SimpleDateFormat(fromFormat!!, Locale.ENGLISH)
        fsdf.timeZone = TimeZone.getDefault()
        return formatDefault(fsdf.parse(date), to_format)
    }

    fun milliToTimeMinsFOrmattedWithSec(millis: Long): String {
        var time = ""
        try {
            val seconds: Int = ((millis / 1000) % 60).toInt()
            val minutes: Int = (millis / (1000 * 60)).toInt()

            val sec = (if (seconds < 10) "0$seconds" else seconds).toString()
            val min = (if (minutes < 10) "0$minutes" else minutes).toString()
            time = "$min:$sec"
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return time
    }

    fun convertDate(date: String?, format: String?): String {
        var spf = SimpleDateFormat(FORMAT_DATE_YMD)
        val newDate = spf.parse(date)
        spf = SimpleDateFormat(format)
        return spf.format(newDate)
    }

    fun currentTime(): String {
        val current: Date = Calendar.getInstance().time
        val df = SimpleDateFormat(FORMAT_DATE_TIME_YMDHMS, Locale.getDefault())
        return df.format(current)
    }

    fun convertTime(dateValue: String?, format: String?): String {
        val inputPattern = FORMAT_DATE_TIME_YMDHMS
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(format)

        val date: Date?
        var str: String? = null

        try {
            date = inputFormat.parse(dateValue)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str!!
    }

    fun format(date: Date?, format: String?): String {
        val frm = SimpleDateFormat(format, Locale.getDefault())
        kotlin.runCatching {
            frm.timeZone = TimeZone.getDefault()
            return frm.format(date)
        }.getOrElse {
            return frm.format(Date(System.currentTimeMillis()))
        }
    }

    fun formatDefault(date: Date?, format: String?): String {
        val frm = SimpleDateFormat(format, Locale.getDefault())
        return frm.format(date)
    }

    @JvmOverloads
    fun parse(date: String?, format: String?, def: Date = Date()): Date {
        val frm = SimpleDateFormat(format, Locale.getDefault())
        frm.timeZone = TimeZone.getDefault()
        return try {
            frm.parse(date)
        } catch (e: ParseException) {
            def
        }
    }

    @JvmOverloads
    fun parseUTC(date: String?, format: String?, def: Date = Date()): Date {
        val frm = SimpleDateFormat(format, Locale.getDefault())
        frm.timeZone = TimeZone.getTimeZone("UTC")
        return try {
            frm.parse(date)
        } catch (e: ParseException) {
            def
        }
    }

    @JvmOverloads
    fun parse(
        date: String?,
        format_awal: String?,
        format_akhir: String?,
        def: Date = Date()
    ): String {
        val _date = parse(date, format_awal, def)
        return format(_date, format_akhir)
    }

    fun diffDate(date_start: Date, date_end: Date): Long {
        val diffInMillies = date_end.time - date_start.time
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
    }

    fun diffMinutes(date_start: Date, date_end: Date): Long {
        val diffInMillies = date_end.time - date_start.time
        return TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS)
    }

    fun diffHourMinutes(date_start: Date, date_end: Date): String {
        val diffInMillies = date_end.time - date_start.time
        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillies)
        return String.format(
            Locale.getDefault(), "%d jam %d menit",
            hours, TimeUnit.MILLISECONDS.toMinutes(diffInMillies) - TimeUnit.HOURS.toMinutes(hours)
        )
    }

    /**
     *
     * Checks if two dates are on the same day ignoring time.
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either date is `null`
     */
    fun isSameDay(date1: Date?, date2: Date?): Boolean {
        require(!(date1 == null || date2 == null)) { "The dates must not be null" }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return isSameDay(cal1, cal2)
    }

    /**
     *
     * Checks if two calendars represent the same day ignoring time.
     *
     * @param cal1 the first calendar, not altered, not null
     * @param cal2 the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is `null`
     */
    fun isSameDay(cal1: Calendar?, cal2: Calendar?): Boolean {
        require(!(cal1 == null || cal2 == null)) { "The dates must not be null" }
        return cal1[Calendar.ERA] == cal2[Calendar.ERA] && cal1[Calendar.YEAR] == cal2[Calendar.YEAR] && cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR]
    }

    /**
     *
     * Checks if a date is today.
     *
     * @param date the date, not altered, not null.
     * @return true if the date is today.
     * @throws IllegalArgumentException if the date is `null`
     */
    fun isToday(date: Date?): Boolean {
        return isSameDay(date, Calendar.getInstance().time)
    }

    /**
     *
     * Checks if a calendar date is today.
     *
     * @param cal the calendar, not altered, not null
     * @return true if cal date is today
     * @throws IllegalArgumentException if the calendar is `null`
     */
    fun isToday(cal: Calendar?): Boolean {
        return isSameDay(cal, Calendar.getInstance())
    }

    /**
     *
     * Checks if the first date is before the second date ignoring time.
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is before the second date day.
     * @throws IllegalArgumentException if the date is `null`
     */
    fun Date?.isBeforeDay(date: Date?): Boolean {
        require(!(this == null || date == null)) { "The dates must not be null" }
        val cal1 = Calendar.getInstance()
        cal1.time = this
        val cal2 = Calendar.getInstance()
        cal2.time = date
        return cal1.isBeforeDay(cal2)
    }

    /**
     *
     * Checks if the first calendar date is before the second calendar date ignoring time.
     *
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is before cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are `null`
     */
    fun Calendar?.isBeforeDay(cal: Calendar?): Boolean {
        require(!(this == null || cal == null)) { "The dates must not be null" }
        if (this[Calendar.ERA] < cal[Calendar.ERA]) return true
        if (this[Calendar.ERA] > cal[Calendar.ERA]) return false
        if (this[Calendar.YEAR] < cal[Calendar.YEAR]) return true
        return if (this[Calendar.YEAR] > cal[Calendar.YEAR]) false else this[Calendar.DAY_OF_YEAR] < cal[Calendar.DAY_OF_YEAR]
    }

    /**
     *
     * Checks if the first date is after the second date ignoring time.
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is after the second date day.
     * @throws IllegalArgumentException if the date is `null`
     */
    fun Date?.isAfterDay(date: Date?): Boolean {
        require(!(this == null || date == null)) { "The dates must not be null" }
        val cal1 = Calendar.getInstance()
        cal1.time = this
        val cal2 = Calendar.getInstance()
        cal2.time = date
        return cal1.isAfterDay(cal2)
    }

    /**
     *
     * Checks if the first calendar date is after the second calendar date ignoring time.
     *
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is after cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are `null`
     */
    fun Calendar?.isAfterDay(cal: Calendar?): Boolean {
        require(!(this == null || cal == null)) { "The dates must not be null" }
        if (this[Calendar.ERA] < cal[Calendar.ERA]) return false
        if (this[Calendar.ERA] > cal[Calendar.ERA]) return true
        if (this[Calendar.YEAR] < cal[Calendar.YEAR]) return false
        return if (this[Calendar.YEAR] > cal[Calendar.YEAR]) true else this[Calendar.DAY_OF_YEAR] > cal[Calendar.DAY_OF_YEAR]
    }

    /**
     *
     * Checks if a date is after today and within a number of days in the future.
     *
     * @param date the date to check, not altered, not null.
     * @param days the number of days.
     * @return true if the date day is after today and within days in the future .
     * @throws IllegalArgumentException if the date is `null`
     */
    fun isWithinDaysFuture(date: Date?, days: Int): Boolean {
        requireNotNull(date) { "The date must not be null" }
        val cal = Calendar.getInstance()
        cal.time = date
        return isWithinDaysFuture(cal, days)
    }

    /**
     *
     * Checks if a calendar date is after today and within a number of days in the future.
     *
     * @param cal  the calendar, not altered, not null
     * @param days the number of days.
     * @return true if the calendar date day is after today and within days in the future .
     * @throws IllegalArgumentException if the calendar is `null`
     */
    fun isWithinDaysFuture(cal: Calendar?, days: Int): Boolean {
        requireNotNull(cal) { "The date must not be null" }
        val today = Calendar.getInstance()
        val future = Calendar.getInstance()
        future.add(Calendar.DAY_OF_YEAR, days)
        return cal.isAfterDay(today) && !cal.isAfterDay(future)
    }

    /**
     * Returns the given date with the time set to the start of the day.
     */
    fun getStart(date: Date?): Date? {
        return clearTime(date)
    }

    /**
     * Returns the given date with the time values cleared.
     */
    fun clearTime(date: Date?): Date? {
        if (date == null) {
            return null
        }
        val c = Calendar.getInstance()
        c.time = date
        c[Calendar.HOUR_OF_DAY] = 0
        c[Calendar.MINUTE] = 0
        c[Calendar.SECOND] = 0
        c[MILLISECOND] = 0
        return c.time
    }
    /** Determines whether or not a date has any time values (hour, minute,
     * seconds or millisecondsReturns the given date with the time values cleared.  */
    /**
     * Determines whether or not a date has any time values.
     *
     * @param date The date.
     * @return true iff the date is not null and any of the date's hour, minute,
     * seconds or millisecond values are greater than zero.
     */
    fun hasTime(date: Date?): Boolean {
        if (date == null) {
            return false
        }
        val c = Calendar.getInstance()
        c.time = date
        if (c[Calendar.HOUR_OF_DAY] > 0) {
            return true
        }
        if (c[Calendar.MINUTE] > 0) {
            return true
        }
        if (c[Calendar.SECOND] > 0) {
            return true
        }
        return c[MILLISECOND] > 0
    }

    /**
     * Returns the given date with time set to the end of the day
     */
    fun getEnd(date: Date?): Date? {
        if (date == null) {
            return null
        }
        val c = Calendar.getInstance()
        c.time = date
        c[Calendar.HOUR_OF_DAY] = 23
        c[Calendar.MINUTE] = 59
        c[Calendar.SECOND] = 59
        c[MILLISECOND] = 999
        return c.time
    }

    /**
     * Returns the maximum of two dates. A null date is treated as being less
     * than any non-null date.
     */
    fun max(d1: Date?, d2: Date?): Date? {
        if (d1 == null && d2 == null) return null
        if (d1 == null) return d2
        if (d2 == null) return d1
        return if (d1.after(d2)) d1 else d2
    }

    /**
     * Returns the minimum of two dates. A null date is treated as being greater
     * than any non-null date.
     */
    fun min(d1: Date?, d2: Date?): Date? {
        if (d1 == null && d2 == null) return null
        if (d1 == null) return d2
        if (d2 == null) return d1
        return if (d1.before(d2)) d1 else d2
    }

    /**
     * The maximum date possible.
     */
    var MAX_DATE = Date(Long.MAX_VALUE)

    val Int.daysAgo: Date
        get() {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -this)
            return calendar.time
        }

    val Int.monthsAgo: Date
        get() {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -this)
            return calendar.time
        }

    fun Date.toTimeAgo(): String? {
        var convTime: String? = null
        val suffix = "Ago"
        kotlin.runCatching {
            val nowTime = Date()
            val dateDiff = nowTime.time - this.time
            val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                convTime = "$second Seconds $suffix"
            } else if (minute < 60) {
                convTime = "$minute Minutes $suffix"
            } else if (hour < 24) {
                convTime = "$hour Hours $suffix"
            } else if (day >= 7) {
                convTime = if (day > 360) {
                    (day / 360).toString() + " Years " + suffix
                } else if (day > 30) {
                    (day / 30).toString() + " Months " + suffix
                } else {
                    (day / 7).toString() + " Week " + suffix
                }
            } else if (day < 7) {
                convTime = "$day Days $suffix"
            }
        }.getOrElse {
            it.printStackTrace()
        }
        return convTime
    }

    fun Date.toTimeAgoAnnouncement(): String? {
        var convTime: String? = null
        val suffix = "Ago"
        kotlin.runCatching {
            val nowTime = Date()
            val dateDiff = nowTime.time - this.time
            val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (minute < 60) {
                convTime = "Just Now"
            } else if (hour < 24) {
                convTime = "$hour Hours $suffix"
            } else if (day >= 7) {
                convTime = if (day > 360) {
                    (day / 360).toString() + " Years " + suffix
                } else if (day > 30) {
                    (day / 30).toString() + " Months " + suffix
                } else {
                    (day / 7).toString() + " Week " + suffix
                }
            } else if (day < 7) {
                convTime = "$day Days $suffix"
            }
        }.getOrElse {
            it.printStackTrace()
        }
        return convTime
    }

    fun formatEn(date: Date?, format: String?): String {
        val frm = SimpleDateFormat(format, Locale.ENGLISH)
        kotlin.runCatching {
            frm.timeZone = TimeZone.getTimeZone(CURRENT_TIME_ZONE)
            return frm.format(date)
        }.getOrElse {
            return frm.format(Date(System.currentTimeMillis()))
        }
    }

    @JvmOverloads
    fun parseEn(date: String?, format: String?, def: Date = Date()): Date {
        val frm = SimpleDateFormat(format, Locale.ENGLISH)
        frm.timeZone = TimeZone.getTimeZone(CURRENT_TIME_ZONE)
        return try {
            frm.parse(date)
        } catch (e: ParseException) {
            def
        }
    }

    @JvmOverloads
    fun parseEn(
        date: String?,
        format_awal: String?,
        format_akhir: String?,
        def: Date = Date()
    ): String {
        val _date = parseEn(date, format_awal, def)
        return formatEn(_date, format_akhir)
    }

    /**
     * check hour is less than minutes
     * [startWorkHour] must be hh:mm:ss
     */
    fun isMinuteBefore(startWorkHour: String, minute: Int? = 60): Boolean {
        return try {
            if (startWorkHour.isBlank()) return false
            val format = SimpleDateFormat(FORMAT_DATE_TIME_YMDHMS, Locale.getDefault())
            val currentDate = format.parse(currentTime())
            val startWorkDate = format.parse(
                "${
                    convertDate(
                        currentTime(),
                        FORMAT_DATE_YMD
                    )
                } $startWorkHour"
            )
            val isOneHourBeforeWork = diffMinutes(currentDate, startWorkDate)
            isOneHourBeforeWork <= minute.orZero()
        } catch (e: ParseException) {
            false
        }
    }

    /**
     * check current time is before compare time
     * [compareHour] must be hh:mm:ss
     */
    fun isCurrentTimeBefore(compareHour: String): Boolean {
        return try {
            if (compareHour.isBlank()) return false
            val format = SimpleDateFormat(FORMAT_DATE_TIME_YMDHMS, Locale.getDefault())
            val currentDate = format.parse(currentTime())
            val compareDate = format.parse(
                "${
                    convertDate(
                        currentTime(),
                        FORMAT_DATE_YMD
                    )
                } $compareHour"
            )

            if (currentDate != null && compareDate != null) {
                diffMinutes(currentDate, compareDate) >= 0
            } else false
        } catch (e: ParseException) {
            false
        }
    }
}