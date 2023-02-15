package uz.hilol.paygambarlartarixi.ui.dialog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import uz.hilol.paygambarlartarixi.R
import uz.hilol.paygambarlartarixi.common.pref.SettingsStorage
import uz.hilol.paygambarlartarixi.di.AppComponent
import javax.inject.Inject

class NightModePickerDialog : DialogFragment(), RadioGroup.OnCheckedChangeListener {

    @Inject
    lateinit var settingsStorage: SettingsStorage

    private lateinit var light: RadioButton
    private lateinit var dark: RadioButton
    private lateinit var system: RadioButton

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDagger()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_night_mode_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        light = view.findViewById(R.id.light)
        dark = view.findViewById(R.id.dark)
        system = view.findViewById(R.id.system)



        when (settingsStorage.nightMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> light.isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> dark.isChecked = true
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> system.isChecked =
                true
            else -> light.isChecked = true
        }


        if (isPreAndroid10()) {
            system.text = getString(R.string.battery_saver)
        }

        view.findViewById<RadioGroup>(R.id.modeGroup).setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(rg: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.light -> switchToMode(AppCompatDelegate.MODE_NIGHT_NO)
            R.id.dark -> switchToMode(AppCompatDelegate.MODE_NIGHT_YES)
            R.id.system -> {
                if (isPreAndroid10()) {
                    switchToMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                } else {
                    switchToMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
    }

    private fun switchToMode(nightMode: Int) {
        dismiss()
        AppCompatDelegate.setDefaultNightMode(nightMode)
        settingsStorage.nightMode = nightMode
    }

    private fun isPreAndroid10() = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q

    fun injectDagger() {
        AppComponent.get().inject(this)
    }
}