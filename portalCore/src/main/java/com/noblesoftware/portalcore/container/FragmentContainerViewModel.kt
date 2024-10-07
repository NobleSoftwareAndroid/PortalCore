package com.noblesoftware.portalcore.container

import androidx.lifecycle.ViewModel
import com.noblesoftware.portalcore.util.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by fizhu on 07 February 2022
 * https://github.com/Fizhu
 */

@HiltViewModel
class FragmentContainerViewModel @Inject constructor(
    networkHelper: NetworkHelper,
) : ViewModel()