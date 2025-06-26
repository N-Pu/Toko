package com.project.toko.dataBase.search.data.remote


import com.project.toko.dataBase.search.data.db.entity.Aired
import com.project.toko.dataBase.search.data.db.entity.AiredDateFrom
import com.project.toko.dataBase.search.data.db.entity.AiredDateTo
import com.project.toko.dataBase.search.data.db.entity.AiredProp
import com.project.toko.dataBase.search.data.db.entity.AnimeEntity
import com.project.toko.dataBase.search.data.db.entity.AnimeImages
import com.project.toko.dataBase.search.data.db.entity.AnimeOrderEntity
import com.project.toko.dataBase.search.data.db.entity.AnimeTitle
import com.project.toko.dataBase.search.data.db.entity.AnimeTrailer
import com.project.toko.dataBase.search.data.db.entity.Broadcast
import com.project.toko.dataBase.search.data.db.entity.Demographic
import com.project.toko.dataBase.search.data.db.entity.ExplicitGenre
import com.project.toko.dataBase.search.data.db.entity.Genre
import com.project.toko.dataBase.search.data.db.entity.ImageType
import com.project.toko.dataBase.search.data.db.entity.Licensor
import com.project.toko.dataBase.search.data.db.entity.PaginationEntity
import com.project.toko.dataBase.search.data.db.entity.Producer
import com.project.toko.dataBase.search.data.db.entity.Studio
import com.project.toko.dataBase.search.data.db.entity.Theme
import com.project.toko.homeScreen.data.model.newAnimeSearchModel.NewAnimeSearchModel

fun NewAnimeSearchModel.toTriple(
    majorId: Long,
    existingPages: Int = 0
): Triple<List<AnimeEntity>, PaginationEntity, List<AnimeOrderEntity>> {
    return Triple(
        this.data.map {
            AnimeEntity(
                mal_id = it.id,
                url = it.url,
                images = AnimeImages(
                    jpg = ImageType(
                        image_url = it.images.jpg.image_url,
                        small_image_url = it.images.jpg.small_image_url,
                        large_image_url = it.images.jpg.large_image_url
                    ), webp = ImageType(
                        image_url = it.images.webp.image_url,
                        small_image_url = it.images.webp.small_image_url,
                        large_image_url = it.images.webp.large_image_url
                    )
                ),
                trailer = AnimeTrailer(
                    youtube_id = it.trailer.youtube_id, url = it.trailer.url,
                    embed_url = it.trailer.embed_url
                ),
                approved = it.approved,
                titles = it.titles.map { ti ->
                    AnimeTitle(type = ti.type, title = ti.title)
                },
                title = it.title,
                title_english = it.title_english,
                title_japanese = it.title_japanese,
                title_synonyms = it.title_synonyms,
                type = it.type,
                source = it.source,
                episodes = it.episodes,
                status = it.status,
                airing = it.airing,
                aired = Aired(
                    from = it.aired.from, to = it.aired.to, prop = AiredProp(
                        from = AiredDateFrom(
                            day = it.aired.prop.from.day,
                            month = it.aired.prop.from.month,
                            year = it.aired.prop.from.year
                        ),
                        to = AiredDateTo(
                            day = it.aired.prop.from.day, month = it.aired.prop.from

                                .month, year = it.aired.prop.from.year
                        ),
                        string = it.aired.prop.string
                    )
                ),
                duration = it.duration,
                rating = it.rating,
                score = it.score,
                scored_by = it.scored_by,
                rank = it.rank,
                popularity = it.popularity,
                members = it.members,
                favorites = it.favorites,
                synopsis = it.synopsis,
                background = it.background,
                season = it.season,
                year = it.year,
                broadcast = Broadcast(
                    day = it.broadcast.day,
                    time = it.broadcast.time,
                    timezone = it.broadcast.timezone,
                    string = it.broadcast.string
                ),
                producers = it.producers.map { pr ->
                    Producer(
                        mal_id = pr.mal_id,
                        type = pr.type,
                        name = pr.name,
                        url = pr.url
                    )
                },
                licensors = it.licensors.map { li ->
                    Licensor(
                        mal_id = li.mal_id,
                        type = li.type,
                        name = li.name,
                        url = li.url
                    )
                },
                studios = it.studios.map { st ->
                    Studio(st.mal_id, st.type, st.name, st.url)
                },
                genres = it.genres.map { ge ->
                    Genre(
                        mal_id = ge.mal_id,
                        type = ge.type,
                        name = ge.name,
                        url = ge.url
                    )
                },
                explicit_genres = it.explicit_genres.map { ex ->
                    ExplicitGenre(
                        mal_id = ex.mal_id,
                        type = ex.type,
                        name = ex.name,
                        url = ex.url
                    )
                },
                themes = it.themes.map { ex ->
                    Theme(
                        mal_id = ex.mal_id,
                        type = ex.type,
                        name = ex.name,
                        url = ex.url
                    )
                }, demographics = it.demographics.map { ex ->
                    Demographic(
                        mal_id = ex.mal_id,
                        type = ex.type,
                        name = ex.name,
                        url = ex.url
                    )
                }
            )

        }, PaginationEntity(
            majorId = majorId, pagination = com.project.toko.dataBase.search.data.db.entity.Pagination(
                lastVisiblePage = this.pagination.last_visible_page,
                hasNextPage = this.pagination.has_next_page,
                currentPage = this.pagination.current_page,
                items = com.project.toko.dataBase.search.data.db.entity.PaginationItems(
                    count = this.pagination.items.count,
                    total = this.pagination.items.total,
                    perPage = this.pagination.items.per_page
                )
            )
        ),this.data.mapIndexed { index, data ->
            AnimeOrderEntity(majorId = majorId, malId = data.id, order = existingPages + index + 1)
        }.toList()
    )
}