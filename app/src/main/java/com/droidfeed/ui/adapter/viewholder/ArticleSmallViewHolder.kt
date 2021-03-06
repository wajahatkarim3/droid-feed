package com.droidfeed.ui.adapter.viewholder

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.droidfeed.data.model.Article
import com.droidfeed.databinding.ListItemArticleSmallBinding
import com.droidfeed.ui.module.feed.ArticleClickListener
import com.droidfeed.util.ObservableColorMatrix
import com.droidfeed.util.glide.GlideApp

/**
 * Created by Dogan Gulcan on 11/2/17.
 */
class ArticleSmallViewHolder(private val binding: ListItemArticleSmallBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article, articleClickListener: ArticleClickListener) {
        binding.articleClickListener = articleClickListener
        binding.rssItem = article
        binding.executePendingBindings()

        GlideApp.with(binding.root.context)
                .load(article.image)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        if (!article.hasFadedIn) {

                            binding.imgArticle.setHasTransientState(true)
                            val cm = ObservableColorMatrix()
                            val saturation = ObjectAnimator.ofFloat(
                                    cm, ObservableColorMatrix.SATURATION, 0f, 1f)
                            saturation.addUpdateListener {
                                binding.imgArticle.colorFilter = ColorMatrixColorFilter(cm)
                            }
                            saturation.duration = 2000L
                            saturation.addListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    binding.imgArticle.clearColorFilter()
                                    binding.imgArticle.setHasTransientState(false)
                                }
                            })
                            saturation.start()
                            article.hasFadedIn = true
                        }
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean =
                            false
                })
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .centerCrop()
                .into(binding.imgArticle)
    }


}