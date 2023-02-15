package uz.hilol.paygambarlartarixi.ui.reader.adapter


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.hilol.paygambarlartarixi.ui.reader.singlepage.ReaderPageFragment

class ReaderAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    var list: List<String> = mutableListOf()

    override fun getItemCount(): Int = list.count()

    override fun createFragment(position: Int): Fragment {
        return ReaderPageFragment.createInstance(list[position], position + 1)
    }


}