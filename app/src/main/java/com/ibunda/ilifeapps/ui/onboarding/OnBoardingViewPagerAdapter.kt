package com.ibunda.ilifeapps.ui.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Onboarding
import com.ibunda.ilifeapps.databinding.ItemOnboardingBinding

class OnBoardingViewPagerAdapter(private val onBoardingData: List<Onboarding>) :
    RecyclerView.Adapter<OnBoardingViewPagerAdapter.OnboardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        return OnboardingViewHolder(
            ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return onBoardingData.size
    }

    override fun onBindViewHolder(
        holder: OnBoardingViewPagerAdapter.OnboardingViewHolder,
        position: Int
    ) {
        holder.bind(onBoardingData[position])
    }

    inner class OnboardingViewHolder(private val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(onBoardingData: Onboarding) {
            with(binding) {
                textDescription.text = onBoardingData.title
                imageOnboarding.setImageResource(onBoardingData.imgHeader)
            }
        }
    }
}