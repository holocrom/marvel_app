package com.example.presentation.adapter


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.R
import com.example.presentation.domain.CharacterModelVo

class SuperHeroAdapter(
    private var superHeroList: List<CharacterModelVo>,
    private val clickListener: (CharacterModelVo)->Unit,
    private val clickListenerFavorite: (CharacterModelVo, position: Int)->Unit
) : RecyclerView.Adapter<SuperHeroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperHeroViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SuperHeroViewHolder(layoutInflater.inflate(R.layout.item_superhero, parent, false))
    }

    override fun onBindViewHolder(holder: SuperHeroViewHolder, position: Int) {
        holder.render(superHeroList[position], clickListener, clickListenerFavorite)
    }

    override fun getItemCount() = superHeroList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listSuperHero: List<CharacterModelVo>) {
        superHeroList = listSuperHero
        notifyDataSetChanged()
    }

    fun updateItem(index:Int){
        notifyItemChanged(index)
    }

    fun removeItem(index: Int){
        superHeroList = superHeroList.toMutableList().apply {
            removeAt(index)
        }
        notifyItemRemoved(index)
    }
}