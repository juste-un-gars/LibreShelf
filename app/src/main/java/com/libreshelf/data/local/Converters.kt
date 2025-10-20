package com.libreshelf.data.local

import androidx.room.TypeConverter
import com.libreshelf.data.model.BookFormat
import com.libreshelf.data.model.NetworkType

class Converters {
    @TypeConverter
    fun fromBookFormat(value: BookFormat): String {
        return value.name
    }

    @TypeConverter
    fun toBookFormat(value: String): BookFormat {
        return try {
            BookFormat.valueOf(value)
        } catch (e: IllegalArgumentException) {
            BookFormat.UNKNOWN
        }
    }

    @TypeConverter
    fun fromNetworkType(value: NetworkType): String {
        return value.name
    }

    @TypeConverter
    fun toNetworkType(value: String): NetworkType {
        return NetworkType.valueOf(value)
    }
}
