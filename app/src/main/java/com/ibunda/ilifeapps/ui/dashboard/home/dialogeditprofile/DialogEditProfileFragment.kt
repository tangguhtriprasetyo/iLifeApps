package com.ibunda.ilifeapps.ui.dashboard.home.dialogeditprofile

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.commit
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.databinding.FragmentDialogEditProfileBinding
import com.ibunda.ilifeapps.ui.dashboard.MainActivity
import com.ibunda.ilifeapps.ui.dashboard.profile.EditProfileFragment


class DialogEditProfileFragment : DialogFragment() {

    private var mView: View? = null
    private var _binding: FragmentDialogEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run {
            //initiate the binding here and pass the root to the dialog view
            _binding = FragmentDialogEditProfileBinding.inflate(layoutInflater).apply {
                btnLengkapiData.setOnClickListener {
                    val mFragmentManager = parentFragmentManager
                    val mEditProfileFragment = EditProfileFragment()
                    mFragmentManager.commit {
                        addToBackStack(null)
                        replace(
                            R.id.host_fragment_activity_main,
                            mEditProfileFragment,
                            MainActivity.CHILD_FRAGMENT
                        )
                    }
                    this@DialogEditProfileFragment.dismiss()
                }

            }
            AlertDialog.Builder(this).apply {
                setView(binding.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mView = null
    }
}