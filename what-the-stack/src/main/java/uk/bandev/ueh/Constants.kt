package uk.bandev.ueh

/**
 * These keys are used to transfer the various components of [ExceptionData] across processes.
 */
internal const val KEY_EXCEPTION_TYPE = "uk.bandev.whatthestack.exception.type"
internal const val KEY_EXCEPTION_CAUSE = "uk.bandev.whatthestack.exception.cause"
internal const val KEY_EXCEPTION_MESSAGE = "uk.bandev.whatthestack.exception.message"
internal const val KEY_EXCEPTION_STACKTRACE = "uk.bandev.whatthestack.exception.stacktrace"
