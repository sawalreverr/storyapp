package com.example.storyappdicoding.ui.detail_story

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyappdicoding.core.utils.ViewModelFactory
import com.example.storyappdicoding.databinding.ActivityDetailStoryBinding
import com.example.storyappdicoding.core.data.Result
import com.bumptech.glide.Glide
import com.example.storyappdicoding.R

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private val detailViewModel: DetailStoryViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra(EXTRA_STORY_ID) ?: return
        observeStoryDetail(storyId)
    }

    private fun observeStoryDetail(storyId: String) {
        detailViewModel.getStoryDetail(storyId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    val story = result.data.story
                    binding.tvDetailName.text = story?.name
                    binding.tvDetailDescription.text = story?.description
                    Glide.with(this)
                        .load(story?.photoUrl)
                        .placeholder(R.drawable.ic_place_holder)
                        .into(binding.ivDetailPhoto)
                }
                is Result.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        const val EXTRA_STORY_ID = "extra_story_id"
    }
}