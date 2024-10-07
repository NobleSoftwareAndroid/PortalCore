package com.noblesoftware.portalcore.container

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.noblesoftware.portalcore.databinding.ActivityFragmentContainerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentContainerActivity : AppCompatActivity() {

    private val viewModel: FragmentContainerViewModel by viewModels()

    lateinit var binding: ActivityFragmentContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            com.noblesoftware.portalcore.R.layout.activity_fragment_container
        )

        openFragment()
    }

    override fun onBackPressed() {
        val keyBoardVisible = ViewCompat.getRootWindowInsets(binding.navHostFragment)
            ?.isVisible(WindowInsetsCompat.Type.ime())
        if (keyBoardVisible == true) {
            val windowInsetsController =
                ViewCompat.getWindowInsetsController(binding.navHostFragment)
            windowInsetsController?.hide(WindowInsetsCompat.Type.ime())
        } else if (!supportFragmentManager.popBackStackImmediate()) {
            super.onBackPressed()
        }
    }

    companion object {
        var mFragment: Fragment? = null
        const val KEY_DEEPLINK = "SignUpFragment.deeplink"
    }

    inner class FragmentTransitionBuilder constructor(val context: Context) {

        fun setFragment(fragment: Fragment): FragmentTransitionBuilder {
            mFragment = fragment
            return this
        }

        fun show(getResult: ActivityResultLauncher<Intent>? = null) {
            if (getResult != null) {
                getResult.launch(
                    Intent(
                        context,
                        FragmentContainerActivity::class.java
                    )
                )

            } else {
                context.startActivity(Intent(context, FragmentContainerActivity::class.java))
            }
        }
    }

    private fun openFragment() {
        mFragment?.let {
            supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(
                    com.noblesoftware.portalcore.R.anim.slide_in_right,
                    com.noblesoftware.portalcore.R.anim.slide_out_left,
                    com.noblesoftware.portalcore.R.anim.slide_in_left,
                    com.noblesoftware.portalcore.R.anim.slide_out_right
                )
            }.replace(com.noblesoftware.portalcore.R.id.nav_host_fragment, it).commit()
        }
    }

    override fun onDestroy() {
        if (mFragment != null) mFragment = null
        super.onDestroy()
    }

}