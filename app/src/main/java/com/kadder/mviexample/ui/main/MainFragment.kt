package com.kadder.mviexample.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kadder.mviexample.ui.main.state.MainStateEvent.GetPhotosEvent
import com.kadder.mviexample.ui.main.state.MainStateEvent.GetUserEvent
import com.kader.mviexample.R

class MainFragment : Fragment() {

    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        subscribeObservers()
    }

    fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            println("DEBUG: DataState: $dataState")
            dataState.photos?.let { photos ->
                viewModel.setPhotosData(photos)
            }
            dataState.user?.let { user ->
                viewModel.setUserData(user)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.photos?.let {
                println("DEBUG: setting photos to recyclerView: $it")
            }
            viewState.user?.let {
                println("DEBUG: setting user  date: $it")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_get_user ->
                triggerGetUSerEvent()

            R.id.action_get_photos ->

                triggerGetPhotoEvent()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun triggerGetPhotoEvent() {
        viewModel.setStateEvent(GetPhotosEvent)
    }

    private fun triggerGetUSerEvent() {
        viewModel.setStateEvent(GetUserEvent("1"))
    }
}