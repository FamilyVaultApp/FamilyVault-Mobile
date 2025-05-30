package com.github.familyvault.ui.components.privateKey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Key
import androidx.compose.runtime.Composable
import com.github.familyvault.DocumentationLinks
import com.github.familyvault.ui.components.HeaderWithIcon
import com.github.familyvault.ui.components.InfoBox
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.private_key_about_content
import familyvault.composeapp.generated.resources.private_key_about_title
import familyvault.composeapp.generated.resources.private_key_assign_password_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun PrivateKeyAssignPasswordHeader() {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        HeaderWithIcon(
            stringResource(Res.string.private_key_assign_password_title),
            Icons.Outlined.Key
        )
        InfoBox(
            title = stringResource(Res.string.private_key_about_title),
            content = stringResource(Res.string.private_key_about_content),
            link = DocumentationLinks.PRIVATE_KEY
        )
    }
}