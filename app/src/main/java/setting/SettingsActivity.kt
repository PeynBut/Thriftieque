package setting

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.rendonapp.thriftique.R

class SettingsActivity : AppCompatActivity() {
    private lateinit var switchVibration: SwitchCompat
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        switchVibration = findViewById(R.id.switchVibration)

        // Set the switch state from preferences
        switchVibration.isChecked = sharedPreferences.getBoolean("vibration", true)

        switchVibration.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("vibration", isChecked)
            editor.apply()
            editor.commit() // Force save
        }

    }
}
