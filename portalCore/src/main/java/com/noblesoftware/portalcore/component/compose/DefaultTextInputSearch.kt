package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.noblesoftware.portalcore.R

@Preview
@Composable
fun DefaultTextInputSearch() {
    DefaultTextInputSearch(
        modifier = Modifier,
        value = "",
        onValueChange = {

        },
        onValueClear = {

        },
        onSearch = {}
    )
}

@Composable
fun DefaultTextInputSearch(
    modifier: Modifier,
    value: String,
    placeholder: String = stringResource(id = R.string.empty_string),
    onValueChange: (String) -> Unit,
    onValueClear: () -> Unit,
    onSearch: () -> Unit,
) {
    DefaultTextInput(
        modifier = modifier,
        placeholder = placeholder,
        inputType = KeyboardType.Text,
        imeAction = ImeAction.Search,
        leadingIcon = {
            DefaultTextInputIcon(
                modifier = Modifier,
                icon = painterResource(id = R.drawable.ic_search)
            )
        },
        trailingIcon = {
            if (value.isNotBlank()) {
                DefaultTextInputIcon(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .clickable {
                            onValueClear()
                        },
                    icon = painterResource(id = R.drawable.ic_close)
                )
            }
        },
        value = value,
        onValueChange = onValueChange,
        keyboardActions = KeyboardActions(
            onSearch = { onSearch() }
        )
    )
}