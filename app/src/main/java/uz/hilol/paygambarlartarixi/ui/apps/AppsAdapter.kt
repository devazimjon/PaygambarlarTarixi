package uz.hilol.paygambarlartarixi.ui.apps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.hilol.paygambarlartarixi.R
import uz.hilol.paygambarlartarixi.data.remote.HilolAppsData
import uz.hilol.paygambarlartarixi.data.remote.BookData
import uz.hilol.paygambarlartarixi.databinding.ItemAppBinding

class AppsAdapter :
    RecyclerView.Adapter<AppsAdapter.AppViewHolder>() {

    private val apps = listOf(
        HilolAppsData("Tafsiri Hilol", R.drawable.tafsiri_hilol,"https://play.google.com/store/apps/details?id=uz.hilal.tafsir"),
        HilolAppsData("Namoz", R.drawable.namoz,"https://play.google.com/store/apps/details?id=uz.hilal.namoz"),
        HilolAppsData("Qur'on tartili (beta nashr)", R.drawable.quran_tartil,"https://play.google.com/store/apps/details?id=uz.islom.tajvid"),
        HilolAppsData("Asmaul husna", R.drawable.asmaul_husna,"https://play.google.com/store/apps/details?id=uz.hilol.asmoihusno"),
        HilolAppsData("Hilol test", R.drawable.hilol_test,"https://play.google.com/store/apps/details?id=uz.hilolnashr.hilolquiz"),
        HilolAppsData("Quroni Karim (so'zma-so'z)", R.drawable.quran_karim,"https://play.google.com/store/apps/details?id=uz.hilolnashr.quran_word_by_word"),
        HilolAppsData("Abdurahmon Qur'on", R.drawable.abdurahman,"https://play.google.com/store/apps/details?id=uz.quran.abdurahmon"),
        HilolAppsData("Multfilmlar", R.drawable.cartoons,"https://play.google.com/store/apps/details?id=uz.hilol.multfilm"),
    )

    inner class AppViewHolder(val binding: ItemAppBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppViewHolder(binding)
    }

    private var onItemClickListener: ((HilolAppsData) -> Unit)? = null


    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val item = apps[position]
        with(holder.binding) {
            img.setImageResource(item.logoResId)
            tvAppName.text = item.name
            app.setOnClickListener { onItemClickListener?.invoke(item) }
        }
    }

    override fun getItemCount(): Int = apps.size


    fun setOnItemClickListener(listener: (HilolAppsData) -> Unit) {
        onItemClickListener = listener
    }
}
