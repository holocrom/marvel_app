package com.example.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.presentation.databinding.FragmentDetailCharacterBinding
import com.example.presentation.domain.CharacterModelVo
import com.example.presentation.util.ConstantsPresentation.VARIABLE_TO_FRAGMENTS
import com.example.presentation.viewmodel.DetailCharacterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DetailCharacterFragment : Fragment() {

    private lateinit var binding : FragmentDetailCharacterBinding
    private val detailCharacterViewModel: DetailCharacterViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailCharacterBinding.inflate(inflater,container,false).apply {
            character = arguments?.getSerializable(VARIABLE_TO_FRAGMENTS) as? CharacterModelVo
        }

        lifecycleScope.launch{
            detailCharacterViewModel.characterModelVo.collect{ superHero ->
                superHero?.let {
                    binding.character = superHero
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            favoriteStar.setOnClickListener {
                character?.let {
                    setSuperHeroToFavoriteList(it)
                }
                //aquí
//                lifecycleScope.launch{
//                    detailCharacterViewModel.characterModelVo.collect{
//                        binding.character = it
//                    }
//                }
                //Toast.makeText(activity, "HOLA HOLA", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setSuperHeroToFavoriteList(superHero: CharacterModelVo) {
        detailCharacterViewModel.updateSuperHeroFavoriteToBridge(superHero)
    }
}
