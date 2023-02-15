package uz.hilol.paygambarlartarixi.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import uz.hilol.paygambarlartarixi.Application
import uz.hilol.paygambarlartarixi.R
import uz.hilol.paygambarlartarixi.databinding.FragmentHomeBinding
import kotlin.properties.Delegates

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val rvAdapter = BooksAdapter()
        binding.booksRv.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        rvAdapter.setOnItemClickListener {

            findNavController().navigate(R.id.readerFragment)
            //TODO open reader here
        }
    }


}