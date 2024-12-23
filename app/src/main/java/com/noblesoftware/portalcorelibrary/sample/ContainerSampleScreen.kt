package com.noblesoftware.portalcorelibrary.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.component.compose.ButtonVariant
import com.noblesoftware.portalcore.component.compose.DefaultButton
import com.noblesoftware.portalcore.component.compose.DefaultSpacer
import com.noblesoftware.portalcore.component.compose.DefaultTopAppBar
import com.noblesoftware.portalcore.container.FragmentContainerActivity
import com.noblesoftware.portalcore.container.WebViewContainerFragment
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.util.extension.findActivity
import com.noblesoftware.portalcore.util.extension.handleSafeScaffoldPadding
import com.noblesoftware.portalcorelibrary.sample.fragment.SampleBlankFragment

@Composable
fun ContainerSampleScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val activity = context.findActivity()

    Scaffold(
        modifier = Modifier.handleSafeScaffoldPadding(),
        topBar = {
            DefaultTopAppBar(
                modifier = Modifier,
                title = "Container",
                canBack = true,
                navigator = navHostController
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.background_body))
                .padding(paddingValues = it)
                .verticalScroll(rememberScrollState())
                .then(
                    Modifier.padding(LocalDimen.current.regular)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Fragment Container",
                buttonVariant = ButtonVariant.Neutral
            ) {
                FragmentContainerActivity().FragmentTransitionBuilder(context)
                    .setFragment(SampleBlankFragment()).show()
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Dynamic Fragment Containet",
                buttonVariant = ButtonVariant.Neutral
            ) {
                FragmentContainerActivity().FragmentTransitionBuilder(context).show(
                    content = {
                        Text(text = "Dynamic Fragment Container example")
                    }
                )
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Web View Container",
                buttonVariant = ButtonVariant.Neutral
            ) {
                WebViewContainerFragment.openWebView(
                    context = context,
                    title = "Web view Sample",
                    url = "https://www.google.com/"
                )
            }
            DefaultSpacer()

        }
    }
}