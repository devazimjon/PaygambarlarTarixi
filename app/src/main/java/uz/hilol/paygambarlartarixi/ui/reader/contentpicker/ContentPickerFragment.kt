package uz.hilol.paygambarlartarixi.ui.reader.contentpicker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import uz.hilol.paygambarlartarixi.data.database.model.BookmarkEntity
import uz.hilol.paygambarlartarixi.data.database.model.SectionEntity
import uz.hilol.paygambarlartarixi.databinding.FragmentContentPickerBinding
import uz.hilol.paygambarlartarixi.di.AppComponent
import uz.hilol.paygambarlartarixi.ui.reader.contentpicker.ContentPickerFragmentVM.ContentPickerType
import uz.hilol.paygambarlartarixi.ui.reader.contentpicker.adapter.ContentPickerAdapter
import javax.inject.Inject
import kotlin.properties.Delegates

class ContentPickerFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<ContentPickerFragmentVM> { factory }

    var listener: ContentPickerCallbackListener? = null

    private var adapter: ContentPickerAdapter by Delegates.notNull()

    private var binding: FragmentContentPickerBinding by Delegates.notNull()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        AppComponent.get().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContentPickerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ContentPickerAdapter(requireContext()) { position ->
            when (viewModel.currentContentState.value) {
                ContentPickerType.SECTION -> {
                    (viewModel.content.value as? List<SectionEntity>)?.let {
                        listener?.sectionSelected(it[position])
                    }
                }
                ContentPickerType.BOOKMARK -> {
                    (viewModel.content.value as? List<BookmarkEntity>)?.let {
                        listener?.bookmarkSelected(it[position])
                    }
                }
            }
        }

        binding.contentRv.apply {
            adapter = this@ContentPickerFragment.adapter
        }



        initUI()
        initVM()
    }

    fun initUI() {

        binding.contentTl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.currentContentState.value = if (tab?.position == 0)
                    ContentPickerType.SECTION
                else
                    ContentPickerType.BOOKMARK
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun initVM() {
        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        viewModel.content.observe(viewLifecycleOwner) {
            when (viewModel.currentContentState.value) {
                ContentPickerType.SECTION -> {
                    (it as? List<SectionEntity>)?.let {
                        adapter.setItems(it.map { it.name })
                        adapter.notifyDataSetChanged()
                    }
                }
                ContentPickerType.BOOKMARK -> {
                    (it as? List<BookmarkEntity>)?.let {
                        adapter.setItems(it.map { "xatchop ${it.page}" })
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}

interface ContentPickerCallbackListener {

    fun sectionSelected(section: SectionEntity)

    fun bookmarkSelected(bookmark: BookmarkEntity)
}