package com.github.familyvault.exceptions

import kotlinx.coroutines.CancellationException

class QrCodeCancellationException(message: String? = null) : CancellationException(message)