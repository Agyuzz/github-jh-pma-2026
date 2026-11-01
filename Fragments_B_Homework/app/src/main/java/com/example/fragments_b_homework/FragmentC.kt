package com.example.fragments_b_homework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.fragments_b_homework.databinding.FragmentCBinding

class FragmentC : Fragment() {

    private var _binding: FragmentCBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vždy, když se změní seznam úkolů (někdo něco zaškrtne nebo přidá), spustí se tento blok
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            val total = tasks.size
            val done = tasks.count { it.done }
            val percent = if (total == 0) 0 else done * 100 / total

            binding.progressBar.progress = percent
            binding.tvPercent.text = "$percent %"
            binding.tvSummary.text = "Hotovo $done z $total úkolů"

            val image: Int
            val message: String
            when {
                percent < 30 -> { // méně než 30 %
                    image = R.drawable.stat_low
                    message = "Zatím nic moc, do toho!"
                }
                percent < 70 -> { // 30 až 69 %
                    image = R.drawable.stat_mid
                    message = "Jde to, pokračuj!"
                }
                else -> {  // 70 % a víc
                    image = R.drawable.stat_high
                    message = "Skvělá práce, jsi šampion!"
                }
            }
            binding.ivStatus.setImageResource(image) // zobrazí vybraný obrázek
            binding.tvMessage.text = message
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}