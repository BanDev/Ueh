package uk.bandev.ueh

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.snackbar.Snackbar
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.windowInsetTypesOf
import uk.bandev.ueh.databinding.CrashActivityBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * An Activity which displays various pieces of information regarding the exception which
 * occurred.
 */
class WhatTheStackActivity : AppCompatActivity() {

    private lateinit var binding: CrashActivityBinding

    private val clipboardManager: ClipboardManager by lazy {
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CrashActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        Insetter.builder()
            .padding(windowInsetTypesOf(statusBars = true, navigationBars = true))
            .applyToView(binding.root)

        val type = intent.getStringExtra(KEY_EXCEPTION_TYPE)
        val cause = intent.getStringExtra(KEY_EXCEPTION_CAUSE)
        val message = intent.getStringExtra(KEY_EXCEPTION_MESSAGE)
        val stackTrace = intent.getStringExtra(KEY_EXCEPTION_STACKTRACE)

        binding.stacktrace.apply {
            text = stackTrace
            setHorizontallyScrolling(true)
            movementMethod =   ScrollingMovementMethod()
        }

        binding.exceptionName.apply {
            text = getString(R.string.exception_name, type)
        }

        binding.exceptionCause.apply {
            text = getString(R.string.exception_cause, cause)
        }

        binding.exceptionMessage.apply {
            text = getString(R.string.exception_message, message)
        }

        binding.reportGithub.apply {
            setOnClickListener {
                val release = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).versionName
                val repo = getGithubRepoUrl(packageName)
                val date = SimpleDateFormat("dd/M/yyyy hh:mm:ss", textLocale).format(Date())
                val device = Build.MANUFACTURER + " " + Build.PRODUCT + " (" + Build.MODEL + ")"
                val androidVersion = Build.VERSION.RELEASE + " (" + Build.VERSION.SDK_INT +")"
                val uri = githubIssueUrl(repo, type!!, device, androidVersion, release, date, stackTrace!!)
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }

        binding.copyStacktrace.apply {
            setOnClickListener {
                val clipping = ClipData.newPlainText("stacktrace", stackTrace)
                clipboardManager.setPrimaryClip(clipping)
                snackbar { R.string.copied_message }
            }
        }

        binding.launchApplication.apply {
            setOnClickListener {
                context.packageManager.getLaunchIntentForPackage(applicationContext.packageName)
                    ?.let {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
            }
        }

    }

    private inline fun snackbar(messageProvider: () -> Int) {
        Snackbar.make(binding.root, messageProvider(), Snackbar.LENGTH_SHORT).show()
    }
}
