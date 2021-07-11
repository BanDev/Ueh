package uk.bandev.ueh

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Messenger

/**
 * **DO NOT USE**
 * A class to allow initialization of WhatTheStack service.
 *
 * WhatTheStack initializes automatically using a content provider. You do not need to initialize
 * it explicitly using this class.
 *
 * @param applicationContext The context used to start the service to catch uncaught exceptions
 */
@Deprecated(
    "WhatTheStack initializes automatically at application startup. Do not explicitly initialize it",
    level = DeprecationLevel.ERROR
)
class WhatTheStack(private val applicationContext: Context) {

    @Suppress("unused")
    fun init() {
        InitializationManager.init(applicationContext)
    }
}

internal object InitializationManager {

    private var isInitialized: Boolean = false

    private val connection = WhatTheStackConnection(
        onConnected = { binder ->
            val messenger = Messenger(binder)
            val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
            val exceptionHandler = WhatTheStackExceptionHandler(messenger, defaultHandler)
            Thread.setDefaultUncaughtExceptionHandler(exceptionHandler)
        }
    )

    fun init(applicationContext: Context) {
        if (isInitialized) { return }
        isInitialized = true
        val intent = Intent(applicationContext, WhatTheStackService::class.java)
        applicationContext.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }
}

fun githubIssueUrl(repo: String, title: String, device: String, androidVersion: String, release: String, date: String, stacktrace: String) : Uri
    = Uri.parse("https://github.com/$repo/issues/new?labels=Unexpected%20Crash&title=Unexpected%20Crash:%20$title&body=**Discovered%20By:**%20BanDev%20Unexpected%20Crash%20Handler%0A**Device:**%20$device%0A**Android version:**%20$androidVersion%0A**Release:**%20$release%0A**Date:**%20$date%0A**Stacktrace:**```$stacktrace```")

fun getGithubRepoUrl(packageName: String): String {
    return when(packageName) {
        "org.bandev.buddhaquotes" -> "bandev/buddha-quotes"
        "org.bandev.labyrinth" -> "bandev/labyrinth"
        else -> "bandev/buddha-quotes"
    }
}