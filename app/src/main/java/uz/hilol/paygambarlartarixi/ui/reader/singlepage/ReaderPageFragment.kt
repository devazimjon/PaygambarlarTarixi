package uz.hilol.paygambarlartarixi.ui.reader.singlepage

import android.content.Context
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import uz.hilol.paygambarlartarixi.common.pref.SettingsStorage
import uz.hilol.paygambarlartarixi.databinding.FragmentReaderPageBinding
import uz.hilol.paygambarlartarixi.di.AppComponent
import uz.hilol.paygambarlartarixi.ui.base.MainActivityViewModel
import uz.hilol.paygambarlartarixi.util.setHtmlText
import javax.inject.Inject
import kotlin.properties.Delegates

class ReaderPageFragment : Fragment() {

    @Inject
    lateinit var settings: SettingsStorage

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val activityViewModel by activityViewModels<MainActivityViewModel> { factory }

    private var binding: FragmentReaderPageBinding by Delegates.notNull()

    private val text: String
        get() {
            return arguments?.getString("text") ?: ""
        }

    private val page: Int
        get() {
            return arguments?.getInt("page") ?: 1
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
        binding = FragmentReaderPageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.readerTv.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }

        binding.readerTv.textSize = settings.fontSize
        binding.readerTv.setHtmlText(text)
        binding.currentPageTV.text = "$page"

        activityViewModel.fontSize.observe(viewLifecycleOwner) {
            binding.readerTv.textSize = it
        }
    }

    companion object {
//        @SuppressLint("SetJavaScriptEnabled")
//        private fun configureWebViews(v: WebView) {
//            v.settings.javaScriptEnabled = true
//            v.addJavascriptInterface(WebAppInterface(requireContext()), "Android")
//            v.settings.loadWithOverviewMode = true
//            v.settings.allowFileAccess = true
//            v.settings.defaultFontSize = settings.fontSize
//
//            v.webViewClient = object : WebViewClient() {
//                override fun onPageFinished(view: WebView?, url: String?) {
//                    view?.loadUrl("javascript:Android.resize(document.body.scrollHeight)");
//                }
//            }
//        }

        fun createInstance(text: String, page: Int): ReaderPageFragment {
            val fr = ReaderPageFragment()
            val args = bundleOf(
                "text" to text,
                "page" to page
            )
            fr.arguments = args
            return fr
        }
    }
}