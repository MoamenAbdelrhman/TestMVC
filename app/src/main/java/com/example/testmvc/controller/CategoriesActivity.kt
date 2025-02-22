package com.example.testmvc.controller

import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.testmvc.model.MealListener
import com.example.testmvc.view.CategoryAdapter
import com.example.testmvc.model.db.FavoriteDatabase
import com.example.testmvc.model.FavoriteMeal
import com.example.testmvc.model.db.FavoriteMealDao
import com.example.testmvc.R
import com.example.testmvc.model.server.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesActivity : AppCompatActivity() , MealListener {
    private val TAG = "AllCategoriesActivity"
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter
    lateinit var progressBar : ProgressBar
    lateinit var dao : FavoriteMealDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_categories)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        progressBar = findViewById(R.id.progress_bar_loading)
        recyclerView = findViewById(R.id.rv_meals)

        adapter = CategoryAdapter(listOf(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, VERTICAL, false)

        dao = FavoriteDatabase.getDatabase(this).favoriteMealDao()

        if (!::adapter.isInitialized) {
            Log.e(TAG, "Adapter is not initialized yet!")
        }

        fetchMeals()


    }


    private fun fetchMeals() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.favServiceObj.getCategories()
                val categories = response.body()?.categories
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (categories != null) {
                        adapter.mealList = categories
                    }
                    adapter.notifyDataSetChanged()
                    Log.i(TAG, "onCreate: $categories")
                    Toast.makeText(this@CategoriesActivity, "Recieved ${(categories)?.size}", Toast.LENGTH_SHORT).show()
                }
            }catch (th:Throwable){
                th.printStackTrace()
            }
        }
    }

    override fun onMealClick(meal: FavoriteMeal) {
        lifecycleScope.launch(Dispatchers.IO) {
            val addingResult = dao.addCategory(meal)
            withContext(Dispatchers.Main){
                if(addingResult>0)
                    Toast.makeText(this@CategoriesActivity, "Added to Favorites ${meal.strCategory}", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@CategoriesActivity, "${meal.strCategory} is already in Favorites", Toast.LENGTH_SHORT).show()

            }
        }
    }
}