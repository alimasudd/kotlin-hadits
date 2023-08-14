package id.alimasudd.hadits

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import id.alimasudd.hadits.adapter.AdapterDoa
import id.alimasudd.hadits.model.ModelDoa
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    var adapterDoa: AdapterDoa? = null
    var modelDoa: MutableList<ModelDoa> = ArrayList()

    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchDoa.imeOptions = EditorInfo.IME_ACTION_DONE
        searchDoa.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapterDoa?.filter?.filter(newText)
                return true
            }
        })

        //transparent background searchview
        val searchPlateId = searchDoa.context.resources.getIdentifier("android:id/search_plate",
            null, null)
        val searchPlate = searchDoa.findViewById<View>(searchPlateId)
        searchPlate?.setBackgroundColor(Color.TRANSPARENT)

        rvListDoa.layoutManager = LinearLayoutManager(this)
        rvListDoa.setHasFixedSize(true)

        //get data
        getDoaHarian()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapterDoa?.notifyDataSetChanged()
    }

    private fun getDoaHarian() {
        try {
            val stream = assets.open("hadits.json")
            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            val strResponse = String(buffer, StandardCharsets.UTF_8)
            try {
                val jsonObject = JSONObject(strResponse)
                val jsonArray = jsonObject.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    val jsonObjectData = jsonArray.getJSONObject(i)
                    val dataModel = ModelDoa()
                    dataModel.strId = jsonObjectData.getString("id")
                    dataModel.strTitle = jsonObjectData.getString("title")
                    dataModel.strArabic = jsonObjectData.getString("arabic")
                    dataModel.strLatin = jsonObjectData.getString("latin")
                    dataModel.strTranslation = jsonObjectData.getString("translation")
                    modelDoa.add(dataModel)
                }
                adapterDoa = AdapterDoa(this, modelDoa)
                rvListDoa.adapter = adapterDoa
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } catch (ignored: IOException) {
        }
    }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val window = activity.window
            val layoutParams = window.attributes
            if (on) {
                layoutParams.flags = layoutParams.flags or bits
            } else {
                layoutParams.flags = layoutParams.flags and bits.inv()
            }
            window.attributes = layoutParams
        }
    }
}