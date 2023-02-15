package uz.hilol.paygambarlartarixi.ui.splash

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uz.hilol.paygambarlartarixi.databinding.FragmentSplashBinding
import uz.hilol.paygambarlartarixi.di.AppComponent
import kotlin.properties.Delegates

class SplashFragment : Fragment() {

    private var binding: FragmentSplashBinding by Delegates.notNull()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AppComponent.get().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater)
        return binding.root
    }

}