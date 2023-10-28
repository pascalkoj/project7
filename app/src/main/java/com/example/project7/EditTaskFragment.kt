package com.example.project7

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.project7.databinding.FragmentEditTaskBinding


/**
 * A simple [Fragment] subclass.
 * Use the [EditTaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditTaskFragment : Fragment() {
    val TAG = "EditTaskFragment"
    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = _binding!!

    // navigates to the main notes fragment and notifies the viewmodel
    fun NavigateToTasksFragment(view: View, viewModel: TasksViewModel)
    {
        view.findNavController()
            .navigate(R.id.action_editTaskFragment_to_tasksFragment)
        viewModel.onNavigatedToList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentEditTaskBinding.inflate(inflater, container, false)
        // initialize viewmodel and bindings
        val view = binding.root
        val taskId = EditTaskFragmentArgs.fromBundle(requireArguments()).taskId
        val viewModel : TasksViewModel by activityViewModels()
        viewModel.taskId = taskId
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // when the viewmodel wants to move to the list, navigate to our main notes fragment
        viewModel.navigateToList.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                NavigateToTasksFragment(view, viewModel)
            }
        })
        // when user wants to delete a note, delete it from the viewmodel and navigate off the note editing fragment
        fun yesPressed(taskId : String) {
            viewModel.deleteTask(taskId)
            NavigateToTasksFragment(view, viewModel)
        }
        binding.bDeleteTask.setOnClickListener {
            ConfirmDeleteDialogFragment(taskId,::yesPressed).show(childFragmentManager,
                ConfirmDeleteDialogFragment.TAG)
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}