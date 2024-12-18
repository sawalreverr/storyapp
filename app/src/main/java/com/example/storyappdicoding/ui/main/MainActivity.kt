package com.example.storyappdicoding.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyappdicoding.R
import com.example.storyappdicoding.databinding.ActivityMainBinding
import com.example.storyappdicoding.ui.welcome.WelcomeActivity
import com.example.storyappdicoding.core.data.paging.LoadingStateAdapter
import com.example.storyappdicoding.core.utils.ViewModelFactory
import com.example.storyappdicoding.ui.add_story.AddStoryActivity
import com.example.storyappdicoding.ui.detail_story.DetailStoryActivity
import com.example.storyappdicoding.ui.map.MapsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var storyAdapter: MainAdapter
    private lateinit var loadingStateAdapter: LoadingStateAdapter

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

        loadingStateAdapter = LoadingStateAdapter { storyAdapter.retry() }

        binding.rvListStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter.withLoadStateFooter(loadingStateAdapter)
            setHasFixedSize(true)
        }
    }

    private fun observeStories() {
        viewModel.getStories.observe(this) { pagingData ->
            storyAdapter.submitData(lifecycle, pagingData)
        }
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
            R.id.action_map -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val launcherAddStory = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            storyAdapter.refresh()
        }
    }
}