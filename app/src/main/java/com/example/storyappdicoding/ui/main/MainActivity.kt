package com.example.storyappdicoding.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyappdicoding.R
import com.example.storyappdicoding.databinding.ActivityMainBinding
import com.example.storyappdicoding.ui.welcome.WelcomeActivity
import com.example.storyappdicoding.core.data.Result
import com.example.storyappdicoding.core.utils.ViewModelFactory
import com.example.storyappdicoding.ui.add_story.AddStoryActivity
import com.example.storyappdicoding.ui.detail_story.DetailStoryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var storyAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeSession()

        binding.fab.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            launcherAddStory.launch(intent)
        }

        binding.btnRetry.setOnClickListener {
            observeStories()
        }
    }

    private fun observeSession() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                if (user.token.isNotEmpty()) {
                    setupRecyclerView()
                    observeStories()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        storyAdapter = MainAdapter { story ->
            val intent = Intent(this, DetailStoryActivity::class.java)
            intent.putExtra(DetailStoryActivity.EXTRA_STORY_ID, story.id)
            startActivity(intent)
        }

        binding.rvListStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }
    }

    private fun observeStories() {
        viewModel.getStories().observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                    binding.btnRetry.visibility = View.GONE
                }
                is Result.Success -> {
                    showLoading(false)
                    binding.btnRetry.visibility = View.GONE
                    storyAdapter.submitList(result.data.listStory)
                }
                is Result.Error -> {
                    showLoading(false)
                    binding.btnRetry.visibility = View.VISIBLE
                    Toast.makeText(this@MainActivity, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_logout ->  {
                viewModel.logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val launcherAddStory = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            observeStories()
        }
    }
}