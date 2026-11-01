package com.example.fragments_b_homework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.fragments_b_homework.databinding.FragmentABinding

class FragmentA : Fragment() {

    private var _binding: FragmentABinding? = null
    private val binding get() = _binding!!

    // sdílený ViewModel – stejná instance pro všechny fragmenty
    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentABinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            val title = binding.etTask.text.toString().trim()
            if (title.isNotEmpty()) {
                viewModel.addTask(title, viewModel.selectedDate.value ?: today())
                binding.etTask.text.clear()
            }
        }

        viewModel.selectedDate.observe(viewLifecycleOwner) {
            binding.tvDate.text = "Nové úkoly se přidají na den: $it"
        }

        viewModel.tasks.observe(viewLifecycleOwner) { render(it) }
    }

    private fun render(tasks: List<Task>) {
        binding.listTasks.removeAllViews()
        tasks.forEach { task ->
            val checkBox = CheckBox(requireContext()).apply {
                text = "${task.title}  (${task.date})"
                isChecked = task.done
                setOnCheckedChangeListener { _, checked -> viewModel.toggle(task, checked) }
            }
            binding.listTasks.addView(checkBox)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}