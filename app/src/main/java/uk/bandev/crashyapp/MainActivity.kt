package uk.bandev.crashyapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uk.bandev.crashyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.crashButton.setOnClickListener {
            throw BecauseICanException()
        }
    }
}

private class BecauseICanException : Exception("This exception is thrown purely because it can be thrown")
