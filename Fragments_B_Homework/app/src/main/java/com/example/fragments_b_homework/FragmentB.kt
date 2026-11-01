package com.example.fragments_b_homework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.fragments_b_homework.databinding.FragmentBBinding

class FragmentB : Fragment() {

    private var _binding: FragmentBBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Po klepnutí na den v kalendáři ulož vybraný den do ViewModelu.
        // Díky tomu ho uvidí i FragmentA (přidá úkol na tento den).
        viewModel.selectedDate.value?.let { binding.calendarView.date = keyToMillis(it) }

        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            viewModel.selectedDate.value = dateKey(year, month, day)
        }
        // Překresli seznam, když se změní vybraný den NEBO se změní úkoly
        viewModel.selectedDate.observe(viewLifecycleOwner) { render() }
        viewModel.tasks.observe(viewLifecycleOwner) { render() }
    }

    private fun render() {
        // Vybraný den (kdyby byl null, použij dnešek)
        val day = viewModel.selectedDate.value ?: today()
        // Z celého seznamu nech jen úkoly, které mají datum rovné vybranému dni
        val tasks = viewModel.tasks.value.orEmpty().filter { it.date == day }

        binding.tvSelectedDay.text = "Úkoly na $day (${tasks.size})"
        binding.listDayTasks.removeAllViews() // smaž starý obsah seznamu
        // Když na ten den nic není, napiš to
        if (tasks.isEmpty()) {
            binding.listDayTasks.addView(TextView(requireContext()).apply {
                text = "Na tento den nemáš žádné úkoly."
            })
        }
        tasks.forEach { task ->
            binding.listDayTasks.addView(TextView(requireContext()).apply {
                text = (if (task.done) "✅ " else "⬜ ") + task.title
                textSize = 16f
                setPadding(0, 8, 0, 8)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}