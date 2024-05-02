package com.example.presentation.view

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.presentation.R
import com.example.presentation.base.ScreenDisplay
import com.example.presentation.domain.CharacterModelVo
import com.example.presentation.ui.CommunicatorBetweenFragments
import com.example.presentation.ui.DetailCharacterFragment
import com.example.presentation.ui.HeroListFragment
import com.example.presentation.util.ConstantsPresentation
import com.example.presentation.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(R.layout.activity_main), CommunicatorBetweenFragments {


    private val mainActivityViewModel: MainActivityViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTopBarColor()
        setActionBarColor()
        launchSuperHeroListFragment()

    }

    private fun launchSuperHeroListFragment() {
        supportFragmentManager.apply {
            beginTransaction()
                .replace(R.id.fragmentContainerView, HeroListFragment(),HeroListFragment::class.java.canonicalName)
                .addToBackStack(HeroListFragment::class.java.canonicalName)
                .commit()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.apply {
//            inflate(R.menu.item_menu,menu)
//            inflate(R.menu.character_search,menu)
//        }
//        return super.onCreateOptionsMenu(menu)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.anySuperHeroe->{
                showList(ScreenDisplay.RegularList, false)
                true
            }
            R.id.favoriteSuperHeroe->{
                showList(ScreenDisplay.FavoriteList, false)
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

    override fun passCharacterVo(superHero: CharacterModelVo) {
        mainActivityViewModel.updateDisplays()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerView,
                DetailCharacterFragment().apply {
                    arguments = Bundle().apply { putSerializable(ConstantsPresentation.VARIABLE_TO_FRAGMENTS,superHero) }
                },DetailCharacterFragment::class.java.canonicalName)
            .addToBackStack(DetailCharacterFragment::class.java.canonicalName)
            .commit()
    }

    override fun showList(screenDisplay: ScreenDisplay, local:Boolean) {
        (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as? HeroListFragment)?.run {
            mainActivityViewModel.updateDisplays()
            loadListToShow(screenDisplay, local)
        }
    }

    override fun getActualDisplay() = mainActivityViewModel.getActualDisplay()

    private fun setTopBarColor(){
        this.window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = ContextCompat.getColor(applicationContext,R.color.blue_darker_2)

        }
    }

    private fun setActionBarColor(){
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(applicationContext,R.color.blue_darker_2))
        )
    }

    override fun onBackPressed() {
        when(supportFragmentManager.backStackEntryCount){
            1 -> if (!mainActivityViewModel.getActualDisplay()) showList(ScreenDisplay.RegularList, false)
                else finish()
            else -> {
                super.onBackPressed()
                if (!mainActivityViewModel.getActualDisplay()) showList(ScreenDisplay.RegularList, false) else showList(ScreenDisplay.FavoriteList, false)
            }
        }//TODO
    }
}
