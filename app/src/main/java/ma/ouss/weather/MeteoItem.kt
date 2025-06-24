package ma.ouss.weather

data class MeteoItem(
    var tempMax: Int = 0,
    var tempMin: Int = 0,
    var pression: Int = 0,
    var humidite: Int = 0,
    var image: String = "",
    var date: String = ""
)
