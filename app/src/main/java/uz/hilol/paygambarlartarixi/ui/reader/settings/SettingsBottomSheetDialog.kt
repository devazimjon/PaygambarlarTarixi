package uz.hilol.paygambarlartarixi.ui.reader.settings

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.hilol.paygambarlartarixi.R
import uz.hilol.paygambarlartarixi.common.extension.setTint
import uz.hilol.paygambarlartarixi.databinding.FragmentReaderSettingsBottomSheetBinding
import uz.hilol.paygambarlartarixi.di.AppComponent
import uz.hilol.paygambarlartarixi.ui.base.MainActivityViewModel
import uz.hilol.paygambarlartarixi.util.color
import javax.inject.Inject

class SettingsBottomSheetDialog : BottomSheetDialogFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<SettingsBottomSheetVM> { factory }

    private val activityViewModel: MainActivityViewModel by activityViewModels()

    var listener: SettingsCallbackListener? = null

    private var _binding: FragmentReaderSettingsBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AppComponent.get().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReaderSettingsBottomSheetBinding.inflate(inflater)
        updateSliderText()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.slider.value = viewModel.currentPage.value!!.toFloat()

        arguments?.let {
            if (it.containsKey(MAX_PAGE)) {
                viewModel.maxPage = it.getInt(MAX_PAGE, 10)
            }
            if (it.containsKey(CURRENT_PAGE)) {
                viewModel.currentPage.value = it.getInt(CURRENT_PAGE, 1)
            }
        }

        binding.confirmBtn.setOnClickListener {
            dismiss()
        }

        initUI()
        initVM()
    }

    private fun initVM() {
        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        viewModel.currentPage.observe(viewLifecycleOwner) {
            viewModel.updateBookmark()
            updateSliderText()

        }

        activityViewModel.bookOrientation.observe(viewLifecycleOwner) {
            if (it == ViewPager2.ORIENTATION_HORIZONTAL) {
                binding.horizontalReadIv.setTint(color(R.color.color_icons_enabled))
                binding.verticalReadIv.setTint(color(R.color.color_icons_disabled))
            } else {
                binding.horizontalReadIv.setTint(color(R.color.color_icons_disabled))
                binding.verticalReadIv.setTint(color(R.color.color_icons_enabled))
            }
        }

        activityViewModel.fontSize.observe(viewLifecycleOwner) {
            val prefix = binding.fontSizeTv.text.split(":".toRegex())[0]
            binding.fontSizeTv.text = "$prefix: ${it.toInt()}"
            binding.fontSizeCurrentIv.text = it.toInt().toString()
        }

        viewModel.isBookmarkEnabled.observe(viewLifecycleOwner) {
            binding.addBookmarkIv.setImageResource(
                if (it)
                    R.drawable.ic_bookmark
                else
                    R.drawable.ic_bookmark_off
            )
        }
    }

    private fun initUI() {
        binding.closeIv.setOnClickListener {
            dismiss()
        }

        listOf(
            binding.addBookmarkIv,
            binding.addBookmarkTv
        ).forEach {
            it.setOnClickListener { viewModel.onClickBookmark() }
        }

        binding.horizontalReadIv.setOnClickListener {
            activityViewModel.changeBookOrientation(ViewPager2.ORIENTATION_HORIZONTAL)
        }

        binding.verticalReadIv.setOnClickListener {
            activityViewModel.changeBookOrientation(ViewPager2.ORIENTATION_VERTICAL)
        }

        binding.fontSizeIncreaseIv.setOnClickListener {
            activityViewModel.increaseFontSize()
        }

        binding.fontSizeDecreaseIv.setOnClickListener {
            activityViewModel.decreaseFontSize()
        }

        binding.slider.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                viewModel.currentPage.value = value.toInt()
                updateSliderText()
                listener?.sliderListener(value.toInt() - 1)
            }
        }

        val colorList = listOf(
            binding.backgroundColorGreyIv,
            binding.backgroundColorWhiteIv,
            binding.backgroundColorOrangeIv
        )

        colorList.forEachIndexed { index, v ->
            v.setOnClickListener {
                colorList.forEach { it.setImageDrawable(null) }
                v.setImageResource(R.drawable.back_outlined_selected)
                listener?.backgroundColor(
                    when (index) {
                        0 -> R.color.chuck_status_requested
                        1 -> R.color.white
                        2 -> R.color.chuck_colorAccent
                        else -> R.color.white
                    }
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateSliderText() {
        binding.slider.valueTo = viewModel.maxPage.toFloat()
        binding.pageIndexTv.text = "${viewModel.currentPage.value}/${viewModel.maxPage}"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CURRENT_PAGE = "currentPage"
        const val MAX_PAGE = "maxPage"

        fun newInstance(maxPage: Int, currentPage: Int): SettingsBottomSheetDialog {
            val fragment = SettingsBottomSheetDialog()
            fragment.arguments = bundleOf(
                MAX_PAGE to maxPage,
                CURRENT_PAGE to currentPage
            )
            return fragment
        }
    }
}

interface SettingsCallbackListener {
    fun addBookmark(page: Int)

    fun backgroundColor(@ColorRes color: Int)

    fun sliderListener(page: Int)
}