package net.samystudio.beaver.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import net.samystudio.beaver.R
import net.samystudio.beaver.data.handleStatesFromFragmentWithLoaderDialog
import net.samystudio.beaver.databinding.FragmentHomeBinding
import net.samystudio.beaver.util.TRANSITION_DURATION
import net.samystudio.beaver.util.toggleLightNavigationBars
import net.samystudio.beaver.util.toggleLightStatusBars
import net.samystudio.beaver.util.viewBinding

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding { FragmentHomeBinding.bind(it) }
    private val viewModel by viewModels<HomeFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exitTransition = MaterialFadeThrough().apply {
            duration = TRANSITION_DURATION
        }
        toggleLightStatusBars(false)
        toggleLightNavigationBars(true)

        binding.toolbar.applyInsetter {
            type(statusBars = true) {
                padding(top = true)
            }
        }
        binding.profileButton.applyInsetter {
            type(navigationBars = true) {
                margin(bottom = true)
            }
        }

        binding.toolbar.title = "Home"
        binding.profileButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_home_to_userProfile))

        viewModel.homeLiveData.observe(
            viewLifecycleOwner,
            { state ->
                state.handleStatesFromFragmentWithLoaderDialog(
                    this,
                    failed = { findNavController().navigate(HomeFragmentDirections.actionGlobalGenericErrorDialog()) },
                    complete = { binding.textView.text = it.content },
                )
            }
        )
    }
}
