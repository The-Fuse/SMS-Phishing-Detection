package com.solvabit.phishingsmsdetector.screens.details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.solvabit.phishingsmsdetector.database.PhishingMessageDatabase
import com.solvabit.phishingsmsdetector.databinding.FragmentMessageDetailsBinding
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.*
import androidx.appcompat.app.AppCompatActivity


private const val TAG = "MessageDetailsFragment"

class MessageDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMessageDetailsBinding
    private lateinit var viewModel: MessageDetailsViewModel
    val args: MessageDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageDetailsBinding.inflate(layoutInflater)

        Log.i(TAG, "onCreateView: ${args.message.address}")
        (activity as AppCompatActivity).supportActionBar?.title = args.message.address

        val database = PhishingMessageDatabase.getDatabase(requireContext())
        val messageDetailsViewModelFactory = MessageDetailsViewModelFactory(args.message, database)
        viewModel = ViewModelProvider(this, messageDetailsViewModelFactory)[MessageDetailsViewModel::class.java]
        binding.message = args.message
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = YoutubeAdapter(YoutubeListener {
            watchYoutubeVideo(requireContext(), it.id.videoId)
        })
        binding.recyclerViewYoutube.adapter = adapter

        viewModel.youtubeList.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it.isNullOrEmpty()) {
                    binding.noVideosFoundTextView.visibility = View.VISIBLE
                }
                else {
                    if(it.size<5)
                        adapter.submitList(it)
                    else
                        adapter.submitList(it.subList(0,5))
                }
            }
        })

        val twitterAdapter = TwitterAdapter(TwitterListener {
            Toast.makeText(context, it.created_at, Toast.LENGTH_SHORT).show()
        })
        binding.recyclerViewTwitter.adapter = twitterAdapter

        viewModel.twitterList.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it.isNullOrEmpty()) {
//                    binding.noVideosFoundTextView.visibility = View.VISIBLE
                }
                else {
                    if(it.size<5)
                        twitterAdapter.submitList(it)
                    else
                        twitterAdapter.submitList(it.subList(0,5))
                }
            }
        })


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = args.message.address
    }

    fun watchYoutubeVideo(context: Context, id: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$id")
        )
        try {
            context.startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(webIntent)
        }
    }

}

