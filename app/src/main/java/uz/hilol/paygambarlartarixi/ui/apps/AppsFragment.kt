package uz.hilol.paygambarlartarixi.ui.apps

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import uz.hilol.paygambarlartarixi.databinding.FragmentAppsBinding
import uz.hilol.paygambarlartarixi.di.AppComponent
import uz.hilol.paygambarlartarixi.ui.base.MainActivityViewModel
import javax.inject.Inject
import kotlin.properties.Delegates


class AppsFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<AppsViewModel> { factory }

    private var binding: FragmentAppsBinding by Delegates.notNull()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AppComponent.get().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val rvAdapter = AppsAdapter()
        binding.bookmarksRv.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        rvAdapter.setOnItemClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(it.playStoreUrl)
                )
            )
        }
    }
}