package com.example.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.R
import com.example.presentation.databinding.ItemSuperheroBinding
import com.example.presentation.domain.CharacterModelVo
import com.squareup.picasso.Picasso

class SuperHeroViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemSuperheroBinding.bind(view)

    fun render(
        superHeroModel: CharacterModelVo,
        clickListener: (CharacterModelVo) -> Unit,
        clickListenerFavorite: (CharacterModelVo, position: Int) -> Unit
    ){
        binding.apply {
            with(superHeroModel) {
                tvsuperHeroName.text = name
                tvsuperDescripcion.text = description
                if (imageURL?.contains("image_not") == true)//check if img is not avaible
                    ivSuper.setImageResource(R.drawable.im_not_available)
                else Picasso.get()
                    .load(imageURL)
                    .fit()
                    .centerCrop()
                    .into(binding.ivSuper)

                btnFavoriteStar.setImageResource(
                    if (favorite) R.drawable.ic_favorite_red else R.drawable.ic_favorito
                )

                view.setOnClickListener {
                    clickListener(this)
                }
                btnFavoriteStar.setOnClickListener {
                    clickListenerFavorite.invoke(this, adapterPosition)
                }
            }
        }
    }
}