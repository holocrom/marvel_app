package com.example.domain.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.domain.R
import com.squareup.picasso.Picasso

@BindingAdapter("avatar")
fun setAvatar(imageView : ImageView, imageURL:String?){
    if(imageURL!=null)
        Picasso.get().load(imageURL).into(imageView)
    else
        imageView.setImageResource(R.drawable.im)
}
@BindingAdapter("favIcon")
fun setIcon(imageView : ImageView,favorite:Boolean){
    if(favorite) imageView.setImageResource(R.drawable.ic_favorite_red)
    else imageView.setImageResource(R.drawable.ic_favorito)
}

