package app.ch.data.weather.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import app.ch.data.weather.mapper.toDaoEntity
import app.ch.data.weather.mapper.toDataModel
import app.ch.data.weather.model.WeatherModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ExperimentalCoroutinesApi
class WeatherLocalDataSource @Inject
constructor(
    private val weatherDao: WeatherDao,
) {

    suspend fun insertWeather(weather: WeatherModel) {
        weatherDao.apply {
            insertWeather(weather.toDaoEntity())
            insertAllConditions(weather.conditions.map { it.toDaoEntity() })
        }
    }

    fun getWeatherHistory(): Flow<PagingData<WeatherModel>> {
        return Pager(
            PagingConfig(10)
        ) {
            weatherDao.getWeathers()
        }.flow.map { pagingData ->
            pagingData.map {
                it.toDataModel()
            }
        }
    }
}
