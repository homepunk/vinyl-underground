package homepunk.github.com.presentation.feature.discover.event

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import homepunk.github.com.presentation.BR
import homepunk.github.com.presentation.R
import homepunk.github.com.presentation.common.adapter.ExpandableBindingRecyclerAdapter
import homepunk.github.com.presentation.common.adapter.SimpleBindingRecyclerAdapter
import homepunk.github.com.presentation.core.base.BaseFragment
import homepunk.github.com.presentation.databinding.FragmentEventBinding
import homepunk.github.com.presentation.feature.discover.event.model.EventModel
import homepunk.github.com.presentation.feature.discover.event.model.UpcomingEventBindingChildModel
import homepunk.github.com.presentation.feature.discover.event.model.UpcomingEventBindingParentModel

class DiscoverEventFragment : BaseFragment<FragmentEventBinding>() {
    private lateinit var eventListViewModel: DiscoverEventViewModel

    override var layoutId = R.layout.fragment_event

    override fun init() {
        eventListViewModel = getViewModel(DiscoverEventViewModel::class.java)
        wLog("init")

        with(mDataBinding) {
            rvPrimaryEvents.adapter = SimpleBindingRecyclerAdapter<EventModel>(R.layout.layout_item_primary_event, BR.model)
            rvUpcomingEvents.adapter = ExpandableBindingRecyclerAdapter<UpcomingEventBindingChildModel, UpcomingEventBindingParentModel>()

            viewModel = eventListViewModel
        }
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        eventListViewModel.fetchUpcomingEventList()
    }

    override fun onResume() {
        super.onResume()
            wLog("onResume")
    }
}