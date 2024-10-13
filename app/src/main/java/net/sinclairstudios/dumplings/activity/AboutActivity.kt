package net.sinclairstudios.dumplings.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import net.sinclairstudios.dumplings.R

class AboutActivity : AppCompatActivity(R.layout.about_layout) {

    private lateinit var sinclairStudiosButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sinclairStudiosButton = findViewById(R.id.sinclairStudiosButton)
        sinclairStudiosButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse(getString(R.string.sinclairStudiosUrl)))
            startActivity(intent)
        }

        actionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }
    }
}