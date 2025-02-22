package com.example.testmvc.controller

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testmvc.model.MealListener
import com.example.testmvc.model.db.FavoriteDatabase
import com.example.testmvc.model.FavoriteMeal
import com.example.testmvc.view.FavoriteMealAdapter
import com.example.testmvc.model.db.FavoriteMealDao
import com.example.testmvc.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteActivity : AppCompatActivity(), MealListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: FavoriteMealAdapter
    private lateinit var mealDao: FavoriteMealDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorite)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.fav_rv_movies)
        progressBar = findViewById(R.id.fav_progress_bar_loading)
        adapter = FavoriteMealAdapter(listOf(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        mealDao = FavoriteDatabase.getDatabase(this).favoriteMealDao()

        loadFavorites()
    }

    private fun loadFavorites() {
        lifecycleScope.launch(Dispatchers.IO) {
            val storedMeals: List<FavoriteMeal>// List<FavoriteMeal>
            try {
                withContext(Dispatchers.IO) {
                    storedMeals = mealDao.getAllFavorites()
                }
                progressBar.visibility = View.GONE
                adapter.mealList = storedMeals
                adapter.notifyDataSetChanged()

            } catch (th: Throwable) {
                th.printStackTrace()
            }
        }
    }

    override fun onMealClick(meal: FavoriteMeal) {

        lifecycleScope.launch(Dispatchers.IO) {
            val deletingResult = mealDao.removeCategory(meal)
            withContext(Dispatchers.Main){
                if(deletingResult>0) {
                    Toast.makeText(
                        this@FavoriteActivity,
                        "Removed ${meal.strCategory}",
                        Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(
                        this@FavoriteActivity,
                        "${meal.strCategory} is already in Favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            val newList = mealDao.getAllFavorites()
            withContext(Dispatchers.Main) {
                adapter.mealList = newList
                adapter.notifyDataSetChanged()
            }
        }
    }
}
