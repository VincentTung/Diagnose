package com.vincent.diagnose.util

object SizeConverter {

    fun convert(s: Long): String {

        var size = s
        if (size < 1024) {
            return size.toString() + "B"
        } else {
            size /= 1024
        }
        if (size < 1024) {
            return size.toString() + "KB"
        } else {
            size /= 1024
        }
        if (size < 1024) {
            size *= 100
            return (size / 100).toString() + "." + (size % 100).toString() + "MB"
        } else {

            size = size * 100 / 1024
            return (size / 100).toString() + "."+(size % 100).toString() + "GB"
        }
    }

}