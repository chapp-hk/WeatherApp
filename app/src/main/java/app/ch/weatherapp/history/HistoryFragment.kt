package app.ch.weatherapp.history

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import app.ch.base.getBinding
import app.ch.base.recyclerview.pagingAdapter
import app.ch.weatherapp.BR
import app.ch.weatherapp.R
import app.ch.weatherapp.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_history) {

    private val viewModel by viewModels<HistoryViewModel>()
    private val adapter by pagingAdapter<HistoryListItem>(BR.listItem)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupViews(view)
        setupEventObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.history_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.deleteAll -> viewModel.deleteAllItems().let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViews(view: View) {
        FragmentHistoryBinding.bind(view).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.recyclerView.adapter = adapter
        }
    }

    private fun setupEventObservers() {
        viewModel.queryWeatherHistory()
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { adapter.submitData(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.historyEvent
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { handleEvent(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        adapter.loadStateFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { loadState ->
                getBinding<FragmentHistoryBinding>().tvWelcome.isVisible =
                    loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.DeleteItem -> viewModel.deleteItem(event.id)
            is HistoryEvent.Display -> {
                setFragmentResult(REQUEST_DISPLAY_CITY, bundleOf(KEY_CITY_NAME to event.cityName))
                findNavController().navigateUp()
            }
            is HistoryEvent.ListChanged -> adapter.refresh()
        }
    }
}
