package com.ibunda.ilifeapps.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Onboarding
import com.ibunda.ilifeapps.databinding.ActivityOnboardingBinding
import com.ibunda.ilifeapps.ui.login.LoginActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private val onBoardingViewPagerAdapter = OnBoardingViewPagerAdapter(
        listOf(
            Onboarding(
                R.drawable.img_landing_1,
                "Pekerjaan rumah tangga sangat menyita waktu?"
            ),
            Onboarding(
                R.drawable.img_landing_2,
                "Selesaikan hanya dengan 1 klik dari genggaman Anda."
            ),
            Onboarding(
                R.drawable.img_landing_3,
                "Dengan tarif terjangkau, mitra I-Life membantu pekerjaan rumah tangga Anda secara cepat dan aman."
            )
        )
    )

    private val indicators = arrayOfNulls<ImageView>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.onboarding_indicator_inactive
                )
            )
            indicators[i]!!.layoutParams = layoutParams
            binding.layoutOnboardingIndicators.addView(indicators[i])
        }

        binding.onboardingViewPager.adapter = onBoardingViewPagerAdapter



        binding.onboardingViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicator(position)
                    if (position == onBoardingViewPagerAdapter.itemCount - 1) {
                        binding.buttonOnboardingAction.text = getString(R.string.tv_mulai)
                        binding.buttonOnboardingAction.setOnClickListener {
                            val intent = Intent(this@OnboardingActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        binding.buttonOnboardingAction.text = getString(R.string.tv_berikutnya)
                        binding.buttonOnboardingAction.setOnClickListener {
                            binding.onboardingViewPager.currentItem = position + 1
                        }
                    }
                    if (position == onBoardingViewPagerAdapter.itemCount - 3) {
                        binding.tvLewati.text = getString(R.string.tv_lewati)
                        binding.tvLewati.background = null
                        binding.tvLewati.setOnClickListener {
                            binding.onboardingViewPager.currentItem = position + 2
                        }
                    } else {
                        binding.tvLewati.text = ""
                        binding.tvLewati.background = null
                    }
                }
            }
        )
    }

    private fun setCurrentIndicator(index: Int) {

        for (i in indicators.indices) {
            val imageView: ImageView = binding.layoutOnboardingIndicators.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.onboarding_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.onboarding_indicator_inactive
                    )
                )
            }
        }
    }
}