package homepunk.github.com.presentation.core.ext

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import homepunk.github.com.domain.model.internal.CityLocation
import homepunk.github.com.presentation.feature.widget.dropdownlistview.DropDownListView
import homepunk.github.com.presentation.feature.widget.dropdownlistview.OnDropDownClickListener
import homepunk.github.com.presentation.feature.widget.menu.MenuLayout
import timber.log.Timber

/**Created by Homepunk on 09.07.2019. **/

/*
@BindingAdapter("dropDownItems")
fun <T> DropDownListView<T>.dropDownItems(oldItems: List<T>?, items: List<T>?) {
    Timber.d("dropDownItems: oldItems SIZE = ${oldItems?.size} LIST SIZE = ${items?.size}")
    if (!items.isNullOrEmpty()) {
        setFavoriteCountries(items as MutableList<T>)
    }
}
*/

@BindingAdapter(value = ["dropDownList", "dropDownListCurrentForUserLocation"])
fun <T> DropDownListView<T>.dropDownList(items: List<T>?, isUserLocation: Boolean) {
    items?.apply {
        if (isUserLocation) {
            with(items as List<CityLocation>) {
                forEachIndexed { index, location ->
                    if (location.isCurrent) {
                        setItemList(index, items)
                        return@with
                    }
                }
            }
        } else {
            setItemList(items)
        }

    } ?: return
}

@BindingAdapter(value = ["menuItems"])
fun MenuLayout.setMenuItems(items: List<MenuLayout.MenuItem>?) {
    if (!items.isNullOrEmpty()) {
        setMenu(items)
    }
}

@BindingAdapter("dropDownSelection")
fun <T> DropDownListView<T>.selectedDropDownItemPosition(position: Int) {
    setCurrentItem(position)
}


@BindingAdapter(value = ["dropDownItems", "dropDownSelection"])
fun <T> DropDownListView<T>.setUpDropDownListView(items: LiveData<MutableList<T>?>, position: Int) {
    Timber.d("setUpDropDownListView: LIST SIZE = ${items.value?.size}, POSITION = $position")
    if (items.value != null && position > 0) {
        setItemList(position, items.value!!)
    }
}

@BindingAdapter("onDropDownClickListener")
fun <T> DropDownListView<T>.onDropDownClickListener(listener: OnDropDownClickListener<T>?) {
    if (listener != null) {
        onDropDownClickListener = listener
    }
}