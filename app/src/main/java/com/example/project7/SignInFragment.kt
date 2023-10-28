package com.example.project7
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.project7.databinding.FragmentSignInBinding

/**
 * A simple [Fragment] subclass.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignInFragment : Fragment() {
    val TAG = "SignInFragment"
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewModel : TasksViewModel by activityViewModels()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // navigate to the tasks fragment when we sign in
        viewModel.navigateToList.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_signInFragment_to_tasksFragment)
                viewModel.onNavigatedToList()
            }
        })
        // navigate to sign up fragment when we click the button
        viewModel.navigateToSignUp.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_signInFragment_to_signUpFragment)
                viewModel.onNavigatedToSignUp()
            }
        })
        // display errors from the viewmodel as toasts
        viewModel.errorHappened.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
        })
        // when we navigate from the sign in fragment to itself (called when clicking sign out button) re-set the edittext fields
        viewModel.navigateToSignIn.observe(viewLifecycleOwner) {
            binding.emailEt.setText(viewModel.user.email)
            binding.passEt.setText(viewModel.user.password)
        }
        // populate the sign in edittext fields with the users current info
        if (viewModel.user.email.isNotBlank() && viewModel.user.password.isNotBlank()) {
            binding.emailEt.setText(viewModel.user.email)
            binding.passEt.setText(viewModel.user.password)
        }

        return view
    }


}