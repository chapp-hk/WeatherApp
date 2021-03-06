package app.ch.weatherapp.history

sealed class HistoryEvent {

    data class DeleteItem(val id: Long) : HistoryEvent()

    data class Display(val cityName: String) : HistoryEvent()

    object ListChanged : HistoryEvent()
}

const val REQUEST_DISPLAY_CITY = "requestDisplayCity"
const val KEY_CITY_NAME = "cityName"
