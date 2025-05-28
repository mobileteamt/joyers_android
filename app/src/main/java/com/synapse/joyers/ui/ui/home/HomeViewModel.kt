package com.synapse.joyers.ui.ui.home


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.synapse.joyers.utils.DimensionUtils

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    // Example dimension values from design spec
    private val designMarginStartDp = 15f
    private val designTitleTextSizeSp = 16f
    private val designButtonSizeDp = 35f
    private val designImageWidthDp = 12f
    private val designImageHeightDp = 16f
    private val designPaddingTopDp = 10f
    private val designPaddingBottomDp = 10f

    // Expose converted/scaled dimensions as LiveData for data binding
    private val _marginStartDp = MutableLiveData<Float>()
    val marginStartDp: LiveData<Float> get() = _marginStartDp

    private val _titleTextSizeSp = MutableLiveData<Float>()
    val titleTextSizeSp: LiveData<Float> get() = _titleTextSizeSp

    private val _buttonSizeDp = MutableLiveData<Float>()
    val buttonSizeDp: LiveData<Float> get() = _buttonSizeDp

    private val _imageWidthDp = MutableLiveData<Float>()
    val imageWidthDp: LiveData<Float> get() = _imageWidthDp

    private val _imageHeightDp = MutableLiveData<Float>()
    val imageHeightDp: LiveData<Float> get() = _imageHeightDp

    private val _paddingTopDp = MutableLiveData<Float>()
    val paddingTopDp: LiveData<Float> get() = _paddingTopDp

    private val _paddingBottomDp = MutableLiveData<Float>()
    val paddingBottomDp: LiveData<Float> get() = _paddingBottomDp

    init {
        // Initialize scaled dimension values
        _marginStartDp.value = DimensionUtils.dpToPx(context, designMarginStartDp)
        _titleTextSizeSp.value = DimensionUtils.getScaledFontSize(context, designTitleTextSizeSp)
        _buttonSizeDp.value = DimensionUtils.dpToPx(context, designButtonSizeDp)
        _imageWidthDp.value = DimensionUtils.dpToPx(context, designImageWidthDp)
        _imageHeightDp.value = DimensionUtils.dpToPx(context, designImageHeightDp)
        _paddingTopDp.value = DimensionUtils.dpToPx(context, designPaddingTopDp)
        _paddingBottomDp.value = DimensionUtils.dpToPx(context, designPaddingBottomDp)
    }
}
