package app.ch.domain.weather.usecase

import app.ch.domain.base.CoroutineDispatcherProvider
import app.ch.domain.weather.repository.IWeatherRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Test

class DeleteAllWeatherUseCaseTest {

    @MockK
    private lateinit var weatherRepository: IWeatherRepository

    @MockK
    private lateinit var coroutineDispatcherProvider: CoroutineDispatcherProvider

    private lateinit var deleteAllWeatherUseCase: DeleteAllWeatherUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every {
            coroutineDispatcherProvider.ioDispatcher
        } returns TestCoroutineDispatcher()

        deleteAllWeatherUseCase = DeleteAllWeatherUseCase(
            coroutineDispatcherProvider,
            weatherRepository
        )
    }

    @Test
    fun invoke() {
        every {
            weatherRepository.deleteAllWeather()
        } returns flowOf()

        deleteAllWeatherUseCase()

        verify {
            weatherRepository.deleteAllWeather()
        }
    }
}
