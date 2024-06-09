package com.example.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.R
import com.example.presentation.base.ScreenDisplay
import com.example.presentation.databinding.FragmentHeroListBinding
import com.example.presentation.domain.CharacterModelVo
import com.example.presentation.feature.SuperHeroState
import com.example.presentation.adapter.SuperHeroAdapter
import com.example.presentation.databinding.ActivityMainBinding.inflate
import com.example.presentation.viewmodel.SuperHeroListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HeroListFragment : Fragment() {
    private lateinit var binding : FragmentHeroListBinding
    private val listViewModel: SuperHeroListViewModel by viewModels()
    private var communicator: CommunicatorBetweenFragments? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as? CommunicatorBetweenFragments
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHeroListBinding.inflate(inflater, container, false)
        initModel()
        initScrollListener()
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listViewModel.onViewCreated()

        setupMenu()//try to register MenuProvider

    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.character_search, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater){
        menuInflater.apply {
            inflate(R.menu.item_menu,menu)
            inflate(R.menu.character_search,menu)
        }

        val searchItem = menu?.findItem(R.id.all_character_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.queryHint = "Buscar personaje"
        searchView.isIconifiedByDefault //watch out
        searchView.onActionViewExpanded()

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!=null){
                }
                return true
            }
        })
    }


    private fun initModel() {
        lifecycleScope.launch {
            listViewModel.superHeroListState.collect { superHeroState ->
                when (superHeroState) {
                    is SuperHeroState.Idle -> {}
                    is SuperHeroState.ShowList -> renderList(superHeroState.list)
                    is SuperHeroState.UpdateItem -> updateSuper(superHeroState.position)
                    is SuperHeroState.RemoveItem -> removeItem(superHeroState.index)
                    is SuperHeroState.EmptyList -> emptySuperHeroFavoriteList(resources.getString(R.string.lista_vacia_favoritos))
                    is SuperHeroState.ShowErrorState -> showError(resources.getString(R.string.error_por_defecto))
                }
            }
        }
    }

    private fun initView() {
        binding.apply {

            progressBar.visibility = View.VISIBLE

            superRecycle.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = SuperHeroAdapter(
                    emptyList(),
                    { listItemClicked(it) },
                    { superH, position -> updateSuperHeroInFavoriteList(superH, position) })
            }
        }
    }

    private fun initScrollListener() {
        binding.apply {
            superRecycle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    if (!listViewModel.getIsLoading() && communicator?.getActualDisplay()==true) {//double check to avoid pagination in favorite list screen
                        if (linearLayoutManager != null && linearLayoutManager.findLastVisibleItemPosition() ==
                                superRecycle.adapter?.itemCount?.minus(1)
                            ){
                                listViewModel.upDateIsLoading()
                                loadListToShow(ScreenDisplay.RegularList, local = true)
                                listViewModel.upDateIsLoading()
                            }
                    }
                }
            })
        }
    }
    private fun updateSuper(position: Int) {
        (binding.superRecycle.adapter as? SuperHeroAdapter)?.run {
            updateItem(position)
        }
    }

    private fun removeItem(position: Int) {
        (binding.superRecycle.adapter as? SuperHeroAdapter)?.apply {
            removeItem(position)
        }
    }

    private fun showError(errorMsg: String) {
        activity?.let { context ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
        }
    }

   private fun renderList(list: List<CharacterModelVo>?) {
       binding.apply {
           list?.let { (superRecycle.adapter as? SuperHeroAdapter)?.setData(list) }

           progressBar.visibility= View.GONE
           shimmerLayout.isVisible = false
           shimmerLayout.stopShimmer()
           superRecycle.visibility = View.VISIBLE//invisible hide the view but keep its space on layer
                                                 //visible.gone hide the view and remove its space on layer
       }
    }

    private fun updateSuperHeroInFavoriteList(superHero: CharacterModelVo, position: Int) {
        listViewModel.updateSuperHeroList(superHero, position)
    }

    private fun listItemClicked(superHero: CharacterModelVo) {
        communicator?.passCharacterVo(superHero)
    }

    fun loadListToShow(screenDisplay: ScreenDisplay, local:Boolean) {
        binding.apply {
            progressBar.visibility=View.VISIBLE
            shimmerLayout.isVisible = true
            superRecycle.isVisible = false
        }
        listViewModel.setListToShow(screenDisplay, local)
    }

    private fun emptySuperHeroFavoriteList(message:String){
        showError(message)
        listViewModel.setListToShow(ScreenDisplay.RegularList, local = true)
    }
}
