package com.github.familyvault.models

import com.github.familyvault.BuildConfig

class AndroidCompilationFlags : ICompilationFlags {
    override val chatEnabled: Boolean = BuildConfig.CHAT_ENABLED
    override val tasksEnabled: Boolean = BuildConfig.TASKS_ENABLED
    override val filesEnabled: Boolean = BuildConfig.FILES_ENABLED
}
