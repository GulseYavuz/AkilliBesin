package com.example.akillibesin.frontend.home.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.akillibesin.R
import com.example.akillibesin.core.database.authentication.InternetConnection
import com.example.akillibesin.core.database.authentication.Logout
import com.example.akillibesin.core.database.authentication.SharedPreferences
import com.example.akillibesin.core.database.realtime.DishImage
import com.example.akillibesin.core.database.realtime.FoodData
import com.example.akillibesin.core.database.realtime.UserData
import com.example.akillibesin.core.models.DishNutritionRegression
import com.example.akillibesin.databinding.ActivityMainBinding
import com.example.akillibesin.databinding.LayoutFloatingMenuItemBinding
import com.example.akillibesin.frontend.authentication.activities.SplashActivity
import com.example.akillibesin.frontend.factory.*
import com.example.akillibesin.frontend.home.fragments.*
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu

lateinit var nutritionViewModel: NutritionViewModel
lateinit var classifierViewModel: ClassifierViewModel

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var swipeListener: SwipeListener
    private lateinit var actionMenu: FloatingActionMenu
    private lateinit var dishImage: Bitmap
    private lateinit var userDataViewModel: UserDataViewModel
    private val permission = Permissions(this, this)

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDataViewModel = ViewModelProviders.of(this).get(UserDataViewModel::class.java)

        if (InternetConnection.isConnected(this)) {
            val userData = UserData()
            userData.getUserData(userDataViewModel)
        } else
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG)
                .show()


        // Hide system bars
        hideNavigationAndStatusBars()

        // Bottom navigation menu items
        initializeBottomNavigation()

        // Add floating action menu
        buildFloatingActionMenu()

        // Add navigation view click actions
        makeNavigationViewItemsHooks()

        // Swipe touches listener
        swipeListener = SwipeListener(this)

        // Drawer menu icon click action
        binding.layoutToolbar.imageMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        // Notification Icon click action
        binding.layoutToolbar.imageNotifications.setOnClickListener {
            loadFragment(ReminderFragment.newInstance())
        }
    }

    private fun initializeBottomNavigation() {
        // Adding menu items
        val bottomNavigation: MeowBottomNavigation = binding.bottomNavigation
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.ic_round_home_24))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_round_recipes_24))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.photo_album_24))

        // Bottom navigation show listener
        bottomNavigation.setOnShowListener {
            val fragment: Fragment? = when (it.id) {
                1 -> HomeFragment.newInstance()
                2 -> RecipesFragment.newInstance()
                3->  ImageGalleryFragment.newInstance()
                else -> null
            }

            // Load Fragment
            if (fragment != null) {
                loadFragment(fragment)
            }
        }

        // Initialize home fragment as the default
        bottomNavigation.show(1, false)

        // Set notification count
        bottomNavigation.setCount(2, "8")

        // Bottom navigation click actions
        bottomNavigation.setOnClickMenuListener {
        }

        // Bottom navigation same button reselect action
        bottomNavigation.setOnReselectListener {
        }
    }

    private fun makeNavigationViewItemsHooks() {
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_item_home -> {
                    loadFragment(HomeFragment.newInstance())
                    binding.drawerLayout.closeDrawers()
                    binding.bottomNavigation.show(1, false)
                    true
                }
                R.id.menu_item_recipes -> {
                    loadFragment(RecipesFragment.newInstance())
                    binding.drawerLayout.closeDrawers()
                    binding.bottomNavigation.show(-1, false)
                    true
                }
                R.id.menu_item_diary -> {
                    loadFragment(DiaryFragment.newInstance())
                    binding.drawerLayout.closeDrawers()
                    binding.bottomNavigation.show(-1, false)
                    true
                }
                R.id.menu_item_reminder -> {
                    loadFragment(ReminderFragment.newInstance())
                    binding.drawerLayout.closeDrawers()
                    binding.bottomNavigation.show(-1, false)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_main_frame, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        // Hide both the navigation bar and the status bar.
        if (hasFocus)
            hideSystemBars()
    }

    private fun hideNavigationAndStatusBars() {
        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            // The system bars will only be "visible" if none of the
            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
            if (visibility == 0)
                hideSystemBars()
        }
    }

    private fun hideSystemBars() {
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        // Collapse FAB Menu and clear the overlay
        if (actionMenu.isOpen) {
            binding.layoutMainFrameOverlay.visibility = View.INVISIBLE
            actionMenu.close(true)
        }
        return super.dispatchTouchEvent(event)
    }

    private fun capturePlate() {
        permission.checkPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, 2)
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
            dishImage = data.extras?.get("data") as Bitmap
            val dishImageUri = data.data
            if (dishImageUri != null) {
                DishImage.uploadImage(dishImage, dishImageUri)
            }
        }

        try {
            val nutritionList = makeRegressionPredictions(dishImage)

            // Update MutableLiveData to display the output
            nutritionViewModel.mutableNutrition.value = nutritionList
            classifierViewModel.mutableDishImage.value = BitmapDrawable(resources, dishImage)

        } catch (e: Exception) {
            Log.d("DLModels", "Exception Occurred in DL Models")
        }

    }

    private fun makeRegressionPredictions(dishImage: Bitmap): ArrayList<Float> {
        val nutrition = DishNutritionRegression(this)
        val tensorBuffer = nutrition.loadImageBuffer(dishImage, 224, 224)
        return (nutrition.predictNutritionModel(tensorBuffer))
    }
    private fun saveFoodDataInDatabase(foodName: String, nutritionList: ArrayList<Float>) {
        val foodData = FoodData(foodName, nutritionList)
        foodData.saveFoodData()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun buildFloatingActionMenu() {
        // Building the primary button
        val primaryBtnIcon = ImageView(this)
        primaryBtnIcon.setImageDrawable(getDrawable(R.drawable.ic_round_add_24))
        val actionButton = FloatingActionButton.Builder(this).setContentView(primaryBtnIcon).build()
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorSecondary, typedValue, true)
        val colorSecondary: Int = typedValue.data
        actionButton.background.setTint(colorSecondary)

        val param = actionButton.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(10, 10, 10, 240)
        actionButton.layoutParams = param

        // Building up the menu
        // Item 1
        val item1 = inflateFABMenuItem(FABItem.SU, R.drawable.ic_menu_water_48, "SU")
        // Item 2
        val item2 = inflateFABMenuItem(FABItem.AKŞAMYEMEĞİ, R.drawable.ic_menu_dinner_64, "AKŞAM YEMEĞİ")
        // Item 3
        val item3 = inflateFABMenuItem(FABItem.ATIŞTIRMALIK, R.drawable.ic_menu_snack_64, "ATIŞTIRMALIK")
        // Item 4
        val item4 = inflateFABMenuItem(FABItem.ÖĞLEYEMEĞİ, R.drawable.ic_menu_lunch_64, "ÖĞLE YEMEĞİ")
        // Item 5
        val item5 =
            inflateFABMenuItem(FABItem.KAHVALTI, R.drawable.ic_menu_breakfast_64, "KAHVALTI")
        // Item 6
        val item6 = inflateFABMenuItem(FABItem.FOTOĞRAF, R.drawable.ic_menu_camera_48, "FOTOĞRAF")

        // Getting runtime screen width
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width: Float = displayMetrics.widthPixels.toFloat()
        val radius: Int = (width * 0.60f).toInt() // radius = 60% of the screen width

        // Attaching the menu to the primary button
        actionMenu = FloatingActionMenu.Builder(this)
            .setRadius(radius)
            .addSubActionView(item1)
            .addSubActionView(item2)
            .addSubActionView(item3)
            .addSubActionView(item4)
            .addSubActionView(item5)
            .addSubActionView(item6)
            .attachTo(actionButton)
            .build()

        actionButton.setOnClickListener {
            actionMenu.toggle(true)
            if (actionMenu.isOpen) {
                // add an overlay on opening
                binding.layoutMainFrameOverlay.visibility = View.VISIBLE
                // Close NavigationDrawer if opened
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
                    binding.drawerLayout.closeDrawers()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun inflateFABMenuItem(id: FABItem, drawableID: Int, label: String): View {
        val itemBinding = LayoutFloatingMenuItemBinding.inflate(layoutInflater)
        val params = FrameLayout.LayoutParams(-2, -2, 51)
        itemBinding.root.layoutParams = params
        itemBinding.fabItem.setImageDrawable(getDrawable(drawableID))
        itemBinding.textFabLabel.text = label

        // Click Actions
        itemBinding.fabItem.setOnClickListener {
            when (id) {
                FABItem.FOTOĞRAF ->
                    capturePlate()
                FABItem.KAHVALTI ->
                    Toast.makeText(this, "Breakfast Button Clicked !!", Toast.LENGTH_SHORT).show()
                FABItem.ÖĞLEYEMEĞİ ->
                    Toast.makeText(this, "Lunch Button Clicked !!", Toast.LENGTH_SHORT).show()
                FABItem.ATIŞTIRMALIK ->
                    Toast.makeText(this, "Snack Button Clicked !!", Toast.LENGTH_SHORT).show()
                FABItem.AKŞAMYEMEĞİ ->
                    Toast.makeText(this, "Dinner Button Clicked !!", Toast.LENGTH_SHORT).show()
                FABItem.SU ->
                    Toast.makeText(this, "Water Button Clicked !!", Toast.LENGTH_SHORT).show()
            }
        }
        return itemBinding.root
    }

    private fun deleteEmailFromSharedPreference() {
        val preference = SharedPreferences(this)
        preference.delete()
    }

    private fun logout() {
        if (InternetConnection.isConnected(this)) {
            deleteEmailFromSharedPreference()
            signOut()
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        } else
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT)
                .show()
    }

    private fun signOut() {
        val logout = Logout()
        logout.signOut()
    }
}
