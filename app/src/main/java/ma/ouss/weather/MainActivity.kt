package ma.ouss.weather


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView;
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException; import org.json.JSONObject;
import java.text.SimpleDateFormat; import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var editTextVille: EditText
    private lateinit var listViewMeteo: ListView
    private lateinit var buttonOK: ImageButton
    private lateinit var model: MeteoListModel
    private val data = ArrayList<MeteoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextVille = findViewById(R.id.editTextVille)
        listViewMeteo = findViewById(R.id.listViewMeteo)
        buttonOK = findViewById(R.id.buttonOK)

        model = MeteoListModel(applicationContext, R.layout.list_item_layout, data)
        listViewMeteo.adapter = model

        buttonOK.setOnClickListener {
            Log.i("MyLog", "......")
            data.clear()
            model.notifyDataSetChanged()

            val queue = Volley.newRequestQueue(applicationContext)
            val ville = editTextVille.text.toString()
            Log.i("MyLog", ville)

            val url = "https://samples.openweathermap.org/data/2.5/forecast?q=$ville&appid=a4578e39643716894ec78b28a71c7110"

            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    try {
                        Log.i("MyLog", "----------------------------")
                        Log.i("MyLog", response)

                        val jsonObject = JSONObject(response)
                        val jsonArray = jsonObject.getJSONArray("list")

                        for (i in 0 until jsonArray.length()) {
                            val meteoItem = MeteoItem()
                            val d = jsonArray.getJSONObject(i)
                            val date = Date(d.getLong("dt") * 1000)
                            val sdf = SimpleDateFormat("dd-MMM-yyyy'T'HH:mm", Locale.getDefault())
                            val dateString = sdf.format(date)

                            val main = d.getJSONObject("main")
                            val weather = d.getJSONArray("weather")

                            val tempMin = (main.getDouble("temp_min") - 273.15).toInt()
                            val tempMax = (main.getDouble("temp_max") - 273.15).toInt()
                            val pression = main.getInt("pressure")
                            val humidity = main.getInt("humidity")

                            meteoItem.tempMin = tempMin
                            meteoItem.tempMax = tempMax
                            meteoItem.pression = pression
                            meteoItem.humidite = humidity
                            meteoItem.date = dateString
                            meteoItem.image = weather.getJSONObject(0).getString("main")

                            data.add(meteoItem)
                        }
                        model.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                {
                    Log.i("MyLog", "Connection problem!")
                }
            )

            queue.add(stringRequest)
        }
    }
}
