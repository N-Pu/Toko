package com.project.toko.core.model.cache

class DataCache {
    private val dataMap: MutableMap<Int, com.project.toko.homeScreen.model.newAnimeSearchModel.Data> = mutableMapOf()

    fun getData(id: Int): com.project.toko.homeScreen.model.newAnimeSearchModel.Data? {
        return dataMap[id]
    }

    fun setData(id: Int, data: com.project.toko.homeScreen.model.newAnimeSearchModel.Data) {
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