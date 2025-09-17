package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.noblesoftware.portalcore.theme.LocalDimen

@Composable
fun DefaultShadowHeader(
    modifier: Modifier = Modifier,
    isShadowVisible: Boolean,
    title: String
) {
    if (isShadowVisible) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = colorResource(id = com.noblesoftware.portalcore.R.color.background_body)),
            shape = RoundedCornerShape(LocalDimen.current.zero),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = LocalDimen.current.small)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(LocalDimen.current.regular)
            ) {
                Text(
                    text = title, style = MaterialTheme.typography.labelLarge.copy(
                        colorResource(id = com.noblesoftware.portalcore.R.color.text_primary),
                    )
                )
            }
        }
    } else {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = colorResource(id = com.noblesoftware.portalcore.R.color.background_body)),
            shape = RoundedCornerShape(LocalDimen.current.zero),
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(LocalDimen.current.regular)
            ) {
                Text(
                    text = title, style = MaterialTheme.typography.labelLarge.copy(
                        colorResource(id = com.noblesoftware.portalcore.R.color.text_primary),
                    )
                )
            }
        }
    }
}