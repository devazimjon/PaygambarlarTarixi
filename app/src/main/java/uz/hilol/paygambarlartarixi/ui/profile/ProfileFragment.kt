package uz.hilol.paygambarlartarixi.ui.profile

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yariksoffice.lingver.Lingver
import uz.hilol.paygambarlartarixi.R
import uz.hilol.paygambarlartarixi.common.pref.SettingsStorage
import uz.hilol.paygambarlartarixi.common.util.Constants
import uz.hilol.paygambarlartarixi.databinding.FragmentProfileBinding
import uz.hilol.paygambarlartarixi.di.AppComponent
import uz.hilol.paygambarlartarixi.ui.dialog.NightModePickerDialog
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates

class ProfileFragment : Fragment() {

    @Inject
    lateinit var settingsStorage: SettingsStorage

    private var binding: FragmentProfileBinding by Delegates.notNull()

    var initialRadioButtonId = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AppComponent.get().inject(this)
        initialRadioButtonId = if (settingsStorage.language == "uz")
            R.id.language_uz else R.id.language_cr
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.languageRg.check(initialRadioButtonId)

        binding.languageRg.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != initialRadioButtonId) {
                changeLanguage(
                    when (checkedId){
                        R.id.language_uz -> Constants.LOCALE_LATIN
                        R.id.language_cr -> Constants.LOCALE_CYRILL
                        else -> Constants.LOCALE_LATIN
                    }
                )
            }
        }
    }

    private fun changeLanguage(locale: Locale) {
        Lingver.getInstance().setLocale(requireContext(), locale)
        settingsStorage.language = locale.language
        Handler(Looper.getMainLooper()).postDelayed({
            activity?.finish()
            activity?.overridePendingTransition(0, 0)
            activity?.startActivity(activity?.intent)
            activity?.overridePendingTransition(0, 0)
        }, 200)
    }

}