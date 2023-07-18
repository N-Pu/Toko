package com.example.animeapp.domain.models.cache

import com.example.animeapp.domain.models.newAnimeSearchModel.Data

class DataCache {
    private val dataMap: MutableMap<Int, Data> = mutableMapOf()

    fun getData(id: Int): Data? {
        return dataMap[id]
    }

    fun setData(id: Int, data: Data) {
        dataMap[id] = data
    }

    fun containsId(id: Int): Boolean {
        return dataMap.containsKey(id)
    }

    fun clear() {
        dataMap.clear()
    }
}

object DataCacheSingleton {
    val dataCache: DataCache = DataCache()
}