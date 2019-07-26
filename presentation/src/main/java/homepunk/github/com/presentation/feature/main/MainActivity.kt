package homepunk.github.com.presentation.feature.main

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import homepunk.github.com.presentation.R
import homepunk.github.com.presentation.common.adapter.SimpleViewPagerAdapter
import homepunk.github.com.presentation.core.base.BaseActivity
import homepunk.github.com.presentation.core.ext.isNotVisible
import homepunk.github.com.presentation.core.ext.isVisible
import homepunk.github.com.presentation.core.ext.setupWithViewPager
import homepunk.github.com.presentation.databinding.ActivityMainBinding
import homepunk.github.com.presentation.feature.menu.MenuFragment
import homepunk.github.com.presentation.feature.widget.animation.AnimationEventLiveData
import homepunk.github.com.presentation.feature.widget.animation.ScrollEvent


class MainActivity : BaseActivity<ActivityMainBinding>() {
    private lateinit var mMainViewModel: MainViewModel

    override var layoutId = R.layout.activity_main

    override fun init() {
        mMainViewModel = getViewModel(MainViewModel::class.java)

        mDataBinding.run {
            viewModel = mMainViewModel.apply {
                isMenuOpenedLiveData.observe(this@MainActivity, Observer { open ->
                    if (open) {
                        supportFragmentManager.run {
                            beginTransaction()
                                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                                    .replace(R.id.menu_fragment_container, MenuFragment())
                                    .addToBackStack(null)
                                    .commit()
                        }
                    } else {
                        onBackPressed()
                    }
                })
            }
            viewPager.adapter = SimpleViewPagerAdapter(supportFragmentManager)
            bottomNav.setupWithViewPager(viewPager)

        }


        with(mDataBinding) {
            var scrollEvent = ScrollEvent()
            var segmentBoundaryY = 0
            var segmentHeight = 0

            appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, ofs ->
                val offset = appBarLayout.y / appBarLayout.totalScrollRange
                val alpha = offset * -1f
                bgToolbarShadowAppearance.alpha = alpha
                bgAppBarShadowAppearance.alpha = 1f - alpha
                with(scrollEvent) {
                    //                    wLog("SCROLL APP BAR scrollY $scrollY, contentHeight = $contentHeight")
                    if (scrollY > segmentBoundaryY) {
                        val segmentDelta = appBarLayout.totalScrollRange - segmentHeight
//                        val alpha = (scrollY - segmentBoundaryY).toFloat() / segmentHeight
                        val alpha = ((appBarLayout.y + segmentDelta + (scrollY - segmentBoundaryY).toFloat()) / segmentHeight)
                        bgBottomBarShadowAppearance.alpha = alpha
                        wLog("CURRENT Y VALUE ON THE SEGMENT alpha = $alpha scrolly = ${scrollY - segmentBoundaryY}, offset = $ofs, contentHeight = $contentHeight")
                    }
                }


/*
                if (lastSegmentY > 0 &&
                        lastSegmentY != segmentHeight.toFloat()) {
                    val segmentDelta = appBarLayout.totalScrollRange - segmentHeight
                    val alpha = ((appBarLayout.y + segmentDelta + lastSegmentY) / segmentHeight)
                    bgBottomBarShadowAppearance.alpha = alpha
                }
*/

                val translation = offset * -appBarLayout.height
                bgAppBarShadowAppearance.translationY = appBarLayout.height - translation
            })

            AnimationEventLiveData.getInstance().scrollEventLivaData.observe(this@MainActivity, Observer {
                setUpTopShadowBehaviour(it)
//                wLog("SCROLL BRO ${it.scrollY}")

                if (it.isScrollToPosition) {
                    appBarLayout.setExpanded(false, true)
                } else {
                    val scrollHeight = it.contentHeight - viewPager.height
                    segmentBoundaryY = scrollHeight - segmentHeight
//                    wLog("SCROLL TO NEW Y = ${it.scrollY}, PREVIOUS Y = ${prevScrollEvent.scrollY}, SCROLL HEIGHT = $scrollHeight")
                    if (scrollHeight > 0) {
                        segmentHeight = if (bgBottomBarShadowAppearance.height < scrollHeight) bgBottomBarShadowAppearance.height else scrollHeight

                        val segmentY = it.scrollY.toFloat() - segmentBoundaryY

                        if (it.scrollY > scrollEvent.scrollY) {
                            // IF WE SCROLLED TO BOTTOM SHADOW VIEW
                            if (segmentY >= 0) {
                                wLog("SCROLL TO BOTTOM")
                                val alpha = segmentY / segmentHeight
                                bgBottomBarShadowAppearance.alpha = 1f - alpha
                            } else {
                                wLog("SCROLL TO TOP")
                                bgBottomBarShadowAppearance.alpha = 1f
                            }
                        } /*else if (bgBottomBarShadowAppearance.alpha != 1f) {
                            bgBottomBarShadowAppearance.alpha = 1f
                        }*/

                    }
                }
                scrollEvent = it
            })

        }
    }

    override fun onBackPressed() {
        if (mMainViewModel.isMenuOpenedLiveData.value == true) {
            mMainViewModel.isMenuOpenedLiveData.value = false
        } else {
            super.onBackPressed()
        }
    }

    private fun setUpTopShadowBehaviour(it: ScrollEvent) {
        with(mDataBinding) {
            if (it.scrollY > appBarLayout.height &&
                    bgAppBarShadowAppearance.isNotVisible()) {
                bgAppBarShadowAppearance.visibility = VISIBLE
            } else if (it.scrollY <= appBarLayout.height &&
                    bgAppBarShadowAppearance.isVisible()) {
                (it.scrollY.toFloat() / appBarLayout.height).let { alpha ->
                    bgAppBarShadowAppearance.alpha = alpha
                    if (alpha == 0f) {
                        bgAppBarShadowAppearance.visibility = GONE
                    }
                }
            }
        }
    }
}



