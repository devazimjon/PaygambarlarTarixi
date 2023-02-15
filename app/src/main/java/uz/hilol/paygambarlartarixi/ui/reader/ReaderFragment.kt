package uz.hilol.paygambarlartarixi.ui.reader

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import uz.hilol.paygambarlartarixi.R
import uz.hilol.paygambarlartarixi.data.database.model.BookmarkEntity
import uz.hilol.paygambarlartarixi.data.database.model.SectionEntity
import uz.hilol.paygambarlartarixi.databinding.FragmentReaderBinding
import uz.hilol.paygambarlartarixi.di.AppComponent
import uz.hilol.paygambarlartarixi.ui.base.MainActivityViewModel
import uz.hilol.paygambarlartarixi.ui.reader.adapter.ReaderAdapter
import uz.hilol.paygambarlartarixi.ui.reader.contentpicker.ContentPickerCallbackListener
import uz.hilol.paygambarlartarixi.ui.reader.contentpicker.ContentPickerFragment
import uz.hilol.paygambarlartarixi.ui.reader.settings.SettingsBottomSheetDialog
import uz.hilol.paygambarlartarixi.ui.reader.settings.SettingsCallbackListener
import javax.inject.Inject
import kotlin.properties.Delegates

class ReaderFragment : Fragment(), SettingsCallbackListener, ContentPickerCallbackListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<ReaderFragmentVM> { factory }

    private val activityViewModel by activityViewModels<MainActivityViewModel> { factory }

    private var adapter: ReaderAdapter by Delegates.notNull()

    private var binding: FragmentReaderBinding by Delegates.notNull()

    private val bookTitle: String by lazy {
        arguments?.getString("title") ?: ""
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AppComponent.get().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReaderBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initVM()
        viewModel.openBook()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initUI() {
        adapter = ReaderAdapter(this)
        binding.viewPager.adapter = adapter
        binding.titleTv.text = bookTitle

        binding.closeIv.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.settingsIv.setOnClickListener {
            val settingsDialog = SettingsBottomSheetDialog.newInstance(
                maxPage = viewModel.maxPage,
                currentPage = viewModel.currentPage.value!! + 1
            )
            settingsDialog.listener = this
            settingsDialog.show(childFragmentManager, settingsDialog.tag)
        }

        binding.contentIv.setOnClickListener {
            viewModel.openContentPicker()
        }

        binding.contentBck.setOnTouchListener { v, event ->
            viewModel.touchAction(event)
            true
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.currentPage.value = position
            }
        })

    }

    private fun initVM() {

        activityViewModel.bookOrientation.observe(viewLifecycleOwner) {
            binding.viewPager.orientation = it
        }

        viewModel.currentPage.observe(viewLifecycleOwner) {
            binding.viewPager.currentItem = it
        }

        viewModel.content.observe(viewLifecycleOwner) {
            adapter.list = it
            adapter.notifyDataSetChanged()
        }

        viewModel.isContentPickerOpened.observe(viewLifecycleOwner) {
            if (it) {
                binding.contentPicker.apply {
                    isVisible = true
                    binding.contentBck.alpha = 0f
                }
                val contentPicker = ContentPickerFragment()
                contentPicker.listener = this
                binding.contentBck.animate()
                    .setDuration(300)
                    .alpha(1f)
                    .withStartAction {
                        childFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.content_enter, R.anim.content_exit)
                            .replace(R.id.content_container, contentPicker)
                            .commitNowAllowingStateLoss()
                    }
                    .start()

            } else {
                binding.contentBck.animate()
                    .setDuration(300)
                    .alpha(0f)
                    .withStartAction {
                        childFragmentManager.fragments.lastOrNull()?.let {
                            childFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.content_enter, R.anim.content_exit)
                                .remove(it)
                                .commitNowAllowingStateLoss()
                        }
                    }
                    .withEndAction {
                        binding.contentPicker.isGone = true
                    }
                    .start()
            }
        }
    }

    override fun addBookmark(page: Int) {
//        TODO("Not yet implemented")
    }

    override fun sliderListener(page: Int) {
        viewModel.currentPage.value = page
    }

    override fun backgroundColor(@ColorRes color: Int) {
        binding.viewPager.setBackgroundColor(ContextCompat.getColor(requireContext(), color))
    }

    override fun sectionSelected(section: SectionEntity) {
        viewModel.currentPage.value = section.pageNumber
        viewModel.closeContentPicker()
    }

    override fun bookmarkSelected(bookmark: BookmarkEntity) {
        viewModel.currentPage.value = bookmark.page
        viewModel.closeContentPicker()
    }
}