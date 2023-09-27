package com.dapascript.movtube.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun formatterDate(date: String?): String {
    val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return parser.parse(date ?: "")?.let { formatter.format(it) }.toString()
}